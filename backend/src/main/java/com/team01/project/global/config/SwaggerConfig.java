package com.team01.project.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openApi() {
		String jwt = "JWT";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
		Components components = new Components().addSecuritySchemes(jwt,
			new SecurityScheme().name(jwt).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
		return new OpenAPI().components(new Components())
			.info(apiInfo())
			.addSecurityItem(securityRequirement)
			.components(components);
	}

	private Info apiInfo() {
		return new Info().title("음악 캘린더 API") // API의 제목
			.description("음악 캘린더 서비스에 관한 API 문서화") // API에 대한 설명
			.version("1.0.0"); // API의 버전
	}
}
