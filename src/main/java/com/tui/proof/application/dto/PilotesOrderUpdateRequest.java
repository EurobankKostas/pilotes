package com.tui.proof.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.tui.proof.domain.validator.AllowedPilotesCount;

@Schema(
    name = "PilotesOrderUpdateRequest",
    description = "Payload for updating an existing Pilotes Order (record-based)."
)
public record PilotesOrderUpdateRequest(

    @Schema(
        title = "Street",
        example = "Main Street",
        description = "Must not be blank",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Street must not be blank.")
    String street,

    @Schema(
        title = "Postcode",
        example = "12345",
        description = "Must not be blank and between 2-20 chars",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Postcode must not be blank.")
    @Size(min = 2, max = 20, message = "Postcode must be between 2 and 20 characters.")
    String postcode,

    @Schema(
        title = "City",
        example = "London",
        description = "Must not be blank",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "City must not be blank.")
    String city,

    @Schema(
        title = "Country",
        example = "United Kingdom",
        description = "Must not be blank",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Country must not be blank.")
    String country,

    @Schema(
        title = "Address Number",
        example = "123",
        requiredMode = RequiredMode.REQUIRED
    )
    int addressNumber,

    @Schema(
        title = "Number of Pilotes",
        example = "10",
        description = "Must be 5, 10, or 15 (validated by @AllowedPilotesCount)",
        requiredMode = RequiredMode.REQUIRED
    )
    @AllowedPilotesCount
    int numberOfPilotes
) {

}
