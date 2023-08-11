package com.example.projekt_wdrozeniowy.job;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class GenerateArticlesJob implements Runnable {

    private final ArticleService articleService;

    @Getter
    private int numberOfGeneratedArticles;
    @Override
    @Scheduled(cron = "*/2 * * * * *")
    @Async
    public void run() {
        log.info("Starting job: " + this.getClass().getSimpleName());
        int counter = 1 + (int)(Math.random() * 3);
        numberOfGeneratedArticles = 0;
        for (int i = 0; i < counter; i++) {
            if (Math.random() * 100 > 50) {
                numberOfGeneratedArticles++;
                articleService.save(new Article(generateGibberish(), generateGibberish(), generateGibberish()));
            }
        }
        log.info("Generated " + numberOfGeneratedArticles + " new Articles.");
    }

    private String generateGibberish() {
        StringBuilder stringBuilder = new StringBuilder();
        int length = 3 + (int) (Math.random() * 32);

        for (int j = 0; j < length; j++) {
            char temp = (char) ('A' + (int) (Math.random() * 27));
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

    public GenerateArticlesJob(ArticleService articleService) {
        this.articleService = articleService;
    }

}
