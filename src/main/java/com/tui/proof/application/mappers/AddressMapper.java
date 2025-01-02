package com.tui.proof.application.mappers;

import com.tui.proof.application.dto.PilotesOrderRequest;
import com.tui.proof.application.dto.PilotesOrderUpdateRequest;
import com.tui.proof.domain.value.Address;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressMapper {

    public static Address map(PilotesOrderRequest input){
        return new Address(
            input.street(),
            input.postcode(),
            input.city(),
            input.country(),
            input.addressNumber()
        );
    }

    public static Address map(PilotesOrderUpdateRequest input){
        return new Address(
            input.street(),
            input.postcode(),
            input.city(),
            input.country(),
            input.addressNumber()
        );
    }

}
