package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpDict;
import com.jims.his.domain.ieqm.entity.ExpPriceList;
import com.jims.his.domain.ieqm.vo.ExpStockDefineVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjing on 2015/9/30.
 */
public class ExpDictFacade extends BaseFacade {



    private EntityManager entityManager ;

    @Inject
    public ExpDictFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 保存内容
     * @param expDictList
     * @return
     */
    @Transactional
    public List<ExpDict> saveDict(List<ExpDict> expDictList){
        List<ExpDict> newUpdateDict = new ArrayList<>();
        if (expDictList.size() > 0) {
            for(ExpDict expDict: expDictList){
                ExpDict merge = merge(expDict);
                newUpdateDict.add(merge);
            }
        }
        return newUpdateDict;
    }

    @Transactional
    public List<ExpDict> updateExpDict(List<ExpDict> updateData) {

        List<ExpDict> newUpdateDict = new ArrayList<>();
        if (updateData.size() > 0) {
            for (ExpDict dict : updateData) {
                ExpDict merge = merge(dict);
                newUpdateDict.add(merge);

            }
        }
        return newUpdateDict;
    }

    @Transactional
    public List<ExpDict> deleteExpDict(List<ExpDict> deleteData) {

        List<ExpDict> newUpdateDict = new ArrayList<>();
        if (deleteData.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (ExpDict dict : deleteData) {
                ids.add(dict.getId());
            }
            super.removeByStringIds(ExpDict.class, ids);
            newUpdateDict.addAll(deleteData);
        }
        return newUpdateDict;
    }
    public List<ExpDict> listExpDict(String expCode) {
        String hql = "from ExpDict as dict where dict.expCode='" + expCode + "'";
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }

    /**
     * 根据拼音码检索字典
     *
     * @param inputCode 拼音码
     * @return
     */
    public List<ExpDict> expDictListByInputCode(String inputCode) {
        String hql = "from ExpDict a where upper(a.inputCode) like upper('" + inputCode + "%')";
        return entityManager.createQuery(hql).getResultList();
    }

    public List<ExpPriceList> expDictListByExpCode(String expCode) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String sql = "select * from EXP_PRICE_LIST a where a.exp_Code = '"+expCode+"' and( a.stop_Date is null or a.stop_Date > to_date('"+df.format(new Date())+"','yyyy-mm-dd hh24:mi:ss'))";
        List <ExpPriceList> list=super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceList.class);
        return list;
    }

    public List<ExpDict> expDictListDefineExpCode(String expCode) {
        String sql = "select distinct a.exp_name,a.exp_Code,a.exp_Spec,a.units from exp_dict a,EXP_STORAGE_PROFILE b " +
                " where " +
                "a.exp_Code <> b.exp_code and " +
                "a.exp_spec <> b.exp_spec and " +
                "a.units    <> b.units and " +
                "upper(a.input_code) like upper('" + expCode + "'||'%')";
        List<ExpDict> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpDict.class);
        return nativeQuery;

    }

    /***
     *
     * chenxy
     * @param expCode
     * @return
     */
    public ExpDict findByCode(String expCode) {
        String sql="from ExpDict where expCode='"+expCode+"'";
        return (ExpDict)entityManager.createQuery(sql).getSingleResult();

    }

    /**
     * chenxy
     * @param expId
     * @return
     */
    public ExpDict findById(String expId) {
        return (ExpDict)entityManager.createQuery("from ExpDict where id='"+expId+"'").getSingleResult();
    }
}
