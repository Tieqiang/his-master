package com.jims.his.service.ieqm;

import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.entity.StaffVsStorage;
import com.jims.his.domain.ieqm.facade.ExpStorageDeptFacade;
import com.jims.his.domain.ieqm.facade.StaffVsStorageFacade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Created by heren on 2016/8/1.
 */
@Produces("application/json")
@Path("staff-vs-storage")
public class StaffVsStorageService {


    private StaffVsStorageFacade staffVsStorageFacade ;
    private ExpStorageDeptFacade expStorageDeptFacade ;


    @Inject
    public StaffVsStorageService(StaffVsStorageFacade staffVsStorageFacade, ExpStorageDeptFacade expStorageDeptFacade) {
        this.staffVsStorageFacade = staffVsStorageFacade;
        this.expStorageDeptFacade = expStorageDeptFacade;
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
        return staffVsStorageFacade.findStorageByStaffId(staffId) ;
    }

}
