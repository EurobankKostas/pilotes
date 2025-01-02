package com.tui.proof.application.dto;

import java.util.List;

import com.tui.proof.domain.model.PilotesOrder;

public record PilotesOrdersPageable(
    List<PilotesOrder> orders,
    int page,
    int size,
    int totalPages,
    long totalElements
) {

}
