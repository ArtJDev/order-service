package ru.itbooks.orderservice.models;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.itbooks.orderservice.dto.OrderRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OrderRequestJsonTests {

    @Autowired
    private JacksonTester<OrderRequest> json;

    @Test
    void serializeTest() throws IOException {
        var orderRequest = new OrderRequest("12345", 3);
        var jsonContent = json.write(orderRequest);
        assertThat(jsonContent).extractingJsonPathStringValue("@.article").isEqualTo(orderRequest.article());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.quantity").isEqualTo(orderRequest.quantity());
    }

    @Test
    void deserializedTest() throws IOException {
        var content = """
                {
                    "article":"12345",
                    "quantity":3
                }
                """;
        assertThat(json.parse(content)).usingRecursiveComparison().isEqualTo(new OrderRequest("12345", 3));
    }
}