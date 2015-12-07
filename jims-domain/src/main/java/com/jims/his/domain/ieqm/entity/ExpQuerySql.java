package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpQuerySql entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_QUERY_SQL", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "QUERY_NAME"))
public class ExpQuerySql implements java.io.Serializable {

	// Fields

	private String id;
	private String queryName;
	private String querySql;

	// Constructors

	/** default constructor */
	public ExpQuerySql() {
	}

	/** full constructor */
	public ExpQuerySql(String queryName, String querySql) {
		this.queryName = queryName;
		this.querySql = querySql;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "QUERY_NAME", unique = true, length = 40)
	public String getQueryName() {
		return this.queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	@Column(name = "QUERY_SQL")
	public String getQuerySql() {
		return this.querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

}