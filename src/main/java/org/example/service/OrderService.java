package org.example.service;

import java.util.List;

import org.example.constant.CashHistoryReason;
import org.example.constant.OrderStatus;
import org.example.entity.CartOption;
import org.example.entity.CashHistory;
import org.example.entity.ProductOption;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.repository.CartRepository;
import org.example.repository.CashHistoryRepository;
import org.example.repository.CategoryRepository;
import org.example.repository.OptionRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.repository.ProductSupport;
import org.example.repository.SellerRepository;
import org.example.repository.StoreRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final StoreRepository storeRepository;
	private final SellerRepository sellerRepository;
	private final OptionRepository optionRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final ProductSupport productSupport;
	private final CashHistoryRepository cashHistoryRepository;

	@Transactional
	public Object orderCancel(Order order) {
		// 주문을 취소할 수 있는 조건이 사용자/판매자별로 다르므로 앞단의 서비스에서 validation 처리함.
		order.changeStatus(OrderStatus.CANCEL);
		
		System.out.println("User 환불");
		User user = order.getUser();
		user.addCash(order.getTotalPrice());
		
		// Cash 히스토리
		CashHistory cashHistory = CashHistory.builder()
		.user(user)
		.cash(order.getTotalPrice())
		.order(order)
		.reason(CashHistoryReason.REFUND)
		.build();
		cashHistoryRepository.save(cashHistory);
		
		// 상품 옵션 재고 롤백
		order.getOrderCarts().stream().forEach((t) -> {
			List<CartOption> cartOptions = t.getCart().getCartOptions();
			cartOptions.stream().forEach((cartOption) -> {
				int count = cartOption.getCount();
				ProductOption option = cartOption.getOption();
				option.addQuantity(count);
			});
		});
		
		return true;
	}
}
