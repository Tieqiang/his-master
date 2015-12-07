package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.DeptDict;
import com.jims.his.domain.common.facade.DeptDictFacade;
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

    @Inject
    public ExpSupplierCatalogFacade(EntityManager entityManager, DeptDictFacade deptDictFacade) {
        this.entityManager = entityManager;
        this.deptDictFacade = deptDictFacade;
    }

    public List<ExpSupplierCatalog> listExpSupplierCatalog() {
        String hql = "from ExpSupplierCatalog as dict where dict.supplierClass='生产商'";
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }
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
    //保存
    @Transactional
    public List<ExpSupplierCatalog> saveExpSupplierCatalog(List<ExpSupplierCatalog> insertDate){
        List<ExpSupplierCatalog> expSupplierCatalogList =  new ArrayList<>();
        if (insertDate.size() > 0){
            for (ExpSupplierCatalog expSupplierCatalog : insertDate){
                ExpSupplierCatalog result = merge(expSupplierCatalog);
                expSupplierCatalogList.add(result);
            }
        }
        return expSupplierCatalogList;
    }
    //修改
    @Transactional
    public List<ExpSupplierCatalog> updateExpSupplierCatalog(List<ExpSupplierCatalog> updateDate){
        List<ExpSupplierCatalog> expSupplierCatalogList = new ArrayList<>();
        if (updateDate.size() > 0){
            for (ExpSupplierCatalog expSupplierCatalog : updateDate){
                ExpSupplierCatalog result = merge(expSupplierCatalog);
                expSupplierCatalogList.add(result);
            }
        }
        return expSupplierCatalogList;
    }
    //删除
    @Transactional
    public List<ExpSupplierCatalog> deleteExpSupplierCatalog(List<ExpSupplierCatalog> deleteDate){
        List<ExpSupplierCatalog> expSupplierCatalogList = new ArrayList<>();
        if (deleteDate.size() > 0){
            List<String> ids = new ArrayList<>();
            for (ExpSupplierCatalog expSupplierCatalog : deleteDate){
                ids.add(expSupplierCatalog.getId());
            }
            removeByStringIds(ExpSupplierCatalog.class,ids);
            expSupplierCatalogList.addAll(deleteDate);
        }
        return expSupplierCatalogList;
    }

    /**
     * 根据科室提供出供应商外的其他
     * @param hospitalId
     * @return
     */
    public List<ExpSupplierVo> listExpSupplierWithDept(String hospitalId) {
        List<ExpSupplierCatalog> supplierCatalogs = this.findSupplierBySupplierClass("供应商");
        List<ExpSupplierVo> expSupplierVos = new ArrayList<>() ;
        List<DeptDict> deptDicts = deptDictFacade.findByHospitalId(hospitalId);

        for (ExpSupplierCatalog catalog:supplierCatalogs){
            ExpSupplierVo vo = new ExpSupplierVo(catalog.getSupplier(),catalog.getSupplierId(),catalog.getInputCode()) ;
            expSupplierVos.add(vo) ;
        }

        for(DeptDict deptDict :deptDicts){
            ExpSupplierVo vo = new ExpSupplierVo(deptDict.getDeptName(),deptDict.getDeptCode(),deptDict.getInputCode()) ;
            expSupplierVos.add(vo) ;
        }

        return expSupplierVos;
    }

    public List<ExpSupplierCatalog> listByInputCodeQ(String q) {
        String sql = "select distinct a.supplier_class,\n" +
                "       a.supplier,\n" +
                "       a.input_code\n" +
                "  from Exp_Supplier_Catalog a\n" +
                "  where upper(a.input_code) like upper('" + q + "%')";
        List<ExpSupplierCatalog> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpSupplierCatalog.class);
        return nativeQuery;
    }
}
