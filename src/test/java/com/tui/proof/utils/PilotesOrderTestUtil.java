package com.tui.proof.utils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.tui.proof.domain.model.Client;
import com.tui.proof.domain.model.PilotesOrder;
import com.tui.proof.domain.value.Address;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PilotesOrderTestUtil {

    public static PilotesOrder copyOf(PilotesOrder pilotesOrder) {
        var order = new PilotesOrder(
            pilotesOrder.getId(),
            null,
            pilotesOrder.getNumberOfPilotes(),
            pilotesOrder.getTotalPrice(),
            pilotesOrder.getCreationTime(),
            pilotesOrder.getUpdateTime(),
            null,
            pilotesOrder.isCancelled()
        );
        if(Objects.nonNull(pilotesOrder.getDeliveryAddress())){
            order.setDeliveryAddress(
                new Address(
                    pilotesOrder.getDeliveryAddress().getStreet(),
                    pilotesOrder.getDeliveryAddress().getPostcode(),
                    pilotesOrder.getDeliveryAddress().getCity(),
                    pilotesOrder.getDeliveryAddress().getCountry(),
                    pilotesOrder.getDeliveryAddress().getAddressNumber()
                )
            );
        }
        if(Objects.nonNull(pilotesOrder.getClient())){
            order.setClient(
                new Client(
                    pilotesOrder.getClient().getId(),
                    pilotesOrder.getClient().getFirstName(),
                    pilotesOrder.getClient().getLastName(),
                    pilotesOrder.getClient().getPhoneNumber(),
                    pilotesOrder.getClient().getEmail()
                )
            );
        }
        return order;
    }

    public static final PilotesOrder PILOTES_ORDER = new PilotesOrder(
        null,
        AddressTestUtil.ADDRESS,
        5,
        6.65,
        null,
        null,
        ClientTestUtil.EXISTING_CLIENT,
        false
    );

    public static final PilotesOrder EXISTING_PILOTES_ORDER = new PilotesOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000001"),
        AddressTestUtil.ADDRESS,
        5,
        6.65,
        LocalDateTime.of(2021, 1, 1, 0, 0),
        LocalDateTime.of(2021, 1, 1, 0, 0),
        ClientTestUtil.EXISTING_CLIENT,
        false
    );

    public static final PilotesOrder UPDATED_PILOTES_ORDER = new PilotesOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000001"),
        AddressTestUtil.ADDRESS_2,
        10,
        13.3,
        LocalDateTime.of(2021, 1, 1, 0, 0),
        LocalDateTime.of(2021, 1, 1, 0, 0),
        ClientTestUtil.EXISTING_CLIENT,
        false
    );

    public static final PilotesOrder CANCELLED_PILOTES_ORDER = new PilotesOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000001"),
        AddressTestUtil.ADDRESS,
        5,
        6.65,
        LocalDateTime.of(2021, 1, 1, 0, 0),
        LocalDateTime.of(2021, 1, 1, 0, 0),
        ClientTestUtil.EXISTING_CLIENT,
        true
    );

}
