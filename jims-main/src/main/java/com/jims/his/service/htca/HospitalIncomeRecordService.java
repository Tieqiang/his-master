package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.HospitalIncomeRecord;
import com.jims.his.domain.htca.facade.HospitalIncomeRecordFacade;
import com.jims.his.domain.htca.vo.HospitalIncomeRecordVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by heren on 2015/10/29.
 */
@Produces("application/json")
@Path("income-record")
public class HospitalIncomeRecordService {

    private HospitalIncomeRecordFacade hospitalIncomeRecordFacade ;

    @Inject
    public HospitalIncomeRecordService(HospitalIncomeRecordFacade hospitalIncomeRecordFacade) {
        this.hospitalIncomeRecordFacade = hospitalIncomeRecordFacade;
    }

    @GET
    @Path("list")
    public List<HospitalIncomeRecordVo> listByHospitalId(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth){
        return hospitalIncomeRecordFacade.findByHospitalId(hospitalId,yearMonth) ;
    }

    @POST
    @Path("income-calc")
    public Response calculateHospitalIncomeRecord(String hospitalId,String yearMonth){

        try{
            List<HospitalIncomeRecord> hospitalIncomeRecords = hospitalIncomeRecordFacade.calculateHospitalIncomeRecord(hospitalId,yearMonth) ;
            return Response.status(Response.Status.OK).build() ;
        }catch(Exception e ){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @Path("get-pre-income")
    @GET
    public List<HospitalIncomeRecord> calcHospitalIncomeRecord(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth){
        return hospitalIncomeRecordFacade.calculateHospitalIncomeRecord(hospitalId,yearMonth) ;
    }

}
