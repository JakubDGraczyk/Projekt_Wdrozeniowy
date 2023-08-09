package com.example.projekt_wdrozeniowy.controller;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ArticleWriter {

    @Autowired
    ArticleService articleService;

    @Scheduled(cron = "*/2 * * * * *")
    //@Scheduled(fixedRate = 1000)
    @Async
    public void run2() {
        int counter = (int) (Math.random() * 3);
        for (int i = 0; i < counter; i++) {
            if (Math.random() * 100 > 50) {
                articleService.save(new Article(Gibberish(), Gibberish(), Gibberish()));
            }
        }
    }

    private String Gibberish() {
        StringBuilder stringBuilder = new StringBuilder();
        int length = 3 + (int) (Math.random() * 32);

        for (int j = 0; j < length; j++) {
            char temp = (char) ('A' + (int) (Math.random() * 27));
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
