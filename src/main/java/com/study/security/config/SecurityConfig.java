package com.study.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.study.security.filter.MyFilter1;
import com.study.security.filter.MyFilter3;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // Spring Security FilterChain 에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
// secured 어노테이션 활성화: 특정 메서드에 권한 설정 가능, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CorsFilter corsFilter;

	// 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class); // security filter chain 에 직접 걸 필요 없이 filter 클래스로 별도 관리를 할 수 있다. 오른쪽 필터보다 먼저 실행. 3,1,2 실행
		http.csrf().disable();
		/**
		 * jwt 사용 전 security 설정
		 */
		// http.authorizeRequests()
		// 	.antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
		// 	.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
		// 	.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		// 	.anyRequest().permitAll()
		// 	.and()
		// 	.formLogin()
		// 	.loginPage("/loginForm")
		// 	.loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행
		// 	.defaultSuccessUrl("/"); // .loginPage("/loginForm")로 로그인을 진행하면 /반환, 다른 페이지에서 로그인 시도하면 해당 페이지 반환

		/**
		 * jwt 사용 후 security 설정
		 */
		// 스프링 시큐리티 세션 정책
		// 스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음 ->JWT 같은토큰방식을 쓸때 사용하는 설정
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilter(corsFilter)	// cors 에러 해결, @CrossOrigin(인증x), 시큐리티 필터에 등록 인증(o)
			.formLogin().disable()	// form 로그인 사용 x
			.httpBasic().disable()	// 기본적인 http 방식 사용 x
			.authorizeRequests()
			.antMatchers("/api/vi/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/api/v1/manager/**")
			.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/api/v1/admin/**")
			.access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll();
	}
}
