package net.shyshkin.study.artsfgmsscbrewery.web.mappers;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class DateMapperTest {

    DateMapper dateMapper = new DateMapper();

    @Test
    void conversionTest() {
        //given
        OffsetDateTime now = OffsetDateTime.now();

        //when
        Timestamp timestamp = dateMapper.asTimestamp(now);
        OffsetDateTime offsetDateTime = dateMapper.asOffsetDateTime(timestamp);
        System.out.println(timestamp);

        //then
        assertThat(offsetDateTime.toInstant()).isCloseTo(now.toInstant(), within(1, ChronoUnit.MILLIS));
    }
}