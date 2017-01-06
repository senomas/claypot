package com.senomas.claypot;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.common.base.Predicates;
import com.senomas.common.persistence.RepositoryFactoryBean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@SpringBootApplication
@ComponentScan({ "com.senomas.boot", "com.senomas.claypot", "com.senomas.common.loggerfilter" })
@EnableSwagger2
@EnableScheduling
@EnableJpaRepositories(repositoryFactoryBeanClass = RepositoryFactoryBean.class)
public class Application {
	private final static Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	Environment env;

	@Autowired
	ResourcePatternResolver resolver;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(Predicates.or(PathSelectors.ant("/api/**"), PathSelectors.ant("/auth/**"))).build();
	}

	public static void main(String[] args) {
		try {
			log.info("CHANGE-LOG:\n{}",
					IOUtils.toString(Application.class.getResourceAsStream("/META-INF/changelog.txt"), "UTF-8"));
		} catch (IOException e) {
			log.warn("Error reading changelog {}", e.getMessage(), e);
		}
		String profiles = System.getProperty("spring.profiles.active");
		if (profiles != null) {
			String px[] = profiles.split(",");
			if (px.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0, il = px.length; i < il; i++) {
					if (i > 0)
						sb.append(',');
					sb.append(px[i]);
					if ("dev".equals(px[i])) {
						sb.append(",dev-").append(System.getProperty("user.name"));
					}
				}
				System.setProperty("spring.profiles.active", sb.toString());
			}
		}
		SpringApplication.run(Application.class, args);
	}

}
