package com.tui.proof.utils;

import static com.tui.proof.utils.PilotesOrderTestUtil.EXISTING_PILOTES_ORDER;

import java.util.List;

import com.tui.proof.application.dto.PilotesOrdersPageable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PilotesOrdersPageableTestUtil {

    public static final PilotesOrdersPageable PILOTES_ORDERS_PAGEABLE = new PilotesOrdersPageable(
        List.of(EXISTING_PILOTES_ORDER),
        0,
        1,
        1,
        1
    );

}
