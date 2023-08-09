package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    FileService fileService;

    public List<Article> findLatestArticles() {
        Logger logger = LoggerFactory.getLogger(FileService.class);
        List<Article> articleList = articleRepository.findLatestArticles(fileService.loadLatestTimeStamp());
        if (articleList.isEmpty()) {
            logger.info("No records were found, that suit this criteria.");
        } else {
            logger.info(articleList.size() + " records were found.");
            articleList.sort(Article::compareTo);
            fileService.saveLatestTimeStamp(articleList.get(articleList.size() - 1).getDate());
        }
        return articleList;
    }

    public void save(Article article) {
        articleRepository.save(article);
    }
}
