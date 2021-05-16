//package org.example;
//
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import org.example.constant.CashHistoryReason;
//import org.example.constant.LoginRole;
//import org.example.constant.ProductStatus;
//import org.example.constant.StoreStatus;
//import org.example.constant.UserGrade;
//import org.example.entity.CashHistory;
//import org.example.entity.Category;
//import org.example.entity.LoginMember;
//import org.example.entity.Product;
//import org.example.entity.ProductOption;
//import org.example.entity.Seller;
//import org.example.entity.Store;
//import org.example.entity.User;
//import org.example.repository.CashHistoryRepository;
//import org.example.repository.CategoryRepository;
//import org.example.repository.LoginMemberRepository;
//import org.example.repository.ProductRepository;
//import org.example.repository.SellerRepository;
//import org.example.repository.StoreRepository;
//import org.example.repository.UserRepository;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//
////@ActiveProfiles("test")
////@TestPropertySource(properties = { "spring.config.location=classpath:application-test.properties" })
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @BeforeAll을 nonStatic으로 사용할 수 있다.
//@TestMethodOrder(OrderAnnotation.class) // @Order 사용
//@SpringBootTest(properties = "spring.config.location=classpath:/application-testtest.properties")
////@SpringBootTest
//public class DummyTest {
//
//	@Autowired private PasswordEncoder passwordEncoder;
//	
//    @Autowired private LoginMemberRepository loginMemberRepository;
//    @Autowired private SellerRepository sellerRepository;
//    @Autowired private UserRepository userRepository;
//    @Autowired private StoreRepository storeRepository;
//    @Autowired private CategoryRepository categoryRepository;
//    @Autowired private ProductRepository productRepository;
//    @Autowired private CashHistoryRepository cashHistoryRepository;
//    
//    @BeforeAll
//    public void reset() {
//    	System.out.println("resetresetresetresetresetresetresetresetresetresetresetreset");
//    	
//    	productRepository.deleteAll();
//    	
//    	categoryRepository.deleteAll();
//    	
//    	storeRepository.deleteAll();
//    	
//    	userRepository.deleteAll();
//    	sellerRepository.deleteAll();
//    	
//    	cashHistoryRepository.deleteAll();
//    	loginMemberRepository.deleteAll();
//    }
//    
//    
//    @Test
//    @Order(1)
//    public void category() {
//    	String[] list = new String[] {"가전", "컴퓨터", "자동차", "가구", "식품", "패션", "여행"};
//    	for (int j = 0; j < list.length; j++) {
//    		Category category = Category.builder().id((long)j + 1).name(list[j]).build();
//    		categoryRepository.save(category);
//		}
//    }
//    
//    
//    @Test
//    @Order(2)
//    public void loginMember() {
//        IntStream.rangeClosed(1, 6).forEach(i -> {
//        	LoginMember loginMember = LoginMember.builder()
//        		.id((long)i)
//		        .username("user" + i) // id
//		        .name("사용자" + i)
//		        .fromSocial(false)
//		        .password(passwordEncoder.encode("1111"))
//		        .build();
//
//        	if (i <= 3) {
//        		loginMember.addMemberRole(LoginRole.USER);
//        		loginMember.addMemberRole(LoginRole.SELLER);
//        	} else if (i <= 4) {
//        		loginMember.addMemberRole(LoginRole.USER);
//        	} else if (i == 5) {
//        		loginMember.addMemberRole(LoginRole.ADMIN_READ);
//        	} else if (i == 6) {
//        		loginMember.addMemberRole(LoginRole.ADMIN_ALL);
//        	}
//            loginMemberRepository.save(loginMember);
//
//            if (i <= 3) {
//            	Seller seller = Seller.builder()
//                	.id((long) i)
//                	.name("user" + i)
//                	.build();
//                sellerRepository.save(seller);
//            }
//            
//            if (i <= 4) {
//            	int cash = 1000000;
//            	User user = User.builder()
//        			.id((long) i)
//        			.name("user" + i)
//        			.cash(cash)
//        			.userGrade(UserGrade.BRONZE)
//        			.build();
//            	userRepository.save(user);
//            	
//            	CashHistory cashHistory = CashHistory.builder()
//            		.user(user)
//            		.cash(cash)
//            		.reason(CashHistoryReason.INIT)
//            		.build();
//            	cashHistoryRepository.save(cashHistory);
//            }
//        });
//        
//    }
//    @Test
//    @Order(3)
//    public void store() {
//        IntStream.rangeClosed(1, 13).forEach(i -> {
//        	Seller seller = sellerRepository.findById(1L).orElse(null);
//        	Store store = Store.builder()
//        		.name("스토어" + i)
//        		.status(StoreStatus.OPEN)
//        		.seller(seller)
//        		.build();
//        	
//        	storeRepository.save(store);
//        });
////        IntStream.rangeClosed(1, 2).forEach(i -> {
////        	Seller seller = sellerRepository.findById(2L).orElse(null);
////        	Store store = Store.builder()
////        			.name("스토어" + i)
////        			.status(StoreStatus.CLOSE)
////        			.seller(seller)
////        			.build();
////        	
////        	storeRepository.save(store);
////        });
//    }
//    @Test
//    @Order(4)
//    public void product() {
//    	Store store = storeRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("not found store, 1L"));
//    	Category category = categoryRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("not found category, 171L"));
//
//    	String[] COLORS = new String[] {"검정", "빨강", "노랑"};
//    	
//    	IntStream.rangeClosed(1, 13).forEach(i -> {
//    		// product마다 맵핑할 option 객체를 매번 생성해야 한다.(영속성되는 객체로써 각각 다르기 때문에)
//    		List<ProductOption> options = IntStream.rangeClosed(1, 3).mapToObj(j -> {
//    			ProductOption option = ProductOption.builder()
//    			.name(COLORS[j-1])
//    			.price(10000 * j)
//    			.quantity(10)
//    			.build();
//    			return option;
//        	}).collect(Collectors.toList());
//    		
//    		Product product = Product.builder()
//				.name("상품" + i)
//				.description("설명1\n설명2")
//				.background("")
//				.price(options.get(0).getPrice()) // 대표 판매가
//				.status(ProductStatus.OPEN)
//				.store(store)
//				.category(category)
//				.options(options)
//				.build();
//    		
//    		options.stream().forEach(t -> t.initProduct(product));
//    		
//    		productRepository.save(product);
//    	});
//    }
//}
