package org.gangel.cloud.dataservice.repository;

import org.gangel.cloud.dataservice.entity.OrderItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends PagingAndSortingRepository<OrderItem, Long> {

}
