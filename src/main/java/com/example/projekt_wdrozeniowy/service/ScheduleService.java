package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.handler.ScheduleHandler;
import com.example.projekt_wdrozeniowy.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ScheduleService {

    private final ScheduleHandler scheduleHandler;

    public List<Article> findNextDataToExport(List<Article> articleList) {
        log.info("Getting next data dump.");
        List<Article> subList;
        if (articleList.isEmpty()) {
            return new ArrayList<>();
        }
        articleList.sort(Comparator.comparing(Article::getDate));
        subList = separateData(articleList);
        if (subList.isEmpty()) {
            subList = scheduleHandler.getNextDump(articleList);
            if (subList.get(0).getDate().getHour() < LocalTime.now().getHour()) {
                return subList;
            } else {
                return new ArrayList<>();
            }
        } else {
            subList = scheduleHandler.getNextDump(subList);
            //TODO: poprawic ta czesc
            return subList;
        }
    }

    public boolean isThereMoreDataToExport(List<Article> articleList) {
        log.info("Cheking is there more data to export.");
        if (articleList.isEmpty()) {
            return false;
        }
        articleList.sort(Comparator.comparing(Article::getDate));
        if (articleList.get(0).getDate().toLocalDate().isBefore(LocalDate.now())) {
            return true;
        }
        return scheduleHandler.isThereDataReadyToExportForToday(articleList);
    }

    private List<Article> separateData(List<Article> articleList) {
        log.info("Separating articles by date");
        List<Article> subList = new ArrayList<>();
        subList.add(articleList.get(0));
        for (int i = subList.size(); i < articleList.size(); i++) {
            if (subList.get(0).getDate().toLocalDate().equals(articleList.get(i).getDate().toLocalDate())) {
                subList.add(articleList.get(i));
            }
        }
        if (subList.get(0).getDate().toLocalDate().equals(LocalDate.now())) {
            return new ArrayList<>();
        }
        {
            return subList;
        }
    }

    public ScheduleService(ScheduleHandler scheduleHandler) {
        this.scheduleHandler = scheduleHandler;
    }

}
