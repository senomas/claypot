package com.senomas.claypot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "BLOG", uniqueConstraints = { @UniqueConstraint(columnNames = "PATH") })
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@Column(name = "PATH", length = 256)
	private String path;

	@NotNull
	@Column(name = "TITLE", length = 1024)
	private String title;
	
	@NotNull
	@Column(name = "CREATED")
	private Date created;
	
	@NotNull
	@Column(name = "CREATE_BY", length=128)
	private String createBy;
	
	@NotNull
	@Column(name = "UPDATED")
	private Date updated;
	
	@NotNull
	@Column(name = "UPDATE_BY", length=128)
	private String updateBy;

}
