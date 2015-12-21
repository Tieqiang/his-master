package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.entity.HospitalDict;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.CalcIncomeDetail;
import com.jims.his.domain.htca.facade.FetchDataFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by heren on 2015/11/18.
 * 提取HIS数据服务
 */
@Produces("application/json")
@Path("fetch-data")
public class FetchDataService {

    private FetchDataFacade fetchDataFacade ;

    @Inject
    public FetchDataService(FetchDataFacade fetchDataFacade) {
        this.fetchDataFacade = fetchDataFacade;
    }

    @POST
    @Path("fetch-from-his")
    public Response fetchFromHis(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth,
                                 @QueryParam("fetchTypeId")String fetchTypeId){
        try {
            List<CalcIncomeDetail> incomeDetails = fetchDataFacade.fetchFromHis(hospitalId, yearMonth,fetchTypeId);
            return Response.status(Response.Status.OK).entity(incomeDetails).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @GET
    @Path("fetch-by-page")
    public PageEntity<CalcIncomeDetail> clacDataFromHis(@QueryParam("hospitalId")String hospitalId,
                                                        @QueryParam("yearMonth")String yearMonth,
                                                        @QueryParam("page")String page,
                                                        @QueryParam("rows")String rows){

        return fetchDataFacade.findByPages(hospitalId,yearMonth,page,rows) ;
    }


    @POST
    @Path("save-calc")
    public Response saveCalc(List<CalcIncomeDetail> calcIncomeDetails){
        try {
            List<CalcIncomeDetail> calcIncomeDetails1 = fetchDataFacade.saveCalc(calcIncomeDetails);
            return Response.status(Response.Status.OK).entity(calcIncomeDetails1).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


    @Path("devide-income")
    @POST
    public Response devideIncome(@QueryParam("hospitalId")String hospitalId ,@QueryParam("yearMonth")String yearMonth){
        try {
            List<CalcIncomeDetail> calcIncomeDetails1 = fetchDataFacade.devideCalcIncome(hospitalId,yearMonth);
            return Response.status(Response.Status.OK).entity(calcIncomeDetails1).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
