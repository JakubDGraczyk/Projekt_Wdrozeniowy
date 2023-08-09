package com.example.projekt_wdrozeniowy;

import com.example.projekt_wdrozeniowy.model.Article;
import com.example.projekt_wdrozeniowy.repository.ArticleRepository;
import com.example.projekt_wdrozeniowy.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ProjektWdrozeniowyApplication implements CommandLineRunner {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(ProjektWdrozeniowyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileService.loadLatestTimeStamp();
        dataInitializer();
    }

    //jak zrobić żeby poczekało na utworzenie tabeli
    private void dataInitializer() {
        Article[] articles = {
                new Article("A", "AAAAAAAAA", "A.A"),
                new Article("B", "BBBBBBBBBBBBB", "B.B"),
                new Article("C", "CCCCCCCCCCCCCCCCCCC", "C.C")
        };
        articleRepository.saveAll(Arrays.stream(articles).toList());
    }
}