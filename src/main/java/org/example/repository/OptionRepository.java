package org.example.repository;

import org.example.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OptionRepository extends JpaRepository<ProductOption, Long> {

	@Modifying
	@Query("delete from ProductOption o where o.product.id = :productId")
	public void deleteByProductId(@Param("productId") Long productId);
}
