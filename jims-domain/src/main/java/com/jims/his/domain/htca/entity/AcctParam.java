package com.jims.his.domain.htca.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by heren on 2015/12/2.
 */
@Entity
@Table(name = "ACCT_PARAM", schema = "HTCA")
public class AcctParam implements java.io.Serializable {

    private String id ;
    private String paramName ;
    private String paramSql ;
    private String hospitalId ;
    private String paramType ;
    public AcctParam(String id, String paramName, String paramSql, String hospitalId, String paramType) {
        this.id = id;
        this.paramName = paramName;
        this.paramSql = paramSql;
        this.hospitalId = hospitalId;
        this.paramType = paramType;
    }

    public AcctParam() {
    }

    // Property accessors
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="param_name")
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Column(name="param_sql")
    public String getParamSql() {
        return paramSql;
    }

    public void setParamSql(String paramSql) {
        this.paramSql = paramSql;
    }

    @Column(name="hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Column(name="param_type")
    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
}
