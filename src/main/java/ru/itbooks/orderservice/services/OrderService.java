package ru.itbooks.orderservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itbooks.orderservice.clients.ProductClient;
import ru.itbooks.orderservice.dto.Book;
import ru.itbooks.orderservice.entities.Order;
import ru.itbooks.orderservice.entities.OrderStatus;
import ru.itbooks.orderservice.event.OrderAcceptedMessage;
import ru.itbooks.orderservice.event.OrderDispatchedMessage;
import ru.itbooks.orderservice.repositories.OrderRepository;
import org.springframework.cloud.stream.function.StreamBridge;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    public OrderService(ProductClient productClient, OrderRepository orderRepository, StreamBridge streamBridge) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.streamBridge = streamBridge;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Mono<Order> submitOrder(String article, int quantity) {
        return productClient.getBookByArticle(article)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectedOrder(article, quantity))
                .flatMap(orderRepository::save)
                .doOnNext(this::publishOrderAcceptedEvent);
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.article(), book.title() + " - " + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(String bookArticle, int quantity) {
        return Order.of(bookArticle, null, null, quantity, OrderStatus.REJECTED);
    }

    public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux) {
        return flux.flatMap(message -> orderRepository.findById(message.orderId()))
                .map(this::buildDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    private Order buildDispatchedOrder(Order existedOrder) {
        return new Order(
                existedOrder.id(),
                existedOrder.bookArticle(),
                existedOrder.bookName(),
                existedOrder.bookPrice(),
                existedOrder.quantity(),
                OrderStatus.DISPATCHED,
                existedOrder.createdDate(),
                existedOrder.lastModifiedDate(),
                existedOrder.version());
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.status().equals(OrderStatus.ACCEPTED)) {
            return;
        }
        var oderAcceptedMessage = new OrderAcceptedMessage(order.id());
        log.info("Отправка события принятого заказа с id {}", order.id());
        var result = streamBridge.send("acceptOrder-out-0", oderAcceptedMessage);
        log.info("Результат отправки данных для заказа с id {}: {}", order.id(), result);
    }
}