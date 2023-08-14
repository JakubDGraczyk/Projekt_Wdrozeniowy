package com.example.projekt_wdrozeniowy;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.repository.ArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class ProjektWdrozeniowyApplication implements CommandLineRunner {

    private final ArticleRepository articleRepository;

    public ProjektWdrozeniowyApplication(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjektWdrozeniowyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= 3; j++) {
                Article article = new Article("Test", "Test", "Test");
                article.setDate(LocalDateTime.of(
                        LocalDate.now().getYear(),
                        LocalDate.now().getMonth(),
                        LocalDate.now().minusDays(j).getDayOfMonth(),
                        LocalTime.now().getHour(),
                        LocalTime.now().getMinute(),
                        LocalTime.now().getSecond()
                ));
                articles.add(article);
            }
        }
        for (int i = 0; i <= 3; i++) {
            Article article = new Article("Test", "Test", "Test");
            article.setDate(LocalDateTime.of(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonth(),
                    LocalDate.now().getDayOfMonth(),
                    LocalTime.now().getHour() - 2,
                    LocalTime.now().getMinute(),
                    LocalTime.now().getSecond()
            ));
            articles.add(article);
        }
        articleRepository.saveAll(articles);
    }
}