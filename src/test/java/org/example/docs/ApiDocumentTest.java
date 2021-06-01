//package org.example.docs;
//
//import static org.example.docs.ApiDocumentUtils.getDocumentRequest;
//import static org.example.docs.ApiDocumentUtils.getDocumentResponse;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.context.WebApplicationContext;
//
//@SpringBootTest
//@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
//public class ApiDocumentTest {
//
//	@Autowired
//	private WebApplicationContext context;
//
//	private MockMvc mockMvc;
//
//	@BeforeEach
//	public void setUp(RestDocumentationContextProvider restDocumentation) {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(documentationConfiguration(restDocumentation))
//				.build();
//	}
//
//	@DisplayName("샘플 테스트1")
//	@Test
//	void doc_test1() throws Exception {
//
//		// when
//		// TODO pathParameters을 사용하려면 MockMvcRequestBuilders.get -> RestDocumentationRequestBuilders.get으로 변경 필요
//		ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/sample/get/{id}", "1")
//				.contentType(MediaType.APPLICATION_JSON)
//				);
//
//		// then
//		result.andExpect(status().isOk()).andDo(print())
////		.andDo(document("{class-name}/{method-name}", getDocumentRequest(), getDocumentResponse(),
//				.andDo(document("sample-test", getDocumentRequest(), getDocumentResponse(),
//						pathParameters(
//								parameterWithName("id").description("id")),
//						// request body에 대한 내용(post)
////						requestFields(fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
////								fieldWithPath("age").type(JsonFieldType.STRING).description("나이")),
//						responseFields(fieldWithPath("code").description("코드").type(JsonFieldType.STRING),
//								fieldWithPath("message").description("메세지").type(JsonFieldType.STRING)
////								fieldWithPath("id").description("아이디").type(JsonFieldType.NUMBER)
//								)));
//	}
//}
