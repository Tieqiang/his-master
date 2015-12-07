package com.jims.his.service.common;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.entity.RoleDict;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.common.entity.StaffVsRole;
import com.jims.his.domain.common.facade.RoleDictFacade;
import com.jims.his.domain.common.facade.StaffDictFacade;
import com.jims.his.domain.common.vo.StaffDictVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by heren on 2015/9/22.
 */
@Path("staff-dict")
@Produces("application/json")
public class StaffDictService {

    private StaffDictFacade staffDictFacade ;
    private RoleDictFacade roleDictFacade ;

    @Inject
    public StaffDictService(StaffDictFacade staffDictFacade, RoleDictFacade roleDictFacade) {
        this.staffDictFacade = staffDictFacade;
        this.roleDictFacade = roleDictFacade;
    }

    /**
     * 保存员工信息
     * @param vo
     * @return
     */
    @Path("add")
    @POST
    public Response addStaffDict(StaffDictVo vo){

        try{
            StaffDict staffDict = new StaffDict();
            staffDict.setDeptDict(vo.getDeptDict());
            staffDict.setId(vo.getId());
            staffDict.setJob(vo.getJob());
            staffDict.setLoginName(vo.getLoginName());
            staffDict.setPassword(vo.getPassword());
            staffDict.setTitle(vo.getTitle());
            staffDict.setHospitalId(vo.getHospitalId());
            Set<StaffVsRole> staffVsRoles = new HashSet<>() ;
            for(String str:vo.getIds()){
                RoleDict roleDict = roleDictFacade.get(RoleDict.class, str);
                StaffVsRole staffVsRole = new StaffVsRole() ;
                staffVsRole.setRoleDict(roleDict);
                staffVsRole.setStaffDict(staffDict);
                staffVsRoles.add(staffVsRole) ;
            }
            staffDict.setStaffVsRoles(staffVsRoles);

            StaffDict staffDict1 = staffDictFacade.saveStaffDict(staffDict);
            return Response.status(Response.Status.OK).entity(staffDict1).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


    @Path("list")
    @GET
    public List<StaffDict> listStaffDict(){
        List<StaffDict> all = staffDictFacade.findAll(StaffDict.class);
        for(StaffDict dict :all){
            Set<StaffVsRole> staffVsRoles = dict.getStaffVsRoles();
            StringBuffer roldIds = new StringBuffer() ;
            StringBuffer roleNames = new StringBuffer() ;
            for (StaffVsRole rol:staffVsRoles){
                roldIds.append(rol.getRoleDict().getId()).append(",") ;
                roleNames.append(rol.getRoleDict().getRoleName()).append(",") ;
            }
            dict.setRoleIds(roldIds.toString());
            dict.setRoleNames(roleNames.toString());
        }
        return all ;
    }

    @DELETE
    @Path("del/{id}")
    public Response delDeptDict(@PathParam("id")String id){
        StaffDict staffDict = staffDictFacade.deleteById(id);
        return Response.status(Response.Status.OK).entity(staffDict).build() ;
    }

    @GET
    @Path("list-by-hospital")
    public List<StaffDict> listStaffDict(@QueryParam("hospitalId")String hospitalId, @QueryParam("q") String q){
        List<StaffDict> all = staffDictFacade.findByHospital(hospitalId,q);
        for(StaffDict dict :all){
            Set<StaffVsRole> staffVsRoles = dict.getStaffVsRoles();
            StringBuffer roldIds = new StringBuffer() ;
            StringBuffer roleNames = new StringBuffer() ;
            for (StaffVsRole rol:staffVsRoles){
                roldIds.append(rol.getRoleDict().getId()).append(",") ;
                roleNames.append(rol.getRoleDict().getRoleName()).append(",") ;
            }
            dict.setRoleIds(roldIds.toString());
            dict.setRoleNames(roleNames.toString());
        }
        return all ;
    }
}
