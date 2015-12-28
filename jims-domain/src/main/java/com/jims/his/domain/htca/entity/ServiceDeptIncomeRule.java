package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * ServiceDeptIncomeRule entity. @ztq
 */
@Entity
@Table(name = "SERVICE_DEPT_INCOME_RULE", schema = "HTCA")
public class ServiceDeptIncomeRule implements java.io.Serializable {

	// Fields

	private String id;
	private String ruleName;
	private String ruleSql;
	private String hospitalId;
	private String depts;
    private String costFlag ;

	// Constructors

	/** default constructor */
	public ServiceDeptIncomeRule() {
	}

	/** full constructor */
	public ServiceDeptIncomeRule(String ruleName, String ruleSql,
                                 String hospitalId, String depts, String costFlag) {
		this.ruleName = ruleName;
		this.ruleSql = ruleSql;
		this.hospitalId = hospitalId;
		this.depts = depts;
        this.costFlag = costFlag;
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

	@Column(name = "RULE_NAME", length = 100)
	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	@Column(name = "RULE_SQL", length = 1000)
	public String getRuleSql() {
		return this.ruleSql;
	}

	public void setRuleSql(String ruleSql) {
		this.ruleSql = ruleSql;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	@Column(name = "DEPTS", length = 500)
	public String getDepts() {
		return this.depts;
	}

	public void setDepts(String depts) {
		this.depts = depts;
	}

    @Column(name="cost_flag")
    public String getCostFlag() {
        return costFlag;
    }

    public void setCostFlag(String costFlag) {
        this.costFlag = costFlag;
    }
}