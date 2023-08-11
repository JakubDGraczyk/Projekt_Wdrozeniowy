package com.example.projekt_wdrozeniowy.job;
import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.service.ArticleService;
import lombok.With;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
class GenerateArticlesJobTest {
    @Mock
    private ArticleService articleService;
    @InjectMocks
    private GenerateArticlesJob generateArticlesJob;
    @Test
    @DisplayName("GenerateArticleJob: should schedule every job")
    void shouldScheduleEveryJob() {
        //given

        //when
        generateArticlesJob.run();

        //then
        Mockito.verify(articleService,Mockito.times(generateArticlesJob.getNumberOfGeneratedArticles())).save(any(Article.class));
    }
}
