package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.BuyExpPlan;
import com.jims.his.domain.ieqm.entity.ExpClassDict;
import com.jims.his.domain.ieqm.facade.BuyExpPlanFacade;
import com.jims.his.domain.ieqm.vo.BuyExpPlanVo;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2015/10/23.
 */
@Path("buy-exp-plan")
@Produces("application/json")
public class BuyExpPlanService {
    private BuyExpPlanFacade buyExpPlanFacade;

    @Inject
    public BuyExpPlanService(BuyExpPlanFacade buyExpPlanFacade) {
        this.buyExpPlanFacade = buyExpPlanFacade;
    }

    /**
     * 查询当前仓库当前操作人的暂存采购单号
     * @param storageCode
     * @param expNo
     * @return
     */
    @GET
    @Path("list-buy-id")
    public List<ExpClassDict> listBuyId(@QueryParam("storageCode") String storageCode, @QueryParam("expNo") String expNo){
        return buyExpPlanFacade.listBuyId(storageCode, expNo);
    }

    /**
     * 新增需要采购的消耗品
     * @param q
     * @return
     */
    @GET
    @Path("list-exp-name-by-input")
    public List<BuyExpPlanVo> listBuyExpPlanCa(@QueryParam("q") String q,@QueryParam("storageCode") String storageCode) {
        List<BuyExpPlanVo> expBuyExpPlanCaVos = buyExpPlanFacade.listBuyExpPlanCaByInputCode(q,storageCode);
        return expBuyExpPlanCaVos;
    }

    /**
     * 查询(低于下限)待采购物品列表
     * @return
     */
    @GET
    @Path("list-low")
//    @Produces({MediaType.APPLICATION_JSON})
    public List<BuyExpPlanVo> listBuyListLow(@QueryParam("storageCode") String storageCode){
        return buyExpPlanFacade.listBuyListLow(storageCode);
    }

    /**
     * 查询(全部)待采购物品列表
     * @param storageCode
     * @return
     */
    @GET
    @Path("list-all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<BuyExpPlanVo> listBuyListAll(@QueryParam("storageCode") String storageCode) {
        return buyExpPlanFacade.listBuyListAll(storageCode);
    }
    /**
     * 生成采购单号
     * @return
     */
    @GET
    @Path("get-buy-id")
    public String getBuyId(){
        return buyExpPlanFacade.getBuyId();
    }

    /**
     * 根据buyId查询最大buyNo,因为会通过buyId,buyNo来确定对象唯一性，为防止保存失败需要此方法
     * @param buyId
     * @return
     */
    @GET
    @Path("get-buy-no")
    @Produces({MediaType.APPLICATION_JSON})
    public int getBuyNo(@QueryParam("buyId") String buyId){
        return buyExpPlanFacade.getBuyNo(buyId);
    }
    /**
     * 保存（暂存）采购单据
     * @param rows
     * @return
     */
    @POST
    @Path("save")
    public Response save(BeanChangeVo<BuyExpPlan> rows){
        List<BuyExpPlan> dicts = null;
        try{
            if(null != rows){
                dicts = buyExpPlanFacade.save(rows);
            }
            return Response.status(Response.Status.OK).entity(dicts).build();
        }catch(Exception e){
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

    /**
     * 根据暂存单号查询暂存采购列表
     * @param buyId
     * @return
     */
    @GET
    @Path("list-temp")
    @Produces({MediaType.APPLICATION_JSON})
    public List<BuyExpPlan> listTemp(@QueryParam("buyId") String buyId){
        return buyExpPlanFacade.listTemp(buyId);
    }

    /**
     * 根据上限生成采购数
     * @param inData
     * @return
     */
    @POST
    @Path("generate-num-up")
    public List<BuyExpPlan> generateNumUp(@QueryParam("storageCode") String storageCode,List<BuyExpPlan> inData){
        return buyExpPlanFacade.generateNumUp(inData,storageCode );
    }

    /**
     * 根据下限生成采购数
     * @param inData
     * @return
     */
    @POST
    @Path("generate-num-low")
    public List<BuyExpPlan> generateNumLow(@QueryParam("storageCode") String storageCode,List<BuyExpPlan> inData) {
        return buyExpPlanFacade.generateNumLow(inData,storageCode );
    }

    /**
     * 根据消耗量生成采购数
     * @param inData
     * @return
     */
    @POST
    @Path("generate-num-sale")
    public List<BuyExpPlan> generateNumSale(List<BuyExpPlan> inData) {
        return buyExpPlanFacade.generateNumSale(inData);
    }

    /**
     * 查询待确认采购单
     * @param storageCode
     * @param loginName
     * @return
     */
    @GET
    @Path("list-confirm")
    @Produces({MediaType.APPLICATION_JSON})
    public List<BuyExpPlan> listConfirm(@QueryParam("storageCode") String storageCode, @QueryParam("loginName") String loginName){
        return buyExpPlanFacade.listConfirm(storageCode, loginName);
    }

    /**
     * 查询待审核采购单
     * @param storageCode
     * @param loginName
     * @return
     */
    @GET
    @Path("list-audit")
    @Produces({MediaType.APPLICATION_JSON})
    public List<BuyExpPlan> listAudit(@QueryParam("storageCode") String storageCode, @QueryParam("loginName") String loginName) {
        return buyExpPlanFacade.listAudit(storageCode,loginName);
    }

    /**
     * 查询待执行采购单
     * @param storageCode
     * @param loginName
     * @return
     */
    @GET
    @Path("list-execute")
    @Produces({MediaType.APPLICATION_JSON})
    public List<BuyExpPlan> listExecute(@QueryParam("storageCode") String storageCode, @QueryParam("loginName") String loginName) {
        return buyExpPlanFacade.listExecute(storageCode, loginName);
    }

    /**
     * 根据采购单编号查询采购单列表详情
     * @param buyId
     * @return
     */
    @GET
    @Path("get-detail")
    public List<BuyExpPlan> getPlansById(@QueryParam("buyId") String buyId){
        return buyExpPlanFacade.getPlansById(buyId);
    }
}
