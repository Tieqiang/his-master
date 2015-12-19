package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.AcctDeptCost;
import com.jims.his.domain.htca.facade.AcctDeptCostFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * 科室成本Service
 * Created by heren on 2015/11/30.
 */
@Produces("application/json")
@Path("acct-dept-cost")
public class AcctDeptCostService {


    private AcctDeptCostFacade acctDeptCostFacade;

    @Inject
    public AcctDeptCostService(AcctDeptCostFacade acctDeptCostFacade) {
        this.acctDeptCostFacade = acctDeptCostFacade;
    }


    @GET
    @Path("list")
    public List<AcctDeptCost> getAcctDeptCost(@QueryParam("yearMonth") String yearMonth, @QueryParam("hospitalId") String hospitalId,
                                              @QueryParam("costItemId") String costItemId) {

        return acctDeptCostFacade.listAccctDeptCost(yearMonth, hospitalId, costItemId);

    }

    @POST
    @Path("save")
    public Response saveAcctDeptCost(List<AcctDeptCost> acctDeptCosts) {

        try {
            acctDeptCostFacade.saveDeriectWrite(acctDeptCosts);
            return Response.status(Response.Status.OK).entity(acctDeptCosts).build();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    @POST
    @Path("save-devide")
    public Response saveDevideAcctDeptCost(List<AcctDeptCost> acctDeptCosts) {

        try {
            acctDeptCostFacade.saveDevideDeriectWrite(acctDeptCosts);
            return Response.status(Response.Status.OK).entity(acctDeptCosts).build();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    @POST
    @Path("delete-dept-cost")
    public Response deleteAcctDeptCost(List<String> ids) {

        try {
            acctDeptCostFacade.deleteAcctDeptCost(ids);
            return Response.status(Response.Status.OK).entity(ids).build();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    @GET
    @Path("list-by-dept")
    public List<AcctDeptCost> findAcctDeptCostByDeptId(@QueryParam("hospitalId") String hospitalId, @QueryParam("deptId") String deptId,
                                                       @QueryParam("yearMonth") String yearMonth) {

        String hql = "from AcctDeptCost as cost where cost.hospitalId='" + hospitalId + "' and cost.yearMonth = '" + yearMonth + "' and " +
                "cost.acctDeptId = '" + deptId + "'";

        return acctDeptCostFacade.find(AcctDeptCost.class, hql, new ArrayList<Object>());

    }


    /**
     * 分页加载某一个月份的数据
     * @param hospitalId
     * @param yearMonth
     * @param page
     * @param rows
     * @return
     */
    @GET
    @Path("list-all")
    public PageEntity<AcctDeptCost> findAllDeptCost(@QueryParam("hospitalId") String hospitalId, @QueryParam("yearMonth") String yearMonth,
                                                    @QueryParam("page")String page, @QueryParam("rows")String rows) {
        return acctDeptCostFacade.listAll(hospitalId, yearMonth,page,rows);
    }

    @GET
    @Path("fetch-cost")
    public Response fetchCostData(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth,
                                  @QueryParam("fetchTypeId")String fetchTypeId ){
        try {
            List<AcctDeptCost> acctDeptCosts = acctDeptCostFacade.fetchCostData(hospitalId, yearMonth, fetchTypeId);
            return Response.status(Response.Status.OK).entity(acctDeptCosts).build();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }


    @GET
    @Path("list-collection")
    public List<AcctDeptCost> listCollectionDeptCost(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth){

        String sql = "select \n" +
                "                 '' id,\n" +
                "                 '' memo,\n" +
                "                 '' fetch_way,\n" +
                "                 acct_dept_id,\n" +
                "                 cost_item_id,\n" +
                "                 sum(cost) cost,\n" +
                "                 sum(minus_cost) minus_cost,\n" +
                "                 year_month,\n" +
                "                 hospital_id\n" +
                "            from htca.acct_dept_cost\n" +
                "          where hospital_id ='"+hospitalId+"'\n" +
                "          and year_month = '"+yearMonth+"'\n" +
                "          group by acct_dept_id,\n" +
                "                 cost_item_id,\n" +
                "                 year_month,\n" +
                "                 hospital_id" +
                "         order by acct_dept_id" ;

        return acctDeptCostFacade.createNativeQuery(sql,new ArrayList<Object>(),AcctDeptCost.class) ;
    }

    //分摊类型成本计算
    @GET
    @Path("cost-devide")
    public List<AcctDeptCost> devideAcctDeptCost(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth,
                                                 @QueryParam("devideWay")String devideWay,@QueryParam("costItemId")String costItemId,
                                                 @QueryParam("totalMoney")Double totalMoney){

        return acctDeptCostFacade.devideAcctDeptCost(hospitalId,yearMonth,devideWay,costItemId,totalMoney) ;
    }

}
