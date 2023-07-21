package com.study.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.security.model.JwtUser;

public interface JwtUserRepository extends JpaRepository<JwtUser, Integer> {

	public JwtUser findByUsername(String username);
}
