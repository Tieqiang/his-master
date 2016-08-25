package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.entity.StaffVsStorage;
import com.jims.his.domain.ieqm.vo.StaffVsStorageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2016/8/1.
 */
public class StaffVsStorageFacade extends BaseFacade {

    /**
     * 查询人员对应库房的人员信息
     * @param hospitalId 组织机构ID
     * @return
     * @author fengyuguag
     */
    public List<StaffVsStorageVo> getStaffList(String name,String hospitalId) {
        String sql = "select a.staff_id,b.name as staff_name\n" +
                "      from jims.staff_vs_storage a,jims.staff_dict b\n" +
                "      where a.staff_id = b.id";
        if(null !=hospitalId && !hospitalId.trim().equals("")){
            sql += " and b.hospital_id = '" + hospitalId + "'";
        }
        if (null != name && !name.trim().equals("")) {
            sql += " and (b.name like upper('%" + name + "%') or b.login_name like upper('%" + name + "%'))";
        }
        sql += " group by a.staff_id,b.name";
        return super.createNativeQuery(sql,new ArrayList<Object>(),StaffVsStorageVo.class);
    }

    /**
     * 根据人员姓名或登录名模糊查询人员对应库房信息(如果名称或登录名为空查询 所有)
     * @param name
     * @return
     * @author fengyuguang
     */
    public List<StaffVsStorageVo> getListByName(String name) {
        String sql = "select c.id,c.staff_id,a.name as staff_name,c.storage_id,b.storage_name as storage_name\n" +
                "from jims.staff_dict a,jims.exp_storage_dept b, jims.staff_vs_storage c\n" +
                "where a.id = c.staff_id and b.id = c.storage_id";
        if(null != name && !name.trim().equals("")){
            sql += " and (a.name like '%" + name + "%' or a.login_name like upper('%" + name + "%'))";
        }
        return super.createNativeQuery(sql, new ArrayList<Object>(), StaffVsStorageVo.class);
    }

    /**
     * 根据人员ID查询人员对应库房信息
     * @param staffId  人员ID
     * @return
     * @author fengyuguang
     */
    public List<StaffVsStorage> getListByStaffId(String staffId){
        String sql = "select * from jims.staff_vs_storage where staff_id = '" + staffId + "'";
        return super.createNativeQuery(sql,new ArrayList<Object>(),StaffVsStorage.class);
    }

    /**
     * 保存增删改
     * @param beanChangeVo
     * @return
     * @author fengyuguang
     */
    @Transactional
    public List<StaffVsStorage> save(BeanChangeVo<StaffVsStorage> beanChangeVo){
        List<StaffVsStorage> list = new ArrayList<>();
        List<StaffVsStorage> insertedList = beanChangeVo.getInserted();
        List<StaffVsStorage> updatedList = beanChangeVo.getUpdated();
        List<StaffVsStorage> deletedList = beanChangeVo.getDeleted();
        if(null != insertedList && insertedList.size() > 0){
            for (StaffVsStorage staffVsStorage : insertedList) {
                StaffVsStorage insert = merge(staffVsStorage);
                list.add(insert);
            }
        }
        if(null != updatedList && updatedList.size() > 0){
            for (StaffVsStorage staffVsStorage : updatedList) {
                StaffVsStorage update = merge(staffVsStorage);
                list.add(update);
            }
        }
        List<String> ids = new ArrayList<>();
        if(null != deletedList && deletedList.size() > 0){
            for (StaffVsStorage staffVsStorage : deletedList) {
                ids.add(staffVsStorage.getId());
                list.add(staffVsStorage);
            }
        }
        this.removeByStringIds(StaffVsStorage.class,ids);
        return list;
    }

    public List<StaffVsStorage> findStaffByStorageCode(String storageCode, String hospitalId) {
        String hql = "select vs from StaffVsStorage vs,ExpStorageDept dept where dept.storageCode='"+storageCode+"' " +
                "and dept.hospitalId='"+hospitalId+"' and vs.storageId=dept.id";
        return  createQuery(StaffVsStorage.class,hql,new ArrayList<Object>()).getResultList();
     }


    public List<StaffDict> findStaffDictByStroageCode(String storageCode, String hospitalId) {
        String hql = "select staff from StaffVsStorage vs,ExpStorageDept dept,StaffDict staff where dept.storageCode='"+storageCode+"' " +
                "and dept.hospitalId='"+hospitalId+"' and vs.storageId=dept.id and staff.id=vs.staffId";
        List<StaffDict> staffDicts = createQuery(StaffDict.class,hql,new ArrayList<Object>()).getResultList() ;
        return staffDicts;
    }


    public List<ExpStorageDept> findStorageByStaffId(String staffId) {
         //没有考虑多机构
        String hql = "select dept from ExpStorageDept as dept,StaffVsStorage st " +
                "where dept.id=st.storageId and st.staffId='"+staffId+"'" ;
        List<ExpStorageDept> list=entityManager.createQuery(hql).getResultList();
        if(list!=null&&!list.isEmpty()){
            return list;
        }
        return null;
    }
}
