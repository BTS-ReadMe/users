package bts.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 60 * 60;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry
            .addMapping("/**")
            .allowedOrigins("http://localhost:8080/auth/token") //, )
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*") // 허용되는 헤더
            .allowCredentials(true) // 자격증명 허용
            .maxAge(MAX_AGE_SECS); // 허용 시간
    }
}
