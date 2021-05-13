package org.example.constant;

// 주문 -> 상품준비중 -> 배송중 -> 구매확정
// (구매확정---------------------------)
// (취소----------)
public enum OrderStatus {
	ORDER, PREPARE, SHIPPING, CONFIRM, CANCEL
}
