package com.senomas.claypot;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.senomas.boot.loader.DataLoader;
import com.senomas.claypot.model.Blog;
import com.senomas.claypot.model.BlogRepository;

@Component
public class InitData extends DataLoader {

	@Autowired
	BlogRepository repo;

	@PostConstruct
	@Transactional
	public void init() {
		for (int i = 1; i <= 77; i++) {
			Blog blog = new Blog();
			blog.setPath("/xxx-"+i);
			blog.setTitle("Title "+i);
			blog.setCreated(new Date());
			blog.setCreateBy("seno");
			blog.setUpdated(new Date());
			blog.setUpdateBy("seno");
			repo.save(blog);
		}
	}

}
