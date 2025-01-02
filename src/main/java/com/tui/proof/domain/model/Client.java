package com.tui.proof.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
    name = "client",
    indexes = {
        @Index(name = "idx_client_first_name", columnList = "firstName"),
        @Index(name = "idx_client_last_name", columnList = "lastName")
    })
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Email
    @NotEmpty
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
