package org.example.controller;

import org.example.dto.CartDto;
import org.example.dto.CartPostDto;
import org.example.dto.CashHistoryDto;
import org.example.dto.OrderDto;
import org.example.dto.PageBaseRequest;
import org.example.dto.PageResultDto;
import org.example.dto.PageSearchProductRequest;
import org.example.dto.ProductDto;
import org.example.entity.Cart;
import org.example.entity.CashHistory;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.security.dto.LoginMemberDTO;
import org.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.TestService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/secure/user")
@RestController
public class UserController {
	
	private final TestService myService;
	private final UserService userService;
	
	@GetMapping(path = "/dashboard")
	public String dashboard() {
		return "A";
	}
	
	@ApiOperation(value = "최근 상품 목록", tags = "상품")
	@GetMapping(path = "/recent")
	public PageResultDto<Product, ProductDto> recentProductList() {
		return userService.recentProductList();
	}
	@ApiOperation(value = "상품 검색", tags = "상품")
	@GetMapping(path = "/search")
	public PageResultDto<Product, ProductDto> searchProductList(PageSearchProductRequest p) {
		return myService.searchProductList(p);
//		return userService.searchProductList(p);
	}
	@ApiOperation(value = "상품 상세", tags = "상품")
	@GetMapping(path = "/product/{id}")
	public ProductDto productDetail(@PathVariable("id") long id) {
		return userService.productDetail(id);
	}
	
	@ApiOperation(value = "장바구니 등록", tags = "장바구니")
	@PostMapping("/cart")
	public Long addCart(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody CartPostDto cartDto) {
		return userService.addCart(loginMemberDto, cartDto);
	}
	@ApiOperation(value = "장바구니 리스트", tags = "장바구니")
	@GetMapping("/cart")
	public PageResultDto<Cart, CartDto> getCart(@AuthenticationPrincipal LoginMemberDTO loginMemberDto) {
		return userService.getCartList(loginMemberDto);
	}
	@ApiOperation(value = "장바구니 삭제", tags = "장바구니")
	@DeleteMapping("/cart/{id}")
	public Boolean getCart(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @PathVariable("id") long id) {
		return userService.deleteCart(loginMemberDto, id);
	}
	
	@ApiOperation(value = "주문하기", tags = "주문")
	@PostMapping("/order")
	public Long addOrder(@AuthenticationPrincipal LoginMemberDTO loginMemberDto) {
		return userService.addOrder(loginMemberDto);
	}
	@ApiOperation(value = "주문 리스트", tags = "주문")
	@GetMapping("/order")
	public PageResultDto<Order, OrderDto> getOrderList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageBaseRequest p) {
		return userService.getOrderList(loginMemberDto, p);
	}
	@ApiOperation(value = "주문 상태 변경(확인)", tags = "주문")
	@PostMapping("/order/confirm")
	public Long orderConfirm(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody Long id) {
		return userService.orderConfirm(loginMemberDto, id);
	}
	@ApiOperation(value = "주문 상태 변경(취소)", tags = "주문")
	@PostMapping("/order/cancel")
	public Long orderCancel(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody Long id) {
		return userService.orderCancel(loginMemberDto, id);
	}
	@ApiOperation(value = "캐시 내역", tags = "캐시")
	@GetMapping("/cash/list")
	public PageResultDto<CashHistory, CashHistoryDto> cashList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageBaseRequest p) {
		return userService.cashList(loginMemberDto, p);
	}
	@ApiOperation(value = "캐시 충전", tags = "캐시")
	@PostMapping("/cash/add")
	public Boolean addCash(@AuthenticationPrincipal LoginMemberDTO loginMemberDto) {
		return userService.addCash(loginMemberDto);
	}
	@ApiOperation(value = "캐시 현재 값 조회", tags = "캐시")
	@GetMapping("/cash/current")
	public Integer cashCurrent(@AuthenticationPrincipal LoginMemberDTO loginMemberDto) {
		return userService.cashCurrent(loginMemberDto);
	}
}
