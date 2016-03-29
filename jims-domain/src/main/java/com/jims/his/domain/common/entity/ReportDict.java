package com.jims.his.domain.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * ReportDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REPORT_DICT", schema = "JIMS")
public class ReportDict implements java.io.Serializable {

	// Fields

	private String id;
    private String hospitalId;
	private String hospitalName;
	private String ip;
	private String port;

	// Constructors

	/** default constructor */
	public ReportDict() {
	}

    /**
     * full constructor
     */
    public ReportDict(String id, String hospitalId, String hospitalName, String ip, String port) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.ip = ip;
        this.port = port;
    }

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

    @Column(name = "hospital_name", length = 100)
	public String getHospitalName() {
		return this.hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

    @Column(name = "HOSPITAL_ID", length = 100)
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Column(name = "IP", length = 100)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "PORT", length = 100)
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}