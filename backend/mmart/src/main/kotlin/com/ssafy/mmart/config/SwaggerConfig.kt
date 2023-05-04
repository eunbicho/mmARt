package com.ssafy.mmart.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
@EnableWebMvc
class SwaggerConfig {
    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.OAS_30)
                .securityContexts(listOf(securityContext()))
                .securitySchemes(listOf(apiKey()) as List<SecurityScheme>?)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
    }


    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder()
                .title("mmARt-Swagger")
                .description("mmARt API 명세서 입니다.")
                .version("V1")
                .build()
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf<SecurityReference>(SecurityReference("Authorization", authorizationScopes))
    }

    private fun apiKey(): ApiKey {
        return ApiKey("Authorization", "Authorization", "header")
    }
}