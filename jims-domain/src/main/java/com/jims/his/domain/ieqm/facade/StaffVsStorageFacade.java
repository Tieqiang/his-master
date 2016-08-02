package com.jims.his.domain.ieqm.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.entity.StaffVsStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2016/8/1.
 */
public class StaffVsStorageFacade extends BaseFacade {


    public List<StaffVsStorage> findStaffByStorageCode(String storageCode, String hospitalId) {
        String hql = "select vs from StaffVsStorage vs,ExpStorageDept dept where dept.storageCode='"+storageCode+"' " +
                "and dept.hospitalId='"+hospitalId+"' and vs.storageId=dept.id";
        createQuery(StaffVsStorage.class,hql,new ArrayList<Object>()) ;
        return null;
    }


    public List<StaffDict> findStaffDictByStroageCode(String storageCode, String hospitalId) {
        String hql = "select staff from StaffVsStorage vs,ExpStorageDept dept,StaffDict staff where dept.storageCode='"+storageCode+"' " +
                "and dept.hospitalId='"+hospitalId+"' and vs.storageId=dept.id and staff.id=vs.staffId";
        List<StaffDict> staffDicts = createQuery(StaffDict.class,hql,new ArrayList<Object>()).getResultList() ;
        return staffDicts;
    }


    public List<ExpStorageDept> findStorageByStaffId(String staffId) {
        //没有考虑多机构
        String hql = "from ExpStorageDept as dept,StaffVsStorage st " +
                "where dept.id=st.storageId and st.staffId='"+staffId+"'" ;

        return createQuery(ExpStorageDept.class,hql,new ArrayList<Object>()).getResultList() ;

    }
}
