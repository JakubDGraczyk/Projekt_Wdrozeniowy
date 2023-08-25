package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CSVServiceTest {

    private  CSVService csvService = new CSVService();
    @Test
    void shouldReturnFalseWhenArticleListIsEmpty() {
        //Given
        List<Article> articles = new ArrayList<>();
        String path = "C:\\Users\\";

        //When
        boolean temp = csvService.saveToCSV(path,articles);

        //Then
        assertThat(temp).isFalse();
    }
}