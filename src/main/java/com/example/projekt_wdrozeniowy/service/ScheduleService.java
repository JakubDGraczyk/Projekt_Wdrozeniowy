package com.example.projekt_wdrozeniowy.service;

import com.example.projekt_wdrozeniowy.model.Article;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class ScheduleService {

    private String reportScheduleProperty;
    private final List<Integer> scheduledHours;

    private int pointer;

    public ScheduleService(@Value("${app.report-schedule}") String reportScheduleProperty) {
        this.reportScheduleProperty = reportScheduleProperty;
        this.scheduledHours = initScheduleList();
    }

    public List<Article> getNextBatch(List<Article> articles) {
        log.info("Separating data into smaller batches depending on configured schedule.");
        if (articles.isEmpty()) {
            log.info("There are no records to export!");
            return new ArrayList<>();
        }
        articles.sort(Comparator.comparing(Article::getDate));
        this.setPointer(articles.get(0).getDate().toLocalTime());
        List<Article> subList = new ArrayList<>();
        subList.add(articles.get(0));
        for (int i = subList.size(); i < articles.size(); i++) {
            if (pointer == 0 && articles.get(i).getDate().getHour() > getPrevPointer()
                    && articles.get(i).getDate().toLocalDate().isEqual(articles.get(0).getDate().toLocalDate())) {
                subList.add(articles.get(i));
            } else if (articles.get(i).getDate().getHour() < this.scheduledHours.get(pointer)) {
                subList.add(articles.get(i));
            } else {
                break;
            }
        }
        log.info("Size of list: " + subList.size());
        return subList;
    }

    private int getPrevPointer() {
        if (this.pointer == 0) {
            return scheduledHours.size() - 1;
        } else {
            return pointer - 1;
        }
    }

    private void setPointer(LocalTime articleTimestamp) {
        log.debug("Setting pointer");
        this.pointer = 0;
        for (int i = 0; i < scheduledHours.size(); i++) {
            if (articleTimestamp.getHour() < scheduledHours.get(i)) {
                this.pointer = i;
                break;
            }
        }
        log.debug("Pointer set to " + this.scheduledHours.get(pointer));
    }

    private List<Integer> initScheduleList() {
        log.info("Initializing schedule.");
        List<Integer> tempList = new ArrayList<>();
        if (reportScheduleProperty.contains("-")) {
            int separatorPos = reportScheduleProperty.indexOf("-");
            for (int i = Integer.parseInt(reportScheduleProperty.substring(0, separatorPos)); i <= Integer.parseInt(reportScheduleProperty.substring(separatorPos + 1)); i++) {
                tempList.add(i);
            }
            tempList.sort(Comparator.comparing(Integer::intValue));
        } else {
            reportScheduleProperty = reportScheduleProperty.replaceAll(",", " ");
            tempList = Arrays.stream(reportScheduleProperty
                            .split(" "))
                    .map(Integer::parseInt)
                    .filter(o -> o >= 0)
                    .filter(o -> o <= 24)
                    .sorted()
                    .toList();
        }
        log.debug("Schedule initialized: " + Arrays.toString(tempList.toArray()));
        return tempList;
    }

}
