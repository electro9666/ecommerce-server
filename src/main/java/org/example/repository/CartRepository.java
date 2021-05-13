package org.example.repository;

import org.example.constant.CartStatus;
import org.example.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

	@EntityGraph(attributePaths = { "product" }, type = EntityGraph.EntityGraphType.LOAD)
	public Page<Cart> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") CartStatus status, Pageable pageable);
	
	public Cart findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
