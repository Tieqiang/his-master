package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpInventoryCheck;
import com.jims.his.domain.ieqm.facade.ExpInventoryCheckFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangjing on 2015/11/3.
 */
@Path("exp-inventory-check")
@Produces("application/json")
public class ExpInventoryCheckService {
    private ExpInventoryCheckFacade expInventoryCheckFacade;

    @Inject
    public ExpInventoryCheckService(ExpInventoryCheckFacade expInventoryCheckFacade) {
        this.expInventoryCheckFacade = expInventoryCheckFacade;
    }

    /**
     * 生成盘点记录前首先查询盘点记录表看是否有这段时间的盘点记录
     * @param storageCode
     * @param subStorage
     * @param checkMonth
     * @param hospitalId
     * @return
     */
    @GET
    @Path("get-inventory-num")
    public int getInventoryNum(@QueryParam("hospitalId") String hospitalId, @QueryParam("storageCode") String storageCode, @QueryParam("subStorage") String subStorage, @QueryParam("checkMonth") String checkMonth) {
        return expInventoryCheckFacade.getInventoryNum(hospitalId, storageCode, subStorage, checkMonth);
    }
    /**
     * 根据查询条件生成待盘点产品数据集合
     * @param storageCode
     * @param hospitalId
     * @param subStorage
     * @param checkMonth
     * @return
     */
    @GET
    @Path("get-inventory")
    public List<ExpInventoryCheck> getInventory(@QueryParam("type") String type, @QueryParam("storageCode") String storageCode, @QueryParam("hospitalId") String hospitalId, @QueryParam("subStorage") String subStorage, @QueryParam("checkMonth") String checkMonth){
        return expInventoryCheckFacade.getInventory(type,storageCode, hospitalId, subStorage, checkMonth);
    }

    @GET
    @Path("inventory-list-by-time")
    public List<ExpInventoryCheck> inventoryListByTime(@QueryParam("hospitalId") String hospitalId,@QueryParam("storageCode") String storageCode, @QueryParam("checkMonth") String checkMonth){
        return expInventoryCheckFacade.inventoryListByTime(hospitalId,storageCode, checkMonth);
    }

    /**
     * 保存盘点信息
     * @param rows
     * @return
     */
    @POST
    @Path("save")
    public Response save(List<ExpInventoryCheck> rows){
        List<ExpInventoryCheck> dicts = null;
        try {
            if (null != rows) {
                dicts = expInventoryCheckFacade.save(rows);
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
}
