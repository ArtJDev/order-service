package ru.itbooks.orderservice.models;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.itbooks.orderservice.entities.Order;
import ru.itbooks.orderservice.entities.OrderStatus;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OrderJsonTests {
    @Autowired
    private JacksonTester<Order> json;

    @Test
    void serializedTest() throws IOException {
        var order = new Order(3L, "12345", "Title", 100.0, 3, OrderStatus.ACCEPTED, Instant.now(), Instant.now(), 5);
        var jsonContent = json.write(order);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(order.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.bookArticle").isEqualTo(order.bookArticle());
        assertThat(jsonContent).extractingJsonPathStringValue("@.bookName").isEqualTo(order.bookName());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.bookPrice").isEqualTo(order.bookPrice());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.quantity").isEqualTo(order.quantity());
        assertThat(jsonContent).extractingJsonPathStringValue("@.status").isEqualTo(order.status().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate").isEqualTo(order.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate").isEqualTo(order.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version").isEqualTo(order.version());
    }

    @Test
    void deserializedTest() throws IOException {
        var instant = Instant.parse("2022-12-23T16:07:37.124138Z");
        var content = """
                {
                    "id":3,
                    "bookArticle":"Title",
                    "bookPrice":100.0,
                    "quantity":3,
                    "status":"ACCEPTED",
                    "createdDate":"2022-12-23T16:07:37.124138Z",
                    "lastModifiedDate":"2022-12-23T16:07:37.124138Z",
                    "version":5
                }
                """;
        assertThat(json.parse(content)).usingRecursiveComparison()
                .isEqualTo(new Order(3L, "12345", "Title", 100.0, 3, OrderStatus.ACCEPTED, instant, instant, 5));
    }
}