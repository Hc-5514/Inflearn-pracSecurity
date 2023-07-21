package com.study.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.security.model.JwtUser;
import com.study.security.repository.JwtUserRepository;
import com.study.security.repository.UserRepository;

//@CrossOrigin
@RestController
public class RestApiController {

	@Autowired
	private JwtUserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/home")
	public String home() {
		return "<h1>home</h1>";
	}

	@PostMapping("/token")
	public String token() {
		return "<h1>token</h1>";
	}

	@PostMapping("join")
	public String join(@RequestBody JwtUser user){
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER"); // 권한 기본 설정
		userRepository.save(user);
		return "회원가입완료";
	}

	// user, manager, admin 권한 접근 가능
	@GetMapping("/api/v1/user")
	public String user(){
		return "user";
	}

	// manager, admin 권한 접근 가능
	@GetMapping("/api/v1/manager")
	public String manager(){
		return "manager";
	}

	// admin 권한 접근 가능
	@GetMapping("/api/v1/admin")
	public String admin(){
		return "admin";
	}
}
