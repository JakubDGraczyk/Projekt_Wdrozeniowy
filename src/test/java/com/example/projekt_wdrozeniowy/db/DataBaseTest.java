package com.example.projekt_wdrozeniowy.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.example.projekt_wdrozeniowy.job.SaveArticlesJob;
import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles(profiles = "test")
class DataBaseTest {

    @Autowired
    SaveArticlesJob saveArticlesJob;

    @Autowired
    ArticleService articleService;

    private static final String KEYSPACE_NAME = "test";
    @Container
    private static final CassandraContainer<?> cassandra = new CassandraContainer<>("cassandra:latest");

    @BeforeAll
    public static void initDataBase() {
        createKeyspace(cassandra.getCluster());
    }

    @DynamicPropertySource
    static void databasesProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cassandra.contact-points", cassandra::getContainerIpAddress);
        registry.add("spring.cassandra.port", cassandra::getFirstMappedPort);
    }

    static void createKeyspace(Cluster cluster) {
        try (Session session = cluster.connect()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE_NAME + " WITH replication = \n" +
                    "{'class':'SimpleStrategy','replication_factor':'1'};");
        }
    }

    @Test
    void shouldAddNewArticle() {
        Article article = new Article("Kuba", "Kuba", "Kuba");
        articleService.save(article);
        List<Article> articles = articleService.findLatestArticles();
        System.out.println(articleService.getClass().getName());
        assertThat(articles.get(0)).isEqualTo(article);
    }

    @Test
    void ApplicationIntegrationTest() {
        //Given
        Clock start = Clock.fixed(Instant.parse("2022-01-01T10:09:00Z"), ZoneId.systemDefault());
        Article article;

        article = new Article("First", "Test", "1");
        article.setDate(LocalDateTime.now(start));
        articleService.save(article);
        article = new Article("Second", "Test", "2");
        article.setDate(LocalDateTime.now(Clock.offset(start, Duration.ofMinutes(25))));
        articleService.save(article);
        article = new Article("Third", "Test", "3");
        article.setDate(LocalDateTime.now(Clock.offset(start, Duration.ofMinutes(50))));
        articleService.save(article);

        //wwhen
        List<Article> articlesBefore = articleService.findLatestArticles();
        Instant.now(Clock.offset(start, Duration.ofHours(1)));
        saveArticlesJob.run();

        //then
        List<Article> articlesAfter = articleService.findLatestArticles();
        assertThat(articlesBefore.size()).isGreaterThan(articlesAfter.size());
        assertThat(articlesBefore).isNotEmpty();

    }

}