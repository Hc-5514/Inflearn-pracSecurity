package com.study.security.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.security.auth.JwtPrincipalDetails;
import com.study.security.model.JwtUser;
import com.study.security.repository.JwtUserRepository;

// 시큐리티가 가지고 있는 filter 중 BasicAuthenticationFilter 가 있다.
// 권한이나 인증이 필요한 특정 주소를 요청하면 BasicAuthenticationFilter 를 무조건 거치게 된다.
// 만약 권한이나 인증이 필요한 주소가 아니라면 BasicAuthenticationFilter 를 거치지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private JwtUserRepository userRepository;
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	// 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 거치게 된다.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		System.out.println("인증이나 권한이 필요한 주소 요청이 됨");

		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader: " + jwtHeader);

		// header가 있는지 확인
		if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}

		// JWT 토큰을 검증을 해서 정상적인 사용자인지 확인한다
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

		String username =
			JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();
		
		// 서명이 정상적으로 됨
		if(username != null){
			System.out.println("username 정상");
			
			JwtUser userEntity = userRepository.findByUsername(username);

			JwtPrincipalDetails principalDetails = new JwtPrincipalDetails(userEntity);


			// authentication 객체가 실제로 로그인을 해서 만들어진게 아닌, 서명을 통해 검증이 돼서 username이 있으면 만든 것이기 때문에 정상적으로 로그인을 요청해서 만든 것이 아니다.
			// Jwt 토큰 서명을 통해서 서명이 정상이면 authentication 객체를 만들어준다.
			Authentication authentication =
				new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

			// 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
			SecurityContextHolder.getContext().setAuthentication(authentication);

			chain.doFilter(request,response);
		}
	}
}
