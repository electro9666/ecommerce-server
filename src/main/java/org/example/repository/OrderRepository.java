package org.example.repository;

import org.example.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

	public Page<Order> findByUserId(@Param("userId") long userId, Pageable pageable);
	public Page<Order> findByIdAndUserId(@Param("id") long id, @Param("userId") long userId, Pageable pageable);
	public Page<Order> findBySellerId(@Param("sellerId") long sellerId, Pageable pageable);
	public Page<Order> findByIdAndSellerId(@Param("id") long id, @Param("sellerId") long userId, Pageable pageable);
}
