package com.tui.proof.application.mappers;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;

import com.tui.proof.application.dto.PilotesOrdersPageable;
import com.tui.proof.domain.model.PilotesOrder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

    public static PilotesOrdersPageable map(Page<PilotesOrder> input){
        if(Objects.isNull(input) || input.isEmpty()){
            return new PilotesOrdersPageable(
                List.of(),
                0,
                0,
                0,
                0
            );
        }
        return new PilotesOrdersPageable(
            input.getContent(),
            input.getNumber(),
            input.getSize(),
            input.getTotalPages(),
            input.getTotalElements()
        );
    }
}
