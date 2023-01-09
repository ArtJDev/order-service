package ru.itbooks.orderservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotBlank(message = "Необходимо указать артикул книги")
        String article,
        @NotNull(message = "Необходимо указать количество книг")
        @Min(value = 1, message = "Минимальное количество выбранной книги для заказа 1 единица")
        @Max(value = 5, message = "Нельзя заказать более 5 единиц одной выбранной книги")
        Integer quantity
) {
}
