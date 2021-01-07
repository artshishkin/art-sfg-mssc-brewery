package net.shyshkin.study.beerdata.dto;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BeerOrderLineDtoTest {

    @Test
    void testNullIntegerUnboxing_NPE() {
        //given
        BeerOrderLineDto beerOrderLineDto = BeerOrderLineDto.builder()
                .quantityAllocated(null)
                .build();

        //when
        ThrowableAssert.ThrowingCallable getQuantityAllocated = () -> {
            int quantityAllocated = beerOrderLineDto.getQuantityAllocated();
        };

        //then
        assertThatThrownBy(getQuantityAllocated).isInstanceOf(NullPointerException.class);
    }

    @Test
    void testNullIntegerUnboxing_usingRequireNonNull() {
        //given
        BeerOrderLineDto beerOrderLineDto = BeerOrderLineDto.builder()
                .quantityAllocated(null)
                .build();

        //when
        int quantityAllocated = Objects.requireNonNullElse(beerOrderLineDto.getQuantityAllocated(), 0);

        //then
        assertThat(quantityAllocated).isEqualTo(0);
    }

    @Test
    void testNullIntegerUnboxing_usingOptional_NPE() {
        //given
        BeerOrderLineDto beerOrderLineDto = BeerOrderLineDto.builder()
                .quantityAllocated(null)
                .build();

        //when
        ThrowableAssert.ThrowingCallable getQuantityAllocated = () -> {
            int quantityAllocated = Optional.of(beerOrderLineDto.getQuantityAllocated())
                    .orElse(0);
        };

        //then
        assertThatThrownBy(getQuantityAllocated).isInstanceOf(NullPointerException.class);
    }

    @Test
    void testNullIntegerUnboxing_usingOptionalOfNullable() {
        //given
        BeerOrderLineDto beerOrderLineDto = BeerOrderLineDto.builder()
                .quantityAllocated(null)
                .build();

        //when
        int quantityAllocated = Optional.ofNullable(beerOrderLineDto.getQuantityAllocated())
                .orElse(0);

        //then
        assertThat(quantityAllocated).isEqualTo(0);
    }
}