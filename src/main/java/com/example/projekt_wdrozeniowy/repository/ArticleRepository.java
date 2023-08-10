package com.example.projekt_wdrozeniowy.repository;

import com.example.projekt_wdrozeniowy.model.Article;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ArticleRepository extends CassandraRepository<Article, UUID> {
    @Query("SELECT * FROM test.article WHERE exported = false ALLOW FILTERING")
    List<Article> findArticlesToExport();
    @Query("UPDATE test.article SET exported = true WHERE id IN :article_ids")
    void modifyExportValue(@Param("article_ids")List<UUID> articleIds);
}
