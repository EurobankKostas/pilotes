package com.tui.proof.application.mappers;

import static com.tui.proof.utils.PilotesOrderTestUtil.EXISTING_PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrdersPageableTestUtil.PILOTES_ORDERS_PAGEABLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;

import com.tui.proof.application.dto.PilotesOrdersPageable;
import com.tui.proof.domain.model.PilotesOrder;

class OrderMapperTest {

    @Test
    void testMap() {
        //given
        PageImpl<PilotesOrder> page = new PageImpl<>(List.of(EXISTING_PILOTES_ORDER));
        //when
        var result = OrderMapper.map(page);
        //then
        assertThat(result).isNotNull().isEqualTo(PILOTES_ORDERS_PAGEABLE);
    }

    @Test
    void testMap_null_input() {
        //when
        var result = OrderMapper.map(null);
        //then
        assertThat(result).isNotNull().isEqualTo(
            new PilotesOrdersPageable(
                List.of(),
                0,
                0,
                0,
                0
            )
        );
    }

    @Test
    void testMap_empty_content() {
        //when
        var result = OrderMapper.map(new PageImpl<>(List.of()));
        //then
        assertThat(result).isNotNull().isEqualTo(
            new PilotesOrdersPageable(
                List.of(),
                0,
                0,
                0,
                0
            )
        );
    }

}
