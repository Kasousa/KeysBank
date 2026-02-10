package com.backend.keysbankapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
public class KeysbankapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeysbankapiApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList(
			// Local development
			"http://localhost:3000",
			"http://localhost:3001",
			"http://localhost",
			"http://localhost:80",
			"http://localhost:8080",
			// Docker Compose
			"http://keysbank-frontend",
			"http://keysbank-frontend:80",
			// AWS
			"http://keysbank-dev-alb-1785602146.sa-east-1.elb.amazonaws.com",
			"http://keysbank-dev-alb-812668860.sa-east-1.elb.amazonaws.com",
			// CloudFront
			"https://d1dv49ajmjbvg6.cloudfront.net",
			// Allow all in local dev (optional - remove in production)
			"*"
		));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setExposedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(false); // Use true only if not using * origins
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}