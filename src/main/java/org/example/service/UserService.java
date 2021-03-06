package org.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.example.constant.CartStatus;
import org.example.constant.CashHistoryReason;
import org.example.constant.OrderStatus;
import org.example.constant.ProductStatus;
import org.example.dto.CartDto;
import org.example.dto.CartPostDto;
import org.example.dto.CashHistoryDto;
import org.example.dto.OrderDto;
import org.example.dto.PageBaseRequest;
import org.example.dto.PageResultDto;
import org.example.dto.PageSearchProductRequest;
import org.example.dto.ProductDto;
import org.example.entity.Cart;
import org.example.entity.CartOption;
import org.example.entity.CashHistory;
import org.example.entity.Order;
import org.example.entity.OrderOption;
import org.example.entity.Product;
import org.example.entity.ProductOption;
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
import org.example.security.dto.LoginMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
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
	private final OrderService orderService;

	@Transactional(readOnly = true)
	public PageResultDto<Product, ProductDto> recentProductList() {
		Page<Product> findAll = productRepository.findAll(PageRequest.of(0, 10, Direction.DESC, "updateDate"));
		return new PageResultDto<>(findAll, t -> new ProductDto(t));
	}
	@Transactional(readOnly = true)
	public PageResultDto<Product, ProductDto> searchProductList(PageSearchProductRequest p) {
		Page<Product> findAll = productSupport.searchProductForUser(p);
		return new PageResultDto<>(findAll, t -> new ProductDto(t));
	}
	@Transactional(readOnly = true)
	public ProductDto productDetail(long id) {
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return null; // ???????????? ?????? ??????, null
		}
		return new ProductDto(product);
	}
	@Transactional
	public Long addCart(LoginMemberDTO loginMemberDto, CartPostDto cartDto) {
		Product product = productRepository.findById(cartDto.getProductId()).orElseThrow(() -> new IllegalArgumentException("not found product"));
		if (!ProductStatus.OPEN.equals(product.getStatus())) {
			throw new IllegalArgumentException("????????? ?????? ???????????? ???????????????.");			
		}
		User user = userRepository.findById(loginMemberDto.getId()).orElseThrow(() -> new IllegalArgumentException("not found user"));
		Cart cart = Cart.builder()
			.user(user)
			.product(product)
			.status(CartStatus.CART)
			.build();
		List<CartOption> cartOptions = cartDto.getCartOptions().stream().map(t -> {
			ProductOption option = optionRepository.findById(t.getId()).orElse(null);
			if (option.getPrice() != t.getPrice()) {
				throw new IllegalArgumentException("????????? ???????????? ?????????????????????. ????????? ????????????????????????.");				
			}
			if (option.getQuantity() < t.getCount()) {
				throw new IllegalArgumentException(String.format("[%s] ????????? ????????? ?????????????????????. ????????? ??????????????????.", option.getName()));
			}
			CartOption cartOption = CartOption.builder()
				.cart(cart)
				.option(option)
				.price(t.getPrice())
				.count(t.getCount())
				.build();
			return cartOption;
		}).collect(Collectors.toList());
		cart.initCartOptions(cartOptions);
		Cart save = cartRepository.save(cart);
		return save.getId();
	}
	@Transactional(readOnly = true)
	public PageResultDto<Cart, CartDto> getCartList(LoginMemberDTO loginMemberDto) {
		Page<Cart> findAll = cartRepository.findByUserIdAndStatus(loginMemberDto.getId(), CartStatus.CART, PageRequest.of(0, 100000, Direction.DESC, "updateDate"));
		return new PageResultDto<>(findAll, t -> new CartDto(t));
	}
	@Transactional
	public Boolean deleteCart(LoginMemberDTO loginMemberDto, long id) {
		Cart cart = cartRepository.findByIdAndUserId(id, loginMemberDto.getId());
		if (cart == null) {
			throw new IllegalArgumentException("not found cart");
		}
		cartRepository.delete(cart);
		return true;
	}
	public boolean isValid(Cart cart) {
		boolean result = true;
		// ?????? ??????
		if (ProductStatus.CLOSE.equals(cart.getProduct().getStatus())) {
			System.err.println("product close");
			result = false;
		}
		// ?????? ??????
		List<ProductOption> options = cart.getProduct().getOptions();
		for (CartOption cartOption : cart.getCartOptions()) {
			ProductOption findOption = options.stream().filter((t) -> t.getId().equals(cartOption.getOption().getId())).collect(Collectors.toList()).get(0);
			if (findOption == null) {
				System.err.println("findOption == null");
				result = false;
				break;
			}
			if (findOption.getPrice() != cartOption.getPrice()) {
				System.err.println("price is not match");
				result = false;
				break;
			} else if (findOption.getQuantity() < cartOption.getCount()) {
				System.err.println("count is less");
				result = false;
				break;
			}
		}
		return result;
	}
	@Transactional
	public Long addOrder(LoginMemberDTO loginMemberDto) {
		List<Cart> cartList = cartRepository.findByUserIdAndStatus(loginMemberDto.getId(), CartStatus.CART, PageRequest.of(0, 100000, Direction.DESC, "updateDate")).getContent();
		List<Cart> cartForOrderList = cartList.stream().filter((cart) -> isValid(cart)).collect(Collectors.toList());
		if (cartForOrderList.size() == 0) {
			throw new IllegalArgumentException("?????? ????????? ????????? ????????????.");
		}
		// ????????? cash ??????
		int totalPrice = cartForOrderList.stream().mapToInt(cart -> cart.getCartOptions().stream().mapToInt(cartOption -> (cartOption.getCount() * cartOption.getPrice())).sum()).sum();
		User user = cartList.get(0).getUser();
		int userCash = user.getCash();
		if (userCash < totalPrice) {
			throw new IllegalArgumentException("cash??? ???????????????. " + (userCash - totalPrice));
		}
		// ??????
		Order order = Order.builder()
			.user(cartList.get(0).getUser())
			.seller(cartList.get(0).getProduct().getStore().getSeller())
			.orderStatus(OrderStatus.ORDER)
			.totalPrice(totalPrice)
			.orderDate(LocalDateTime.now())
			.build();
		List<OrderOption> orderCarts = cartForOrderList.stream().map(cart2 -> {
			System.out.println("Option ?????? ??????");
			for (int i = 0; i < cart2.getCartOptions().size(); i++) {
				CartOption cartOption = cart2.getCartOptions().get(i);
				ProductOption option = cartOption.getOption();
				option.minusQuantity(cartOption.getCount());
			}
			OrderOption orderCart = OrderOption.builder()
				.order(order)
				.cart(cart2)
				.build();
			cart2.changeStatus(CartStatus.ORDER);
			return orderCart;
		}).collect(Collectors.toList());
		order.initOrderCarts(orderCarts);
		
		// User cash ??????
		user.minusCash(totalPrice);
		// Cash ????????????
		CashHistory cashHistory = CashHistory.builder()
		.user(user)
		.cash(totalPrice * -1)
		.order(order)
		.reason(CashHistoryReason.ORDER)
		.build();
		cashHistoryRepository.save(cashHistory);
		
		Order newOrder = orderRepository.save(order);
		return newOrder.getId();
	}
	@Transactional(readOnly = true)
	public PageResultDto<Order, OrderDto> getOrderList(LoginMemberDTO loginMemberDto, PageBaseRequest p) {
		Page<Order> findAll = orderRepository.findByUserId(loginMemberDto.getId(), PageRequest.of(p.getPage() - 1, p.getTake(), Direction.DESC, "id"));
		return new PageResultDto<>(findAll, t -> new OrderDto(t));
	}
	@Transactional
	public Long orderConfirm(LoginMemberDTO loginMemberDto, Long id) {
		PageRequest pr = PageRequest.of(0, 1, Direction.DESC, "id");
		Page<Order> orderAll = orderRepository.findByIdAndUserId(id, loginMemberDto.getId(), pr);
		Order order = orderAll.getContent().get(0);
		if (order == null) {
			throw new IllegalArgumentException("not found order");
		}
		if (!OrderStatus.SHIPPING.equals(order.getOrderStatus())) {
			throw new IllegalArgumentException("???????????? ?????? ?????? ???????????? ??? ??? ????????????.");
		}
		order.changeStatus(OrderStatus.CONFIRM);
		return order.getId();
	}
	@Transactional
	public Long orderCancel(LoginMemberDTO loginMemberDto, Long id) {
		PageRequest pr = PageRequest.of(0, 1, Direction.DESC, "id");
		Page<Order> orderAll = orderRepository.findByIdAndUserId(id, loginMemberDto.getId(), pr);
		Order order = orderAll.getContent().get(0);
		if (order == null) {
			throw new IllegalArgumentException("not found order");
		}
		if (!OrderStatus.ORDER.equals(order.getOrderStatus()) || !OrderStatus.PREPARE.equals(order.getOrderStatus())) {
			throw new IllegalArgumentException("???????????? ??? ????????????????????? ?????? ??? ??? ????????????.");
		}
		orderService.orderCancel(order);
		return order.getId();
	}
	@Transactional(readOnly = true)
	public PageResultDto<CashHistory, CashHistoryDto> cashList(LoginMemberDTO loginMemberDto, PageBaseRequest p) {
		Page<CashHistory> findAll = cashHistoryRepository.findByUserId(loginMemberDto.getId(), PageRequest.of(p.getPage() - 1, p.getTake(), Direction.DESC, "regDate"));
		return new PageResultDto<>(findAll, t -> new CashHistoryDto(t));
	}
	@Transactional
	public Boolean addCash(LoginMemberDTO loginMemberDto) {
		int cash = 1000000;
		User user = userRepository.findById(loginMemberDto.getId()).orElseThrow(() -> new IllegalArgumentException("not found user"));
		user.addCash(cash);
		userRepository.save(user);
		CashHistory cashHistory = CashHistory.builder()
			.user(user)
			.cash(cash)
			.reason(CashHistoryReason.ADD)
			.build();
		cashHistoryRepository.save(cashHistory);
		return true;
	}
	@Transactional(readOnly = true)
	public Integer cashCurrent(LoginMemberDTO loginMemberDto) {
		User user = userRepository.findById(loginMemberDto.getId()).orElseThrow(() -> new IllegalArgumentException("not found user"));
		return user.getCash();
	}
}
