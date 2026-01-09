package com.microservices.inventoryservice.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    public Timestamp getCurrentTimestamp() {
        Instant instant = Instant.now();

        // Set the time zone to GMT
        ZoneId zone = ZoneId.of("Asia/Jakarta");

        // Format the date and time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(instant.atZone(zone).format(formatter));
    }

    public String getCurrentDate() {
        LocalDateTime date = LocalDateTime.now();

        ZoneId zone = ZoneId.of("Asia/Jakarta");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SS 'WIB'");

        return date.atZone(zone).format(formatter);
    }
}
