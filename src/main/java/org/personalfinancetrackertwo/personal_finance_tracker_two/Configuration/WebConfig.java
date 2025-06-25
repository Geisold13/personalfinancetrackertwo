package org.personalfinancetrackertwo.personal_finance_tracker_two.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  This WebConfig class configures Cross-Origin Resource Sharing (CORS) in the Spring boot app.
 *  It ensures the angular front end at "http://localhost:4200" for local development
 *  can make requests to the spring boot backend
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {


    /**
     * This is an overridden method from WebMvcConfigurer use to configure CORS settings
     * .addMapping - specifies the URL pattern for which CORS should be enabled (/**) means all backend paths
     * .allowedOrigins - Defines which origins are allowed to make requests to the backend (only http://localhost:4200)
     * .allowedMethods - Defines which HTTP methods are allowed from the frontend.
     * .allowedHeaders - Defines the allowed HTTP headers to be sent in the request.
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
