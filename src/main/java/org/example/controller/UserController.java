package org.example.controller;

import org.example.dto.CartDto;
import org.example.dto.OrderDto;
import org.example.dto.PageRequestDto;
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
	
	@GetMapping(path = "/recent")
	public Object recentProductList() {
		return userService.recentProductList();
	}
	@GetMapping(path = "/search")
	public Object searchProductList(PageRequestDto p) {
		return myService.searchProductList(p);
//		return userService.searchProductList(p);
	}
	@GetMapping(path = "/product/{id}")
	public Object productDetail(@PathVariable("id") long id) {
		return userService.productDetail(id);
	}
	
	@PostMapping("/cart")
	public Object addCart(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody CartDto cartDto) {
		return userService.addCart(loginMemberDto, cartDto);
	}
	@GetMapping("/cart")
	public Object getCart(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageRequestDto p) {
		return userService.getCartList(loginMemberDto, p);
	}
	@DeleteMapping("/cart/{id}")
	public Object getCart(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @PathVariable("id") long id) {
		return userService.deleteCart(loginMemberDto, id);
	}
	@PostMapping("/order")
	public Object addOrder(@AuthenticationPrincipal LoginMemberDTO loginMemberDto) {
		return userService.addOrder(loginMemberDto);
	}
	@GetMapping("/order")
	public Object getOrderList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageRequestDto p) {
		return userService.getOrderList(loginMemberDto, p);
	}
	@PostMapping("/order/confirm")
	public Object orderConfirm(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody OrderDto orderDto) {
		return userService.orderConfirm(loginMemberDto, orderDto);
	}
	@PostMapping("/order/cancel")
	public Object orderCancel(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, @RequestBody OrderDto orderDto) {
		return userService.orderCancel(loginMemberDto, orderDto);
	}
	@GetMapping("/cash/list")
	public Object cashList(@AuthenticationPrincipal LoginMemberDTO loginMemberDto, PageRequestDto p) {
		return userService.cashList(loginMemberDto, p);
	}
	@PostMapping("/cash/add")
	public Object addCash(@AuthenticationPrincipal LoginMemberDTO loginMemberDto) {
		return userService.addCash(loginMemberDto);
	}
	@GetMapping("/cash/current")
	public Object cashCurrent(@AuthenticationPrincipal LoginMemberDTO loginMemberDto) {
		return userService.cashCurrent(loginMemberDto);
	}
}
