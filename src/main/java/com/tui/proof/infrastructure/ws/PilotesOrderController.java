package com.tui.proof.infrastructure.ws;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tui.proof.application.dto.PilotesOrderRequest;
import com.tui.proof.application.dto.PilotesOrderUpdateRequest;
import com.tui.proof.application.dto.PilotesOrdersPageable;
import com.tui.proof.application.service.PilotesOrderService;
import com.tui.proof.domain.exception.PilotesOrderException;
import com.tui.proof.domain.model.PilotesOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for managing Pilotes Orders.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pilotes Orders", description = "Manage Pilotes Orders (create, update, search, etc.)")
public class PilotesOrderController {

    private final PilotesOrderService pilotesOrderService;

    /**
     * Get a paginated list of all orders.
     * Example: GET /api/orders?page=0&size=5
     */
    @Operation(
            summary = "Get all orders (paginated)",
            description = "Retrieves a paginated list of all existing Pilotes Orders."
    )
    @GetMapping
    public PilotesOrdersPageable getAllOrders(
            @ParameterObject Pageable pageable
    ) {
        log.info("GET /api/orders, pageable={}", pageable);
        return pilotesOrderService.getAllOrders(pageable);
    }

    /**
     * Get a single order by its ID (UUID).
     * Example: GET /api/orders/{id}
     */
    @Operation(
            summary = "Get an order by ID",
            description = "Retrieves a single Pilotes Order using its UUID."
    )
    @GetMapping("/{id}")
    public PilotesOrder getOrderById(
            @Parameter(description = "UUID of the order", example = "c0322824-67f2-410a-952c-89bbe9a01412")
            @PathVariable UUID id
    ) throws PilotesOrderException {
        log.info("GET /api/orders/{}", id);
        return pilotesOrderService.getOrderById(id);
    }

    /**
     * Search orders by partial name (first or last).
     * Endpoint is secured if needed by Spring Security config.
     * Example: GET /api/orders/search?q=John&page=0&size=5
     */
    @Operation(
            summary = "Search orders by partial name",
            description = "Find all Pilotes Orders for customers whose first/last name contains the query string."
    )
    @GetMapping("/search")
    public PilotesOrdersPageable searchOrdersByName(
            @Parameter(description = "Partial name to search for", example = "John")
            @RequestParam("q") String query,
            @ParameterObject Pageable pageable
    ) {
        return pilotesOrderService.searchOrdersByName(query, pageable);
    }

    /**
     * Create a new Pilotes Order.
     * Example: POST /api/orders
     */
    @Operation(
            summary = "Create a new Pilotes Order",
            description = "Creates a new Pilotes Order with 5, 10, or 15 pilotes, validating all fields."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PilotesOrder createOrder(
            @Parameter(description = "Payload to create a new Pilotes Order")
            @RequestBody @Valid PilotesOrderRequest pilotesOrderRequest
    ) {
        log.info("POST /api/orders, request={}", pilotesOrderRequest);
        return pilotesOrderService.createOrder(pilotesOrderRequest);
    }

    /**
     * Update an existing Pilotes Order within 5 minutes of creation.
     * Example: PUT /api/orders/{id}
     */
    @Operation(
            summary = "Update an existing Pilotes Order",
            description = "Updates a Pilotes Order within 5 minutes of creation. After that, the order cannot be changed."
    )
    @PatchMapping("/{id}")
    public PilotesOrder updateOrder(
            @Parameter(description = "UUID of the order to update")
            @PathVariable UUID id,
            @Parameter(description = "Updated fields for the Pilotes Order")
            @RequestBody @Valid PilotesOrderUpdateRequest pilotesOrderUpdateRequest
    ) throws PilotesOrderException {
        log.info("PUT /api/orders/{}, request={}", id, pilotesOrderUpdateRequest);
        return pilotesOrderService.updateOrder(id, pilotesOrderUpdateRequest);
    }

    @Operation(
            summary = "Cancel an existing Pilotes Order",
            description = "Cancels a Pilotes Order by its UUID."
    )
    @DeleteMapping("/{id}")
    public void cancelOrder(
            @Parameter(description = "UUID of the order to cancel")
            @PathVariable UUID id
    ) throws PilotesOrderException {
        log.info("DELETE /api/orders/{}", id);
        pilotesOrderService.cancelOrder(id);
    }
}
