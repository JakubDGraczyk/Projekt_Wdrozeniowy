package com.example.projekt_wdrozeniowy.handler;

import com.example.projekt_wdrozeniowy.model.Article;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ScheduleHandlerTest {
    private ScheduleHandler scheduleHandler;

    @Test
        //
    void shouldReturn3Records() {
        //given
        scheduleHandler = new ScheduleHandler("3");
        List<Article> articles = this.init(0, 2);

        //when
        List<Article> result = scheduleHandler.getNextDump(articles);

        //then
        assertThat(result.size()).isEqualTo(articles.size());
    }

    private List<Article> init(int min, int max) {
        List<Article> articles = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            Article article = new Article("Test", "Test", "Test");
            article.setDate(LocalDateTime.of(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonth(),
                    LocalDate.now().getDayOfMonth(),
                    i,
                    0,
                    0
            ));
            System.out.println(article);
            articles.add(article);
        }
        articles.sort(Comparator.comparing(Article::getDate));
        return articles;
    }
}