package com.tui.proof.domain.factory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClockTest {

    Clock clock = new Clock();

    @Test
    void now() {
        //given + when
        var result = clock.now();

        //then
        assertThat(result).isNotNull();
    }

}
