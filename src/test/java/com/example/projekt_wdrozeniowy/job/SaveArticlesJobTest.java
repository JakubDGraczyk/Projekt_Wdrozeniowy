package com.example.projekt_wdrozeniowy.job;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import com.example.projekt_wdrozeniowy.service.CSVService;
import com.example.projekt_wdrozeniowy.service.ScheduleService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class SaveArticlesJobTest {

    @Mock
    private ArticleService articleService;
    @Mock
    private CSVService csvService;
    @Mock
    private ScheduleService scheduleService;
    @InjectMocks
    private SaveArticlesJob saveArticlesJob;

    @Test
    @Description("SaveArticlesJob: should schedule every job")
    void shouldScheduleEveryJob() {
        //given
        List<Article> articles= new ArrayList<>();
        articles.add(new Article());
        Mockito.when(articleService.findLatestArticles()).thenReturn(articles);
        Mockito.when(scheduleService.getNextBatch(articles)).thenReturn(new ArrayList<>());
        Mockito.when(csvService.saveToCSV(any(), any())).thenReturn(false);
        Mockito.doNothing().when(articleService).updateExportValue(new ArrayList<>());

        //when
        saveArticlesJob.run();

        //then
        Mockito.verify(articleService, Mockito.times(1)).findLatestArticles();
        Mockito.verify(scheduleService, Mockito.times(1)).getNextBatch(articles);
        Mockito.verify(csvService, Mockito.times(1)).saveToCSV(any(), any());
        Mockito.verify(articleService, Mockito.times(1)).updateExportValue(anyList());
    }

}