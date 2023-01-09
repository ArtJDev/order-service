package ru.itbooks.orderservice.dto;

public record Book(
        String article,
        String title,
        String author,
        Double price
) {
}
