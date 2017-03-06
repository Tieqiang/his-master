package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.AcctDeptCost;
import com.jims.his.domain.htca.entity.AcctDeptProfit;
import com.jims.his.domain.htca.entity.CostItemDict;
import com.jims.his.domain.htca.entity.ServiceDeptIncome;
import com.jims.his.domain.htca.facade.AcctDeptCostFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * 查询成本发布情况
     * @param yearMonth
     * @param hospitalId
     * @param costItemId
     * @param operator
     * @return
     */
    @GET
    @Path("list-publish")
    public List<AcctDeptCost> getAcctDeptCostPublish(@QueryParam("yearMonth") String yearMonth, @QueryParam("hospitalId") String hospitalId,
                                              @QueryParam("costItemId") String costItemId,@QueryParam("operator")String operator) {

        return acctDeptCostFacade.listAcctDeptCostPublish(yearMonth, hospitalId, costItemId,operator);

    }

    /**
     * 科室成本确认
     * @param yearMonth
     * @param hospitalId
     * @param acctDeptId
     * @return
     */
    @GET
    @Path("publish-confirm")
    public List<AcctDeptCost> getAcctDeptCostPublishConfirm(@QueryParam("yearMonth") String yearMonth, @QueryParam("hospitalId") String hospitalId,
                                              @QueryParam("acctDeptId")String acctDeptId) {

        return acctDeptCostFacade.listAcctDeptCostPublishConfirm(yearMonth, hospitalId, acctDeptId);

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
    @Path("save-dept-devide/{saveModel}/{incomeDeptId}")
    public Response saveDeptDevideAcctDeptCost(List<AcctDeptCost> acctDeptCosts,@PathParam("incomeDeptId")String incomeDeptId,
                                               @PathParam("saveModel")String saveModel) {

        try {
            acctDeptCostFacade.saveDeptDevideDeriectWrite(acctDeptCosts, incomeDeptId,saveModel);
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


    @POST
    @Path("save-cost")
    public Response saveCostData(List<AcctDeptCost> acctDeptCosts){
        try {
            acctDeptCostFacade.saveCostData(acctDeptCosts);
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
    public List<AcctDeptCost> devideHospitalAcctDeptCost(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth,
                                                 @QueryParam("devideWay")String devideWay,@QueryParam("costItemId")String costItemId,
                                                 @QueryParam("totalMoney")Double totalMoney,
                                                 @QueryParam("depts")String depts){

        return acctDeptCostFacade.devideAcctDeptCost(hospitalId,yearMonth,devideWay,costItemId,totalMoney,depts) ;
    }



    @POST
    @Path("save-last")
    @Produces("text/plain")
    public Response sameWithLastMonth(@QueryParam("yearMonth")String yearMonth,@QueryParam("hospitalId")String hospitalId,@QueryParam("costItemId")String costItemId){
        if(yearMonth==null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("没有获取同步的月份").build();
        }
        String[] yearMonths = yearMonth.split("-") ;
        Integer month = Integer.parseInt(yearMonths[1]) ;
        Integer lastMonth = month - 1;
        Integer lastYear = 0 ;
        Integer year = Integer.parseInt(yearMonths[0]) ;

        if(lastMonth==0){
            lastMonth = lastMonth + 12 ;
            lastYear = year - 1 ;
        }else{
            lastYear = year  ;

        }
        String lastYearMonth = "" ;
        if(lastMonth<10){
            lastYearMonth = lastYear + "-0"+lastMonth ;
        }else{
            lastYearMonth = lastYear +"-"+lastMonth ;
        }
        try {
            acctDeptCostFacade.sameLastMonth(yearMonth,lastYearMonth,costItemId,hospitalId) ;
            return Response.status(Response.Status.OK).entity("ok").build() ;
        }catch(Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException.getErrorMessage()).build() ;
        }

    }



    @GET
    @Path("profit-cost")
    public List<AcctDeptCost> fetchProfitAcctDeptCost(@QueryParam("yearMonth")String yearMonth ,@QueryParam("hospitalId")String hospitalId){

        try {
            String hql = "select profit from AcctDeptProfit as profit where yearMonth = '"+yearMonth+"' and hospitalId='"+hospitalId+"'" ;
            String costItemHql = "from CostItemDict where id='402880905a207a8b015a209dbfef01c0'" ;
            CostItemDict costItemDict = acctDeptCostFacade.createQuery(CostItemDict.class,costItemHql,new ArrayList<Object>()).getSingleResult();
            List<AcctDeptProfit> acctDeptProfits = acctDeptCostFacade.createQuery(AcctDeptProfit.class,hql,new ArrayList<Object>()).getResultList() ;
            List<AcctDeptCost> acctDeptCosts = new ArrayList<>() ;

            for(AcctDeptProfit profit :acctDeptProfits){
                AcctDeptCost cost = new AcctDeptCost();
                cost.setAcctDeptId(profit.getAcctDeptId());
                double costValue = profit.getDeptIncome()-profit.getDeptCost()+profit.getIncomeChangeItem()-profit.getCostChangeItem()-profit.getManagerStaffCost()-profit.getManagerProfitCost()+profit.getManagerCostMinus() ;
                costValue = costValue*(profit.getConvertRate()/100)+profit.getSpecialIncome();
                //如果绩效奖金正数，则计入成本，否则不计成本
                if(costValue>0){
                    cost.setCost(costValue);
                }else{
                    cost.setCost(0.0);
                }
                cost.setMinusCost(cost.getCost()*((100-Double.parseDouble(costItemDict.getCalcPercent()))/100));
                cost.setCostItemId("402880905a207a8b015a209dbfef01c0");//绩效成本
                acctDeptCosts.add(cost);
            }
            return acctDeptCosts ;
        }catch (Exception e){
            return null ;
        }

    }

}
