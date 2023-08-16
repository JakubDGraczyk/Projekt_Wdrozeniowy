package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
class ScheduleServiceTest {

    private ScheduleService scheduleService;

    @Test
    @Description("Checks if records from yesterday that were posted after last scheduled hour, show the next day")
    void shouldReturn3Records() {
        //given
        scheduleService = new ScheduleService("0,3,6,9,12,15,18,21");
        List<Article> inputList = new ArrayList<>();
        this.addSpecificArticles(inputList, LocalTime.of(23, 59), true, 1, 1);
        this.addSpecificArticles(inputList, LocalTime.of(3, 0), false, 0, 2);
        //when
        List<Article> outputList = scheduleService.getNextBatch(inputList);

        //then
        assertThat(outputList.size()).isEqualTo(3);
    }

    @Test
    @Description("Checks if 3 articles will be returned when scheduled export is made every 3h")
    void shouldReturn3Recordsv2() {
        //given
        scheduleService = new ScheduleService("0,3,6,9,12,15,18,21");
        List<Article> inputList = new ArrayList<>();
        this.addSpecificArticles(inputList, LocalTime.of(23, 59), false, 1, 6);

        //when
        List<Article> outputList = scheduleService.getNextBatch(inputList);

        //then
        assertThat(outputList.size()).isEqualTo(3);
    }

    private void addSpecificArticles(List<Article> articles, LocalTime startHour, boolean dayOffset, int lower, int upper) {
        for (int i = lower; i <= upper; i++) {
            Article article = new Article("Test", "Test", "Test");
            article.setDate(LocalDateTime.of(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonth(),
                    dayOffset ? LocalDate.now().plusDays(i).getDayOfMonth() : LocalDate.now().getDayOfMonth(),
                    startHour.plusHours(i).getHour(),
                    startHour.getMinute(),
                    LocalTime.now().getSecond()
            ));
            articles.add(article);
        }
    }

    private List<Article> removeUsedArticles(List<Article> main, List<Article> secondary) {
        for (Article article : secondary) {
            main.remove(article);
        }
        return main;
    }
}