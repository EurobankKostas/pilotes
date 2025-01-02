package com.tui.proof.application.service;

import static com.tui.proof.utils.ClientTestUtil.CLIENT;
import static com.tui.proof.utils.ClientTestUtil.EXISTING_CLIENT;
import static com.tui.proof.utils.PilotesOrderRequestTestUtil.PILOTES_ORDER_REQUEST;
import static com.tui.proof.utils.PilotesOrderTestUtil.CANCELLED_PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrderTestUtil.EXISTING_PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrderTestUtil.PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrderTestUtil.UPDATED_PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrderUpdateRequestTestUtil.PILOTES_ORDER_UPDATE_REQUEST;
import static com.tui.proof.utils.PilotesOrdersPageableTestUtil.PILOTES_ORDERS_PAGEABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import com.tui.proof.domain.exception.PilotesOrderException;
import com.tui.proof.domain.factory.Clock;
import com.tui.proof.domain.model.Client;
import com.tui.proof.domain.model.PilotesOrder;
import com.tui.proof.infrastructure.config.PilotesProperties;
import com.tui.proof.infrastructure.database.PilotesClientRepository;
import com.tui.proof.infrastructure.database.PilotesOrderRepository;
import com.tui.proof.utils.ClientTestUtil;
import com.tui.proof.utils.PilotesOrderTestUtil;

@ExtendWith(MockitoExtension.class)
class PilotesOrderServiceTest {

    @Mock
    Clock clock;
    @Mock
    PilotesProperties pilotesProperties;
    @Mock
    PilotesOrderRepository pilotesOrderRepository;
    @Mock
    PilotesClientRepository pilotesClientRepository;
    @InjectMocks
    PilotesOrderService sut;

    @Test
    void searchOrdersByName() {
        //given
        when(pilotesOrderRepository.searchByName("john", PageRequest.of(0, 1)))
            .thenReturn(
                new PageImpl<>(
                    List.of(EXISTING_PILOTES_ORDER))
            );

        //when
        var result = sut.searchOrdersByName("john", PageRequest.of(0, 1));

        //then
        assertThat(result)
            .isNotNull()
            .isEqualTo(PILOTES_ORDERS_PAGEABLE);

    }

    @Test
    void getOrderById() throws PilotesOrderException {
        //given
        when(pilotesOrderRepository.findById(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.of(EXISTING_PILOTES_ORDER));

        //when
        var result = sut.getOrderById(EXISTING_PILOTES_ORDER.getId());

        //then
        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison().isEqualTo(EXISTING_PILOTES_ORDER);
    }

    @Test
    void getOrderById_PilotesOrderException() {
        //given
        when(pilotesOrderRepository.findById(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> sut.getOrderById(EXISTING_PILOTES_ORDER.getId()))
            .isInstanceOf(PilotesOrderException.class)
            .hasMessage("Order not found: " + EXISTING_PILOTES_ORDER.getId());
    }

    @Test
    void getAllOrders() {
        //given
        when(pilotesOrderRepository.findAll(PageRequest.of(0, 1)))
            .thenReturn(
                new PageImpl<>(
                    List.of(EXISTING_PILOTES_ORDER))
            );

        //when
        var result = sut.getAllOrders(PageRequest.of(0, 1));

        //then
        assertThat(result)
            .isNotNull()
            .isEqualTo(PILOTES_ORDERS_PAGEABLE);

    }

    @Test
    void createOrder_existing_client() {
        //given
        when(pilotesProperties.getPricePerPilotes()).thenReturn(1.33);
        when(pilotesClientRepository.findByEmail(PILOTES_ORDER_REQUEST.email())).thenReturn(
            Optional.of(EXISTING_CLIENT));
        when(pilotesOrderRepository.save(PILOTES_ORDER)).thenReturn(EXISTING_PILOTES_ORDER);
        ArgumentCaptor<PilotesOrder> captor = ArgumentCaptor.forClass(PilotesOrder.class);

        //when
        var result = sut.createOrder(PILOTES_ORDER_REQUEST);

        //then
        verify(pilotesOrderRepository).save(captor.capture());
        assertThat(captor.getValue())
            .isNotNull()
            .usingRecursiveComparison().isEqualTo(PILOTES_ORDER);
        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison().isEqualTo(EXISTING_PILOTES_ORDER);
    }

    @Test
    void createOrder_new_client() {
        //given
        when(pilotesProperties.getPricePerPilotes()).thenReturn(1.33);
        when(pilotesClientRepository.findByEmail(PILOTES_ORDER_REQUEST.email())).thenReturn(Optional.empty());
        when(pilotesClientRepository.save(CLIENT)).thenReturn(ClientTestUtil.copyOf(EXISTING_CLIENT));
        when(pilotesOrderRepository.save(PILOTES_ORDER)).thenReturn(PilotesOrderTestUtil.copyOf(EXISTING_PILOTES_ORDER));
        ArgumentCaptor<PilotesOrder> orderCaptor = ArgumentCaptor.forClass(PilotesOrder.class);
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

        //when
        var result = sut.createOrder(PILOTES_ORDER_REQUEST);

        //then
        verify(pilotesClientRepository).save(clientCaptor.capture());
        verify(pilotesOrderRepository).save(orderCaptor.capture());
        assertThat(clientCaptor.getValue())
            .isNotNull()
            .usingRecursiveComparison().isEqualTo(CLIENT);
        assertThat(orderCaptor.getValue())
            .isNotNull()
            .usingRecursiveComparison().isEqualTo(PILOTES_ORDER);
        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison().isEqualTo(EXISTING_PILOTES_ORDER);
    }

    @Test
    void updateOrder() throws PilotesOrderException {
        //given
        when(pilotesProperties.getPricePerPilotes()).thenReturn(1.33);
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.of(PilotesOrderTestUtil.copyOf(EXISTING_PILOTES_ORDER)));
        when(pilotesOrderRepository.save(EXISTING_PILOTES_ORDER)).thenReturn(UPDATED_PILOTES_ORDER);
        when(clock.now()).thenReturn(EXISTING_PILOTES_ORDER.getCreationTime());

        //when
        var result = sut.updateOrder(EXISTING_PILOTES_ORDER.getId(), PILOTES_ORDER_UPDATE_REQUEST);

        //then
        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison().isEqualTo(UPDATED_PILOTES_ORDER);
    }

    @Test
    void updateOrder_order_not_found() {
        //given
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.empty());

        //when
        assertThatThrownBy(
            () -> sut.updateOrder(EXISTING_PILOTES_ORDER.getId(), PILOTES_ORDER_UPDATE_REQUEST))
            .isInstanceOf(PilotesOrderException.class)
            .hasMessage("Order not found: " + EXISTING_PILOTES_ORDER.getId())
            .extracting("status")
            .isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void updateOrder_late_update() {
        //given
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.of(EXISTING_PILOTES_ORDER));
        when(clock.now()).thenReturn(EXISTING_PILOTES_ORDER.getCreationTime().plusMinutes(10));

        //when
        assertThatThrownBy(
            () -> sut.updateOrder(EXISTING_PILOTES_ORDER.getId(), PILOTES_ORDER_UPDATE_REQUEST))
            .isInstanceOf(PilotesOrderException.class)
            .hasMessage("Cannot update this order after 5 minutes have passed.")
            .extracting("status")
            .isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void updateOrder_cancelled_order() {
        //given
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.of(CANCELLED_PILOTES_ORDER));

        //when
        assertThatThrownBy(
            () -> sut.updateOrder(EXISTING_PILOTES_ORDER.getId(), PILOTES_ORDER_UPDATE_REQUEST))
            .isInstanceOf(PilotesOrderException.class)
            .hasMessage("Cannot update a cancelled order.")
            .extracting("status")
            .isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void cancelOrder() throws PilotesOrderException {
        //given
        var order = PilotesOrderTestUtil.copyOf(EXISTING_PILOTES_ORDER);
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.of(order));
        when(clock.now()).thenReturn(EXISTING_PILOTES_ORDER.getCreationTime());

        //when
        sut.cancelOrder(EXISTING_PILOTES_ORDER.getId());

        //then
        verify(pilotesOrderRepository).save(CANCELLED_PILOTES_ORDER);
    }

    @Test
    void cancelOrder_order_not_found() {
        //given
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.empty());

        //when
        assertThatThrownBy(
            () -> sut.cancelOrder(EXISTING_PILOTES_ORDER.getId()))
            .isInstanceOf(PilotesOrderException.class)
            .hasMessage("Order not found: " + EXISTING_PILOTES_ORDER.getId())
            .extracting("status")
            .isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void cancelOrder_late_update() {
        //given
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.of(EXISTING_PILOTES_ORDER));
        when(clock.now()).thenReturn(EXISTING_PILOTES_ORDER.getCreationTime().plusMinutes(10));

        //when
        assertThatThrownBy(
            () -> sut.cancelOrder(EXISTING_PILOTES_ORDER.getId()))
            .isInstanceOf(PilotesOrderException.class)
            .hasMessage("Cannot update this order after 5 minutes have passed.")
            .extracting("status")
            .isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void cancelOrder_cancelled_order() {
        //given
        when(pilotesOrderRepository.findByIdForUpdate(EXISTING_PILOTES_ORDER.getId()))
            .thenReturn(Optional.of(CANCELLED_PILOTES_ORDER));

        //when
        assertThatThrownBy(
            () -> sut.cancelOrder(EXISTING_PILOTES_ORDER.getId()))
            .isInstanceOf(PilotesOrderException.class)
            .hasMessage("Cannot update a cancelled order.")
            .extracting("status")
            .isEqualTo(HttpStatus.BAD_REQUEST);

    }
}
