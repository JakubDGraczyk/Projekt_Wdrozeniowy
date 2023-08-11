package com.example.projekt_wdrozeniowy.job;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import com.example.projekt_wdrozeniowy.service.CSVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@Slf4j
public class SaveArticlesJob implements Runnable {

    private final ArticleService articleService;
    private final CSVService csvService;
    @Value("${app.save-path}")
    private final String savePath;

    @Override
    @Scheduled(cron = "*/10 * * * * *")
    //@Scheduled(fixedRate = 1000)
    @Async
    public void run() {
        log.info("Starting job: " + this.getClass().getSimpleName());
        List<Article> articleList = articleService.findLatestArticles();
        articleService.updateExportValue(articleList);
        csvService.saveToCSV(savePath, articleList);
    }

    public SaveArticlesJob(ArticleService articleService, CSVService csvService) {
        this.articleService = articleService;
        this.csvService = csvService;
        savePath = "";
    }
}
