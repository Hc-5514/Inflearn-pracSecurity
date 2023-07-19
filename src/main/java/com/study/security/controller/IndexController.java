package com.study.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.security.model.User;
import com.study.security.repository.UserRepository;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping({"", "/"})
	public String index() {
		// 머스테치 기본 폴더: src/main/resources/
		// 뷰리졸버 설정 - 생략가능: templates (prefix), .mustache (suffix)
		return "index";
	}

	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}

	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}

	// SecurityConfig 파일 생성 후 작동 안함.
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}

	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user); // 회원가입이 잘 됨. 비밀번호: 1234 -> 시큐리티로 로그인을 할 수 없음. 이유는 패스워드가 암호화 되어 있지 않기 때문.
		return "redirect:/loginForm";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info(){
		return "개인정보";
	}

	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data(){
		return "데이터정보";
	}
}
