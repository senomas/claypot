package com.senomas.claypot.rest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senomas.claypot.model.ModelRepository;
import com.senomas.common.rs.ResourceNotFoundException;

public abstract class ModelRestService<T, K extends Serializable> {
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ModelRepository<T, K> repo;
	
	@Autowired
	EntityManager em;
	
	private final String modelName;
	private final Class<T> modelType;

	public ModelRestService(Class<T> modelType) {
		this.modelName = modelType.getSimpleName();
		this.modelType = modelType;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Transactional
	public Page<T> getList(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int pageSize,
			@RequestParam(name = "sort", required = false) String sort,
			@RequestParam(name = "ascending", defaultValue = "true") boolean ascending) throws Exception {
		PageRequest pageReq;
		if (sort != null) {
			pageReq = new PageRequest(page, pageSize,
					new Sort(new Order(ascending ? Direction.ASC : Direction.DESC, sort)));
		} else {
			pageReq = new PageRequest(page, pageSize);
		}
		return repo.findAll(pageReq);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public Page<T> getListWithFilter(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int pageSize,
			@RequestParam(name = "sort", required = false) String sort,
			@RequestParam(name = "ascending", defaultValue = "true") boolean ascending,
			@RequestBody(required = false) String filterString) throws Exception {
		Specifications<T> specs = null;
		if (filterString != null) {
			ObjectMapper om = new ObjectMapper();
			JsonNode filter = om.readTree(filterString);
			for (Iterator<Entry<String, JsonNode>> itr = filter.fields(); itr.hasNext();) {
				Entry<String, JsonNode> fi = itr.next();
				String fin = fi.getKey();
				Specification<T> spec;
				try {
					spec = (Specification<T>) getClass()
							.getMethod("filter" + fin.substring(0, 1).toUpperCase() + fin.substring(1), JsonNode.class)
							.invoke(this, fi.getValue());
				} catch (NoSuchMethodException e) {
					Class<?> ftype = em.getMetamodel().entity(modelType).getAttribute(fin).getJavaType();
					if (String.class.isAssignableFrom(ftype)) {
						spec = filterString(fin, fi.getValue());
					} else if (Date.class.isAssignableFrom(ftype)) {
						spec = filterDate(fin, fi.getValue());
					} else if (Number.class.isAssignableFrom(ftype)) {
						spec = filterNumber(fin, fi.getValue());
					} else {
						log.warn(e.getMessage(), e);
						throw new RuntimeException("'"+modelName+"' not support field '" + fin + "'");
					}
				}
				if (specs == null) {
					specs = Specifications.where(spec);
				} else {
					specs = specs.and(spec);
				}
			}
		}
		PageRequest pageReq;
		if (sort != null) {
			pageReq = new PageRequest(page, pageSize,
					new Sort(new Order(ascending ? Direction.ASC : Direction.DESC, sort)));
		} else {
			pageReq = new PageRequest(page, pageSize);
		}
		if (specs != null) {
			return repo.findAll(specs, pageReq);
		}
		return repo.findAll(pageReq);
	}

	@RequestMapping(value = "/{id}", method = { RequestMethod.GET })
	@Transactional
	public T getById(@PathVariable("id") K id) {
		T obj = repo.findOne(id);
		if (obj == null)
			throw new ResourceNotFoundException("Object '"+modelName+"' with id '" + id + "' not found.");
		return obj;
	}

	@RequestMapping(value = "", method = { RequestMethod.POST })
	@Transactional
	public T save(@RequestBody T obj) {
		return repo.save(obj);
	}

	@RequestMapping(value = "/{id}", method = { RequestMethod.DELETE })
	@Transactional
	public T delete(@PathVariable("id") K id) {
		T obj = repo.findOne(id);
		if (obj == null)
			throw new ResourceNotFoundException("Object '"+modelName+"' with id '" + id + "' not found.");
		repo.delete(id);
		return obj;
	}

	public static <T> Specification<T> filterString(String field, JsonNode param) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (param.isObject()) {
					if (param.has("equal")) {
						return cb.equal(root.get(field), param.get("equal").asText());
					}
					if (param.has("like")) {
						return cb.like(root.get(field), param.get("like").asText());
					}
					throw new RuntimeException("Not supported "+param);
				}
				return cb.like(root.get(field), param.asText());
			}
		};
	}

	public static <T> Specification<T> filterDate(String field, JsonNode param) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					if (param.isObject()) {
						return cb.between(root.get(field).as(Date.class), sdf.parse(param.get("start").asText()),
								sdf.parse(param.get("end").asText()));
					}
					return cb.equal(root.get(field).as(Date.class), sdf.parse(param.asText()));
				} catch (ParseException e) {
					throw new RuntimeException(
							"Invalid field date format field: '" + field + "' value: '" + param.asText() + "'");
				}
			}
		};
	}

	public static <T> Specification<T> filterNumber(String field, JsonNode param) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (param.isObject()) {
					return cb.between(root.get(field).as(BigDecimal.class), new BigDecimal(param.get("start").asText()),
							new BigDecimal(param.get("end").asText()));
				}
				return cb.equal(root.get(field).as(BigDecimal.class), new BigDecimal(param.asText()));
			}
		};
	}
}