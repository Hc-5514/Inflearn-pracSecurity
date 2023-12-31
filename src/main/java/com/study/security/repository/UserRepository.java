package com.study.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.security.model.User;

// CRUD 함수를 JpaRepository 가 들고 있음.
// @Repository 라는 어노테이션이 없어도 IoC가 된다. JpaRepository를 상속했기 때문이다.
public interface UserRepository extends JpaRepository<User, Integer> {

	// findBy 규칙 -> Username 문법
	// select * from user where username = ?
	public User findByUsername(String username);
}
