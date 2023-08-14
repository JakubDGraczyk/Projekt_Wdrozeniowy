package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class CSVService {

    public boolean saveToCSV(String filePath, List<Article> articles) {
        if (articles.isEmpty()) {
            log.info("There were no articles to save!");
            return false;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH");
        String fileName = "Articles_" + articles.get(0).getDate().format(dateTimeFormatter) + ".CSV";
        String fullPath = filePath + fileName;

        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(fullPath), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Id", "Date", "Title", "Text", "Author");
            for (Article article : articles) {
                csvPrinter.printRecord(article.getId(), article.getDate(), article.getTitle(), article.getContent(), article.getAuthor());
            }
            log.info(articles.size() + " articles were saved to " + fullPath + Arrays.toString(articles.toArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean saveToCSV(String filePath, List<Article> articles, LocalDate localDate) {
        if (articles.isEmpty()) {
            log.info("There were no articles to save!");
            return false;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH");
        LocalDateTime localDateTime = LocalDateTime.of(localDate, articles.get(0).getDate().toLocalTime());
        String fileName = "Articles_" + localDateTime.format(dateTimeFormatter) + ".CSV";
        String fullPath = filePath + fileName;

        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(fullPath), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Id", "Date", "Title", "Text", "Author");
            for (Article article : articles) {
                csvPrinter.printRecord(article.getId(), article.getDate(), article.getTitle(), article.getContent(), article.getAuthor());
            }
            log.info(articles.size() + " articles were saved to " + fullPath + Arrays.toString(articles.toArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


}

