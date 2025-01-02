package com.tui.proof.application.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.tui.proof.utils.AddressTestUtil;
import com.tui.proof.utils.PilotesOrderRequestTestUtil;
import com.tui.proof.utils.PilotesOrderUpdateRequestTestUtil;

class AddressMapperTest {

    @Test
    void testMap() {
        var result = AddressMapper.map(PilotesOrderRequestTestUtil.PILOTES_ORDER_REQUEST);
        assertThat(result).isNotNull().isEqualTo(AddressTestUtil.ADDRESS);
    }

    @Test
    void testMap_update() {
        var result = AddressMapper.map(PilotesOrderUpdateRequestTestUtil.PILOTES_ORDER_UPDATE_REQUEST);
        assertThat(result).isNotNull().isEqualTo(AddressTestUtil.ADDRESS_2);
    }

}
