package org.example.dto;

import java.time.LocalDateTime;

import org.example.constant.CashHistoryReason;
import org.example.entity.CashHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashHistoryDto {
	private Long id;
	private int cash;
	private CashHistoryReason reason;
	private Long orderId; 
	
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	
	public CashHistoryDto(CashHistory cashHistory) {
		if (cashHistory == null) return;
		this.id = cashHistory.getId();
		this.cash = cashHistory.getCash();
		this.reason = cashHistory.getReason();
		if (cashHistory.getOrder() != null) {
			this.orderId = cashHistory.getOrder().getId();
		}
		
		this.regDate = cashHistory.getRegDate();
		this.updateDate = cashHistory.getUpdateDate();
	}
}
