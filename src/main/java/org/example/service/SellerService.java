package org.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.constant.OrderStatus;
import org.example.dto.OrderDto;
import org.example.dto.PageBaseRequest;
import org.example.dto.PageRequestDto;
import org.example.dto.PageResultDto;
import org.example.dto.ProductDto;
import org.example.dto.ProductRequestDto;
import org.example.dto.StoreDto;
import org.example.dto.StoreRequestDto;
import org.example.entity.Category;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.entity.ProductOption;
import org.example.entity.Seller;
import org.example.entity.Store;
import org.example.repository.CategoryRepository;
import org.example.repository.OptionRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.repository.ProductSupport;
import org.example.repository.SellerRepository;
import org.example.repository.StoreRepository;
import org.example.security.dto.LoginMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {

	private final StoreRepository storeRepository;
	private final SellerRepository sellerRepository;
	private final OptionRepository optionRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductSupport productSupport;
	private final OrderRepository orderRepository;
	private final OrderService orderService;
	
	@Transactional(readOnly = true)
	public List<Category> category() {
		return categoryRepository.findAll();
	}
	
	/**
	 * store
	 */
	@Transactional(readOnly = true)
	public PageResultDto<Store, StoreDto> storeList(LoginMemberDTO loginMemberDto, PageBaseRequest p) {
		PageRequest pr = PageRequest.of(p.getPage() - 1, p.getTake(), Direction.DESC, "id");
		Page<Store> findAll = storeRepository.findBySellerId(loginMemberDto.getId(), pr);
		return new PageResultDto<>(findAll, t -> new StoreDto(t));
	}
	@Transactional(readOnly = true)
	public StoreDto storeDetail(LoginMemberDTO loginMemberDto, long id) {
		Store store = storeRepository.findBySellerIdAndId(loginMemberDto.getId(), id);
		if (store == null) {
			throw new IllegalArgumentException("store is null");
		}
		return new StoreDto(store);
	}
	@Transactional
	public Long saveStore(LoginMemberDTO loginMemberDto, StoreRequestDto.Post storeDto) {
		Long storeId = null;
		if (storeDto instanceof StoreRequestDto.Put) {
			storeId = ((StoreRequestDto.Put) storeDto).getId();
		}
		Seller seller = sellerRepository.findById(loginMemberDto.getId()).orElseThrow(() -> new IllegalArgumentException("not found seller"));
		Store store = Store.builder()
				.id(storeId)
				.name(storeDto.getName())
				.status(storeDto.getStatus())
				.seller(seller)
				.build();
		Store save = storeRepository.save(store);
		return save.getId();
	}
	/**
	 * product
	 */
	@Transactional(readOnly = true)
	public PageResultDto<Product, ProductDto> productList(LoginMemberDTO loginMemberDto, PageRequestDto p) {
		PageRequest pr = PageRequest.of(p.getPage() - 1, p.getTake(), Direction.DESC, "id");
//		Page<Product> findAll = productRepository.findBySellerId(loginMemberDto.getId(), pr);
		// 상세 검색을 위해서 queryDsl을 이용함.
		Page<Product> findAll = productSupport.searchProductForSeller(loginMemberDto.getId(), pr, p);
		return new PageResultDto<>(findAll, t -> new ProductDto(t));
	}
	// 자기자신 것만 조회 가능하도록
	@Transactional(readOnly = true)
	public ProductDto productDetail(LoginMemberDTO loginMemberDto, long id) {
		System.out.println("product-----------------");
		Product product = productRepository.productDetail(loginMemberDto.getId(), id);
		if (product == null) {
			throw new IllegalArgumentException("product is null");
		}
		return new ProductDto(product);
	}
	// 자기자신 것만 등록/수정 가능하도록
	@Transactional
	public Long saveProduct(LoginMemberDTO loginMemberDto, ProductRequestDto.Post productDto) {
		Long productId = null;
		if (productDto instanceof ProductRequestDto.Put) {
			productId = ((ProductRequestDto.Put) productDto).getId();
		}
		// TODO option 항목을 삭제하는 것은 화면에서 막았지만, 서버에서 한번 더 검증해야 한다.(다른 옵션이 들어오거나 부족한 경우..) 물론 FK 에러 발생.
		if (productDto.getOptions() == null || productDto.getOptions().size() < 1) {
			throw new IllegalArgumentException("option.size should be more than 0.");
		}
		productDto.getOptions().stream().forEach((option) -> {
			if (option.getPrice() < 1) {
				throw new IllegalArgumentException("option.price should be more than 0.");
			}
		});
		
		if (productId != null) {
			// 수정
			Product product0 = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("not found product"));
			List<ProductOption> options = product0.getOptions();
			if (options == null || options.size() < 1) {
				throw new IllegalArgumentException("options is invalid.");
			}
			
			// 기획: option 개수는 수정불가 (DB에서 조회한 optionId는 모두 포함해야 한다.)
			List<Long> optionIdsByParams = productDto.getOptions().stream().map(t -> t.getId()).collect(Collectors.toList());
			List<Long> optionIdsByDB = options.stream().map((t) -> t.getId()).collect(Collectors.toList());
			for (Long optionId : optionIdsByDB) {
				if (!optionIdsByParams.contains(optionId)) {
					throw new IllegalArgumentException(String.format("there is not option [%s] ", optionId));
				}
			}
		}
		
		Store store = storeRepository.findBySellerIdAndId(loginMemberDto.getId(), productDto.getStoreId());
		if (store == null) {
			throw new IllegalArgumentException("store is null");
		}
		Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("invalid category"));
		
		Product product = Product.builder()
			.id(productId)
			.name(productDto.getName())
			.description(productDto.getDescription())
			.background(productDto.getBackground())
			.price(productDto.getOptions().get(0).getPrice()) // 대표 판매가
			.status(productDto.getStatus())
			.store(store)
			.category(category)
			.build();
		List<ProductOption> options = productDto.getOptions().stream().map((t) -> {
			ProductOption option = ProductOption.builder()
			.id(t.getId()) // id가 null이면 추가 / 아니면 수정
			.name(t.getName())
			.price(t.getPrice())
			.quantity(t.getQuantity())
			.product(product)
			.build();
			return option;
		}).collect(Collectors.toList());
		product.initOptions(options);
		Product save = productRepository.save(product);
		return save.getId();
	}
	/**
	 * order
	 */
	@Transactional(readOnly = true)
	public PageResultDto<Order, OrderDto> orderList(LoginMemberDTO loginMemberDto, PageBaseRequest p) {
		PageRequest pr = PageRequest.of(p.getPage() - 1, p.getTake(), Direction.DESC, "id");
		Page<Order> findAll = orderRepository.findBySellerId(loginMemberDto.getId(), pr);
		return new PageResultDto<>(findAll, t -> new OrderDto(t));
	}
	public Order findOrder(LoginMemberDTO loginMemberDto, Long id) {
		PageRequest pr = PageRequest.of(0, 1, Direction.DESC, "id");
		Page<Order> orderAll = orderRepository.findByIdAndSellerId(id, loginMemberDto.getId(), pr);
		Order order = orderAll.getContent().get(0);
		if (order == null) {
			throw new IllegalArgumentException("not found order");
		}	
		return order;
	}
	@Transactional
	public Long orderCancel(LoginMemberDTO loginMemberDto, Long id) {
		Order order = findOrder(loginMemberDto, id);	
		
		if (!OrderStatus.ORDER.equals(order.getOrderStatus()) || !OrderStatus.PREPARE.equals(order.getOrderStatus()) || !OrderStatus.SHIPPING.equals(order.getOrderStatus())) {
			throw new IllegalArgumentException("주문시작,상품준비중,배송중이 아닌 경우 취소 할 수 없습니다.");
		}
		
		orderService.orderCancel(order);
		return order.getId();
	}
	@Transactional
	public Long orderPrepare(LoginMemberDTO loginMemberDto, Long id) {
		Order order = findOrder(loginMemberDto, id);	
		if (!OrderStatus.ORDER.equals(order.getOrderStatus())) {
			throw new IllegalArgumentException("주문시작이 아닌 경우 상태를 변경할 수 없습니다.");
		}
		order.changeStatus(OrderStatus.PREPARE);
		return order.getId();
	}
	@Transactional
	public Long orderShipping(LoginMemberDTO loginMemberDto, Long id) {
		Order order = findOrder(loginMemberDto, id);
		if (!OrderStatus.PREPARE.equals(order.getOrderStatus())) {
			throw new IllegalArgumentException("상품준비중이 아닌 경우 상태를 변경할 수 없습니다.");
		}
		order.changeStatus(OrderStatus.SHIPPING);
		return order.getId();
	}
}
