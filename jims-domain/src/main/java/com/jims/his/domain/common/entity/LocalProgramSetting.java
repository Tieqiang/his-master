package com.jims.his.domain.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * author :赵铁强
 */
@Entity
@Table(name = "LOCAL_PROGRAM_SETTING", schema = "JIMS")
public class LocalProgramSetting implements java.io.Serializable {

	// Fields

	private String id;
	private String appName;
	private String appLocalPath;
	private String loginUser;
	private String appClass;
	private String appWidthExt;
    private String color ;
    private String picUrl ;

	// Constructors

	/** default constructor */
	public LocalProgramSetting() {
	}

	/** full constructor */
	public LocalProgramSetting(String appName, String appLocalPath,
                               String loginUser, String appClass, String appWidthExt, String color, String picUrl) {
		this.appName = appName;
		this.appLocalPath = appLocalPath;
		this.loginUser = loginUser;
		this.appClass = appClass;
		this.appWidthExt = appWidthExt;
        this.color = color;
        this.picUrl = picUrl;
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

	@Column(name = "APP_NAME", length = 100)
	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Column(name = "APP_LOCAL_PATH", length = 200)
	public String getAppLocalPath() {
		return this.appLocalPath;
	}

	public void setAppLocalPath(String appLocalPath) {
		this.appLocalPath = appLocalPath;
	}

	@Column(name = "LOGIN_USER", length = 100)
	public String getLoginUser() {
		return this.loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	@Column(name = "APP_CLASS", length = 100)
	public String getAppClass() {
		return this.appClass;
	}

	public void setAppClass(String appClass) {
		this.appClass = appClass;
	}

	@Column(name = "APP_WIDTH_EXT", length = 100)
	public String getAppWidthExt() {
		return this.appWidthExt;
	}

	public void setAppWidthExt(String appWidthExt) {
		this.appWidthExt = appWidthExt;
	}


    @Column(name="color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    @Column(name="pic_url")
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}