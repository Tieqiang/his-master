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
    public List<ExpSupplierCatalog> findSupplierBySupplierClass(String supplierClass,String q){
        String hql = "from ExpSupplierCatalog a where a.supplierClass = '"+supplierClass+"'";
        if(q!=null&&!"".equals(q)){
            //upper(a.input_code) like upper('" + q + "%')"
            hql+=" and upper(a.inputCode) like upper('%" + q + "%')";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 根据输入的拼音码定位供应商
     * @param inputCode
     * @return
     * @author fengyuguang
     */
    public List<ExpSupplierCatalog> getByInputCode(String inputCode) {
        String hql = "from ExpSupplierCatalog a where 1 = 1";
        if (inputCode != null && !"".equals(inputCode)) {
            hql += " and upper(a.inputCode) like upper('%" + inputCode + "%')";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    //查询供应商
    public List<ExpSupplierCatalog> findSupplierByInputCode(String inputCode,String q){
        String hql = "from ExpSupplierCatalog a where a.supplier =  '"+inputCode+"'  ";
        if(q!=null&&!"".equals(q)){
            hql+=" and upper(a.inputCode) like upper('" + q + "%')";
        }
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
    public List<ExpSupplierVo> listExpSupplierWithDept(String hospitalId,String q) {
        List<ExpSupplierCatalog> supplierCatalogs = this.findSupplierBySupplierClass("供应商",q);
        List<ExpSupplierCatalog> supplierCatalogs1 = this.findSupplierBySupplierClass("生产商",q);
        List<ExpSupplierVo> expSupplierVos = new ArrayList<>() ;
        //List<DeptDict> deptDicts = deptDictFacade.findByHospitalId(hospitalId);
        List<ExpStorageDept> expStorageDepts = expStorageDeptFacade.getByHospitalId(hospitalId,q,null) ;

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
                "  where upper(a.input_code) like upper('%" + q + "%')";
        if (null != type && !type.trim().equals("")) {
            sql += " and a.supplier_class = '" + type + "'";
        }
        List<ExpSupplierCatalog> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpSupplierCatalog.class);
        return nativeQuery;
    }
    /**
     * 根据当前库房以及出库类别查询可以发放的科室等
     * @author chenxy
     * @param hospitalId
     * @param storageCode
     * @param exportClass
     * @return
     */
    public List<ExpSupplierVo> listLevelByThis(String hospitalId, String storageCode, String exportClass) {
        List<ExpSupplierVo> list=new ArrayList<ExpSupplierVo>();

        /**
         * 先查询当前库房的level
         */
        Integer currentLevel=this.findLevelByStorageCode(storageCode);
        if(exportClass.equals("全部")){//
            list=findByStorageLevel(currentLevel,2,list);
            list=findAll(list);
        }else if(exportClass.equals("上级库房或者供货商")){
            if(1==currentLevel){//一级库房---
                list=findAll(list);
            }else{
                list=findByStorageLevel(currentLevel,1,list);
            }
        }else if(exportClass.equals("同级库房")){
            list=findByStorageLevel(currentLevel,0,list);
        }else if(exportClass.equals("下级库房")){
            list=findByStorageLevel(currentLevel,-1,list);
        }
        return list;
    }
    //
//    /**
//     * 通过库房代码查询库房等级
//     * @param storageCode
//     * @return
//     */
    private Integer findLevelByStorageCode(String storageCode) {

        String sql="select storageLevel from ExpStorageDept where storageCode='"+storageCode+"'";
        Object o=entityManager.createQuery(sql).getSingleResult();
        if(o!=null&&!"".equals(o)){
            return (Integer)o;
        }
        return null;
    }


    /**
     *flag==0 同级 flag=-1 下级  flag==1 上级
     * @param storageLevel
     * @return
     */
    private List<ExpSupplierVo> findByStorageLevel(Integer storageLevel,Integer flag, List<ExpSupplierVo> expSupplierVos){
//        List<ExpSupplierVo> expSupplierVos=new ArrayList<ExpSupplierVo>();
        String sql="from ExpStorageDept where 1=1";
        if(0==flag){
            sql+=" and storageLevel="+storageLevel;
        }else if(-1==flag){
            sql+=" and storageLevel="+(storageLevel+1);
        }else if(1==flag){
            sql+=" and storageLevel="+(storageLevel-1);
        }
        List<ExpStorageDept>  list=entityManager.createQuery(sql).getResultList();
        for(ExpStorageDept e:list){
            ExpSupplierVo v=new ExpSupplierVo();
            v.setSupplierName(e.getStorageName());
            v.setSupplierCode(e.getStorageCode());
            v.setInputCode(e.getDisburseNoPrefix());
            expSupplierVos.add(v);
        }

        return expSupplierVos;
    }

    /**
     * 查找所有供货商和生产商
     * @return
     */
    public List<ExpSupplierVo> findAll(List<ExpSupplierVo> expSupplierVos){
//        List<ExpSupplierVo> expSupplierVos=new ArrayList<ExpSupplierVo>();
        List<ExpSupplierCatalog> list=this.findAll(ExpSupplierCatalog.class);
        for(ExpSupplierCatalog expSupplierCatalog:list){
            ExpSupplierVo e=new ExpSupplierVo();
            e.setInputCode(expSupplierCatalog.getInputCode());
            e.setSupplierCode(expSupplierCatalog.getSupplierId());
            e.setSupplierName(expSupplierCatalog.getSupplier());
            e.setId(expSupplierCatalog.getId());
            expSupplierVos.add(e);
        }
        return expSupplierVos;
    }

    /**
     *
     * @param supplierId
     * @return
     */
    public String findNameById(String supplierId) {
        String sql="select supplier from ExpSupplierCatalog where id='"+supplierId+"'";
        return (String)entityManager.createQuery(sql).getSingleResult();
    }

    /**
     * chenxy
     * @param firmId
     * @return
     */
    public String findBySuppierId(String firmId) {
        List<String> obj=entityManager.createQuery("select id from ExpSupplierCatalog where supplierId='"+firmId+"'").getResultList();
        if(obj!=null&&!obj.isEmpty()){
            return (String)obj.get(0);
        }
        return null;
    }

    /**
     *
     * @param firmId
     * @return
     */
    public ExpSupplierCatalog findById(String firmId) {
        return (ExpSupplierCatalog)entityManager.createQuery("from ExpSupplierCatalog where id='"+firmId+"'").getSingleResult();
    }
}
