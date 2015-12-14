package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpDict;
import com.jims.his.domain.ieqm.entity.ExpNameDict;
import com.jims.his.domain.ieqm.facade.ExpDictFacade;
import com.jims.his.domain.ieqm.facade.ExpNameDictFacade;
import com.jims.his.domain.ieqm.vo.ExpDictNameVO;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2015/9/23
 */
@Path("exp-name-dict")
@Produces("application/json")
public class ExpNameDictService {

    private ExpNameDictFacade expNameDictFacade ;
    private ExpDictFacade expDictFacade;
    @Inject
    public ExpNameDictService(ExpNameDictFacade expNameDictFacade, ExpDictFacade expDictFacade) {
        this.expNameDictFacade = expNameDictFacade;
        this.expDictFacade = expDictFacade;
    }

    /**
     * 查询产品及价格联合显示的自定义对象列表
     * @return
     */
    @GET
    @Path("listExpNameCa")
    public List<ExpNameCaVo> listExpNameCa(){
        List<ExpNameCaVo> resultList = expNameDictFacade.listExpNameCa();
        return resultList;
    }

    /**
     * 根据expCode查询对象
     * @param expCode
     * @return
     */
    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpNameDict> listExpNameDict(@QueryParam("expCode") String expCode){
        return expNameDictFacade.listExpNameDict(expCode);
    }

    /**
     * 保存增删改操作的expNameDict和expDict对象集合
     * @param
     * @return
     */
    @POST
    @Path("save")
    public Response saveExpDictNameVo(ExpDictNameVO beanChangeVo){
        try{
            List<ExpNameDict> newUpdateDict = new ArrayList<>();
            if(beanChangeVo != null) {
                newUpdateDict = expNameDictFacade.saveChanges(beanChangeVo);
            }
            return Response.status(Response.Status.OK).entity(newUpdateDict).build();
        }catch(Exception e){
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
     * 根据输入的拼音码，获取相应的字典列表
     *
     * @param q 拼音码
     * @return
     */
    @GET
    @Path("list-exp-name-by-input")
    public List<ExpNameCaVo> listExpNameCa(@QueryParam("q") String q) {
        List<ExpNameCaVo> expNameCaVos = expNameDictFacade.listExpNameCaByInputCode(q);
        return expNameCaVos;
    }

    /**
     * 自动生成产品代码时，查询当前前缀所匹配的最大序号
     * @param len
     * @param header
     * @return
     */
    @GET
    @Path("list-max-exp-code")
    public String getMaxExpCode(@QueryParam("length")int len, @QueryParam("header") String header){
        try{
            int max = expNameDictFacade.findMaxExpCode(len, header);
            return String.valueOf(max);
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            } else {
                errorException.setErrorMessage("操作失败！");
            }
            return errorException.getErrorMessage();
        }
    }

}
