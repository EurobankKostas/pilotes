package com.tui.proof.utils;

import com.tui.proof.application.dto.PilotesOrderRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PilotesOrderRequestTestUtil {

    public static final PilotesOrderRequest PILOTES_ORDER_REQUEST = new PilotesOrderRequest(
        "John",
        "Doe",
        "+34666666666",
        "johndoe@yopmail.com",
        "Main Street",
        "12345",
        "Springfield",
        "USA",
        1,
        5
    );

    public static final PilotesOrderRequest PILOTES_ORDER_REQUEST_BAD_REQUEST = new PilotesOrderRequest(
        "John",
        "Doe",
        "+34666666666",
        "johndoe@yopmail.com",
        null,
        "12345",
        "Springfield",
        "USA",
        1,
        20
    );

}
