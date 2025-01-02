package com.tui.proof.application.service;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tui.proof.application.dto.PilotesOrderRequest;
import com.tui.proof.application.dto.PilotesOrderUpdateRequest;
import com.tui.proof.application.dto.PilotesOrdersPageable;
import com.tui.proof.application.mappers.AddressMapper;
import com.tui.proof.application.mappers.ClientMapper;
import com.tui.proof.application.mappers.OrderMapper;
import com.tui.proof.domain.exception.PilotesOrderException;
import com.tui.proof.domain.factory.Clock;
import com.tui.proof.domain.model.Client;
import com.tui.proof.domain.model.PilotesOrder;
import com.tui.proof.infrastructure.config.PilotesProperties;
import com.tui.proof.infrastructure.database.PilotesClientRepository;
import com.tui.proof.infrastructure.database.PilotesOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PilotesOrderService {

    private final Clock clock;
    private final PilotesProperties pilotesProperties;
    private final PilotesOrderRepository pilotesOrderRepository;
    private final PilotesClientRepository pilotesClientRepository;

    /**
     * Search for orders based on query.
     */
    public PilotesOrdersPageable searchOrdersByName(String query, Pageable pageable) {
        log.info("#searchOrdersByName: query={}, pageable={}", query, pageable);
        return OrderMapper.map(pilotesOrderRepository.searchByName(query, pageable));
    }

    /**
     * Get an order by its ID.
     */
    public PilotesOrder getOrderById(UUID id) throws PilotesOrderException {
        log.info("#getOrderById: id={}", id);
        return pilotesOrderRepository.findById(id)
            .orElseThrow(() -> new PilotesOrderException("Order not found: " + id, HttpStatus.NOT_FOUND));
    }

    /**
     * Get a paginated list of all orders.
     */
    public PilotesOrdersPageable getAllOrders(Pageable pageable) {
        log.info("#getAllOrders: pageable={}", pageable);
        return OrderMapper.map(pilotesOrderRepository.findAll(pageable));
    }

    /**
     * Create a new order. A user will be created if it doesn't exist.
     */
    @Transactional
    public PilotesOrder createOrder(PilotesOrderRequest pilotesOrderRequest) {
        log.info("#createOrder:  pilotesOrderRequest={}", pilotesOrderRequest);
        PilotesOrder newPilotesOrder = new PilotesOrder(
            AddressMapper.map(pilotesOrderRequest),
            pilotesOrderRequest.numberOfPilotes(),
            pilotesProperties.getPricePerPilotes(),
            resolveClient(pilotesOrderRequest)
        );
        return pilotesOrderRepository.save(newPilotesOrder);
    }

    /**
     * Update an existing order. Overwrites the order's address and number of pilotes.
     */
    @Transactional
    public PilotesOrder updateOrder(UUID orderId, PilotesOrderUpdateRequest pilotesOrderUpdateRequest)
        throws PilotesOrderException {
        log.info("#updateOrder: orderId={}, pilotesOrderRequest={}", orderId, pilotesOrderUpdateRequest);
        PilotesOrder existingOrder = getOrderOrThrowIfLate(orderId);
        return pilotesOrderRepository.save(
            existingOrder.update(
                AddressMapper.map(pilotesOrderUpdateRequest),
                pilotesOrderUpdateRequest.numberOfPilotes(),
                pilotesProperties.getPricePerPilotes()
            )
        );
    }

    /**
     * Cancel an existing order. The order will be marked as cancelled and no further updates will be allowed.
     */
    @Transactional
    public void cancelOrder(UUID orderId) throws PilotesOrderException {
        log.info("#cancelOrder: orderId={}", orderId);
        PilotesOrder existingOrder = getOrderOrThrowIfLate(orderId);
        existingOrder.setCancelled(true);
        pilotesOrderRepository.save(existingOrder);
    }

    private Client resolveClient(PilotesOrderRequest pilotesOrderRequest) {
        return pilotesClientRepository.findByEmail(pilotesOrderRequest.email())
            .orElseGet(() -> pilotesClientRepository.save(ClientMapper.map(pilotesOrderRequest)));
    }

    private PilotesOrder getOrderOrThrowIfLate(UUID orderId) throws PilotesOrderException {
        PilotesOrder existingOrder = pilotesOrderRepository.findByIdForUpdate(orderId)
            .orElseThrow(() -> new PilotesOrderException("Order not found: " + orderId, HttpStatus.NOT_FOUND));
        if (existingOrder.isCancelled()) {
            throw new PilotesOrderException("Cannot update a cancelled order.", HttpStatus.BAD_REQUEST);
        }
        long minutesElapsed = ChronoUnit.MINUTES.between(
            clock.now(),
            existingOrder.getCreationTime()
        );
        if (Math.abs(minutesElapsed) > pilotesProperties.getOrderTimeWindow()) {
            throw new PilotesOrderException(
                "Cannot update this order after 5 minutes have passed.",
                new IllegalArgumentException(String.format("Time window exceeded. id=%s", orderId)),
                HttpStatus.BAD_REQUEST
            );
        }
        return existingOrder;
    }
}
