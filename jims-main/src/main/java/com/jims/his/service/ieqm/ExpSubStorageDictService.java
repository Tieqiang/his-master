package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpSubStorageDict;
import com.jims.his.domain.ieqm.facade.ExpStockFacade;
import com.jims.his.domain.ieqm.facade.ExpSubStorageDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by wangjing on 2015/10/14.
 */
@Path("exp-sub-storage-dict")
@Produces("application/json")
public class ExpSubStorageDictService {
    private ExpSubStorageDictFacade expSubStorageDictFacade;
    private ExpStockFacade expStockFacade;

    @Inject
    public ExpSubStorageDictService(ExpSubStorageDictFacade expSubStorageDictFacade, ExpStockFacade expStockFacade) {
        this.expSubStorageDictFacade = expSubStorageDictFacade;
        this.expStockFacade = expStockFacade;
    }

    /**
     * 根据子库房的subStorage查询子库房，like%名称%
     * @param subStorage
     * @return
     */
    @GET
    @Path("list")
    public List<ExpSubStorageDict> listExpSubStorageDict(@QueryParam("subStorage") String subStorage) {
        return expSubStorageDictFacade.listExpSubStorageDict(subStorage);
    }

    /**
     * 保存对子库房数据列表的增删改操作
     * @param expSubStorageDictChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response mergerExpSubStorageDict(BeanChangeVo<ExpSubStorageDict> expSubStorageDictChangeVo) {
        try {
            expSubStorageDictFacade.mergeExpSubStorageDict(expSubStorageDictChangeVo);
            return Response.status(Response.Status.OK).entity(expSubStorageDictChangeVo).build();
        } catch (Exception ex) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(ex);
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
     * 查询某一个医院的某一个库房的子库房
     * @param storageCode
     * @param hospitalId
     * @return
     */
    @Path("list-by-storage")
    @GET
    public List<ExpSubStorageDict> listExpSubStorageDictByStorageCode(@QueryParam("storageCode") String storageCode,@QueryParam("hospitalId")String hospitalId){
        return expSubStorageDictFacade.listExpSubStorageDict(storageCode, hospitalId) ;
    }
}
