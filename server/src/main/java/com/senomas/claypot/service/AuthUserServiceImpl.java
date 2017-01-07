package com.senomas.claypot.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.senomas.boot.security.domain.SecurityUser;
import com.senomas.boot.security.service.AuthUserService;

@Component
public class AuthUserServiceImpl implements AuthUserService {

	@Override
	@Transactional
	public SecurityUser findByLogin(String username) {
		return null;
	}

	@Override
	public SecurityUser save(SecurityUser user) {
		return user;
	}

}
