package com.jims.his.domain.htca.vo;

import java.io.Serializable;

/**
 * Created by heren on 2015/11/17.
 */
public class PriceItemVo implements Serializable {

    private String itemClass ;
    private String itemCode ;
    private String itemName ;
    private String classOnReckoning;
    private String className ;

    public PriceItemVo() {
    }

    public PriceItemVo(String itemClass, String itemCode, String itemName, String classOnReckoning, String className) {
        this.itemClass = itemClass;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.classOnReckoning = classOnReckoning;
        this.className = className;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getClassOnReckoning() {
        return classOnReckoning;
    }

    public void setClassOnReckoning(String classOnReckoning) {
        this.classOnReckoning = classOnReckoning;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
