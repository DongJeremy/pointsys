package jp.co.nri.point.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * swagger文档
 */

@Configuration
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi groupOpenApi() {
        String paths[] = { "/api/v1/**" };
        String packagesToscan[] = { "jp.co.nri.point.api.controller" };
        return GroupedOpenApi.builder().group("point-system").pathsToMatch(paths).packagesToScan(packagesToscan)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components())
                .info(new Info().title("PointSystem API").version("1.0")
                        .description("This is Spring Boot RESTful service using API 1.")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

}
