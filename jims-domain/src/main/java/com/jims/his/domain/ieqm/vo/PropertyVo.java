package com.jims.his.domain.ieqm.vo;

/**
 * Created by admin on 2016/6/18.
 */
public class PropertyVo {

    private String expCode;

    private String packageSpec;

    private String firmId;

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public PropertyVo(String expCode, String packageSpec, String firmId) {
        this.expCode = expCode;
        this.packageSpec = packageSpec;
        this.firmId = firmId;
    }

    @Override
    public boolean equals(Object obj) {
        PropertyVo p=null;
        if(obj!=null){
            p=(PropertyVo)obj;
            if(p.getExpCode().equals(this.expCode)&&p.getPackageSpec().equals(this.packageSpec)&&p.getFirmId().equals(this.firmId)){
                return true;
            }
        }
        return false;
    }


}
