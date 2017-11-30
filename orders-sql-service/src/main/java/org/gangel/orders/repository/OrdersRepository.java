package org.gangel.orders.repository;

import org.gangel.orders.entity.Orders;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface OrdersRepository extends PagingAndSortingRepository<Orders, Long> {

    Stream<Orders> findByCustomerId(long customerId);
}
