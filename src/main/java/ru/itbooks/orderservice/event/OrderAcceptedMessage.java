package ru.itbooks.orderservice.event;

public record OrderAcceptedMessage(
        Long orderId
) {
}