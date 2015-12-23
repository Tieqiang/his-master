package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpPriceList;
import com.jims.his.domain.ieqm.entity.ExpPriceModify;
import com.jims.his.domain.ieqm.entity.ExpPriceModifyProfit;
import com.jims.his.domain.ieqm.vo.ExpPriceHisVo;
import com.jims.his.domain.ieqm.vo.ExpPriceModifyProfitVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangjing on 2015/10/16.
 */
public class ExpPriceModifyFacade extends BaseFacade {
    private EntityManager entityManager;
    private ExpPriceListFacade expPriceListFacade;
    @Inject
    public ExpPriceModifyFacade(EntityManager entityManager, ExpPriceListFacade expPriceListFacade) {
        this.entityManager = entityManager;
        this.expPriceListFacade = expPriceListFacade;
    }

    /**
     * 根据开始结束时间查询调价记录结果集
     * @param startDate
     * @param stopDate
     * @return
     */
    public List<ExpPriceModify> findExpPriceModify(Date startDate, Date stopDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = formatter.format(startDate.getTime());
        String stopTime = formatter.format(stopDate.getTime());

        String sql = "SELECT EXP_PRICE_MODIFY.ID,EXP_DICT.EXP_NAME,   \n" +
                "         EXP_PRICE_MODIFY.MIN_SPEC,   \n" +
                "         EXP_PRICE_MODIFY.EXP_SPEC,   \n" +
                "         EXP_PRICE_MODIFY.UNITS,   \n" +
                "         EXP_PRICE_MODIFY.FIRM_ID,   \n" +
                "         EXP_PRICE_MODIFY.ORIGINAL_TRADE_PRICE,   \n" +
                "         EXP_PRICE_MODIFY.ORIGINAL_RETAIL_PRICE,   \n" +
                "         EXP_PRICE_MODIFY.CURRENT_TRADE_PRICE,   \n" +
                "         EXP_PRICE_MODIFY.CURRENT_RETAIL_PRICE,   \n" +
                "         EXP_PRICE_MODIFY.NOTICE_EFFICIENT_DATE,   \n" +
                "         EXP_PRICE_MODIFY.ACTUAL_EFFICIENT_DATE,   \n" +
                "         EXP_PRICE_MODIFY.MODIFY_CREDENTIAL,   \n" +
                "         EXP_PRICE_MODIFY.EXP_CODE,   \n" +
                "         EXP_PRICE_MODIFY.MIN_UNITS,\n" +
                "         EXP_PRICE_MODIFY.Modify_Man,\n" +
                "         EXP_PRICE_MODIFY.hospital_id,\n" +
                "\t\t\tEXP_PRICE_MODIFY.material_code\n" +
                "    FROM EXP_DICT,   \n" +
                "         EXP_PRICE_MODIFY  \n" +
                "   WHERE  EXP_DICT.EXP_CODE = EXP_PRICE_MODIFY.EXP_CODE  and  \n" +
                "          EXP_DICT.EXP_SPEC = EXP_PRICE_MODIFY.MIN_SPEC  and  \n" +
                "          EXP_PRICE_MODIFY.NOTICE_EFFICIENT_DATE >= to_date('" + startTime + "','YYYY-MM-DD HH24:MI:SS') AND  \n" +
                "          EXP_PRICE_MODIFY.NOTICE_EFFICIENT_DATE <= to_date('" + stopTime + "','YYYY-MM-DD HH24:MI:SS') AND  \n" +
                "          EXP_PRICE_MODIFY.ACTUAL_EFFICIENT_DATE is null  ";
        List<ExpPriceModify> modifyList = super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceModify.class);
        return modifyList;
    }

    /**
     * 保存对调价记录的增删改操作
     * @param changeData
     * @return
     */
    @Transactional
    public List<ExpPriceModify> saveExpPriceModifyList(BeanChangeVo<ExpPriceModify> changeData) {
        List<ExpPriceModify> inserted = changeData.getInserted();
        List<ExpPriceModify> updated = changeData.getUpdated();
        List<ExpPriceModify> deleted = changeData.getDeleted();
        List<ExpPriceModify> newUpdateDict = new ArrayList<>();
        if (inserted.size() > 0) {
            for (ExpPriceModify dict : inserted) {
                ExpPriceModify ins = this.merge(dict);
                newUpdateDict.add(ins);
            }
        }

        if (updated.size() > 0) {
            for (ExpPriceModify dict : updated) {
                ExpPriceModify up = this.merge(dict);
                newUpdateDict.add(up);
            }
        }

        if (deleted.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (ExpPriceModify dict : deleted) {
                ids.add(dict.getId());
            }
            this.removeByStringIds(ExpPriceModify.class, ids);
            newUpdateDict.addAll(deleted);
        }
        return newUpdateDict;
    }

    /**
     * 根据产品代码expCode查询产品历史价格结果集
     *
     * @param expCode
     * @return
     */
    public List<ExpPriceHisVo> findExpPriceHis(String expCode) {
        String sql = "SELECT DISTINCT exp_price_list.exp_code,   \n" +
                "         Nvl(exp_name,exp_price_list.exp_code) exp_name,   \n" +
                "         exp_spec,   \n" +
                "         firm_id,\n" +
                "      units,   \n" +
                "         trade_price,   \n" +
                "         retail_price,   \n" +
                "         start_date,\n" +
                "      stop_date,\n" +
                "      amount_per_package,\n" +
                "      min_spec,\n" +
                "      min_units,\n" +
                "      memos, \n" +
                //"      Sign(stop_date - sysdate) oldornew  ,\n" +
                "      material_code \n" +
                "    FROM exp_price_list,exp_name_dict   \n" +
                "   WHERE exp_price_list.exp_code = exp_name_dict.exp_code AND\n" +
                "      std_indicator = 1 AND \n" +
                "      exp_price_list.exp_code ='" + expCode + "'";
        List<ExpPriceHisVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceHisVo.class);
        return nativeQuery;
    }

    /**
     * 获取某一个消耗品当前的调价信息
     *
     * @param firmId 厂商
     * @param expSpec 消耗品规格
     * @param expCode 消耗品代码
     * @return
     */
    public ExpPriceModify getExpPriceModify(String firmId, String expSpec, String expCode,String hospitalId ) {
        String hql = "from ExpPriceModify as mo where sysdate between mo.noticeEfficientDate and mo.actualEfficientDate and " +
                "mo.firmId = '" + firmId + "' and mo.expSpec = '" + expSpec + "' and expCode = '" + expCode + "' and mo.hospitalId = '"+hospitalId+"'";


        List resultList = this.entityManager.createQuery(hql).getResultList();
        if(resultList.size()>0){
            return (ExpPriceModify) resultList.get(0);
        }else{
            return null ;
        }

    }

    /**
     * 调价记录确认的保存（同时保存调价盈亏，价格记录）
     * @param changeData
     * @return
     */
    @Transactional
    public List<ExpPriceModify> saveExpPriceModifyConfirmList(ExpPriceModifyProfitVo changeData){
        List<ExpPriceModify> newUpdateDict = new ArrayList<>();

        List<ExpPriceModifyProfit> inserted = changeData.getExpPriceModifyProfitChange().getInserted();
        List<ExpPriceModify> updated = changeData.getExpPriceModifyChange().getUpdated();
        List<ExpPriceModify> deleted = changeData.getExpPriceModifyChange().getDeleted();
        //保存盈亏记录
        if (inserted.size() > 0) {
            for (ExpPriceModifyProfit dict : inserted) {
                //保存盈亏记录的时候去除页面里面显示的小计合计行
                if(null != dict.getStorage() && null != dict.getHospitalId()){
                    if(dict.getExpName().trim() != "小计" && dict.getExpName().trim() != "合计"){
                        ExpPriceModifyProfit ins = this.merge(dict);
                    }
                }

            }
        }
        //更新调价确认
        if (updated.size() > 0) {
            for (ExpPriceModify dict : updated) {
                ExpPriceModify up = this.merge(dict);
                newUpdateDict.add(up);
                //查询到当前的expPriceList，更新它的stopDate
                ExpPriceList priceOld = expPriceListFacade.getExpPriceList(dict.getExpCode(), dict.getExpSpec(), dict.getFirmId(), dict.getUnits());
                priceOld.setStopDate(dict.getActualEfficientDate());
                this.merge(priceOld);
                //向expPriceList表里面增加新的价格信息
                ExpPriceList priceNew = new ExpPriceList();
                priceNew.setExpCode(priceOld.getExpCode());
                priceNew.setExpSpec(priceOld.getExpSpec());
                priceNew.setFirmId(priceOld.getFirmId());
                priceNew.setUnits(priceOld.getUnits());
                priceNew.setTradePrice(up.getCurrentTradePrice());
                priceNew.setRetailPrice(up.getCurrentRetailPrice());
                priceNew.setAmountPerPackage(priceOld.getAmountPerPackage());
                priceNew.setMinUnits(priceOld.getMinUnits());
                priceNew.setMinSpec(priceOld.getMinSpec());
                priceNew.setClassOnInpRcpt(priceOld.getClassOnInpRcpt());
                priceNew.setClassOnOutpRcpt(priceOld.getClassOnOutpRcpt());
                priceNew.setClassOnReckoning(priceOld.getClassOnReckoning());
                priceNew.setSubjCode(priceOld.getSubjCode());
                priceNew.setClassOnMr(priceOld.getClassOnMr());
                priceNew.setStartDate(up.getActualEfficientDate());
                priceNew.setStopDate(null);
                priceNew.setMemos(priceOld.getMemos());
                priceNew.setMaxRetailPrice(priceOld.getMaxRetailPrice());
                priceNew.setMaterialCode(priceOld.getMaterialCode());
                priceNew.setOperator(dict.getModifyMan());
                priceNew.setPermitNo(priceOld.getPermitNo());
                priceNew.setPermitDate(priceOld.getPermitDate());
                priceNew.setRegisterNo(priceOld.getRegisterNo());
                priceNew.setRegisterDate(priceOld.getRegisterDate());
                priceNew.setFdaOrCeNo(priceOld.getFdaOrCeNo());
                priceNew.setFdaOrCeDate(priceOld.getFdaOrCeDate());
                priceNew.setOtherNo(priceOld.getOtherNo());
                priceNew.setOtherDate(priceOld.getOtherDate());
                priceNew.setHospitalId(up.getHospitalId());
                this.merge(priceNew);
            }
        }
        //删除调价确认
        if (deleted.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (ExpPriceModify dict : deleted) {
                ids.add(dict.getId());
            }
            this.removeByStringIds(ExpPriceModify.class, ids);
            newUpdateDict.addAll(deleted);
        }

        return newUpdateDict;
    }

}
