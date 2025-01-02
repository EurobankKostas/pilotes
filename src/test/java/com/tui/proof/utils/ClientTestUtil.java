package com.tui.proof.utils;

import java.util.UUID;

import com.tui.proof.domain.model.Client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientTestUtil {

    public static Client copyOf(Client client) {
        return new Client(
            client.getId(),
            client.getFirstName(),
            client.getLastName(),
            client.getPhoneNumber(),
            client.getEmail()
        );
    }

    public static final Client CLIENT = new Client(
        null,
        "John",
        "Doe",
        "+34666666666",
        "johndoe@yopmail.com"
    );

    public static final Client CLIENT_2 = new Client(
        null,
        "Peter",
        "Doe",
        "+35666666666",
        "peterdoe@yopmail.com"
    );

    public static final Client EXISTING_CLIENT = new Client(
        UUID.fromString("00000000-0000-0000-0000-000000000001"),
        "John",
        "Doe",
        "+34666666666",
        "johndoe@yopmail.com"
    );

}
