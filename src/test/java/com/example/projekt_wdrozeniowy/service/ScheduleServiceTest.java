package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.handler.ScheduleHandler;
import com.example.projekt_wdrozeniowy.model.Article;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;

class ScheduleServiceTest {

    ScheduleHandler scheduleHandler;
    ScheduleService scheduleService;

    @Test
    void getNextDataDump() {
        //given
        scheduleHandler = new ScheduleHandler("13");
        scheduleService = new ScheduleService(scheduleHandler);
        List<Article> articles = this.init(0,3);

        //when
         List<Article> result = scheduleService.findNextDataToExport(articles);

         //then
        assertThat(result.size()).isEqualTo(1);

    }
    private List<Article> init(int min, int max) {
        List<Article> articles = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            Article article = new Article("Test", "Test", "Test");
            article.setDate(LocalDateTime.of(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonth(),
                    LocalDate.now().minusDays(i).getDayOfMonth(),
                    LocalTime.now().getHour(),
                    LocalTime.now().getMinute(),
                    LocalTime.now().getSecond()
            ));
            System.out.println(article);
            articles.add(article);
        }
        articles.sort(Comparator.comparing(Article::getDate));
        return articles;
    }
}