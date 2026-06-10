package cl.duoc.review_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Review Service")
                .version("2.0")
                .description("Servicio encargado de gestionar las calificaciones y comentarios realizados por los usuarios sobre los libros del catálogo."));
    }
}