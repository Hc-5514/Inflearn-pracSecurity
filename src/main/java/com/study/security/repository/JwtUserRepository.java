package com.study.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.security.model.JwtUser;
import com.study.security.model.User;

public interface JwtUserRepository extends JpaRepository<User, Integer> {

	public JwtUser findByUsername(String username);
}
