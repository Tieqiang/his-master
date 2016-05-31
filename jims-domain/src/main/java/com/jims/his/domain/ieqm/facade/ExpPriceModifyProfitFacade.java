package com.jims.his.domain.ieqm.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpPriceModify;
import com.jims.his.domain.ieqm.entity.ExpPriceModifyProfit;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.vo.ExpStorageProfileVo;



import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wbb on 2015/10/13.
 */
public class ExpPriceModifyProfitFacade extends BaseFacade {

    private EntityManager entityManager ;
    private ExpPriceModifyFacade expPriceModifyFacade ;

    @Inject
    public ExpPriceModifyProfitFacade(EntityManager entityManager, ExpPriceModifyFacade expPriceModifyFacade) {
        this.entityManager = entityManager;
        this.expPriceModifyFacade = expPriceModifyFacade;
    }


    /**
     * 产品盈亏统计
     * @param longStartTime
     * @param longStopTime
     * @return
     */
    public List<ExpPriceModifyProfit> findCountAll(String longStartTime, String longStopTime,String hospitalId) {
        String sql ="select distinct c.exp_name,storage,b.storage_name,a.exp_code,a.exp_spec,a.units,a.firm_id,quantity," +
                "original_trade_price,trade_price_profit,current_trade_price,original_retail_price,\n" +
                "current_retail_price,retail_price_profit,actual_efficient_date \n" +
                "from exp_price_modify_profit a,exp_storage_dept b,exp_dict c,exp_price_list d \n" +
                "where b.storage_code =a.storage and actual_efficient_date >= to_date('"+ longStartTime+"','YYYY-MM-DD HH24:MI:SS')  " +
                " and a.exp_code  = c.exp_code and a.exp_spec = d.exp_spec and and a.exp_code=d.exp_code and actual_efficient_date <= to_date('"+ longStopTime+"','YYYY-MM-DD HH24:MI:SS')       and " +
                "a.hospital_id = '"+hospitalId+"'";
        List<ExpPriceModifyProfit> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceModifyProfit.class);
        System.out.println("nativeQuery");
        return nativeQuery;
    }


    /**
     * 计算消耗品调价盈亏
     * @param stockageCode
     * @param expCode
     * @param expSpec
     * @param firmId
     * @param units
     * @param quantity
     */
    public ExpPriceModifyProfit calcExpPriceModifyPriceProfit(String stockageCode,String expCode ,String expSpec,String firmId ,String units ,Double quantity,String hospitalId){

        ExpPriceModify expPriceModify = expPriceModifyFacade.getExpPriceModify(firmId, expSpec, expCode,hospitalId);
        ExpPriceModifyProfit priceModifyProfit ;
        if(expPriceModify !=null){
            priceModifyProfit = this.getPriceModifyProfit(stockageCode,expCode,expSpec,firmId,expPriceModify.getActualEfficientDate(),hospitalId) ;
            if(priceModifyProfit ==null){
                priceModifyProfit = new ExpPriceModifyProfit() ;
            }
            double tradePriceProfit = (expPriceModify.getCurrentTradePrice() - expPriceModify.getOriginalTradePrice()) * quantity;//进价盈亏
            double retailPriceProfit = (expPriceModify.getCurrentRetailPrice() - expPriceModify.getOriginalRetailPrice())*quantity ;//零售价盈亏
            priceModifyProfit.setQuantity(priceModifyProfit.getQuantity()+quantity);
            priceModifyProfit.setTradePriceProfit(priceModifyProfit.getTradePriceProfit() +tradePriceProfit);
            priceModifyProfit.setRetailPriceProfit(priceModifyProfit.getRetailPriceProfit() + retailPriceProfit);
            priceModifyProfit.setStorage(stockageCode);
            priceModifyProfit.setExpSpec(expSpec);
            priceModifyProfit.setFirmId(firmId);
            priceModifyProfit.setExpCode(expCode);
            priceModifyProfit.setUnits(units);
            priceModifyProfit.setCurrentRetailPrice(expPriceModify.getCurrentRetailPrice());
            priceModifyProfit.setOriginalRetailPrice(expPriceModify.getOriginalRetailPrice());
            priceModifyProfit.setCurrentTradePrice(expPriceModify.getCurrentTradePrice());
            priceModifyProfit.setOriginalTradePrice(expPriceModify.getOriginalTradePrice());
            priceModifyProfit.setHospitalId(hospitalId);

        }else{
            priceModifyProfit = null ;
        }
        return priceModifyProfit ;
    }

    /**
     * 根据传入的调价确认对象及其stockageCode，quantity计算其盈亏
     * @param storageCode
     * @param quantity
     * @param expPriceModify
     * @return
     */
    public ExpPriceModifyProfit calcExpPriceModifyPriceProfit(String storageCode,  Double quantity, ExpPriceModify expPriceModify) {

        ExpPriceModifyProfit priceModifyProfit;
        if (expPriceModify != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String acString = sdf.format(expPriceModify.getActualEfficientDate());

            priceModifyProfit = this.getPriceModifyProfit(storageCode, expPriceModify.getExpCode(), expPriceModify.getExpSpec(), expPriceModify.getFirmId(), acString, expPriceModify.getHospitalId());
            if (priceModifyProfit == null) {
                priceModifyProfit = new ExpPriceModifyProfit();
            }
            double tradePriceProfit = (expPriceModify.getCurrentTradePrice() - expPriceModify.getOriginalTradePrice()) * quantity;//进价盈亏
            double retailPriceProfit = (expPriceModify.getCurrentRetailPrice() - expPriceModify.getOriginalRetailPrice()) * quantity;//零售价盈亏

            if(null != priceModifyProfit.getQuantity()){
                priceModifyProfit.setQuantity(priceModifyProfit.getQuantity() + quantity);
            }else{
                priceModifyProfit.setQuantity(quantity);
            }
            if(null != priceModifyProfit.getTradePriceProfit()){
                priceModifyProfit.setTradePriceProfit(priceModifyProfit.getTradePriceProfit() + tradePriceProfit);
            }else{
                priceModifyProfit.setTradePriceProfit(tradePriceProfit);
            }
            if (null != priceModifyProfit.getRetailPriceProfit()) {
                priceModifyProfit.setRetailPriceProfit(priceModifyProfit.getRetailPriceProfit() + retailPriceProfit);
            } else {
                priceModifyProfit.setRetailPriceProfit(retailPriceProfit);
            }
            String hql = "from ExpStorageDept where storageCode='"+ storageCode+"'";
            List storageList = this.entityManager.createQuery(hql).getResultList();
            if (storageList.size() > 0) {
                ExpStorageDept storageDept = (ExpStorageDept) storageList.get(0);
                priceModifyProfit.setStorageName(storageDept.getStorageName());
            } else {
                priceModifyProfit.setStorageName("");
            }
            priceModifyProfit.setExpName(expPriceModify.getExpName());
            priceModifyProfit.setStorage(storageCode);
            priceModifyProfit.setExpSpec(expPriceModify.getExpSpec());
            priceModifyProfit.setFirmId(expPriceModify.getFirmId());
            priceModifyProfit.setExpCode(expPriceModify.getExpCode());
            priceModifyProfit.setUnits(expPriceModify.getUnits());
            priceModifyProfit.setCurrentRetailPrice(expPriceModify.getCurrentRetailPrice());
            priceModifyProfit.setOriginalRetailPrice(expPriceModify.getOriginalRetailPrice());
            priceModifyProfit.setCurrentTradePrice(expPriceModify.getCurrentTradePrice());
            priceModifyProfit.setOriginalTradePrice(expPriceModify.getOriginalTradePrice());
            priceModifyProfit.setHospitalId(expPriceModify.getHospitalId());
            priceModifyProfit.setActualEfficientDate(expPriceModify.getActualEfficientDate());
        } else {
            priceModifyProfit = null;
        }
        return priceModifyProfit;
    }
    /**
     * 获取某一个库房，某一个消耗品的调价盈亏记录，实际生效时间是Date型
     * @param stockageCode 库房代码
     * @param expCode 消耗品代码
     * @param expSpec 消耗品规格
     * @param firmId//厂商
     * @param actualEfficientDate //实际生效时间
     * @return
     */
    private ExpPriceModifyProfit getPriceModifyProfit(String stockageCode, String expCode, String expSpec, String firmId, Date actualEfficientDate,String hospitalId) {
        String hql = "from ExpPriceModifyProfit pro where pro.storage = '"+stockageCode+"' and pro.expCode = '"+expCode+"' and " +
                "pro.expSpec = '"+expSpec+"' and pro.hospitalId = '"+hospitalId+"' and  pro.actualEfficientDate = "+actualEfficientDate;

        List resultList = this.entityManager.createQuery(hql).getResultList();
        if(resultList.size()>0){
            return (ExpPriceModifyProfit) resultList.get(0);
        }else{
            return null ;
        }
    }

    /**
     * 获取某一个库房，某一个消耗品的调价盈亏记录，实际生效时间是String类型
     * @param stockageCode
     * @param expCode
     * @param expSpec
     * @param firmId
     * @param actualEfficientDate
     * @param hospitalId
     * @return
     */
    private ExpPriceModifyProfit getPriceModifyProfit(String stockageCode, String expCode, String expSpec, String firmId, String actualEfficientDate, String hospitalId) {
        String hql = "from ExpPriceModifyProfit pro where pro.storage = '" + stockageCode + "' and pro.expCode = '" + expCode + "' and " +
                "pro.expSpec = '" + expSpec + "' and pro.hospitalId = '" + hospitalId + "' and  pro.actualEfficientDate = to_date('" + actualEfficientDate+"' ,'yyyy/mm/dd hh24:mi:ss')";

        List resultList = this.entityManager.createQuery(hql).getResultList();
        if (resultList.size() > 0) {
            return (ExpPriceModifyProfit) resultList.get(0);
        } else {
            return null;
        }
    }
    /**
     * 根据传入的调价确认对象计算其盈亏
     * @param inData
     * @return
     */
    public List<ExpPriceModifyProfit> calcProfit(List<ExpPriceModify> inData) {
        List<ExpPriceModifyProfit> result = new ArrayList<>();
        if (inData != null && inData.size() > 0) {
            //合计结果
            ExpPriceModifyProfit totalProfit = new ExpPriceModifyProfit();
            totalProfit.setExpName("合计");
            double totalTradePriceProfit = 0.00;
            double totalRetailPriceProfit = 0.00;

            String sql = "";
            for (ExpPriceModify modify : inData) {
                sql = "SELECT storage storage_code, sum ( quantity ) stock_quantity FROM exp_stock WHERE 1=1";
                if (null != modify.getExpCode() && modify.getExpCode().trim() != "") {
                    sql += " and exp_code ='" + modify.getExpCode() + "'";
                }
                if (null != modify.getExpSpec() && modify.getExpSpec().trim() != "") {
                    sql += " and package_spec ='" + modify.getExpSpec() + "'";
                }
                if (null != modify.getFirmId() && modify.getFirmId().trim() != "") {
                    sql += " and firm_id ='" + modify.getFirmId() + "'";
                }
                if (null != modify.getUnits() && modify.getUnits().trim() != "") {
                    sql += " and package_units='" + modify.getUnits() + "'";
                }
                if (null != modify.getHospitalId() && modify.getHospitalId().trim() != "") {
                    sql += " and hospital_id='" + modify.getHospitalId() + "'";
                }
                sql += " GROUP BY storage";
                //小计结果
                ExpPriceModifyProfit partProfit = new ExpPriceModifyProfit();
                partProfit.setExpName("小计");
                double partTradePriceProfit = 0.00;
                double partRetailPriceProfit = 0.00;

                List<ExpStorageProfileVo> profileVos = super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageProfileVo.class);
                if (null != profileVos && profileVos.size() > 0) {
                    for(ExpStorageProfileVo pv:profileVos){
                        ExpPriceModifyProfit profit = calcExpPriceModifyPriceProfit(pv.getStorageCode(),pv.getStockQuantity().doubleValue(), modify);
                        profit.setTradePriceProfit(Double.valueOf(String.format("%.2f", profit.getTradePriceProfit())));
                        result.add(profit);
                        //小计
                        partTradePriceProfit = partTradePriceProfit + profit.getTradePriceProfit().doubleValue();
                        partRetailPriceProfit = partRetailPriceProfit + profit.getRetailPriceProfit().doubleValue();
                    }
                    partProfit.setTradePriceProfit(new BigDecimal(partTradePriceProfit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    partProfit.setRetailPriceProfit(new BigDecimal(partRetailPriceProfit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    result.add(partProfit);
                    //合计
                    totalTradePriceProfit = totalTradePriceProfit + partTradePriceProfit;
                    totalRetailPriceProfit = totalRetailPriceProfit + partRetailPriceProfit;
                }
            }
            totalProfit.setTradePriceProfit(new BigDecimal(totalTradePriceProfit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            totalProfit.setRetailPriceProfit(new BigDecimal(totalRetailPriceProfit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            result.add(totalProfit);
        }
        return result;
    }
}
