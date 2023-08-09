package com.example.projekt_wdrozeniowy.repository;

import com.example.projekt_wdrozeniowy.model.Article;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Repository
public interface ArticleRepository extends CassandraRepository<Article, UUID> {
/*    @Value("${spring.cassandra.keyspace-name}")
    String keyspace;*/
    @Query("SELECT * FROM test.article WHERE date > :localDate ALLOW FILTERING")
    List<Article> findLatestArticles(@Param("localDate") LocalDateTime localDateTime);
}
