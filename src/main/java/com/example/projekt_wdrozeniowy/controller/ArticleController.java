package com.example.projekt_wdrozeniowy.controller;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import com.example.projekt_wdrozeniowy.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class ArticleController implements Runnable {

    @Autowired
    ArticleService articleService;
    @Autowired
    CSVService csvService;
    @Value("${app.save-path}")
    String savePath;

    @Override
    @Scheduled(cron = "*/10 * * * * *")
    //@Scheduled(fixedRate = 1000)
    @Async
    public void run() {
        //TODO: what will happen when schedule is set to 1 ms or something? (possible problem with file access)
        List<Article> articleList = articleService.findLatestArticles();
        csvService.saveToCSV(savePath, articleList);
    }

}
