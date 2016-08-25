package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.common.facade.StaffDictFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.entity.StaffVsStorage;
import com.jims.his.domain.ieqm.facade.ExpStorageDeptFacade;
import com.jims.his.domain.ieqm.facade.StaffVsStorageFacade;
import com.jims.his.domain.ieqm.vo.StaffVsStorageVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2016/8/1.
 */
@Produces("application/json")
@Path("staff-vs-storage")
public class StaffVsStorageService {


    private StaffVsStorageFacade staffVsStorageFacade ;
    private ExpStorageDeptFacade expStorageDeptFacade ;
    private StaffDictFacade staffDictFacade;


    @Inject
    public StaffVsStorageService(StaffVsStorageFacade staffVsStorageFacade, ExpStorageDeptFacade expStorageDeptFacade,StaffDictFacade staffDictFacade) {
        this.staffVsStorageFacade = staffVsStorageFacade;
        this.expStorageDeptFacade = expStorageDeptFacade;
        this.staffDictFacade = staffDictFacade;
    }

    /**
     * 查询人员信息(不包含已经存在对应库房的人员)
     * @param hospitalId 组织机构ID
     * @return
     * @author fengyuguang
     */
    @GET
    @Path("list-staff-remove-exist-in-vs")
    public List<StaffDict> listStaffRemoveExistInVs(@QueryParam("hospitalId")String hospitalId,@QueryParam("q")String q){
        List<StaffDict> allStaff = staffDictFacade.findByHospital(hospitalId,q);
        List<StaffDict> temp = new ArrayList<>();   //临时集合，存放需要移除的数据(即已经有对应库房的数据)
        for (StaffDict staffDict : allStaff) {
            List<StaffVsStorage> vsList = staffVsStorageFacade.getListByStaffId(staffDict.getId());
            if(null != vsList && vsList.size() > 0){
                temp.add(staffDict);
            }
        }
        allStaff.removeAll(temp);
        return allStaff;
    }

    /**
     * 查询人员对应库房的人员信息
     * @param hospitalId 组织机构ID
     * @return
     * @author fengyuguag
     */
    @GET
    @Path("list-staff")
    public List<StaffVsStorageVo> getStaffList(@QueryParam("name")String name,@QueryParam("hospitalId")String hospitalId){
        return staffVsStorageFacade.getStaffList(name,hospitalId);
    }

    /**
     * 根据人员姓名或登录名模糊查询人员对应库房信息(如果名称或登录名为空查询所有)
     * @param name
     * @return
     * @author fengyuguang
     */
    @Path("list")
    @GET
    public List<StaffVsStorageVo> getListByName(@QueryParam("name")String name){
        return staffVsStorageFacade.getListByName(name);
    }

    /**
     * 保存增删改
     * @param beanChangeVo
     * @return
     * @author fengyuguang
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<StaffVsStorage> beanChangeVo){
        try {
            List<StaffVsStorage> lists = new ArrayList<>();
            lists = staffVsStorageFacade.save(beanChangeVo);
            return Response.status(Response.Status.OK).entity(lists).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if (errorException.getErrorMessage().toString().indexOf("最大值") != -1) {
                errorException.setErrorMessage("输入数据超过长度！");
            } else if (errorException.getErrorMessage().toString().indexOf("唯一") != -1) {
                errorException.setErrorMessage("数据已存在，保存失败！");
            } else {
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    @GET
    @Path("find-by-storage-code")
    public List<StaffVsStorage> findStaffByStorageCode(@QueryParam("storageCode")String storageCode,String hospitalId){
        return staffVsStorageFacade.findStaffByStorageCode(storageCode,hospitalId) ;
    }

    @GET
    @Path("find-staff-by-storage")
    public List<StaffDict> findStaffDictByStorageCode(@QueryParam("storageCode")String storageCode,@QueryParam("hospitalId")String hospitalId){
        return staffVsStorageFacade.findStaffDictByStroageCode(storageCode,hospitalId) ;
    }

    @GET
    @Path("find-storage-by-hospital")
    public List<ExpStorageDept> findByHospitalId(@QueryParam("hospitalId")String hospitalId){
        return expStorageDeptFacade.findStroageByHospitalId(hospitalId) ;
    }

    @GET
    @Path("find-storage-by-staffId")
    public List<ExpStorageDept> findByStaffId(@QueryParam("staffId")String staffId){
        List<ExpStorageDept> list= staffVsStorageFacade.findStorageByStaffId(staffId) ;
        return list;
    }

}
