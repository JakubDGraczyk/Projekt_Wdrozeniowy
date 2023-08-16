package com.example.projekt_wdrozeniowy.job;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import com.example.projekt_wdrozeniowy.service.CSVService;
import com.example.projekt_wdrozeniowy.service.ScheduleService;
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
    private final ScheduleService scheduleService;
    private final CSVService csvService;
    private final String savePath;

    @Override
    @Scheduled(cron = "0 0/15 * * * *")
    @Async
    public void run() {
        log.info("Starting job: " + this.getClass().getSimpleName());
        List<Article> startingList = articleService.findLatestArticles();
        List<Article> jobList = scheduleService.getNextBatch(startingList);
        csvService.saveToCSV(savePath, jobList);
        articleService.updateExportValue(jobList);
    }

    public SaveArticlesJob(ArticleService articleService, ScheduleService scheduleService, CSVService csvService, @Value("${app.save-path}") String path) {
        this.articleService = articleService;
        this.scheduleService = scheduleService;
        this.csvService = csvService;
        savePath = path;
    }
}
