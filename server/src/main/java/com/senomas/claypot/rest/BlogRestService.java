package com.senomas.claypot.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senomas.claypot.model.Blog;

@RestController
@RequestMapping("/${rest.uri}/blog")
public class BlogRestService extends ModelRestService<Blog, Long> {

	public BlogRestService() {
		super(Blog.class);
	}
}
