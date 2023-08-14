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
import java.util.Optional;


@Controller
@Slf4j
public class SaveArticlesJob implements Runnable {

    private final ArticleService articleService;
    private final ScheduleService scheduleService;
    private final CSVService csvService;
    private final String savePath;

    @Override
    @Scheduled(cron = "0 0/55 * * * *")
    @Async
    public void run() {
        this.run(Optional.of(articleService.findLatestArticles()));
    }

    public void run(Optional<List<Article>> articleList) {
        log.info("Starting job: " + this.getClass().getSimpleName());
        List<Article> startingList = articleList.orElseGet(articleService::findLatestArticles);
        List<Article> jobList = scheduleService.findNextDataToExport(startingList);
        csvService.saveToCSV(savePath, jobList);
        articleService.updateExportValue(jobList);
        jobList = this.removeExportedRecords(startingList, jobList);
        if (scheduleService.isThereMoreDataToExport(jobList)) {
            log.info("Continuing job, there is still more data to export");
            this.run(Optional.of(jobList));
        }
    }

    private List<Article> removeExportedRecords(List<Article> main, List<Article> exportedRecords) {
        for (Article exportedRecord : exportedRecords) {
            main.remove(exportedRecord);
        }
        return main;
    }

    public SaveArticlesJob(ArticleService articleService, ScheduleService scheduleService, CSVService csvService, @Value("${app.save-path}") String path) {
        this.articleService = articleService;
        this.scheduleService = scheduleService;
        this.csvService = csvService;
        savePath = path;
    }
}
