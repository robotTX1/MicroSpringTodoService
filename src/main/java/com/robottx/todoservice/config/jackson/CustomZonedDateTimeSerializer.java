package com.robottx.todoservice.config.jackson;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CustomZonedDateTimeSerializer extends ZonedDateTimeSerializer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
            .withZone(ZoneOffset.UTC);

    public CustomZonedDateTimeSerializer() {
        super(FORMATTER);
    }

}
