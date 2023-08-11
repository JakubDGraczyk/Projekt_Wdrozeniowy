package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.repository.ArticleRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleService articleService;
    List<Article> articleList = new ArrayList<>();
    List<UUID> ids;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        articleList.add(new Article("Test","Test","Test"));
        articleList.add(new Article("Test","Test","Test"));
        articleList.add(new Article("Test","Test","Test"));
        ids = articleList.stream().map(Article::getId).toList();
    }
    @Test
    @DisplayName("findLatestArticles - should return article list")
    void shouldReturnArticleList() {
        //Given

        //When
        Mockito.doNothing().when(articleRepository).modifyExportValue(ids);
        Mockito.when(articleRepository.findArticlesToExport()).thenReturn(articleList);

        //Then
        assertThat(articleService.findLatestArticles()).isEqualTo(articleList);
    }

    @Test
    @DisplayName("findLatestArticles - should return empty list when no records were found in database")
    void shouldReturnEmptyListWhenNoRecordsWereFound(){
        //given

        //when
        Mockito.when(articleRepository.findArticlesToExport()).thenReturn(new ArrayList<>());
        //then
        assertThat(articleService.findLatestArticles().size()).isEqualTo(0);
        Mockito.verify(articleRepository, Mockito.times(1)).findArticlesToExport();
        Mockito.verify(articleRepository, Mockito.times(0)).modifyExportValue(ids);
    }

    @Test
    @DisplayName("updateExportValue: should update records")
    void shouldUpdateRecords() {
        //Given

        //When
        Mockito.when(articleRepository.findArticlesToExport()).thenReturn(new ArrayList<>());
        articleService.updateExportValue(articleList);

        // Then
        Mockito.verify(articleRepository, Mockito.times(1)).modifyExportValue(ids);
        assertThat(articleService.findLatestArticles().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("save: should save new records")
    void save() {
        //given

        //when
        Mockito.when(articleRepository.save(any(Article.class))).thenReturn(null);
        articleService.save(new Article());

        //then
        Mockito.verify(articleRepository, Mockito.times(1)).save(any(Article.class));
    }
}