package org.example.repository;

import org.example.entity.CashHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CashHistoryRepository extends JpaRepository<CashHistory, Long> {
	
	public Page<CashHistory> findByUserId(@Param("userId") long userId, Pageable pageable);
}
