package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 人员对应库房Vo集合类
 * Created by fyg on 2016/8/22.
 */
@XmlRootElement
public class StaffVsStorageVo implements Serializable {
    private String id;
    private String staffId;         //人员ID
    private String storageId;       //库房ID

    private String staffName;       //人员姓名
    private String storageName;     //库房名称

    public StaffVsStorageVo() {
    }

    public StaffVsStorageVo(String id, String staffId, String storageId, String staffName, String storageName) {
        this.id = id;
        this.staffId = staffId;
        this.storageId = storageId;
        this.staffName = staffName;
        this.storageName = storageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
}
