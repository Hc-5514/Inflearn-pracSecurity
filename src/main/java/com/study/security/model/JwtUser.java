package com.study.security.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class JwtUser {
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String roles; //USER, ADMIN

	public List<String> getRoleList(){
		if(this.roles.length() > 0){
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>();
	}
}
