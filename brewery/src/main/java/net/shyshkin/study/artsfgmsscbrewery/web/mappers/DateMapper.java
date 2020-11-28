package net.shyshkin.study.artsfgmsscbrewery.web.mappers;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class DateMapper {
    public Timestamp asTimestamp(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) return null;
        return Timestamp.from(offsetDateTime.toInstant());
    }

    public OffsetDateTime asOffsetDateTime(Timestamp timestamp) {
        if (timestamp == null) return null;
        return OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC"));
    }


}
