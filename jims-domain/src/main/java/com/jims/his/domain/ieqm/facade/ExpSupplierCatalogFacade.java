package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.common.entity.DeptDict;
import com.jims.his.domain.common.facade.DeptDictFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.entity.ExpSupplierCatalog;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;
import com.jims.his.domain.ieqm.vo.ExpSupplierVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangxinbo on 2015/10/10.
 */
public class ExpSupplierCatalogFacade extends BaseFacade {
    private EntityManager entityManager;
    private DeptDictFacade deptDictFacade ;
    private ExpStorageDeptFacade expStorageDeptFacade ;

    @Inject
    public ExpSupplierCatalogFacade(EntityManager entityManager, DeptDictFacade deptDictFacade, ExpStorageDeptFacade expStorageDeptFacade) {
        this.entityManager = entityManager;
        this.deptDictFacade = deptDictFacade;
        this.expStorageDeptFacade = expStorageDeptFacade;
    }

    //public List<ExpSupplierCatalog> listExpSupplierCatalog() {
    //    String hql = "from ExpSupplierCatalog as dict where dict.supplierClass='生产商'";
    //    Query query = entityManager.createQuery(hql);
    //    List resultList = query.getResultList();
    //    return resultList;
    //}
    //查询供应商
    public List<ExpSupplierCatalog> findSupplierBySupplierClass(String supplierClass){
        String hql = "from ExpSupplierCatalog a where a.supplierClass = '"+supplierClass+"'";
        return entityManager.createQuery(hql).getResultList();
    }
    //查询供应商
    public List<ExpSupplierCatalog> findSupplierByInputCode(String inputCode){
        String hql = "from ExpSupplierCatalog a where a.supplier =  '"+inputCode+"'  ";
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpSupplierCatalog> save(BeanChangeVo<ExpSupplierCatalog> beanChangeVo) {
        List<ExpSupplierCatalog> newUpdateDict = new ArrayList<>();
        List<ExpSupplierCatalog> inserted = beanChangeVo.getInserted();
        List<ExpSupplierCatalog> updated = beanChangeVo.getUpdated();
        List<ExpSupplierCatalog> deleted = beanChangeVo.getDeleted();
        for (ExpSupplierCatalog dict : inserted) {
            dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getSupplier().substring(0, dict.getSupplier().length() >= 8 ? 8 : dict.getSupplier().length())));
            ExpSupplierCatalog merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpSupplierCatalog dict : updated) {
            dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getSupplier().substring(0, dict.getSupplier().length() >= 8 ? 8 : dict.getSupplier().length())));
            ExpSupplierCatalog merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpSupplierCatalog dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpSupplierCatalog.class, ids);
        return newUpdateDict;
    }

    /**
     * 根据科室提供出供应商外的其他
     * @param hospitalId
     * @return
     */
    public List<ExpSupplierVo> listExpSupplierWithDept(String hospitalId) {
        List<ExpSupplierCatalog> supplierCatalogs = this.findSupplierBySupplierClass("供应商");
        List<ExpSupplierCatalog> supplierCatalogs1 = this.findSupplierBySupplierClass("生产商");
        List<ExpSupplierVo> expSupplierVos = new ArrayList<>() ;
        //List<DeptDict> deptDicts = deptDictFacade.findByHospitalId(hospitalId);
        List<ExpStorageDept> expStorageDepts = expStorageDeptFacade.getByHospitalId(hospitalId,null,null) ;

        for (ExpSupplierCatalog catalog:supplierCatalogs){
            ExpSupplierVo vo = new ExpSupplierVo(catalog.getSupplier(),catalog.getSupplierId(),catalog.getInputCode()) ;
            expSupplierVos.add(vo) ;
        }
        for (ExpSupplierCatalog catalog: supplierCatalogs1){
            ExpSupplierVo vo = new ExpSupplierVo(catalog.getSupplier(),catalog.getSupplierId(),catalog.getInputCode()) ;
            expSupplierVos.add(vo) ;
        }

        //for(DeptDict deptDict :deptDicts){
        //    ExpSupplierVo vo = new ExpSupplierVo(deptDict.getDeptName(),deptDict.getDeptCode(),deptDict.getInputCode()) ;
        //    expSupplierVos.add(vo) ;
        //}
        for(ExpStorageDept dept:expStorageDepts){
            ExpSupplierVo vo = new ExpSupplierVo(dept.getStorageName(),dept.getStorageCode(),dept.getDisburseNoPrefix()) ;
            expSupplierVos.add(vo) ;
        }

        return expSupplierVos;
    }

    public List<ExpSupplierCatalog> listByInputCodeQ(String q, String type) {
        String sql = "select distinct a.supplier_class,\n" +
                "       a.supplier,\n" +
                "       a.input_code\n" +
                "  from Exp_Supplier_Catalog a\n" +
                "  where upper(a.input_code) like upper('" + q + "%')";
        if (null != type && !type.trim().equals("")) {
            sql += " and a.supplier_class = '" + type + "'";
        }
        List<ExpSupplierCatalog> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpSupplierCatalog.class);
        return nativeQuery;
    }
}
