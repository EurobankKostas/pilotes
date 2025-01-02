package com.tui.proof.utils;

import com.tui.proof.domain.value.Address;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressTestUtil {

    public static final Address ADDRESS = new Address(
        "Main Street",
        "12345",
        "Springfield",
        "USA",
        1
    );

    public static final Address ADDRESS_2 = new Address(
        "Second Street",
        "12345",
        "City",
        "USA",
        2
    );

}
