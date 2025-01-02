package com.tui.proof.utils;

import com.tui.proof.application.dto.PilotesOrderUpdateRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PilotesOrderUpdateRequestTestUtil {

    public static final PilotesOrderUpdateRequest PILOTES_ORDER_UPDATE_REQUEST = new PilotesOrderUpdateRequest(
        "Second Street",
        "12345",
        "City",
        "USA",
        2,
        10
    );

}
