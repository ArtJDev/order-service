package ru.itbooks.orderservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.itbooks.orderservice.dto.OrderRequest;
import ru.itbooks.orderservice.entities.Order;
import ru.itbooks.orderservice.entities.OrderStatus;
import ru.itbooks.orderservice.services.OrderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@WebFluxTest(OrderController.class)
public class OrderControllerWebFluxTests {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectOrderTest() {
        var orderRequest = new OrderRequest("12345", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.article(), orderRequest.quantity());
        given(orderService.submitOrder(orderRequest.article(), orderRequest.quantity())).willReturn(Mono.just(expectedOrder));

        webTestClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(actualOrder -> {
                    assertThat(actualOrder).isNotNull();
                    assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });
    }
}