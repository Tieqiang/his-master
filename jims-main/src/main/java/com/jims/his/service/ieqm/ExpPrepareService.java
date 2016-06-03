package com.jims.his.service.ieqm;

import com.jims.his.domain.ieqm.facade.ExpPrepareDetailFacade;
import com.jims.his.domain.ieqm.vo.ExpPrepareVo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/2.
 */
@Path("exp-prepare")
@Produces("application/json")
public class ExpPrepareService {
    private ExpPrepareDetailFacade expPrepareDetailFacade;

    @Inject
    public ExpPrepareService(ExpPrepareDetailFacade expPrepareDetailFacade){
        this.expPrepareDetailFacade=expPrepareDetailFacade;
    }

    /**
     * 根据条形码获取高值耗材信息
     * */
    @GET
    @Path("find-by-bar-code")
    public Map<String,Object> findByBarCode(@QueryParam("barCode") String barCode){
        Map<String,Object> map=new HashMap<String,Object>();
        List <ExpPrepareVo> list= expPrepareDetailFacade.findByBarCode(barCode);
        if(list!=null&&list.size()>0){
            map.put("info",list);
         }else{
            if(barCode!=null&&!"".equals(barCode)){
                map.put("info","找不到对应的消耗品，barCode错误！");

            }else{
                map.put("info","barCode为空！");
            }
        }
        return map;
     }
}
