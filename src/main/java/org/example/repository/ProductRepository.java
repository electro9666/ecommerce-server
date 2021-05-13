package org.example.repository;

import org.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select p from Product p where p.store.seller.id = :sellerId")
	Page<Product> findBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);

	@EntityGraph(attributePaths = { "store", "category" }, type = EntityGraph.EntityGraphType.LOAD)
	@Query("select p from Product p where p.id = :id and p.store.seller.id = :sellerId")
	Product productDetail(@Param("sellerId") Long sellerId, @Param("id") Long id);
}
