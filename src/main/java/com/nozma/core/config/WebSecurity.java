package com.nozma.core.config;

import com.nozma.core.constant.ApiURL;
import com.nozma.core.filter.JwtRequestFilter;
import com.nozma.core.service.TokenService;
import com.nozma.core.service.impl.CustomUserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
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
                        /*
                         * CSRF: Cross-site request forgery means a bad site tricks a user's browser into making an
                         * unwanted requests to your site
                         * Store CSRF token in a cookie that JS/TS can access it
                         * Prevent CSRF attacks
                         * */
                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .ignoringRequestMatchers(ApiURL.ROOT_PATH + "/**")
                )
                .cors(cors -> cors.configurationSource(apiConfigurationSource()))
                .headers(
                        /*
                         Prevent your server from being embedded in iframe of other sites.
                         Iframe: Like there are a bunch of pictures on your notebook. Basically, a webpage inside your webpage
                         XSS: Trick a website into running attacker's code without the end-user's consent
                         Clickjacking: Attackers hide something behind their webpage and trick the users into clicking it
                         x-frame-options = Same Origin: like only my friends can use my toys
                         */
                        header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                                .xssProtection(xXssConfig -> xXssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                /*
                                 Allow only scripts from website (not external sources) to be executed
                                 Prevent malicious scripts from running on your site
                                 */
                                .contentSecurityPolicy(contentSecurityPolicyConfig ->
                                        contentSecurityPolicyConfig.policyDirectives("script-src 'self'"))
                                /*
                                * Allow only your website to be embedded in your site in an iframe
                                * */
                                .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("frame-ancestor 'self"))
                )
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(
                                        EndpointRequest.to(HealthEndpoint.class),
                                        EndpointRequest.to(InfoEndpoint.class)
                                ).permitAll()
                                .requestMatchers(
                                        ApiURL.ROOT_PATH + ApiURL.REISSUE_PASSWORD,
                                        ApiURL.ROOT_PATH + ApiURL.REISSUE_TOKEN,
                                        ApiURL.ROOT_PATH + ApiURL.REGISTER_USER,
                                        ApiURL.ROOT_PATH + ApiURL.LOGIN,
                                        ApiURL.ROOT_PATH + ApiURL.LOGOUT,
                                        ApiURL.ROOT_PATH + ApiURL.GET_EXERCISE_GUEST
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
