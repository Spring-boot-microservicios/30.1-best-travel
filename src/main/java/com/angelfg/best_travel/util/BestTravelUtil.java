package com.angelfg.best_travel.util;

import java.time.LocalDateTime;
import java.util.Random;

public class BestTravelUtil {
    public static final Random random = new Random();

    public static LocalDateTime getRandomSoon() {
        // 5 horas menos el numero menor 2 horas mas el numero menor
        // intervalo entre 2 y 5
        int randomHours = random.nextInt(5 - 2) + 2;
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(randomHours);
    }

    public static LocalDateTime getRandomLater() {
        int randomHours = random.nextInt(12 - 6) + 6;
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(randomHours);
    }

}
