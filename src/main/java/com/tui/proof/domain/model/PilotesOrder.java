package com.tui.proof.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.tui.proof.domain.validator.AllowedPilotesCount;
import com.tui.proof.domain.value.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Domain Aggregate Root: PilotesOrder.
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
    name = "orders"
)
@NoArgsConstructor
@AllArgsConstructor
public class PilotesOrder {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Address deliveryAddress;

    @Column(name = "number_of_pilotes", nullable = false)
    @AllowedPilotesCount
    private int numberOfPilotes;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "cancelled", nullable = false)
    private boolean cancelled;

    public PilotesOrder(
        Address deliveryAddress,
        int numberOfPilotes,
        double pricePerPilotes,
        Client client
    ) {
        this.deliveryAddress = deliveryAddress;
        this.numberOfPilotes = numberOfPilotes;
        this.totalPrice = calculatePrice(this.numberOfPilotes, pricePerPilotes);
        this.client = client;
    }

    public PilotesOrder update(
        Address address,
        int numberOfPilotes,
        double pricePerPilotes
    ) {
        this.deliveryAddress = address;
        this.numberOfPilotes = numberOfPilotes;
        this.totalPrice = calculatePrice(numberOfPilotes, pricePerPilotes);
        return this;
    }

    private double calculatePrice(int pilotes, double pricePerPilot) {
        return pilotes * pricePerPilot;
    }
}
