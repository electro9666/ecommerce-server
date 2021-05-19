package org.example.controller;

import javax.validation.Valid;

import org.example.dto.OrderDto;
import org.example.dto.PageRequestDto;
import org.example.dto.ProductDto;
import org.example.dto.StoreDto;
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
	@GetMapping("/store/list")
	public Object storeList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageRequestDto pageRequestDto) {
		// filterChain 확인하기
//		boolean a = true;
//		if (a) {
//			throw new RuntimeException("");
//		}
		return sellerService.storeList(loginMemberDto, pageRequestDto);
	}
	@GetMapping("/store/detail/{id}")
	public Object storeDetail(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @PathVariable("id") long id) {
		return sellerService.storeDetail(loginMemberDto, id);
	}
	@PostMapping("/store")
	public Object createStore(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody StoreDto storeDto) {
		System.out.println("storeDto?" + storeDto);
		return sellerService.saveStore(loginMemberDto, storeDto);
	}
	@PutMapping("/store")
	public Object updateStore(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody StoreDto storeDto) {
		return sellerService.saveStore(loginMemberDto, storeDto);
	}
	@GetMapping("/product/list")
	public Object productList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageRequestDto pageRequestDto) {
		return sellerService.productList(loginMemberDto, pageRequestDto);
	}
	@GetMapping("/product/detail/{id}")
	public Object productDetail(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @PathVariable("id") long id) {
		return sellerService.productDetail(loginMemberDto, id);
	}
	@PostMapping("/product")
	public Object createProduct(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody ProductDto ProductDto) {
		return sellerService.saveProduct(loginMemberDto, ProductDto);
	}
	@PutMapping("/product")
	public Object saveProduct(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @Valid @RequestBody ProductDto ProductDto) {
		return sellerService.saveProduct(loginMemberDto, ProductDto);
	}
	@GetMapping("/order/list")
	public Object orderList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageRequestDto pageRequestDto) {
		return sellerService.orderList(loginMemberDto, pageRequestDto);
	}
	@PostMapping("/order/cancel")
	public Object orderCancel(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody OrderDto orderDto) {
		return sellerService.orderCancel(loginMemberDto, orderDto);
	}
	@PostMapping("/order/prepare")
	public Object orderPrepare(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody OrderDto orderDto) {
		return sellerService.orderPrepare(loginMemberDto, orderDto);
	}
	@PostMapping("/order/shipping")
	public Object orderShipping(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody OrderDto orderDto) {
		return sellerService.orderShipping(loginMemberDto, orderDto);
	}
}
