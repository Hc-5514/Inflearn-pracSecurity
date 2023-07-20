package com.study.security.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에 UsernamePasswordAuthenticationFilter 가 있음
// login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 가 동작을 한다.
// 하지만 SecurityConfig 파일에서 .formLogin().disable()을 했기에 동작을 안 한다.
// 따라서 JwtAuthenticationFilter 를 다시 SecurityConfig 에 등록해줘야 한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	// /login 요청을 하면 로그인 시도를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		System.out.println("JwtAuthenticationFilter: 로그인 시도 중");

		// 1. username, password를 받는다.
		// 2. 정상인지 로그인 시도를 해본다.
		// authenticationManager 로 로그인 시도를 하면
		// PrincipalDetailsService 가 호출 되고, loadUserByUsername() 함수가 실행된다.

		// 3. PrincipalDetails 를 세션에 담고 (권한 관리를 위해서 세션에 담는다.)

		// 4. JWT 토큰을 만들어서 응답해준다.
		
		return super.attemptAuthentication(request, response);
	}
}
