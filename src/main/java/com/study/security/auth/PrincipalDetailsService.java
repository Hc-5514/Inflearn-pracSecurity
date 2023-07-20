// package com.study.security.auth;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
//
// import com.study.security.model.User;
// import com.study.security.repository.UserRepository;
//
// // 시큐리티 설정에서 loginProcessingUrl("/login");
// // login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
// @Service
// public class PrincipalDetailsService implements UserDetailsService {
//
// 	@Autowired
// 	private UserRepository userRepository;
//
// 	// 시큐리티 session = Authentication = UserDetails
// 	// return 된 UserDetails 값은 Authentication(내부 UserDetails)로 들어간다.
// 	@Override
// 	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
// 		User userEntity = userRepository.findByUsername(username);
// 		if(userEntity != null){
// 			return new PrincipalDetails(userEntity);
// 		}
// 		return null;
// 	}
// }
