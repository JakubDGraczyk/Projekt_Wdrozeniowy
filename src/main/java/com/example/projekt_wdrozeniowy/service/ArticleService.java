package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findLatestArticles() {
        List<Article> articleList = articleRepository.findArticlesToExport();
        if (articleList.isEmpty()) {
            log.info("No records were found, that suit this criteria.");
        } else {
            log.info(articleList.size() + " records were found.");
            updateExportValue(articleList);
            articleList.sort(Article::compareTo);
        }
        return articleList;
    }

    public void updateExportValue(List<Article> articles){
        log.info("Updating export values of selected Articles to true.");
        List<UUID> ids = articles.stream()
                .map(Article::getId)
                .toList();
        articleRepository.modifyExportValue(ids);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
}
