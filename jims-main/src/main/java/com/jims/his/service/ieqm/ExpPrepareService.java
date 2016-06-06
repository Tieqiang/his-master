package com.jims.his.service.ieqm;

import com.google.inject.Inject;
import com.jims.his.domain.ieqm.entity.ExpDict;
import com.jims.his.domain.ieqm.entity.ExpPrepareDetail;
import com.jims.his.domain.ieqm.entity.ExpPrepareMaster;
import com.jims.his.domain.ieqm.facade.ExpDictFacade;
import com.jims.his.domain.ieqm.facade.ExpNameDictFacade;
import com.jims.his.domain.ieqm.facade.ExpPrepareDetailFacade;
import com.jims.his.domain.ieqm.facade.ExpPrepareMasterFacade;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;
import com.jims.his.domain.ieqm.vo.ExpPrepareVo;
import com.jims.his.domain.ieqm.vo.ExpStockRecord;

import javax.ws.rs.*;
import java.util.ArrayList;
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
    private ExpPrepareMasterFacade expPrepareMasterFacade;

    private ExpNameDictFacade expNameDictFacade;

    private ExpDictFacade expDictFacade;

    @Inject
    public ExpPrepareService(ExpPrepareDetailFacade expPrepareDetailFacade, ExpPrepareMasterFacade expPrepareMasterFacade,ExpNameDictFacade expNameDictFacade,ExpDictFacade expDictFacade) {
        this.expPrepareDetailFacade = expPrepareDetailFacade;
        this.expPrepareMasterFacade = expPrepareMasterFacade;
        this.expNameDictFacade=expNameDictFacade;
        this.expDictFacade=expDictFacade;
    }

    /**
     * chenxy
     * 根据expCode 查询相关规格的信息
     */
    @Path("exp-spec")
    @GET
    public List<ExpStockRecord> findExpSpec(@QueryParam("expCode") String expCode, @QueryParam("hospitalId") String hospitalId) {
        String sql = " SELECT distinct b.EXP_NAME,\n" +
                "        b.EXP_FORM,\n" +
                "        c.EXP_CODE,\n" +
                "        c.EXP_SPEC,\n" +
                "        c.units,\n" +
                "        c.min_spec,\n" +
                "        c.min_UNITS,\n" +
                "        c.FIRM_ID,\n" +
                "        c.TRADE_PRICE,\n" +
                "        c.TRADE_PRICE purchase_Price,\n" +
                "        c.retail_price,\n" +
                "        c.material_code,\n" +
                "        c.Register_no,\n" +
                "        c.Permit_no,\n" +
                "        c.amount_per_package\n" +
                "          \n" +
                "    FROM\n" +
                "        exp_dict b,\n" +
                "        exp_price_list c\n" +
                "       \n" +
                "    WHERE\n" +
                "        b.EXP_CODE = c.EXP_CODE     \n" +
                "        AND b.exp_spec = c.min_spec     \n" +
                "        AND c.start_date <= sysdate     \n" +
                "       \n" +
                "        AND (\n" +
                "            c.stop_date IS NULL \n" +
                "            OR c.stop_date > sysdate\n" +
                "        )     \n" +
                "        AND b.EXP_CODE = '" + expCode + "'     \n" +
                "   \n" +
                "        and c.hospital_id = '" + hospitalId + "'   ";
        return expPrepareMasterFacade.createNativeQuery(sql, new ArrayList<Object>(), ExpStockRecord.class);
    }


    /**
     * 根据厂商和inputCode 查询相应的exp
     * @param q
     * @param supplerId
     * @return
     */
    @GET
    @Path("find-by-firm-name")
    public  List<ExpNameCaVo> findByFirmName(@QueryParam("q") String q,@QueryParam("supplierId") String supplerId){
        return expNameDictFacade.listExpNameBySupplier(q,supplerId);
    }

    /**
     *生成备货明细
     * @param supplierId
     * @param expCodes
     * @param operator
     * @return
     */
    @POST
    @Path("make-data")
    public List<ExpPrepareDetail> makeData(@QueryParam("supplierId") String supplierId,@QueryParam("expCodes") String expCodes,@QueryParam("operator") String operator,@QueryParam("amounts") String amounts,@QueryParam("prices") String priceStr){
        List<ExpPrepareDetail> list=new ArrayList<ExpPrepareDetail>();
        if(expCodes!=null&&!"".equals(expCodes)){
            String[] amountArr=amounts.split(",");
            String[] expCodeArr=expCodes.split(",");
            String[] priceArr=priceStr.split(",");
            for(int i=0;i<expCodeArr.length;i++){
               ExpDict expDict= expDictFacade.findByCode(expCodeArr[i]);
               list=expPrepareMasterFacade.save(expDict.getId(),supplierId,amountArr[i],operator,Double.parseDouble(priceArr[i]),list);
            }
         }
        return list;
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
            if(list.get(0).getUseFlag().equals("未使用")){
                map.put("info",list.get(0));
            }else{
                map.put("info","物品已使用，此条码无效");
            }
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
