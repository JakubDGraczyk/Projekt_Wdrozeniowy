package com.example.projekt_wdrozeniowy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class FileService {


    Logger logger = LoggerFactory.getLogger(FileService.class);

    //TODO: Prefered solution - save last record. It should be loaded after every app startup, and checked with database
    public void saveLatestTimeStamp(LocalDateTime newTimeStamp) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/lastTimeStamp.txt", StandardCharsets.UTF_8)) {
            fileWriter.write(newTimeStamp.toString());
            logger.info("Latest article timestamp has been saved. Timestamp: " + newTimeStamp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDateTime loadLatestTimeStamp() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("src/main/resources/lastTimeStamp.txt", StandardCharsets.UTF_8))) {
            String data = fileReader.readLine();
            if (data == null) {
                LocalDateTime localDateTime = LocalDateTime.now().withNano(0).minusSeconds(1);
                logger.warn("lastTimeStamp.txt is empty, getting current time. Timestamp: " + localDateTime);
                this.saveLatestTimeStamp(localDateTime);
                return localDateTime;
            }
            logger.info("Latest article timestamp has been loaded. Timestamp: " + data);
            return LocalDateTime.parse(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
