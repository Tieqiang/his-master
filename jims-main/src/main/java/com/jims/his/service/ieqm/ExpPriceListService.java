package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpPriceList;
import com.jims.his.domain.ieqm.facade.ExpPriceListFacade;
import com.jims.his.domain.ieqm.vo.ExpPriceListVo;
import org.hibernate.loader.custom.Return;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangjing on 2015/10/10.
 */
@Path("exp-price-list")
@Produces("application/json")
public class ExpPriceListService {
    private ExpPriceListFacade expPriceListFacade;

    @Inject
    public ExpPriceListService(ExpPriceListFacade expPriceListFacade) {
        this.expPriceListFacade = expPriceListFacade;
    }

    /**
     * 根据expCode查询产品价格结果集
     * @param expCode
     * @return
     */
    @GET
    @Path("get-exp-price-list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpPriceList> getExpPriceList(@QueryParam("expCode") String expCode) {
        return expPriceListFacade.listExpPriceList(expCode);
    }

    /**
     * 对EXP_PRICE_LIST和EXP_DICT联合查询，取出产品价格自定义对象ExpPriceListVo结果集
     * @param expCode
     * @return
     */
    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpPriceList> findExpPriceList(@QueryParam("expCode") String expCode, @QueryParam("hospitalId") String hospitalId) {
        List<ExpPriceList> result = expPriceListFacade.findExpPriceList(expCode, hospitalId);
        if(result != null && result.size() > 0){
            Iterator ite = result.iterator();
            while(ite.hasNext()){
                ExpPriceList vo = (ExpPriceList)ite.next();
                vo.setPriceRatio(String.valueOf(vo.getRetailPrice() / vo.getTradePrice()));
                if(vo.getStopDate()!=null && vo.getStopDate().compareTo(new Date())<0){
                    vo.setStopPrice("on");
                }else{
                    vo.setStopPrice("off");
                }
            }
        }
        return result;
    }

    /**
     * 保存产品价格
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("save")
    public Response saveExpPriceList(BeanChangeVo<ExpPriceListVo> beanChangeVo){

        try {
            List<ExpPriceList> dicts = expPriceListFacade.saveExpPriceList(beanChangeVo);
            return Response.status(Response.Status.OK).entity(dicts).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            } else {
                errorException.setErrorMessage("提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    /**
     * 对exp_dict , exp_price_list ,exp_stock联合查询，取出产品价格自定义对象ExpPriceListVo结果集
     * @param q
     * @param storageCode
     * @return
     */
    @GET
    @Path("find-exp-list")
    public Response finExpListByInput(@QueryParam("q") String q,@QueryParam("storageCode") String storageCode){
        try {
            List<ExpPriceListVo> expPriceListVos = expPriceListFacade.findExpList(q, storageCode);
            return Response.status(Response.Status.OK).entity(expPriceListVos).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            } else {
                errorException.setErrorMessage("操作失败！");
            }
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    /**
     * 停价
     * @param price
     * @return
     */
    @POST
    @Path("stop-price")
    public Response stopPrice(ExpPriceList price) {
        try {
            ExpPriceList dict = expPriceListFacade.stopPrice(price);
            return Response.status(Response.Status.OK).entity(dict).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if (errorException.getErrorMessage().toString().indexOf("最大值") != -1) {
                errorException.setErrorMessage("输入数据超过长度！");
            } else if (errorException.getErrorMessage().toString().indexOf("唯一") != -1) {
                errorException.setErrorMessage("数据已存在，提交失败！");
            } else {
                errorException.setErrorMessage("提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }
}
