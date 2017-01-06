package com.senomas.claypot.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.senomas.boot.security.LoginRequest;
import com.senomas.boot.security.domain.AuthToken;
import com.senomas.boot.security.domain.SecurityUser;
import com.senomas.boot.security.service.AuthenticationService;
import com.senomas.boot.security.service.TokenService;

@Controller
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	TokenService tokenService;
	
	@Override
	public SecurityUser getUser() {
		return null;
	}

	@Override
	public AuthToken login(HttpServletRequest request, LoginRequest login) {
		return null;
	}

	@Override
	public AuthToken refresh(HttpServletRequest req, String login, String refreshToken) {
		return null;
	}

	@Override
	public SecurityUser logout() {
		return null;
	}

	@Override
	public SecurityUser logout(String login) {
		return null;
	}
}
