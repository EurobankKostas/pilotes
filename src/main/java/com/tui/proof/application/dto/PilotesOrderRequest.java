package com.tui.proof.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.tui.proof.domain.validator.AllowedPilotesCount;

@Schema(
    name = "PilotesOrderRequest",
    description = "Payload for creating or updating a Pilotes Order (record-based)."
)
public record PilotesOrderRequest(

    @Schema(
        title = "Customer First Name",
        example = "John",
        description = "Must not be blank and between 2-200 chars",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "First name must not be blank.")
    @Size(min = 2, max = 200, message = "First name must be between 2 and 50 characters.")
    String firstName,

    @Schema(
        title = "Customer Last Name",
        example = "Doe",
        description = "Must not be blank and between 2-200 chars",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Last name must not be blank.")
    @Size(min = 2, max = 200, message = "Last name must be between 2 and 200 characters.")
    String lastName,

    @Schema(
        title = "Phone Number",
        example = "+123456789",
        description = "Must not be blank, 7-15 digits/spaces, may start with +",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Phone number must not be blank.")
    @Pattern(
        regexp = "^(?:\\+?[0-9]{1,3})?[0-9\\s-]{7,15}$",
        message = "Phone number must be 7-15 digits long (may include spaces, dashes, and an optional leading +)"
    )
    String phoneNumber,

    @Schema(
        title = "Email Address",
        example = "john.doe@example.com",
        description = "Must be a valid email format",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email must not be blank.")
    @Email(message = "Email format is invalid.")
    String email,

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
    Integer numberOfPilotes
) {

}
