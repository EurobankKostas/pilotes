package com.tui.proof.application.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.tui.proof.utils.ClientTestUtil;
import com.tui.proof.utils.PilotesOrderRequestTestUtil;

class ClientMapperTest {

    @Test
    void testMap() {
        var result = ClientMapper.map(PilotesOrderRequestTestUtil.PILOTES_ORDER_REQUEST);
        assertThat(result).isNotNull().isEqualTo(ClientTestUtil.CLIENT);
    }

}
