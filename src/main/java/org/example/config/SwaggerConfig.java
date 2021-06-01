package org.example.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	// swagger-ui.html

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Demo").description("API EXAMPLE").build();
	}

	@Bean
	public Docket api0() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("common")
				.ignoredParameterTypes(AuthenticationPrincipal.class) // @AuthenticationPrincipal은 문서에서 제외하기
				.apiInfo(this.apiInfo())
				.select()
//                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
				.apis(RequestHandlerSelectors.basePackage("org.example.controller"))
                .paths(Predicates.or(PathSelectors.ant("/sample/**"), PathSelectors.ant("/api/secure/common/**")))
//                .paths(PathSelectors.ant("/api/**"))
				.build()
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
	}
	@Bean
	public Docket api1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("user")
				.ignoredParameterTypes(AuthenticationPrincipal.class) // @AuthenticationPrincipal은 문서에서 제외하기
				.apiInfo(this.apiInfo())
				.select()
//                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
				.apis(RequestHandlerSelectors.basePackage("org.example.controller"))
                .paths(PathSelectors.ant("/api/secure/user/**")) // 그중 /sample/** 인 URL들만 필터링
//                .paths(PathSelectors.ant("/api/**"))
				.build()
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
	}
	@Bean
	public Docket api2() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("seller")
				.ignoredParameterTypes(AuthenticationPrincipal.class) // @AuthenticationPrincipal은 문서에서 제외하기
				.apiInfo(this.apiInfo())
				.select()
//                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
				.apis(RequestHandlerSelectors.basePackage("org.example.controller"))
                .paths(PathSelectors.ant("/api/secure/seller/**")) // 그중 /sample/** 인 URL들만 필터링
//                .paths(PathSelectors.ant("/api/**"))
				.build()
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}
	
    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }	
}
