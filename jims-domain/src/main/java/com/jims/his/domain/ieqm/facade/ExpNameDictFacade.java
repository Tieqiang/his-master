package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpDict;
import com.jims.his.domain.ieqm.entity.ExpNameDict;
import com.jims.his.domain.ieqm.vo.ExpDictNameVO;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangjing on 2015/9/23.
 */
public class ExpNameDictFacade extends BaseFacade {



    private EntityManager entityManager ;

    @Inject
    public ExpNameDictFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public ExpNameDict saveOrUpdate(ExpNameDict expNameDict) {
        ExpNameDict expNameDict1 = super.merge(expNameDict);
        return expNameDict1 ;
    }

    @Transactional
    public ExpNameDict deleteById(String expNameId) {
        ExpNameDict expNameDict = get(ExpNameDict.class,expNameId) ;
        remove(expNameDict);
        return expNameDict ;
    }

    /**
     * 查询产品及价格联合显示的自定义对象列表
     * @return
     */
    public List<ExpNameCaVo> listExpNameCa(){
        String sql = "select a.exp_code,a.exp_name,a.input_code,input_code_wb,b.exp_spec,b.firm_id,b.trade_price retail_price from EXP_NAME_DICT a,exp_price_list b\n" +
                "where a.exp_code = b.exp_code and b.stop_date is null";
        List<ExpNameCaVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpNameCaVo.class);
        return nativeQuery;
    }

    /**
     * 根据expCode查询对象
     * @param expCode
     * @return
     */
    public List<ExpNameDict> listExpNameDict(String expCode){
        String hql = "from ExpNameDict as dict where dict.expCode='" + expCode + "'";
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }

    /**
     * 保存增删改操作的expNameDict和expDict对象集合
     * @param beanChangeVo
     * @return
     */
    @Transactional
    public List<ExpNameDict> saveChanges(ExpDictNameVO beanChangeVo) {
        List<ExpNameDict> newUpdateDict = new ArrayList<>();
        BeanChangeVo<ExpNameDict> expNameDicts = beanChangeVo.getExpNameDictBeanChangeVo();
        BeanChangeVo<ExpDict> expDicts = beanChangeVo.getExpDictBeanChangeVo();

        if (expNameDicts != null) {
            List<ExpNameDict> insertNameDicts = expNameDicts.getInserted();
            List<ExpNameDict> updateNameDicts = expNameDicts.getUpdated();
            List<ExpNameDict> deleteNameDicts = expNameDicts.getDeleted();

            if (insertNameDicts != null && insertNameDicts.size() > 0) {
                for (ExpNameDict nameDict : insertNameDicts) {
                    nameDict.setInputCode(PinYin2Abbreviation.cn2py(nameDict.getExpName()));
                    ExpNameDict merge = merge(nameDict);
                    newUpdateDict.add(merge);
                }
            }
            if (updateNameDicts != null && updateNameDicts.size() > 0) {
                for (ExpNameDict dict : updateNameDicts) {
                    dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getExpName()));
                    ExpNameDict merge = merge(dict);
                    newUpdateDict.add(merge);
                }
            }
            if (deleteNameDicts != null && deleteNameDicts.size() > 0) {
                List<String> ids = new ArrayList<>();
                for (ExpNameDict dict : deleteNameDicts) {
                    ids.add(dict.getId());
                }
                super.removeByStringIds(ExpNameDict.class, ids);
                newUpdateDict.addAll(deleteNameDicts);
            }
        }

        if (expDicts != null) {
            List<ExpDict> insertDicts = expDicts.getInserted();
            List<ExpDict> updateDicts = expDicts.getUpdated();
            List<ExpDict> deleteDicts = expDicts.getDeleted();

            if (insertDicts != null && insertDicts.size() > 0) {
                for (ExpDict expDict : insertDicts) {
                    expDict.setInputCode(PinYin2Abbreviation.cn2py(expDict.getExpName()));
                    merge(expDict);
                }
            }
            if (updateDicts != null && updateDicts.size() > 0) {
                for (ExpDict dict : updateDicts) {
                    dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getExpName()));
                    merge(dict);
                }
            }
            if (deleteDicts != null && deleteDicts.size() > 0) {
                List<String> ids = new ArrayList<>();
                for (ExpDict dict : deleteDicts) {
                    ids.add(dict.getId());
                }
                super.removeByStringIds(ExpDict.class, ids);
            }
        }
        return newUpdateDict;
    }
    /**
     * 保存内容
     * @param expNameDictList
     * @return
     */
    @Transactional
    public List<ExpNameDict> saveNameDict(List<ExpNameDict> expNameDictList){
        List<ExpNameDict> newUpdateDict = new ArrayList<>();
        if (expNameDictList.size() > 0) {
            for(ExpNameDict nameDict: expNameDictList){
                ExpNameDict merge = merge(nameDict);
                newUpdateDict.add(merge);
            }
        }
        return newUpdateDict;
    }

    /**
     * 修改对象内容
     * @param updateData
     * @return
     */
    @Transactional
    public List<ExpNameDict> updateExpNameDict(List<ExpNameDict> updateData) {

        List<ExpNameDict> newUpdateDict = new ArrayList<>();
        if (updateData.size() > 0) {
            for (ExpNameDict dict : updateData) {
                ExpNameDict merge = merge(dict);
                newUpdateDict.add(merge);

            }
        }
        return newUpdateDict;
    }

    /**
     * 删除对象
     * @param deleteData
     * @return
     */
    @Transactional
    public List<ExpNameDict> deleteExpNameDict(List<ExpNameDict> deleteData) {

        List<ExpNameDict> newUpdateDict = new ArrayList<>();
        if (deleteData.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (ExpNameDict dict : deleteData) {
                ids.add(dict.getId());
            }
            super.removeByStringIds(ExpNameDict.class, ids);
            newUpdateDict.addAll(deleteData);
        }
        return newUpdateDict;
    }
    /**
     * 根据拼音码检索字典
     *
     * @param inputCode 拼音码
     * @return
     */
    public List<ExpNameCaVo> listExpNameCaByInputCode(String inputCode) {
        String sql = "select distinct a.exp_code,\n" +
                "       a.exp_name,\n" +
                "       a.input_code\n" +
                "  from EXP_NAME_DICT a,jims.exp_price_list b \n" +
                "  where (upper(a.input_code) like upper('%" + inputCode + "%')" +
                "   or a.exp_code like '%"+inputCode+"%')" +
                "  and a.exp_code = b.exp_code and b.start_date <= sysdate\n" +
                "   AND (b.stop_date IS NULL OR\n" +
                "       b.stop_date > sysdate) ";
        List<ExpNameCaVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpNameCaVo.class);
        return nativeQuery;
    }

    /**
     * 根据输入的拼音码查询物品(条件:该物品在指定库房库存不为0)
     * @param inputCode
     * @param storageCode 库房代码
     * @return
     * @fengyuguang
     */
    public List<ExpNameCaVo> listByInput(String inputCode,String storageCode){
        String sql = "select\n" +
                "        distinct a.exp_code,\n" +
                "        a.exp_name,\n" +
                "        a.input_code\n" +
                "    from\n" +
                "        jims.EXP_NAME_DICT a,\n" +
                "        jims.exp_price_list b, \n" +
                "        jims.exp_stock  c \n" +
                "    where\n" +
                "        (\n" +
                "            upper(a.input_code) like upper('%" + inputCode + "%')   \n" +
                "            or a.exp_code like '%" + inputCode + "%'\n" +
                "        )  \n" +
                "        and a.exp_code = b.exp_code \n" +
                "        and c.exp_code=a.exp_code\n" +
                "        and b.start_date <= sysdate    \n" +
                "        AND (\n" +
                "            b.stop_date IS NULL \n" +
                "            OR        b.stop_date > sysdate\n" +
                "        )\n" +
                "        and c.quantity >0\n" +
                "        and c.storage = '" + storageCode + "'";
        List<ExpNameCaVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpNameCaVo.class);
        return nativeQuery;
    }

    /**
     * 查询当前前缀所匹配的最大序号
     * @param length
     * @param header
     * @return
     */
    public int findMaxExpCode(int length, String header){
        int newLen = length+1;
        String sql = "SELECT max(to_number(substr(exp_code,"+  newLen+",length(exp_code) - "+ length+"))) FROM EXP_NAME_DICT WHERE " +
                "substr(EXP_NAME_DICT.EXP_CODE,1,"+ length+") = '"+header+"'";

        List result = super.createNativeQuery(sql).getResultList();
        if(result != null && result.size() > 0){
            BigDecimal num = (BigDecimal) result.get(0);
            if(num!=null){
                return num.intValue()+1;
            }else{
                return 1;
            }
        }else{
            return 1;
        }

    }

    /**
     * 查询次商家所提供的产品
     * @param inputCode
     * @param supplerId
     * @return
     */
    public List<ExpNameCaVo> listExpNameBySupplier(String inputCode, String supplerId) {
        String sql = "select distinct a.exp_code,\n" +
                "       a.exp_name,\n" +
                "       a.input_code\n" +
                "  from EXP_NAME_DICT a,jims.exp_price_list b \n" +
                "  where upper(a.input_code) like upper('" + inputCode + "%')" +
                "   or(a.exp_code) like '"+inputCode+"%'" +
                "  and a.exp_code = b.exp_code and b.start_date <= sysdate\n" +
                "   AND (b.stop_date IS NULL OR\n" +
                "       b.stop_date > sysdate) AND B.FIRM_ID='"+supplerId+"'";
        List<ExpNameCaVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpNameCaVo.class);
        return nativeQuery;
    }
}
