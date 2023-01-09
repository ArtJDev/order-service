package ru.itbooks.orderservice.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.itbooks.orderservice.dto.Book;

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
                .bodyToMono(Book.class);
    }
}