package ru.itbooks.orderservice.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.itbooks.orderservice.dto.OrderRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OrderRequestValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void allFieldsCorrectTest() {
        var orderRequest = new OrderRequest("12345", 3);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void fieldArticleIsEmptyTest() {
        var orderRequest = new OrderRequest("", 3);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Необходимо указать артикул книги");
    }

    @Test
    void fieldQuantityIsEmptyTest() {
        var orderRequest = new OrderRequest("12345", null);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Необходимо указать количество книг");
    }

    @Test
    void fieldQuantityIsLowerThenMinTest() {
        var orderRequest = new OrderRequest("12345", 0);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Минимальное количество выбранной книги для заказа 1 единица");
    }

    @Test
    void fieldQuantityIsGreaterThenMaxTest() {
        var orderRequest = new OrderRequest("12345", 6);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Нельзя заказать более 5 единиц одной выбранной книги");
    }
}