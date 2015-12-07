package com.jims.his.domain.htca.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by heren on 2015/11/16.
 */
@Entity
@Table(name = "acct_dept_vs_dept_dict",schema = "HTCA")
public class AcctDeptVsDeptDict implements Serializable {

    private String id ;
    private String acctDeptId ;
    private String deptDictId ;

    public AcctDeptVsDeptDict(String id, String acctDeptId, String deptDictId) {
        this.id = id;
        this.acctDeptId = acctDeptId;
        this.deptDictId = deptDictId;
    }

    public AcctDeptVsDeptDict() {
    }

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

    @Column(name = "acct_dept_id")
    public String getAcctDeptId() {
        return acctDeptId;
    }

    public void setAcctDeptId(String acctDeptId) {
        this.acctDeptId = acctDeptId;
    }

    @Column(name = "dept_dict_id")
    public String getDeptDictId() {
        return deptDictId;
    }

    public void setDeptDictId(String deptDictId) {
        this.deptDictId = deptDictId;
    }
}
