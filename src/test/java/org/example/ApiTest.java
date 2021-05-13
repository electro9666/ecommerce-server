package org.example;

import static org.example.utils.JsonUtils.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.example.constant.CashHistoryReason;
import org.example.constant.LoginRole;
import org.example.constant.UserGrade;
import org.example.entity.CashHistory;
import org.example.entity.Category;
import org.example.entity.LoginMember;
import org.example.entity.Seller;
import org.example.entity.User;
import org.example.repository.CashHistoryRepository;
import org.example.repository.CategoryRepository;
import org.example.repository.LoginMemberRepository;
import org.example.repository.ProductRepository;
import org.example.repository.SellerRepository;
import org.example.repository.StoreRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.jayway.jsonpath.JsonPath;

//@ActiveProfiles("test")
@AutoConfigureMockMvc
//@TestPropertySource(properties = { "spring.config.location=classpath:application-test.properties" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @BeforeAll을 nonStatic으로 사용할 수 있다.
@TestMethodOrder(MethodName.class) // 메소드 이름 순으로
@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
@DisplayName("API 통합 테스트")
public class ApiTest {

	String TOKEN;
	Category category;
	int storeId = 0;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoginMemberRepository loginMemberRepository;
	@Autowired
	private SellerRepository sellerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CashHistoryRepository cashHistoryRepository;

	private MockMvc mockMvc;

	@Autowired
	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@BeforeAll
	public void reset() {
		System.out.println("resetresetresetresetresetresetresetresetresetresetresetreset");
		
		// 기본 로그인 데이터 생성
		LoginMember loginMember = LoginMember.builder()
			.id(1l)
			.username("user1") // id
			.name("사용자1")
			.fromSocial(false)
			.password(passwordEncoder.encode("1111"))
			.build();
		loginMember.addMemberRole(LoginRole.USER);
		loginMember.addMemberRole(LoginRole.SELLER);
		loginMemberRepository.save(loginMember);
		
		int cash = 1000000;
		User user = User.builder().id(1l).name("user1").cash(cash).userGrade(UserGrade.BRONZE).build();
		userRepository.save(user);
		
		CashHistory cashHistory = CashHistory.builder().user(user).cash(cash).reason(CashHistoryReason.INIT).build();
		cashHistoryRepository.save(cashHistory);
		
		Seller seller = Seller.builder().id(1l).name("user1").build();
		sellerRepository.save(seller);
	}

	@Test
	@DisplayName("sample/all")
	public void _01_() throws Exception {
		ResultActions result = mockMvc.perform(get("/sample/all").accept(MediaType.APPLICATION_JSON));
		result.andDo(print()).andExpect(status().isOk());
	}

	@Test
	@DisplayName("로그인 후에 token이 리턴된다.")
	public void _02_() throws Exception {
		ResultActions result = mockMvc.perform(post("/api/common/login").contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(toJson(new HashMap<String, String>() {
				{
					put("username", "user1");
					put("password", "1111");
				}
			}))
		);
		MvcResult andReturn = result.andDo(print()).andExpect(status().isOk()).andReturn();
		;
		
		String content = andReturn.getResponse().getContentAsString();
		System.out.println("content?" + content);
		TOKEN = content;
	}
	@Test
	@DisplayName("user token을 이용해서 recent api 호출")
	public void _03_() throws Exception {
		System.out.println("TOKEN?" + TOKEN);
		ResultActions result = mockMvc.perform(get("/api/secure/user/recent").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + TOKEN)
			.accept(MediaType.APPLICATION_JSON)
		);
		MvcResult andReturn = result.andDo(print()).andExpect(status().isOk()).andReturn();
		
		String content = andReturn.getResponse().getContentAsString();
		System.out.println("recent?" + content);
		
	}
	@Test
	@DisplayName("카테고리 생성")
	public void _04_() throws Exception {
		Category builder = Category.builder()
		.name("카테고리1")
		.build();
		category = categoryRepository.save(builder);
	}
	
	@Test
	@DisplayName("스토어, 상품 생성")
	public void _05_() throws Exception {
		// 스토어 등록
		ResultActions result = mockMvc.perform(post("/api/secure/seller/store").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + TOKEN)
			.accept(MediaType.APPLICATION_JSON)
			.content(toJson(new HashMap<String, String>() {
				{
					put("name", "스토어테스트1");
					put("status", "OPEN");
				}
			}))
		);
		result.andDo(print()).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	@DisplayName("스토어 찾기")
	public void _06_() throws Exception {
		// 스토어 찾기
		ResultActions result1 = mockMvc.perform(get("/api/secure/seller/store/list").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + TOKEN)
			.accept(MediaType.APPLICATION_JSON)
		);
		MvcResult andReturn1 = result1.andDo(print()).andExpect(status().isOk()).andReturn();
		String contentAsString = andReturn1.getResponse().getContentAsString();
		List<Object> storeList = JsonPath.parse(contentAsString).read("$.result");
		assertEquals(storeList.size(), 1);
		
		storeId = (Integer)JsonPath.parse(contentAsString).read("$.result[0].id");
	}
	
	@Test
	@DisplayName("상품등록 실패 - 옵션 0개")
	public void _07_() throws Exception {
		ResultActions result = mockMvc.perform(post("/api/secure/seller/product").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + TOKEN)
			.accept(MediaType.APPLICATION_JSON)
			.content(toJson(new HashMap<String, Object>() {
				{
					put("name", "상품테스트1");
					put("description", "description");
					put("price", 0);
					put("status", "OPEN");
					put("categoryId", category.getId());
					put("storeId", storeId);
					put("options", null);
					
				}
			}))
		);
		result.andDo(print()).andExpect(status().is4xxClientError());
	}
	@Test
	@DisplayName("상품등록 실패 - 옵션 1개, price 0원")
	public void _08_() throws Exception {
		HashMap<String, Object> option1 = new HashMap<>();
		option1.put("name", "option1");
		option1.put("price", 0);
		option1.put("quantity", 2);
		ArrayList<HashMap<String, Object>> options = new ArrayList<>();
		options.add(option1);
		
		ResultActions result = mockMvc.perform(post("/api/secure/seller/product").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + TOKEN)
			.accept(MediaType.APPLICATION_JSON)
			.content(toJson(new HashMap<String, Object>() {
				{
					put("name", "상품테스트1");
					put("description", "description");
					put("price", 0);
					put("status", "OPEN");
					put("categoryId", category.getId());
					put("storeId", storeId);
					put("options", options);
				}
			}))
		);
		result.andDo(print()).andExpect(status().is4xxClientError());
	}
	@Test
	@DisplayName("상품등록 실패 - 상품명 빈값")
	public void _09_() throws Exception {
		HashMap<String, Object> option1 = new HashMap<>();
		option1.put("name", "option1");
		option1.put("price", 10);
		option1.put("quantity", 2);
		ArrayList<HashMap<String, Object>> options = new ArrayList<>();
		options.add(option1);
		
		ResultActions result = mockMvc.perform(post("/api/secure/seller/product").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.content(toJson(new HashMap<String, Object>() {
					{
						put("name", "  ");
						put("description", "description");
						put("status", "OPEN");
						put("categoryId", category.getId());
						put("storeId", storeId);
						put("options", options);
					}
				}))
				);
		result.andDo(print()).andExpect(status().is4xxClientError());
	}
	@Test
	@DisplayName("상품등록 성공")
	public void _10_() throws Exception {
		HashMap<String, Object> option1 = new HashMap<>();
		option1.put("name", "a");
		option1.put("price", 10);
		option1.put("quantity", 2);
		ArrayList<HashMap<String, Object>> options = new ArrayList<>();
		options.add(option1);
		
		ResultActions result = mockMvc.perform(post("/api/secure/seller/product").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.content(toJson(new HashMap<String, Object>() {
					{
						put("name", "상품테스트1");
						put("description", "description");
						put("status", "OPEN");
						put("categoryId", category.getId());
						put("storeId", storeId);
						put("options", options);
					}
				}))
				);
		result.andDo(print()).andExpect(status().isOk());
	}
}
