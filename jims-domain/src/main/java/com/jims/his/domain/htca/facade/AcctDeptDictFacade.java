package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.DeptDict;
import com.jims.his.domain.common.facade.DeptDictFacade;
import com.jims.his.domain.htca.entity.AcctDeptDict;
import com.jims.his.domain.htca.entity.AcctDeptVsDeptDict;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 核算科室维护Facade
 * Created by heren on 2015/10/28.
 */
public class AcctDeptDictFacade extends BaseFacade {


    private DeptDictFacade deptDictFacade ;

    @Inject
    public AcctDeptDictFacade(DeptDictFacade deptDictFacade) {
        this.deptDictFacade = deptDictFacade;
    }

    /**
     * 根据同步日期 和医院的信息，同步核算科室
     * @param hospitalId
     * @return
     */
    public List<AcctDeptDict> synAcctDeptDict( String hospitalId) {
        List<AcctDeptDict> acctDeptDicts = new ArrayList<>() ;
        List<DeptDict> byHospitalId = deptDictFacade.findByHospitalId(hospitalId);

        for(DeptDict deptDict :byHospitalId){

            AcctDeptDict acctDeptDict = new AcctDeptDict() ;
            acctDeptDict.setInputCode(deptDict.getInputCode());
            acctDeptDict.setDeptAttr(deptDict.getDeptAttr());
            acctDeptDict.setDeptDevideAttr(deptDict.getDeptDevideAttr());
            acctDeptDict.setDeptCode(deptDict.getDeptCode());
            acctDeptDict.setDeptName(deptDict.getDeptName());
            acctDeptDict.setDeptOutpInp(deptDict.getDeptOutpInp());
            acctDeptDict.setDeptType(deptDict.getDeptType());
            acctDeptDict.setDeptClass(deptDict.getDeptClass());
            acctDeptDict.setEndDept(deptDict.getEndDept());

            String deptTyep = deptDict.getDeptType() ;
            if("直接医疗类科室".equals(deptTyep)||"未纳入科室".equals(deptTyep)){
                acctDeptDict.setCostAppInd("否");
            }if("医疗技术类科室".equals(deptTyep)||"医疗辅助类科室".equals(deptTyep)||"管理类科室".equals(deptTyep)){
                acctDeptDict.setCostAppInd("是");
            }else{
                acctDeptDict.setCostAppInd("否");
            }

            if("直接医疗类科室".equals(deptTyep)){
                acctDeptDict.setCostAppLevel("5");
            }else if("医疗技术类科室".equals(deptTyep)){
                acctDeptDict.setCostAppLevel("4");
            }else if("医疗辅助类科室".equals(deptTyep)){
                acctDeptDict.setCostAppLevel("3");
            }else if("管理类科室".equals(deptTyep)){
                acctDeptDict.setCostAppLevel("2");
            }else if("未纳入科室".equals(deptTyep)){
                acctDeptDict.setCostAppLevel("1");
            }else{
                acctDeptDict.setCostAppLevel("0");
            }
            acctDeptDict.setHospitalId(hospitalId);
            acctDeptDicts.add(acctDeptDict) ;
        }

        return acctDeptDicts ;
    }


    public List<AcctDeptDict> checkAcctDeptDict( String hospitalId) {
        String hql = "from AcctDeptDict as dict where  " +
                "dict.hospitalId = '"+hospitalId+"'" ;
        List<AcctDeptDict> resultList = createQuery(AcctDeptDict.class, hql, new ArrayList<>()).getResultList();
        return resultList;
    }


    /**
     * 保存同步的科室
     * @param acctDeptDicts
     */
    @Transactional
    public void saveAcctDeptDict(List<AcctDeptDict> acctDeptDicts) {
        if(acctDeptDicts.size()>0){
            AcctDeptDict acctDeptDict = acctDeptDicts.get(0);
            List<AcctDeptDict> acctDeptDicts1 = this.checkAcctDeptDict(acctDeptDict.getHospitalId()) ;
            for(AcctDeptDict dict:acctDeptDicts1){
                remove(dict);
            }
        }
        for(AcctDeptDict dict:acctDeptDicts){
            merge(dict) ;
        }
    }

    /**
     * 获取目前的核算组
     * @param hospitalId
     * @return
     */
    public List<AcctDeptDict> getAcctDeptDict(String hospitalId) {

        String hql = "from AcctDeptDict as a where a.hospitalId = '"+hospitalId+"' and a.delFlag = '1'" ;
        TypedQuery<AcctDeptDict> query = createQuery(AcctDeptDict.class, hql, new ArrayList<Object>());
        return query.getResultList() ;
    }

    @Transactional
    public AcctDeptDict deleteById(String id) {
        AcctDeptDict dict = get(AcctDeptDict.class, id);
        String hql = "delete AcctDeptVsDeptDict as vsd where vsd.acctDeptId='"+id+"'" ;
        this.getEntityManager().createQuery(hql).executeUpdate() ;
        dict.setDelFlag("0");
        //通过标志位选择
        merge(dict) ;
        //remove(dict);
        return dict ;
    }

    /**
     * 保存单个核算单元
     * @param acctDeptDict
     * @return
     */
    @Transactional
    public AcctDeptDict saveAcctDeptDict(AcctDeptDict acctDeptDict) {
        AcctDeptDict merge = merge(acctDeptDict);

        return merge ;
    }

    /**
     * 根据核算单元的Id删除其与科室的对照信息
     * @param id
     */
    @Transactional
    public void delAcctVsDeptByAcctId(String id) {
        String hql = "delete from AcctDeptVsDeptDict as dict where dict.acctDeptId='"+id+"'" ;
        super.update(hql) ;
    }

    /**
     * 首先清除之前的
     * @param acctDeptVsDeptDicts
     */
    @Transactional
    public void saveAcctVsDept(List<AcctDeptVsDeptDict> acctDeptVsDeptDicts) {

        for (AcctDeptVsDeptDict acctDeptVsDeptDict :acctDeptVsDeptDicts){
            String hql = "delete from AcctDeptVsDeptDict as dict where dict.acctDeptId='"+acctDeptVsDeptDict.getAcctDeptId()+"'" +
                    " and dict.deptDictId = '"+acctDeptVsDeptDict.getDeptDictId()+"'" ;
            update(hql) ;
        }
        for (AcctDeptVsDeptDict dict:acctDeptVsDeptDicts){
            merge(dict) ;
        }
    }

    /**
     * 根据科室找到该科室对照的核算单元
     * @param deptId
     * @return
     */
    public AcctDeptVsDeptDict getAcctDeptVsDeptDict(String deptId) {
        String hql ="from AcctDeptVsDeptDict as dict where dict.deptDictId='"+deptId+"'" ;

        TypedQuery<AcctDeptVsDeptDict> query = createQuery(AcctDeptVsDeptDict.class, hql, new ArrayList<Object>());
        List<AcctDeptVsDeptDict> resultList = query.getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }else{
            return null ;
        }
    }

    @Transactional
    public void deleteDeptVsDeptDicts(List<AcctDeptVsDeptDict> acctDeptVsDeptDicts) {
        for(AcctDeptVsDeptDict vs:acctDeptVsDeptDicts){
            String hql = "delete from AcctDeptVsDeptDict as dict where dict.deptDictId = '"+vs.getDeptDictId()+"' and " +
                    "dict.acctDeptId = '"+vs.getAcctDeptId()+"'" ;
            this.getEntityManager().createQuery(hql).executeUpdate() ;
        }
    }

    /**
     * 获取所有历史核算单元信息
     * @param hospitalId
     * @return
     */
    public List<AcctDeptDict> listAll(String hospitalId) {
        String hql = "from AcctDeptDict as dict where dict.hospitalId='"+hospitalId+"'" ;
        return createQuery(AcctDeptDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }


}
