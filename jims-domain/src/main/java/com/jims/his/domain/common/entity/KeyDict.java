package com.jims.his.domain.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by heren on 2016/7/14.
 */
@Entity
@Table(name = "KEY_DICT", schema = "JIMS")
public class KeyDict implements Serializable{
    private String id ;
    private String keyName ;
    private String keyCode ;

    public KeyDict() {
    }

    public KeyDict(String id, String keyName, String keyCode) {
        this.id = id;
        this.keyName = keyName;
        this.keyCode = keyCode;
    }
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="key_code")
    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    @Column(name="key_name")
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
