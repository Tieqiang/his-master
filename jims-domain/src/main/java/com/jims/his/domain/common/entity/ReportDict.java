package com.jims.his.domain.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * ReportDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REPORT_SERVER_CONFIG", schema = "JIMS")
public class ReportDict implements java.io.Serializable {

	// Fields

	private String id;
    private String hospitalId;
	private String ip;
	private String port;
    private String remoteIp ;
    private String remotePort ;
    private String hospitalName ;
	// Constructors

	/** default constructor */
	public ReportDict() {
	}

    /**
     * full constructor
     */
    public ReportDict(String id, String hospitalId, String ip, String port, String remoteIp, String remotePort, String hospitalName) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.ip = ip;
        this.port = port;
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
        this.hospitalName = hospitalName;
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

    @Column(name="remote_ip")
    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    @Column(name="remote_port")
    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }


    @Column(name="hospital_name")
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}