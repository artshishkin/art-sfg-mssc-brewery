package net.shyshkin.study.artsfgmsscbrewery.web.mappers;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.*;

@Component
public class DateMapper {
    public Timestamp asTimestamp(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) return null;
//        return Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        return Timestamp.valueOf(LocalDateTime.ofInstant(offsetDateTime.toInstant(), ZoneOffset.systemDefault()));
    }

    public OffsetDateTime asOffsetDateTime(Timestamp timestamp) {
        if (timestamp == null) return null;
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("UTC"));
    }


}
