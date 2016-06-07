package com.jims.his.service.ieqm;

import com.google.inject.Inject;
import com.jims.his.common.util.StringUtils;
import com.jims.his.domain.ieqm.entity.*;
import com.jims.his.domain.ieqm.facade.*;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;
import com.jims.his.domain.ieqm.vo.ExpPrepareVo;
import com.jims.his.domain.ieqm.vo.ExpStockRecord;
import org.eclipse.jetty.util.StringUtil;
import com.jims.his.domain.ieqm.facade.ExpPrepareDetailFacade;
import com.jims.his.domain.ieqm.vo.ExpPrepareVo;

//import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
@Path("exp-prepare")
@Produces("application/json")
public class ExpPrepareService {

     private ExpPrepareMasterFacade expPrepareMasterFacade;

    private ExpNameDictFacade expNameDictFacade;

    private ExpPrepareDetailFacade expPrepareDetailFacade;

    private ExpSubStorageDictFacade expSubStorageDictFacade;

    private ExpPriceListFacade expPriceListFacade;

    @javax.inject.Inject
    public ExpPrepareService(ExpPrepareDetailFacade expPrepareDetailFacade, ExpPrepareMasterFacade expPrepareMasterFacade, ExpNameDictFacade expNameDictFacade, ExpSubStorageDictFacade expSubStorageDictFacade,ExpPriceListFacade expPriceListFacade) {
        this.expPrepareDetailFacade = expPrepareDetailFacade;
        this.expPrepareMasterFacade = expPrepareMasterFacade;
        this.expNameDictFacade = expNameDictFacade;
        this.expSubStorageDictFacade = expSubStorageDictFacade;
        this.expPriceListFacade=expPriceListFacade;
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
     *
     * @param q
     * @param supplerId
     * @return
     */
    @GET
    @Path("find-by-firm-name")
    public List<ExpNameCaVo> findByFirmName(@QueryParam("q") String q, @QueryParam("supplierId") String supplerId) {
        return expNameDictFacade.listExpNameBySupplier(q, supplerId);
    }

    /**
     * 生成备货明细
     *
     * @param supplierId
     * @param expCodes
     * @param operator
     * @return
     */
    @POST
    @Path("make-data")
    public List<ExpPrepareDetail> makeData(@QueryParam("supplierId") String supplierId, @QueryParam("expCodes") String expCodes, @QueryParam("operator") String operator, @QueryParam("amounts") String amounts, @QueryParam("prices") String priceStr,@QueryParam("packageSpecs") String packageSpecs) {
        List<ExpPrepareDetail> list = new ArrayList<ExpPrepareDetail>();
        if (expCodes != null && !"".equals(expCodes)) {
            String[] amountArr = amounts.split(",");
            String[] expCodeArr = expCodes.split(",");
            String[] priceArr = priceStr.split(",");
            String[] packageSpecArr=packageSpecs.split(",");
            for (int i = 0; i < expCodeArr.length; i++) {
//                ExpDict expDict = expDictFacade.findByCode(expCodeArr[i],packageSpecArr[i]);
                ExpPriceList expPriceList=this.expPriceListFacade.findByCodeAndPackageSpec(expCodeArr[i],packageSpecArr[i]);
                list = expPrepareMasterFacade.save(expPriceList.getId(), supplierId, amountArr[i], operator, Double.parseDouble(priceArr[i]), list,expPriceList.getFirmId());
            }
        }
        return list;
    }

    /**
     * 根据条形码获取高值耗材信息
     */
    @POST
    @Path("find-by-bar-code")
    public Map<String, Object> findByBarCode(@QueryParam("barCode") String barCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ExpPrepareVo> list = expPrepareDetailFacade.findByBarCode(barCode);
        if (list != null && list.size() > 0) {
            map.put("info", list);
        } else {
            if (barCode != null && !"".equals(barCode)) {
                map.put("info", "找不到对应的消耗品，barCode错误！");
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

            } else {
                map.put("info", "barCode为空！");
            }else{
                map.put("info","barCode为空！");
            }
        }
        return map;
    }
    /**
     * 病人计费接口
     *
     * @param barCode
     * @param storageCode
     * @param operator
     * @param patientId
     * @return
     */
    @GET
    @Path("prepare-fee")
    public Map<String,Object> prepareFee(@QueryParam("barCode") String barCode, @QueryParam("storageCode") String storageCode, @QueryParam("operator") String operator, @QueryParam("patientId") String patientId,@QueryParam("hospitalId") String hospitalId) {
        Map<String,Object> returnVal=new HashMap<String,Object>();
        /**
         * 入库操作
         * 出库操作
         * 回写数据
         */
        if (StringUtil.isNotBlank(barCode) && StringUtil.isNotBlank(operator) && StringUtil.isNotBlank(storageCode) && StringUtil.isNotBlank(patientId)) {
            ExpSubStorageDict expSubStorageDict = this.expSubStorageDictFacade.findByStorageCode(storageCode);
            String documentNo = "";//入库单据号
            String importNoPrefix = expSubStorageDict.getImportNoPrefix();//前缀
            if (importNoPrefix.length() <= 4) {
                documentNo = importNoPrefix + "000000".substring((expSubStorageDict.getImportNoAva() + "").length()) + expSubStorageDict.getImportNoAva();
            } else if (importNoPrefix.length() == 5) {
                documentNo = expSubStorageDict.getImportNoPrefix() + "00000".substring((expSubStorageDict.getImportNoAva() + "").length()) + expSubStorageDict.getImportNoAva();
            } else if (importNoPrefix.length() == 6) {
                documentNo = expSubStorageDict.getImportNoPrefix() + "0000".substring((expSubStorageDict.getImportNoAva() + "").length()) + expSubStorageDict.getImportNoAva();
            }
            /**
             * 跟新 exp_sub_storage_dict.import_no_ava
             */

            String documentNo2 = "";//出库单据号
            String suffer2 = expSubStorageDict.getExportNoPrefix();//前缀
            if (suffer2.length() <= 4) {
                documentNo2 = expSubStorageDict.getExportNoPrefix() + "000000".substring((expSubStorageDict.getExportNoAva() + "").length()) + expSubStorageDict.getExportNoAva();
            } else if (suffer2.length() == 5) {
                documentNo2 = expSubStorageDict.getExportNoPrefix() + "00000".substring((expSubStorageDict.getExportNoAva() + "").length()) + expSubStorageDict.getExportNoAva();
            } else if (suffer2.length() == 6) {
                documentNo2 = expSubStorageDict.getExportNoPrefix() + "0000".substring((expSubStorageDict.getExportNoAva() + "").length()) + expSubStorageDict.getExportNoAva();
            }
            String masterId=this.expPrepareDetailFacade.findByExpBarCode(barCode);
            ExpPrepareMaster expPrepareMaster=this.expPrepareMasterFacade.findById(masterId);
            ExpPrepareVo expPrepareVo=this.expPrepareMasterFacade.prepareFee(expPrepareMaster,documentNo,documentNo2,storageCode,operator,patientId,hospitalId,barCode);
            returnVal.put("info",expPrepareVo);
        }else{
            returnVal.put("info","参数错误！");
        }
         return returnVal;
    }

    /**
     * 回滚操作
     * @return
     */
    @GET
    @Path("roll-back-prepare")
    public Map<String,Object> rollBackPrepare(@QueryParam("inDocumentNo") String inDocumentNo,@QueryParam("outDocumentNo") String outDocumentNo,@QueryParam("barCode") String barCode){
        Map<String,Object> retVal=this.expPrepareMasterFacade.rollBack(inDocumentNo,outDocumentNo,barCode);
        return retVal;
    }


    }
}
