package com.study.security.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.security.auth.JwtPrincipalDetails;
import com.study.security.model.JwtUser;

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
		try {
			/*BufferedReader br = request.getReader();
			// request.getInputStream() 에 username,password 가 담겨있다.
			// System.out.println(request.getInputStream().toString());
			String input = null;
			while((input = br.readLine()) !=null){
				System.out.println(input);

				// username=ssar12&password=1234

				// json 방식
				// {
				//     "username" : "ssar12",
				//     "password" : "5678"
				// }*/
			ObjectMapper om = new ObjectMapper();
			JwtUser user = om.readValue(request.getInputStream(), JwtUser.class);
			System.out.println(user);

			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

			// PrincipalDetailsService 의 loadUserByUsername() 함수가 실행 된 후 정상이면 authentication 이 리턴된다.
			// 토큰을 통해 로그인 시도를 하고, 로그인이 정상적으로 완료되면 authentication 가 만들어진다.
			// DB에 있는 username 과 password가 일치한다.
			Authentication authentication =
				authenticationManager.authenticate(authenticationToken);

			// 로그인이 되었다는 뜻.
			JwtPrincipalDetails principalDetails = (JwtPrincipalDetails)authentication.getPrincipal();
			System.out.println("로그인 완료됨: " + principalDetails.getUser().getUsername()); // 로그인이 정상적으로 되었다는 뜻

			// authentication 객체를 session 영역에 저장해야 하고 그 방법은 return 해주면 된다.
			// 리턴의 이유는 권한 관리를 security 가 대신 해주기 때문에 편하기 때문
			// 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 단지 권한 처리 때문에 session에 넣어준다.
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다.
	// JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 된다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 실행됨: 인증이 완료되었다는 뜻임");
		JwtPrincipalDetails principalDetails = (JwtPrincipalDetails)authResult.getPrincipal();

		// RSA 방식이 아닌, Hash 암호 방식
		String jwtToken = JWT.create()
			.withSubject(principalDetails.getUsername())
			.withExpiresAt(new Date(System.currentTimeMillis()+(JwtProperties.EXPIRATION_TIME))) // 만료 시간: 현재 시간 + @
			.withClaim("id", principalDetails.getUser().getId())
			.withClaim("username", principalDetails.getUser().getUsername())
			.sign(Algorithm.HMAC512(JwtProperties.SECRET));

		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
}
