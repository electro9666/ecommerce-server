package org.test;


import org.example.dto.PageRequestDto;
import org.example.dto.PageResultDto;
import org.example.dto.ProductDto;
import org.example.entity.Product;
import org.example.service.UserService;

/**
 * 컴포넌트를 스캔하지 않고, 직접 주입(SampleConfig에서...)
 */
public class TestService {
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public PageResultDto<Product, ProductDto> searchProductList(PageRequestDto pageRequestDto) {
		System.out.println("MyService");
		return userService.searchProductList(pageRequestDto);
	}

}
