package com.example.projekt_wdrozeniowy.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public interface AsyncJob extends Runnable {
    @Override
    @Async
    void run();
}
