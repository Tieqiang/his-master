package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
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
    public List<ExpPriceListVo> findExpPriceList(@QueryParam("expCode") String expCode, @QueryParam("hospitalId") String hospitalId) {
        List<ExpPriceListVo> result = expPriceListFacade.findExpPriceList(expCode, hospitalId);
        if(result != null && result.size() > 0){
            Iterator ite = result.iterator();
            while(ite.hasNext()){
                ExpPriceListVo vo = (ExpPriceListVo)ite.next();
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
     * @param expPriceListVo
     * @return
     */
    @POST
    @Path("save")
    public Response saveExpPriceList(List<ExpPriceListVo> expPriceListVo){
        List<ExpPriceList> dicts = null;

        try {
            if(expPriceListVo != null && expPriceListVo.size() > 0){
                List<ExpPriceList> addList = new ArrayList<ExpPriceList>();
                Iterator ite = expPriceListVo.iterator();
                ExpPriceList price;
                while(ite.hasNext()){
                    ExpPriceListVo vo = (ExpPriceListVo)ite.next();
                    price = new ExpPriceList();
                    price.setExpCode(vo.getExpCode());
                    price.setExpSpec(vo.getExpSpec());
                    price.setFirmId(vo.getFirmId());
                    price.setUnits(vo.getUnits());
                    price.setTradePrice(vo.getTradePrice());
                    price.setRetailPrice(vo.getRetailPrice());
                    price.setAmountPerPackage(vo.getAmountPerPackage());
                    price.setMinSpec(vo.getMinSpec());
                    price.setMinUnits(vo.getMinUnits());
                    price.setClassOnInpRcpt(vo.getClassOnInpRcpt());
                    price.setClassOnOutpRcpt(vo.getClassOnOutpRcpt());
                    price.setClassOnReckoning(vo.getClassOnReckoning());
                    price.setSubjCode(vo.getSubjCode());
                    price.setClassOnMr(vo.getClassOnMr());
                    price.setStartDate(new Date());
                    price.setMemos(vo.getMemos());
                    price.setMaxRetailPrice(vo.getMaxRetailPrice());
                    price.setMaterialCode(vo.getMaterialCode());
                    price.setOperator(vo.getOperator());
                    price.setPermitNo(vo.getPermitNo());
                    price.setPermitDate(new Date());
                    price.setRegisterNo(vo.getRegisterNo());
                    price.setRegisterDate(new Date());
                    price.setFdaOrCeNo(vo.getFdaOrCeNo());
                    price.setFdaOrCeDate(new Date());
                    price.setOtherNo(vo.getOtherNo());
                    price.setOtherDate(new Date());
                    price.setHospitalId(vo.getHospitalId());
                    addList.add(price);
                }
                dicts = expPriceListFacade.saveExpPriceList(addList);
            }
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
}
