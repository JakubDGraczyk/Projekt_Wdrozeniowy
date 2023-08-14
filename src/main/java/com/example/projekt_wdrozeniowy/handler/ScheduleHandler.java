package com.example.projekt_wdrozeniowy.handler;

import com.example.projekt_wdrozeniowy.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class ScheduleHandler {

    private String reportScheduleProperty;
    private final List<Integer> scheduledHours;

    private int pointer;

    public ScheduleHandler(@Value("${app.report-schedule}") String reportScheduleProperty) {
        this.reportScheduleProperty = reportScheduleProperty;
        this.scheduledHours = initScheduleList();
    }

    public List<Article> getNextDump(List<Article> articles) {
        log.info("Separating data by schedule.");
        this.setPointer(articles.get(0).getDate().toLocalTime());
        List<Article> subList = new ArrayList<>();
        if (articles.get(0).getDate().getHour() > this.scheduledHours.get(this.scheduledHours.size() - 1)){
            return new ArrayList<>();
        }else {
            subList.add(articles.get(0));
        }
            for (int i = subList.size(); i < articles.size(); i++) {
                if (subList.get(0).getDate().getHour() == 23 && articles.get(i).getDate().getHour() == 23) {
                    subList.add(articles.get(i));
                    log.info("Adding another article with hour: " + articles.get(i).getDate().getHour());
                } else if (articles.get(i).getDate().getHour() < this.scheduledHours.get(pointer)) {
                    subList.add(articles.get(i));
                    log.info("Adding another article with hour: " + articles.get(i).getDate().getHour());
                }
            }
        log.info("Size of list: " + subList.size());
        return subList;
    }

    public boolean isThereDataReadyToExportForToday(List<Article> articles) {
        int checkedHour = articles.get(0).getDate().getHour();
        for (int i = 0; i < this.scheduledHours.size(); i++) {
            if (checkedHour < this.scheduledHours.get(i)) {
                return (this.scheduledHours.get(i) < LocalTime.now().getHour());
            }
        }
        return false;
    }

    private void setPointer(LocalTime articleTimestamp) {
        log.info("Looking for next data dump scheduled hour");
        this.pointer = 0;
        for (int i = 0; i < scheduledHours.size(); i++) {
            if (articleTimestamp.getHour() == 23) {
                this.pointer = scheduledHours.get(0);
                break;
            }
            if (articleTimestamp.getHour() < scheduledHours.get(i)) {
                this.pointer = i;
                break;
            }
        }
        log.info("Pointer set to " + this.scheduledHours.get(pointer));
    }

    private List<Integer> initScheduleList() {
        log.info("Initializing schedule.");
        //TODO: create data validation 4-3, 1231123fdskfm213; 1,2,3,43,5,6
        List<Integer> tempList = new ArrayList<>();
        if (reportScheduleProperty.contains("-")) {
            int separatorPos = reportScheduleProperty.indexOf("-");
            for (int i = Integer.parseInt(reportScheduleProperty.substring(0, separatorPos)); i <= Integer.parseInt(reportScheduleProperty.substring(separatorPos + 1)); i++) {
                tempList.add(i);
            }
            tempList.sort(Comparator.comparing(Integer::intValue));
            log.info("Schedule initialized: " + Arrays.toString(tempList.toArray()));
            return tempList;
        } else {
            reportScheduleProperty = reportScheduleProperty.replaceAll(",", "");
            tempList = Arrays.stream(reportScheduleProperty
                            .split(" "))
                    .map(Integer::parseInt)
                    .filter(o -> o >= 0)
                    .filter(o -> o <= 24)
                    .sorted()
                    .toList();
            log.info("Schedule initialized: " + Arrays.toString(tempList.toArray()));
            return tempList;
        }
    }

}
