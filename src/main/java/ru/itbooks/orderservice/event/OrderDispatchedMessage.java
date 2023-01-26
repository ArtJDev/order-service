package ru.itbooks.orderservice.event;

public record OrderDispatchedMessage(
        Long orderId
) {
}
