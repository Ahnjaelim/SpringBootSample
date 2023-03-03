package com.example.demo02.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.example.demo02.security.CustomUserDetailsService;
import com.example.demo02.security.handler.Custom403Handler;
import com.example.demo02.security.handler.CustomSocialLoginSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {
	
	// 주입
	private final DataSource dataSource;
	private final CustomUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}	

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("----------configure----------");
		http.formLogin().loginPage("/member/login"); // 커스텀 로그인 페이지
		http.csrf().disable(); // CSRF 토큰 비활성화
		
		http.rememberMe()
			.key("12345678")
			.tokenRepository(persistentTokenRepository())
			.userDetailsService(userDetailsService)
			.tokenValiditySeconds(60*60*24*30);
			
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()); //403
        http.oauth2Login().loginPage("/member/login").successHandler(authenticationSuccessHandler());

        return http.build();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new Custom403Handler();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		log.info("----------web configure----------");
		return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		return repo;
	}
	

}
