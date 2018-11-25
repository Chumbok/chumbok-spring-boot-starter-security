package com.chumbok.starter.security.config;


import com.chumbok.security.properties.SecurityProperties;
import com.chumbok.security.util.EncryptionKeyUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(name = "com.chumbok.security.enable", havingValue = "true", matchIfMissing = false)
public class ChumbokSecurityAutoConfig {

    @Bean
    public EncryptionKeyUtil encryptionKeyUtil() {
        return new EncryptionKeyUtil();
    }

    @Bean
    @ConfigurationProperties("com.chumbok.security")
    public SecurityProperties serviceProps() {
        return new SecurityProperties();
    }

    // TODO: Make a conditional bean for dev and prod profile. Make sure wildcard allowed origin is removed.
    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}