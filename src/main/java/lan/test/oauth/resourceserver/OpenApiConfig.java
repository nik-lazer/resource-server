package lan.test.oauth.resourceserver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "security_auth", type = SecuritySchemeType.OAUTH2, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER,
        flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "${lan.test.oauth.authorization-url}"))
)
@OpenAPIDefinition(
        info = @Info(title = "Demo", description = "Demo"),
        security = {@SecurityRequirement(name = "security_auth")}
)
public class OpenApiConfig {
}