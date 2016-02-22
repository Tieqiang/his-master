package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.CalcIncomeDetailForHoliday;
import com.jims.his.domain.htca.facade.FetchDataHolidayFacade;
import com.jims.his.domain.htca.facade.FetchDataHolidayFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by heren on 2015/11/18.
 * 提取HIS数据服务
 */
@Produces("application/json")
@Path("fetch-data-holiday")
public class FetchHolidayDataService {

    private FetchDataHolidayFacade fetchDataHolidayFacade ;

    @Inject
    public FetchHolidayDataService(FetchDataHolidayFacade fetchDataHolidayFacade) {
        this.fetchDataHolidayFacade = fetchDataHolidayFacade;
    }

    @POST
    @Path("fetch-from-his")
    public Response fetchFromHis(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth,
                                 @QueryParam("fetchTypeId")String fetchTypeId){
        try {
            List<CalcIncomeDetailForHoliday> incomeDetails = fetchDataHolidayFacade.fetchFromHis(hospitalId, yearMonth,fetchTypeId);
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
    public PageEntity<CalcIncomeDetailForHoliday> clacDataFromHis(@QueryParam("hospitalId")String hospitalId,
                                                        @QueryParam("yearMonth")String yearMonth,
                                                        @QueryParam("page")String page,
                                                        @QueryParam("rows")String rows){

        return fetchDataHolidayFacade.findByPages(hospitalId,yearMonth,page,rows) ;
    }


    @POST
    @Path("save-calc")
    public Response saveCalc(List<CalcIncomeDetailForHoliday> calcIncomeDetails){
        try {
            List<CalcIncomeDetailForHoliday> calcIncomeDetails1 = fetchDataHolidayFacade.saveCalc(calcIncomeDetails);
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
            List<CalcIncomeDetailForHoliday> calcIncomeDetails1 = fetchDataHolidayFacade.devideCalcIncome(hospitalId,yearMonth);
            return Response.status(Response.Status.OK).entity(calcIncomeDetails1).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
