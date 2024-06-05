package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.OrderEntity;
import com.platzi.pizza.persistence.projection.OrderSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends ListCrudRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByDateAfter(LocalDateTime date);
    List<OrderEntity> findAllByMethodIn(List<String> methdos);
    @Query(value = "SELECT * FROM pizza_order WHERE  id_customer = :id", nativeQuery = true)
    List<OrderEntity> findCustomerOrders(@Param("id") String idCustomer);

    @Query(value = "SELECT tpo.id_order AS idOrder, cu.name AS customerName, po.date AS orderDate, " +
            "tpo.total AS orderToral, GROUP_CONCAT(pi.name) AS pizzaNames" +
            "FROM pizza_order po " +
            "   INNER JOIN customer cu ON po.id_customer = cu.id_customer", nativeQuery = true)
    OrderSummary findSummary(Integer orderId);
}
