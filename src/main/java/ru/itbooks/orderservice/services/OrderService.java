package ru.itbooks.orderservice.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itbooks.orderservice.clients.ProductClient;
import ru.itbooks.orderservice.dto.Book;
import ru.itbooks.orderservice.entities.Order;
import ru.itbooks.orderservice.entities.OrderStatus;
import ru.itbooks.orderservice.repositories.OrderRepository;

@Service
public class OrderService {
    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public OrderService(ProductClient productClient, OrderRepository orderRepository) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String article, int quantity) {
        return productClient.getBookByArticle(article)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectedOrder(article, quantity))
                .flatMap(orderRepository::save);
    }

    private static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.article(), book.title() + " - " + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    private static Order buildRejectedOrder(String bookArticle, int quantity) {
        return Order.of(bookArticle, null, null, quantity, OrderStatus.REJECTED);
    }
}
