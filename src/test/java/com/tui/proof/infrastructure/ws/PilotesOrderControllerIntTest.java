package com.tui.proof.infrastructure.ws;

import static com.tui.proof.utils.ClientTestUtil.CLIENT;
import static com.tui.proof.utils.ClientTestUtil.CLIENT_2;
import static com.tui.proof.utils.PilotesOrderRequestTestUtil.PILOTES_ORDER_REQUEST;
import static com.tui.proof.utils.PilotesOrderRequestTestUtil.PILOTES_ORDER_REQUEST_BAD_REQUEST;
import static com.tui.proof.utils.PilotesOrderTestUtil.EXISTING_PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrderTestUtil.PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrderTestUtil.UPDATED_PILOTES_ORDER;
import static com.tui.proof.utils.PilotesOrderUpdateRequestTestUtil.PILOTES_ORDER_UPDATE_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tui.proof.application.dto.PilotesOrdersPageable;
import com.tui.proof.domain.factory.Clock;
import com.tui.proof.domain.model.Client;
import com.tui.proof.domain.model.PilotesOrder;
import com.tui.proof.infrastructure.config.PilotesProperties;
import com.tui.proof.infrastructure.database.PilotesClientRepository;
import com.tui.proof.infrastructure.database.PilotesOrderRepository;
import com.tui.proof.utils.ClientTestUtil;
import com.tui.proof.utils.PilotesOrderTestUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PilotesOrderControllerIntTest {

    @MockitoSpyBean
    Clock clock;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PilotesProperties pilotesProperties;
    @MockitoSpyBean
    PilotesOrderRepository pilotesOrderRepository;
    @MockitoSpyBean
    PilotesClientRepository pilotesClientRepository;
    @Autowired
    PilotesOrderController pilotesOrderController;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        pilotesOrderRepository.deleteAll();
        pilotesClientRepository.deleteAll();
    }

    @Test
    void getAllOrders() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        pilotesOrderRepository.save(order);

        //when
        var result = mockMvc
            .perform(MockMvcRequestBuilders.get("/orders?page=0&size=5"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var object = mapper.readValue(result, PilotesOrdersPageable.class);

        //then
        assertThat(object)
            .isNotNull();
        assertThat(object)
            .extracting("page", "size", "totalPages", "totalElements")
            .containsExactly(0, 5, 1, 1L);
        assertThat(object.orders())
            .hasSize(1)
            .containsExactly(order);
    }

    @Test
    void getOrderById() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        pilotesOrderRepository.save(order);

        //when
        var result = mockMvc
            .perform(MockMvcRequestBuilders.get("/orders/" + order.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var object = mapper.readValue(result, PilotesOrder.class);

        //then
        assertThat(object)
            .isNotNull()
            .isEqualTo(order);
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "BPO")
    void searchOrdersByName() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var client2 = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT_2));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        pilotesOrderRepository.save(order);
        var order2 = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order2.setClient(client2);
        pilotesOrderRepository.save(order2);

        //when
        var result = mockMvc
            .perform(MockMvcRequestBuilders.get("/orders/search?q=J&page=0&size=5"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var object = mapper.readValue(result, PilotesOrdersPageable.class);

        //then
        assertThat(object)
            .isNotNull();
        assertThat(object)
            .extracting("page", "size", "totalPages", "totalElements")
            .containsExactly(0, 5, 1, 1L);
        assertThat(object.orders())
            .hasSize(1)
            .containsExactly(order);
    }

    @Test
    void searchOrdersByName_unauthorized() throws Exception {
        //when + then
        mockMvc
            .perform(MockMvcRequestBuilders.get("/orders/search?q=J&page=0&size=5"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void createOrder_existing_client() throws Exception {
        //given
        verify(pilotesClientRepository).deleteAll();
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        verify(pilotesClientRepository, times(1)).save(any(Client.class));
        //when
        var result = mockMvc
            .perform(
                MockMvcRequestBuilders.post("/orders")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(PILOTES_ORDER_REQUEST))
            )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var object = mapper.readValue(result, PilotesOrder.class);

        //then
        verify(pilotesOrderRepository, times(1)).save(any(PilotesOrder.class));
        verify(pilotesClientRepository, times(1)).findByEmail(PILOTES_ORDER_REQUEST.email());
        verifyNoMoreInteractions(pilotesClientRepository);
        assertThat(object)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id", "creationTime", "updateTime", "client")
            .isEqualTo(EXISTING_PILOTES_ORDER);
        assertThat(object.getClient())
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(client);
    }

    @Test
    void createOrder() throws Exception {
        //when
        var result = mockMvc
            .perform(
                MockMvcRequestBuilders.post("/orders")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(PILOTES_ORDER_REQUEST))
            )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var object = mapper.readValue(result, PilotesOrder.class);

        //then
        verify(pilotesOrderRepository, times(1)).save(any(PilotesOrder.class));
        verify(pilotesClientRepository, times(1)).save(any(Client.class));
        assertThat(object)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id", "creationTime", "updateTime", "client")
            .isEqualTo(EXISTING_PILOTES_ORDER);
        assertThat(object.getClient())
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(CLIENT);
    }

    @Test
    void createOrder_bad_request() throws Exception {
        //when
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/orders")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(PILOTES_ORDER_REQUEST_BAD_REQUEST))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.detail").value(StringContains.containsString("Number of pilotes must be 5, 10, or 15.")))
            .andExpect(jsonPath("$.detail").value(StringContains.containsString("Street must not be blank.")));
    }

    @Test
    void updateOrder() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        pilotesOrderRepository.save(order);

        //when
        var result = mockMvc
            .perform(
                MockMvcRequestBuilders.patch("/orders/" + order.getId())
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(PILOTES_ORDER_UPDATE_REQUEST))
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var object = mapper.readValue(result, PilotesOrder.class);

        //then
        assertThat(object)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id", "creationTime", "updateTime", "client")
            .isEqualTo(UPDATED_PILOTES_ORDER);
        assertThat(object.getClient())
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(CLIENT);
    }

    @Test
    void updateOrder_not_found() throws Exception {
        //when + then
        mockMvc
            .perform(
                MockMvcRequestBuilders.patch("/orders/00000000-0000-0000-0000-000000000001")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(PILOTES_ORDER_UPDATE_REQUEST))
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(jsonPath("$.detail").value("Order not found: 00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void updateOrder_cancelled() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        order.setCancelled(true);
        pilotesOrderRepository.save(order);

        //when
        mockMvc
            .perform(
                MockMvcRequestBuilders.patch("/orders/" + order.getId())
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(PILOTES_ORDER_UPDATE_REQUEST))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Cannot update a cancelled order."));
    }

    @Test
    void updateOrder_late_update() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        pilotesOrderRepository.save(order);

        //when
        when(clock.now()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 15));
        mockMvc
            .perform(
                MockMvcRequestBuilders.patch("/orders/" + order.getId())
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(PILOTES_ORDER_UPDATE_REQUEST))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Cannot update this order after 5 minutes have passed."));
    }

    @Test
    void cancelOrder() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        pilotesOrderRepository.save(order);

        //when
        mockMvc
            .perform(MockMvcRequestBuilders.delete("/orders/" + order.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        //then
        verify(pilotesOrderRepository, times(2)).save(any(PilotesOrder.class));
    }

    @Test
    void cancelOrder_not_found() throws Exception {
        //when + then
        mockMvc
            .perform(MockMvcRequestBuilders.delete("/orders/00000000-0000-0000-0000-000000000001"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(jsonPath("$.detail").value("Order not found: 00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void cancelOrder_already_cancelled() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        order.setCancelled(true);
        pilotesOrderRepository.save(order);

        //when
        mockMvc
            .perform(MockMvcRequestBuilders.delete("/orders/" + order.getId()))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Cannot update a cancelled order."));
    }

    @Test
    void cancelOrder_late_update() throws Exception {
        //given
        var client = pilotesClientRepository.save(ClientTestUtil.copyOf(CLIENT));
        var order = PilotesOrderTestUtil.copyOf(PILOTES_ORDER);
        order.setClient(client);
        pilotesOrderRepository.save(order);

        //when
        when(clock.now()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 15));
        mockMvc
            .perform(MockMvcRequestBuilders.delete("/orders/" + order.getId()))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Cannot update this order after 5 minutes have passed."));
    }


}
