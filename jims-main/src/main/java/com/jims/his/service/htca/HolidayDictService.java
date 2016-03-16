package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.entity.HospitalDict;
import com.jims.his.domain.htca.entity.HolidayDict;
import com.jims.his.domain.htca.facade.HolidayDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**取消测试内容
 * Created by heren on 2016/3/6.
 */
@Path("holiday-dict")
@Produces("application/json")
public class HolidayDictService {

    private HolidayDictFacade holidayDictFacade ;

    @Inject
    public HolidayDictService(HolidayDictFacade holidayDictFacade) {
        this.holidayDictFacade = holidayDictFacade;
    }

    @GET
    @Path("list")
    public List<HolidayDict> listHolidayDict(@QueryParam("yearMonth")String yearMonth){
        return holidayDictFacade.listHolidayDictByYearMonth(yearMonth) ;
    }

    /**
     * 保存更新日期
     * @param holidayDicts
     * @return
     */
    @POST
    @Path("merge")
    public Response mergeHolidayDict(List<HolidayDict> holidayDicts){
        try{
            List<HolidayDict> holidayDictList  = holidayDictFacade.mergeHolidays(holidayDicts) ;
            return Response.status(Response.Status.OK).entity(holidayDictList).build() ;
        }catch (Exception e ){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @POST
    @Path("del")
    public Response deleteHolidayDict(@QueryParam("id")String id){
        try{
            HolidayDict holidayDict  = holidayDictFacade.deleteHolidayDict(id) ;
            return Response.status(Response.Status.OK).entity(holidayDict).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
