package com.powercut.predictor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI powerCutOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PowerCut Predictor API")
                        .description("Power cut prediction "
                                + "for Chennai")
                        .version("1.0"));
    }
}