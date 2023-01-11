package ru.itbooks.orderservice.clients;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.itbooks.orderservice.dto.Book;

import java.io.IOException;

public class ProductClientTests {
    private MockWebServer mockWebServer;
    private ProductClient productClient;

    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString())
                .build();
        this.productClient = new ProductClient(webClient);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void getBookExistTest() {
        var article = "12345";
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "article": %s,
                        "title": "Title",
                        "author": "Author",
                        "price": 100.0
                        }
                        """.formatted(article));

        mockWebServer.enqueue(mockResponse);

        Mono<Book> book = productClient.getBookByArticle(article);

        StepVerifier.create(book)
                .expectNextMatches(b -> b.article().equals(article))
                .verifyComplete();
    }

    @Test
    void getBookNotExistTest() {
        var article = "12345";
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404);

        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(productClient.getBookByArticle(article))
                .expectNextCount(0)
                .verifyComplete();
    }
}