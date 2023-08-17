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
    @Description("Checks if records from yesterday that were posted after last scheduled hour, are exported the next day")
    void getNExtBatchTest1() {
        //given
        scheduleService = new ScheduleService("3,6,9,12,15,18,21");
        List<Article> inputList = new ArrayList<>();
        this.addSpecificArticles(inputList, LocalTime.of(23, 0), true, 1, 1);
        this.addSpecificArticles(inputList, LocalTime.of(3, 0), false, 0, 2);
        //when
        List<Article> outputList = scheduleService.getNextBatch(inputList);

        //then
        assertThat(outputList.size()).isEqualTo(3);
    }

    @Test
    @Description("Checks if 3 articles will be returned when scheduled export is made every 3h")
    void getNExtBatchTest2() {
        //given
        scheduleService = new ScheduleService("0,3,6,9,12,15,18,21");
        List<Article> inputList = new ArrayList<>();
        this.addSpecificArticles(inputList, LocalTime.of(21, 0), false, 1, 6);

        //when
        List<Article> outputList = scheduleService.getNextBatch(inputList);

        //then
        assertThat(outputList.size()).isEqualTo(3);

    }
    @Test
    @Description("Should return 6 articles, from both days when there is only one schedule hour")
    void getNExtBatchTest3() {
        //given
        scheduleService = new ScheduleService("14");
        List<Article> inputList = new ArrayList<>();
        this.addSpecificArticles(inputList, LocalTime.of(17, 10), false, 0, 5);
        this.addSpecificArticles(inputList, LocalTime.of(23, 10), true, 0, 3);

        //when
        List<Article> outputList = scheduleService.getNextBatch(inputList);

        //then
        assertThat(outputList.size()).isEqualTo(6);
    }
    @Test
    @Description("Should return 1 article every hour, for two consecutive days.")
    void getNExtBatchTest4() {
        //given
        scheduleService = new ScheduleService("0-23");
        List<Article> inputList = new ArrayList<>();
        this.addSpecificArticles(inputList, LocalTime.of(23, 0), false, 0, 23);
        this.addSpecificArticles(inputList, LocalTime.of(23, 0), true, 0, 23);

        for (int i = 0; i < 48; i++) {
            //when
            List<Article> outputList = scheduleService.getNextBatch(inputList);
            //then
            assertThat(outputList.size()).isEqualTo(1);

            //setup for repeat
            this.removeUsedArticles(inputList,outputList);
        }
    }
    @Test
    @Description("Should return 2 article every 2 hours, for two consecutive days.")
    void getNExtBatchTest5() {
        //given
        scheduleService = new ScheduleService("0,2,4,6,8,10,12,14,16,18,20,22");
        List<Article> inputList = new ArrayList<>();
        this.addSpecificArticles(inputList, LocalTime.of(23, 0), false, 0, 23);
        this.addSpecificArticles(inputList, LocalTime.of(23, 0), true, 0, 23);

        for (int i = 0; i < 24; i++) {
            //when
            List<Article> outputList = scheduleService.getNextBatch(inputList);
            //then
            assertThat(outputList.size()).isEqualTo(2);

            //setup for repeat
            this.removeUsedArticles(inputList,outputList);
        }
    }
    private void addSpecificArticles(List<Article> articles, LocalTime startHour, boolean dayOffset, int lower, int upper) {
        for (int i = lower; i <= upper; i++) {
            Article article = new Article("Test", "Test", "Test");
            article.setDate(LocalDateTime.of(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonth(),
                    dayOffset ? LocalDate.now().minusDays(1).getDayOfMonth() : LocalDate.now().getDayOfMonth(),
                    startHour.minusHours(i).getHour(),
                    startHour.getMinute(),
                    LocalTime.now().getSecond()
            ));
            articles.add(article);
        }
    }

    private void removeUsedArticles(List<Article> main, List<Article> secondary) {
        for (Article article : secondary) {
            main.remove(article);
        }
    }
}