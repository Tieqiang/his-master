package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpPriceModify;
import com.jims.his.domain.ieqm.facade.ExpPriceModifyFacade;
import com.jims.his.domain.ieqm.vo.ExpPriceHisVo;
import com.jims.his.domain.ieqm.vo.ExpPriceModifyProfitVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjing on 2015/10/16.
 */
@Path("exp-price-modify")
@Produces("application/json")
public class ExpPriceModifyService {
    private ExpPriceModifyFacade expPriceModifyFacade;

    @Inject
    public ExpPriceModifyService(ExpPriceModifyFacade expPriceModifyFacade) {
        this.expPriceModifyFacade = expPriceModifyFacade;
    }

    /**
     * 根据开始时间结束时间，查询调价记录结果集
     * @param startDateString
     * @param stopDateString
     * @return
     */
    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpPriceModify> findExpPriceModify(@QueryParam("startDate") String startDateString, @QueryParam("stopDate") String stopDateString){
        Date startDate = new Date();
        Date stopDate = new Date();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startDate = sdf.parse(startDateString);
            stopDate = sdf.parse(stopDateString);
        }catch(Exception e){
            e.printStackTrace();
        }
        return expPriceModifyFacade.findExpPriceModify(startDate, stopDate);
    }

    /**
     * 根据产品代码expCode查询产品历史价格结果集
     * @param expCode
     * @return
     */
    @GET
    @Path("list-history")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpPriceHisVo> findExpPriceHis(@QueryParam("expCode") String expCode) {
        return expPriceModifyFacade.findExpPriceHis(expCode);
    }

    /**
     * 保存对调价记录的增删改操作
     * @param expPriceModifyList
     * @return
     */
    @POST
    @Path("save")
    public Response saveExpPriceModify(BeanChangeVo<ExpPriceModify> expPriceModifyList){
        try {
            List<ExpPriceModify> result = expPriceModifyFacade.saveExpPriceModifyList(expPriceModifyList);
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            } else {
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    /**
     * 调价记录确认的保存（同时保存调价盈亏，价格记录）
     * @param expPriceModifyProfitVo
     * @return
     */
    @POST
    @Path("save-modify-confirm")
    public Response saveExpPriceModifyConfirm(ExpPriceModifyProfitVo expPriceModifyProfitVo){
        try {
            List<ExpPriceModify> result = expPriceModifyFacade.saveExpPriceModifyConfirmList(expPriceModifyProfitVo);
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            } else {
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

}
