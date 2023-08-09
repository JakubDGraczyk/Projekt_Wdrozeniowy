package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Service
public class CSVService {
    Logger logger = LoggerFactory.getLogger(FileService.class);

    public void saveToCSV(String filePath, List<Article> articles) {
        if (articles.isEmpty()) {
            return;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath + "Articles_" + LocalDateTime.now().format(dateTimeFormatter) + ".CSV"), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Id", "Date", "Title", "Text", "Author");
            for (Article article : articles) {
                csvPrinter.printRecord(article.getId(), article.getDate(), article.getTitle(), article.getContent(), article.getAuthor());
            }
            logger.info(articles.size() + " articles were saved to csv file: " + Arrays.toString(articles.toArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

