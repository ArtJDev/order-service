package ru.itbooks.orderservice.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.itbooks.orderservice.dto.Book;

import java.time.Duration;

@Component
public class ProductClient {
    private static final String PRODUCT_ROOT_API = "/books/";
    private final WebClient webClient;

    public ProductClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByArticle(String article) {
        return webClient
                .get()
                .uri(PRODUCT_ROOT_API + article)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(3), Mono.empty())
                .onErrorResume(WebClientResponseException.class, error -> Mono.empty())
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, error -> Mono.empty());
    }
}