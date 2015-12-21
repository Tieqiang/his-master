package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.IncomeItemDict;
import com.jims.his.domain.htca.vo.PriceItemVo;

import javax.persistence.TypedQuery;
import javax.print.attribute.standard.RequestingUserName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 收入项目维护
 * Created by heren on 2015/10/28.
 */
public class IncomeItemDictFacade extends BaseFacade {
    /**
     * 查询所有的收费项目
     * @param hospitalId
     * @return
     */
    public List<IncomeItemDict> findByHospitalId(String hospitalId) {
        String hql = "from IncomeItemDict as dict where dict.hospitalId='"+hospitalId+"' order by dict.reckItemName" ;
        return createQuery(IncomeItemDict.class,hql,new ArrayList<Object>()).getResultList();
    }


    /**
     * 修改收入项目字典
     * @param incomeItemDictBeanChangeVo
     */
    @Transactional
    public void saveIncomeItemDict(BeanChangeVo<IncomeItemDict> incomeItemDictBeanChangeVo) {
        List<IncomeItemDict> inserted = incomeItemDictBeanChangeVo.getInserted();
        List<IncomeItemDict> updated = incomeItemDictBeanChangeVo.getUpdated();
        List<IncomeItemDict> deleted = incomeItemDictBeanChangeVo.getDeleted();

        inserted.addAll(updated) ;

        for (IncomeItemDict incomeItemDict :inserted){
            merge(incomeItemDict) ;

        }

        List<String> ids = new ArrayList<>() ;
        for(IncomeItemDict dict: deleted){
            ids.add(dict.getId()) ;
        }
        if(ids.size()>0){
            removeByStringIds(IncomeItemDict.class,ids);
        }
    }

    /**
     * 更新收费项目的收入法
     * @param hospitalId
     * @return
     */
    @Transactional
    public List<IncomeItemDict> updateInput(String hospitalId) {
        List<IncomeItemDict> incomeItemDicts = this.findByHospitalId(hospitalId) ;
        for(IncomeItemDict dict:incomeItemDicts){
            dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getReckItemName()));
            merge(dict) ;
        }
        return incomeItemDicts;
    }

    /**
     * 分页查询
     * @param hospitalId
     * @param rows
     * @param page
     * @return
     */
    public PageEntity<IncomeItemDict> findByHospitalId(String hospitalId, String rows, String page) {
        String hql = "from IncomeItemDict as dict where dict.hospitalId='"+hospitalId+"' order by dict.reckItemName" ;
        TypedQuery<IncomeItemDict> query = createQuery(IncomeItemDict.class, hql, new ArrayList<Object>());
        Integer pageSize = Integer.parseInt(rows) ;
        Integer currentPage = Integer.parseInt(page) ;
        query.setFirstResult(pageSize * (currentPage-1)) ;
        query.setMaxResults(pageSize) ;
        PageEntity<IncomeItemDict> pageEntity = new PageEntity<>() ;
        pageEntity.setRows(query.getResultList());
        pageEntity.setTotal(super.count(new IncomeItemDict()));
        return pageEntity ;
    }


    public PageEntity<IncomeItemDict> findPriceItem(String hospitalId, String reckCode) {

        String sql = "\n" +
                "select distinct a.item_class,\n" +
                "                a.item_code,\n" +
                "                a.item_name,\n" +
                "                a.class_on_reckoning,\n" +
                "                b.class_name\n" +
                "  from comm.price_list a, COMM.reck_item_class_dict b\n" +
                " where a.class_on_reckoning = b.class_code\n" +
                "   and a.item_code not in\n" +
                "       (select NVL(price_item_code, '000000') from htca.income_item_dict where hospital_id='"+hospitalId+"')" ;

        if(!"".equals(reckCode) && reckCode!=null){
            sql+=" and a.class_on_reckoning='"+reckCode+"'" ;
        }

        PageEntity<IncomeItemDict> pageEntity = new PageEntity<>() ;
        pageEntity.setTotal(1);

        List<PriceItemVo> priceItemVos = createNativeQuery(sql, new ArrayList<Object>(), PriceItemVo.class);
        List<IncomeItemDict> incomeItemDicts = new ArrayList<>() ;

        for(PriceItemVo vo:priceItemVos){
            IncomeItemDict dict = new IncomeItemDict() ;
            dict.setInputCode(PinYin2Abbreviation.cn2py(vo.getItemName()));
            dict.setHospitalId(hospitalId);
            dict.setPriceItemClass(vo.getItemClass());
            dict.setPriceItemName(vo.getItemName());
            dict.setPriceItemCode(vo.getItemCode());
            dict.setReckItemCode(vo.getClassOnReckoning());
            dict.setReckItemName(vo.getClassName());
            incomeItemDicts.add(dict) ;
        }
        pageEntity.setRows(incomeItemDicts);

        return pageEntity ;

    }

    public PageEntity<IncomeItemDict> findByHOspitalIdAndReckCode(String hospitalId, String reckCode) {

        PageEntity<IncomeItemDict> pageEntity = new PageEntity<>() ;

        String hql = "from IncomeItemDict as dict where dict.hospitalId='"+hospitalId+"' and dict.reckItemCode = '"+reckCode+"'" ;

        TypedQuery<IncomeItemDict> query = createQuery(IncomeItemDict.class, hql, new ArrayList<Object>());
        List<IncomeItemDict> resultList = query.getResultList();
        pageEntity.setRows(resultList);
        pageEntity.setTotal(1);

        return pageEntity;
    }
}
