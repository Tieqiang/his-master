package com.jims.his.domain.htca.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * HolidayDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HOLIDAY_DICT", schema = "HTCA")
public class HolidayDict implements java.io.Serializable {

	// Fields

	private String id;
	private String holiday;
	private String holidayName;
	private String memo;
	private String operator;
	private Date operatorDate;
    private String fullDay ;

	// Constructors

	/** default constructor */
	public HolidayDict() {
	}

	/** full constructor */
	public HolidayDict(String holiday, String holidayName, String memo,
                       String operator, Date operatorDate, String fullDay) {
		this.holiday = holiday;
		this.holidayName = holidayName;
		this.memo = memo;
		this.operator = operator;
		this.operatorDate = operatorDate;
        this.fullDay = fullDay;
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

	@Column(name = "HOLIDAY", length = 20)
	public String getHoliday() {
		return this.holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	@Column(name = "HOLIDAY_NAME", length = 100)
	public String getHolidayName() {
		return this.holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "OPERATOR", length = 64)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OPERATOR_DATE", length = 7)
	public Date getOperatorDate() {
		return this.operatorDate;
	}

	public void setOperatorDate(Date operatorDate) {
		this.operatorDate = operatorDate;
	}


    @Column(name="FULL_DAY")
    public String getFullDay() {
        return fullDay;
    }

    public void setFullDay(String fullDay) {
        this.fullDay = fullDay;
    }
}