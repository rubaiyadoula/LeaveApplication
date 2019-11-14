package com.itc.leaveapplication.config;

import io.github.jhipster.config.apidoc.customizer.SwaggerCustomizer;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import static java.util.Collections.singletonList;

@Configuration
public class SwaggerConfiguration implements SwaggerCustomizer {
    @Override
    public void customize(Docket docket) {
        docket.securitySchemes(singletonList(apiKey()))
            .securityContexts(singletonList(
                SecurityContext.builder()
                    .securityReferences(
                        singletonList(SecurityReference.builder()
                            .reference("Authorization")
                            .scopes(new AuthorizationScope[0])
                            .build()
                        )
                    )
                    .build())
            );
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");

    }

}
