package com.ecommerce.userservice.config;

import com.ecommerce.userservice.constant.ApiURL;
import com.ecommerce.userservice.service.TokenService;
import com.ecommerce.userservice.service.impl.CustomUserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurity implements WebMvcConfigurer {
    
    private ApplicationProperties applicationProperties;
    
    /*
    Stores Locale in Session: SessionLocaleResolver stores the user's locale preference in the HTTP session.
    This means that once a user selects a locale, it remains consistent across their session until it is changed.
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang"); // URL parameter to change locale
        return localeChangeInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailServiceImpl customUserDetailServiceImpl) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    @Bean
    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(applicationProperties.isCorsAllowCredentials());
        configuration.setAllowedHeaders(applicationProperties.getCorsAllowHeaders());
        configuration.setAllowedMethods(applicationProperties.getCorsAllowMethods());
        configuration.setAllowedOrigins(applicationProperties.getCorsAllowOrigins());
        configuration.setExposedHeaders(applicationProperties.getCorsAllowExposedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            CustomUserDetailServiceImpl customUserDetailServiceImpl,
                                            ObjectMapper objectMapper,
                                            TokenService tokenService,
                                            MessageSource messageSource
    ) throws Exception {
        return http
                .csrf(csrf ->
                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .ignoringRequestMatchers(ApiURL.ROOT_PATH + "/**")
                )
                .cors(cors -> cors.configurationSource(apiConfigurationSource()))
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(
                                        ApiURL.ROOT_PATH + ApiURL.REISSUE_PASSWORD,
                                        ApiURL.ROOT_PATH + ApiURL.REGISTER_USER,
                                        ApiURL.ROOT_PATH + ApiURL.LOGIN
                                ).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider(customUserDetailServiceImpl))
                .addFilterBefore(new JwtRequestFilter(tokenService, customUserDetailServiceImpl, objectMapper,
                                messageSource),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
