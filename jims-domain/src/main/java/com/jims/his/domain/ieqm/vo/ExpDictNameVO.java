package com.jims.his.domain.ieqm.vo;

import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpDict;
import com.jims.his.domain.ieqm.entity.ExpNameDict;

import java.util.List;

/**
 * Created by heren on 2015/10/9.
 * 适用：消耗品目录维护，进行保存操作涉及到两张表的集合进行提交，定义此对象用来盛装两种集合
 */
public class ExpDictNameVO {
    private BeanChangeVo<ExpDict> expDictBeanChangeVo ;
    private BeanChangeVo<ExpNameDict> expNameDictBeanChangeVo ;

    public ExpDictNameVO() {
    }

    public ExpDictNameVO(BeanChangeVo<ExpDict> expDictBeanChangeVo, BeanChangeVo<ExpNameDict> expNameDictBeanChangeVo) {
        this.expDictBeanChangeVo = expDictBeanChangeVo;
        this.expNameDictBeanChangeVo = expNameDictBeanChangeVo;
    }

    public BeanChangeVo<ExpDict> getExpDictBeanChangeVo() {
        return expDictBeanChangeVo;
    }

    public void setExpDictBeanChangeVo(BeanChangeVo<ExpDict> expDictBeanChangeVo) {
        this.expDictBeanChangeVo = expDictBeanChangeVo;
    }

    public BeanChangeVo<ExpNameDict> getExpNameDictBeanChangeVo() {
        return expNameDictBeanChangeVo;
    }

    public void setExpNameDictBeanChangeVo(BeanChangeVo<ExpNameDict> expNameDictBeanChangeVo) {
        this.expNameDictBeanChangeVo = expNameDictBeanChangeVo;
    }
}
