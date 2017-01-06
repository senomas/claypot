package com.senomas.claypot.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.senomas.boot.security.LoginUser;
import com.senomas.boot.security.domain.SecurityUser;
import com.senomas.boot.security.service.AuthUserService;
import com.senomas.boot.security.service.TokenService;

@Component
public class TokenServiceImpl implements TokenService {
	Map<String, LoginUser> tokens = new LinkedHashMap<>();

	@Autowired
	AuthUserService authUserService;

	@Override
	public LoginUser create(SecurityUser user) {
		synchronized (tokens) {
			String token;
			do {
				token = UUID.randomUUID().toString();
			} while (tokens.containsKey(token));
			LoginUser luser = new LoginUser(token, user);
			tokens.put(token, luser);
			return luser;
		}
	}

	@Override
	@Transactional
	public LoginUser get(String key) {
		return tokens.get(key);
	}

	@Override
	public void remove(String key) {
		tokens.remove(key);
	}

	@Override
	public void clear() {
		synchronized (tokens) {
			tokens.clear();
		}
	}

	@Override
	public List<LoginUser> getList() {
		return new LinkedList<>(tokens.values());
	}

	@Override
	public LoginUser getByLogin(String login) {
		for (LoginUser lu : tokens.values()) {
			if (login.equals(lu.getUser().getLogin())) {
				return lu;
			}
		}
		return null;
	}
}
