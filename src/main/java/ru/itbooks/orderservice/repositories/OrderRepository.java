package ru.itbooks.orderservice.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.itbooks.orderservice.entities.Order;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}
