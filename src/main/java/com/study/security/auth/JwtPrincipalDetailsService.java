package com.study.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.study.security.model.JwtUser;
import com.study.security.model.User;
import com.study.security.repository.JwtUserRepository;
import com.study.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login => 여기서 동작을 안한다.
@Service
@RequiredArgsConstructor
public class JwtPrincipalDetailsService  implements UserDetailsService {

	private final JwtUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		JwtUser userEntity = userRepository.findByUsername(username);
		return new JwtPrincipalDetails(userEntity);
	}
}
