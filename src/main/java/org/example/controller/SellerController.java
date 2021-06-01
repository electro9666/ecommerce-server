package org.example.controller;

import javax.validation.Valid;

import org.example.dto.OrderDto;
import org.example.dto.PageBaseRequest;
import org.example.dto.PageRequestDto;
import org.example.dto.PageResultDto;
import org.example.dto.ProductDto;
import org.example.dto.ProductRequestDto;
import org.example.dto.StoreDto;
import org.example.dto.StoreRequestDto;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.entity.Store;
import org.example.security.dto.LoginMemberDTO;
import org.example.service.SellerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/secure/seller")
@RestController
public class SellerController {
	
	private final SellerService sellerService;
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "A";
	}
	@ApiOperation(value = "스토어 리스트", tags = "스토어")
	@GetMapping("/store/list")
	public PageResultDto<Store, StoreDto> storeList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageBaseRequest pageRequestDto) {
		// filterChain 확인하기
//		boolean a = true;
//		if (a) {
//			throw new RuntimeException("");
//		}
		return sellerService.storeList(loginMemberDto, pageRequestDto);
	}
	@ApiOperation(value = "스토어 상세", tags = "스토어")
	@GetMapping("/store/detail/{id}")
	public StoreDto storeDetail(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @PathVariable("id") long id) {
		return sellerService.storeDetail(loginMemberDto, id);
	}
	@ApiOperation(value = "스토어 등록", tags = "스토어")
	@PostMapping("/store")
	public Long createStore(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody StoreRequestDto.Post storeDto) {
		return sellerService.saveStore(loginMemberDto, storeDto);
	}
	@ApiOperation(value = "스토어 수정", tags = "스토어")
	@PutMapping("/store")
	public Long updateStore(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody StoreRequestDto.Put storeDto) {
		return sellerService.saveStore(loginMemberDto, storeDto);
	}
	@ApiOperation(value = "상품 리스트", tags = "상품")
	@GetMapping("/product/list")
	public PageResultDto<Product, ProductDto> productList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageRequestDto pageRequestDto) {
		return sellerService.productList(loginMemberDto, pageRequestDto);
	}
	@ApiOperation(value = "상품 상세", tags = "상품")
	@GetMapping("/product/detail/{id}")
	public ProductDto productDetail(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @PathVariable("id") long id) {
		return sellerService.productDetail(loginMemberDto, id);
	}
	@ApiOperation(value = "상품 등록", tags = "상품")
	@PostMapping("/product")
	public Long createProduct(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody ProductRequestDto.Post ProductDto) {
		return sellerService.saveProduct(loginMemberDto, ProductDto);
	}
	@ApiOperation(value = "상품 수정", tags = "상품")
	@PutMapping("/product")
	public Long saveProduct(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody ProductRequestDto.Put ProductDto) {
		return sellerService.saveProduct(loginMemberDto, ProductDto);
	}
	@ApiOperation(value = "주문 리스트", tags = "주문")
	@GetMapping("/order/list")
	public PageResultDto<Order, OrderDto> orderList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageBaseRequest pageRequestDto) {
		return sellerService.orderList(loginMemberDto, pageRequestDto);
	}
	@ApiOperation(value = "주문 상태 변경(취소)", tags = "주문")
	@PostMapping("/order/cancel")
	public Long orderCancel(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody Long id) {
		return sellerService.orderCancel(loginMemberDto, id);
	}
	@ApiOperation(value = "주문 상태 변경(준비중)", tags = "주문")
	@PostMapping("/order/prepare")
	public Long orderPrepare(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody Long id) {
		return sellerService.orderPrepare(loginMemberDto, id);
	}
	@ApiOperation(value = "주문 상태 변경(배송중)", tags = "주문")
	@PostMapping("/order/shipping")
	public Long orderShipping(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody Long id) {
		return sellerService.orderShipping(loginMemberDto, id);
	}
}
