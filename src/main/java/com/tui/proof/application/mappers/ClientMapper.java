package com.tui.proof.application.mappers;

import com.tui.proof.application.dto.PilotesOrderRequest;
import com.tui.proof.domain.model.Client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client map(PilotesOrderRequest input) {
        return new Client(
            null,
            input.firstName(),
            input.lastName(),
            input.phoneNumber(),
            input.email()
        );
    }

}
