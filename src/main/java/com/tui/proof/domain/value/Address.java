package com.tui.proof.domain.value;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Schema(
        title = "Street",
        example = "Main Street",
        description = "Must not be blank",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Street must not be blank.")
    private String street;
    @Schema(
        title = "Postcode",
        example = "12345",
        description = "Must not be blank and between 2-20 chars",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Postcode must not be blank.")
    @Size(min = 2, max = 20, message = "Postcode must be between 2 and 20 characters.")
    private String postcode;
    @Schema(
        title = "City",
        example = "London",
        description = "Must not be blank",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "City must not be blank.")
    private String city;
    @Schema(
        title = "Country",
        example = "United Kingdom",
        description = "Must not be blank",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Country must not be blank.")
    private String country;
    @Schema(
        title = "Address Number",
        example = "123",
        requiredMode = RequiredMode.REQUIRED
    )
    private int addressNumber;
}
