package com.jims.his.domain.ieqm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/18.
 */
@Entity
@Table(name = "MEASURES_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
        "MEASURES_CLASS","MEASURES_NAME" }))
public class MeasuresDict implements Serializable {

    // Fields
    private String id;
    private String measuresName;//单位名称
    private String measuresCode;//单位代码
    private String measuresClass;//单位类别
    private String baseUnits;//基准单位
    private String conversionRatio;//换算系数
    private String inputCode;//输入码
    // Constructors

    public MeasuresDict() {
    }

    /** default constructor */
    public MeasuresDict(String id, String measuresName, String measuresCode, String measuresClass, String baseUnits, String conversionRatio, String inputCode) {
        this.id = id;
        this.measuresName = measuresName;
        this.measuresCode = measuresCode;
        this.measuresClass = measuresClass;
        this.baseUnits = baseUnits;
        this.conversionRatio = conversionRatio;
        this.inputCode = inputCode;
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
    @Column(name = "MEASURES_NAME", unique = true , length = 8)
    public String getMeasuresName() {
        return measuresName;
    }

    public void setMeasuresName(String measuresName) {
        this.measuresName = measuresName;
    }
    @Column(name = "MEASURES_CODE", length = 3)
    public String getMeasuresCode() {
        return measuresCode;
    }

    public void setMeasuresCode(String measuresCode) {
        this.measuresCode = measuresCode;
    }
    @Column(name = "MEASURES_CLASS", unique = true , length = 10)
    public String getMeasuresClass() {
        return measuresClass;
    }

    public void setMeasuresClass(String measuresClass) {
        this.measuresClass = measuresClass;
    }
    @Column(name = "BASE_UNTIS", length = 8)
    public String getBaseUnits() {
        return baseUnits;
    }

    public void setBaseUnits(String baseUnits) {
        this.baseUnits = baseUnits;
    }
    @Column(name = "CONVERSION_RATIO", precision = 12, scale = 6)
    public String getConversionRatio() {
        return conversionRatio;
    }

    public void setConversionRatio(String conversionRatio) {
        this.conversionRatio = conversionRatio;
    }
    @Column(name = "INPUT_CODE", length = 8)
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }
}
