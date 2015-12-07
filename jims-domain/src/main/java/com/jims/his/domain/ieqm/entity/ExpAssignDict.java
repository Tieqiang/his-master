package com.jims.his.domain.ieqm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.print.DocFlavor;

/**
 * ExpAssignDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_ASSIGN_DICT", schema = "JIMS",uniqueConstraints = @UniqueConstraint(columnNames = {"ASSIGN_CODE"}))
public class ExpAssignDict implements java.io.Serializable {

	// Fields

	private String id;
    private String assignCode;
	private String assignName;
	private String inputCode;
	private String inputCodeWb;

	// Constructors

	/** default constructor */
	public ExpAssignDict() {
	}


    /** full constructor */
    public ExpAssignDict(String id, String assignCode, String assignName, String inputCode, String inputCodeWb) {
        this.id = id;
        this.assignCode = assignCode;
        this.assignName = assignName;
        this.inputCode = inputCode;
        this.inputCodeWb = inputCodeWb;
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

    @Column(name = "ASSIGN_CODE", length = 10)
    public String getAssignCode() {
        return assignCode;
    }

    public void setAssignCode(String assignCode) {
        this.assignCode = assignCode;
    }

    @Column(name = "ASSIGN_NAME", length = 20)
	public String getAssignName() {
		return this.assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	@Column(name = "INPUT_CODE", length = 10)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "INPUT_CODE_WB", length = 10)
	public String getInputCodeWb() {
		return this.inputCodeWb;
	}

	public void setInputCodeWb(String inputCodeWb) {
		this.inputCodeWb = inputCodeWb;
	}

}