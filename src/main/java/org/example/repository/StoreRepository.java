package org.example.repository;

import org.example.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

	Page<Store> findBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);
	
	Store findBySellerIdAndId(@Param("sellerId") Long sellerId, @Param("id") Long id);
}
