package net.shyshkin.study.beerservice.web.mappers;

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
        OffsetDateTime offsetDateTimeUTC = dateMapper.asOffsetDateTime(timestamp);

        //then
        assertThat(offsetDateTimeUTC.toInstant()).isCloseTo(now.toInstant(), within(1, ChronoUnit.MILLIS));
    }

}