package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.BuyExpPlan;
import com.jims.his.domain.ieqm.entity.ExpClassDict;
import com.jims.his.domain.ieqm.vo.BuyExpPlanVo;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangjing on 2015/10/23.
 */
public class BuyExpPlanFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public BuyExpPlanFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 查询当前仓库当前操作人的暂存采购单号
     * @param storageCode
     * @param expNo
     * @return
     */
    public List<ExpClassDict> listBuyId(String storageCode, String expNo){
        List<ExpClassDict> idList = new ArrayList<>();
        String sql = "SELECT distinct buy_id\n" +
                "\tFROM buy_exp_plan\n" +
                "\tWHERE flag = 0 \n" +
                "\tand storer = '" + expNo +"'"+
                "\tand storage = '" + storageCode +"'"+
                "\tORDER BY buy_id desc";
        List result = super.createNativeQuery(sql).getResultList();

        if (result != null && result.size() > 0) {
            ExpClassDict expClassDict = null;
            for(int i=0;i<result.size();i++){
                String buyId = (String) result.get(i);
                expClassDict = new ExpClassDict();
                expClassDict.setClassCode(buyId);
                expClassDict.setClassName(buyId);
                idList.add(expClassDict);
            }

        }
        return idList;
    }
    /**
     * 查询待采购物品列表
     * @return
     */
    public List<BuyExpPlanVo> listBuyListLow(String storageCode){
        String sql = "SELECT distinct exp_stock.storage,\n" +
                "                exp_dict.exp_code,\n" +
                "                exp_stock.firm_id,\n" +
                "                avg(exp_price_list.trade_price) purchase,\n" +
                "                exp_price_list.exp_spec pack_spec,\n" +
                "                exp_price_list.units pack_unit,\n" +
                "               EXP_STOCK.QUANTITY quantity,\n" +
                "                exp_price_list.retail_price,\n" +
                "                \"EXP_DICT\".\"EXP_NAME\",\n" +
                "                EXP_DICT.exp_form\n" +
                "  FROM \"EXP_DICT\", exp_stock, exp_storage_profile, exp_price_list\n" +
                " WHERE (exp_dict.exp_code = \"EXP_STORAGE_PROFILE\".\"EXP_CODE\")\n" +
                "   and  (\"EXP_STOCK\".\"PACKAGE_SPEC\" = \"EXP_STORAGE_PROFILE\".\"EXP_SPEC\")\n" +
                "   and (\"EXP_STOCK\".\"STORAGE\" = \"EXP_STORAGE_PROFILE\".\"STORAGE\")\n" +
                "   and (\"EXP_STOCK\".\"EXP_CODE\" = \"EXP_DICT\".\"EXP_CODE\")\n" +
                "   and (\"EXP_STOCK\".\"EXP_SPEC\" = \"EXP_DICT\".\"EXP_SPEC\")\n" +
                "   and (exp_dict.exp_code = exp_price_list.exp_code)\n" +
                "   and exp_storage_profile.exp_code=exp_dict.exp_code\n" +
                "   and exp_storage_profile.exp_spec=exp_price_list.exp_spec\n" +
                "   and  (exp_dict.EXP_SPEC = exp_price_list.MIN_SPEC)\n" +
                "   and (exp_stock.firm_id = exp_price_list.firm_id)\n" +
                "       and (exp_price_list.start_date < sysdate and\n" +
                "        (sysdate <= exp_price_list.stop_date or\n" +
                "       exp_price_list.stop_date is null))\n" +
                "   and (\"EXP_STOCK\".\"STORAGE\" = '"+storageCode+"')\n" +
                "   \n" +
                "   \n" +
                " group by exp_stock.storage,\n" +
                "           exp_dict.exp_code,\n" +
                "          exp_DICT.exp_spec,\n" +
                "          exp_stock.Quantity,\n" +
                "          exp_stock.firm_id,\n" +
                "           exp_price_list.exp_spec ,\n" +
                "           exp_price_list.units,\n" +
                "          \"EXP_DICT\".\"EXP_NAME\",\n" +
                "          exp_storage_profile.LOW_LEVEL,\n" +
                "          exp_storage_profile.UPPER_LEVEL,\n" +
                "          exp_price_list.retail_price,\n" +
                "          EXP_DICT.exp_form\n" +
                "having(sum(\"EXP_STOCK\".\"QUANTITY\") < exp_storage_profile.LOW_LEVEL) or (exp_storage_profile.low_level + exp_storage_profile.UPPER_LEVEL = 0) or (exp_storage_profile.UPPER_LEVEL is null) or (exp_storage_profile.LOW_LEVEL IS NULL)\n" +
                "\n" +
                " ";

        List<BuyExpPlanVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), BuyExpPlanVo.class);
        return result;
    }

    public List<BuyExpPlanVo> listBuyListAll(String storageCode) {
        String sql =  "SELECT distinct exp_stock.storage,\n" +
                "                exp_dict.exp_code,\n" +
                "                exp_stock.firm_id,\n" +
                "               (exp_price_list.trade_price) purchase,\n" +
                "                exp_price_list.exp_spec pack_spec,\n" +
                "                exp_price_list.units pack_unit,\n" +
                "               EXP_STOCK.QUANTITY quantity,\n" +
                "                exp_price_list.retail_price,\n" +
                "                \"EXP_DICT\".\"EXP_NAME\",\n" +
                "                EXP_DICT.exp_form\n" +
                "  FROM \"EXP_DICT\", exp_stock, exp_storage_profile, exp_price_list\n" +
                " WHERE (exp_dict.exp_code = \"EXP_STORAGE_PROFILE\".\"EXP_CODE\")\n" +
                "    and (\"EXP_STOCK\".\"PACKAGE_SPEC\" = \"EXP_STORAGE_PROFILE\".\"EXP_SPEC\")\n" +
                "   and (\"EXP_STOCK\".\"STORAGE\" = \"EXP_STORAGE_PROFILE\".\"STORAGE\")\n" +
                "   and (\"EXP_STOCK\".\"EXP_CODE\" = \"EXP_DICT\".\"EXP_CODE\")\n" +
                "   and (\"EXP_STOCK\".\"EXP_SPEC\" = \"EXP_DICT\".\"EXP_SPEC\")\n" +
                "   and (exp_dict.exp_code = exp_price_list.exp_code)\n" +
                "   and exp_storage_profile.exp_code=exp_dict.exp_code\n" +
                "   and exp_storage_profile.exp_spec=exp_price_list.exp_spec\n" +
                "   and  (exp_dict.EXP_SPEC = exp_price_list.MIN_SPEC)\n" +
                "   and (exp_stock.firm_id = exp_price_list.firm_id)\n" +
                "       and (exp_price_list.start_date < sysdate and\n" +
                "        (sysdate <= exp_price_list.stop_date or\n" +
                "       exp_price_list.stop_date is null))\n" +
                "   and (\"EXP_STOCK\".\"STORAGE\" = 1503)\n" +
                   " ";
         List<BuyExpPlanVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), BuyExpPlanVo.class);
        return result;
    }

    /**
     * 生成采购单号
     * @return
     */
    public String getBuyId(){
        Calendar now = Calendar.getInstance();
        String dmd = now.get(Calendar.YEAR)+"";
        String month = (now.get(Calendar.MONTH) + 1)+"";
        String day = now.get(Calendar.DAY_OF_MONTH)+"";
        month = month.length()==2? month: "0"+month;
        day = day.length()==2?day:"0"+day;
        dmd = dmd + month + day;

        String sql = "select count(*) from buy_exp_plan\n" +
                "where buy_id like '"+dmd+"%'";
        List result = super.createNativeQuery(sql).getResultList();
        int num = 1;
        if (result != null && result.size() > 0) {
            BigDecimal count = (BigDecimal) result.get(0);
            if (count != null) {
                num = count.intValue() + 1;
            }
        }
        if(num>9999){
            num=9999;
        }
        String numString = num+"";
        while(numString.length()<4)
        {
            numString = "0"+numString;
        }
        numString = dmd+numString;
        return numString;
    }

    /**
     * 根据buyId查询最大buyNo
     * @param buyId
     * @return
     */
    public int getBuyNo(String buyId){
        String sql = "select  nvl(max(buy_no),0) buy_no from buy_exp_plan\n" +
                "where buy_id='" + buyId + "'";
        List result = super.createNativeQuery(sql).getResultList();

        if (result != null && result.size() > 0) {
            BigDecimal max = (BigDecimal) result.get(0);
            return max.intValue();
        }else{
            return 0;
        }

    }
    /**
     * 保存（暂存）
     * @param rows
     * @return
     */
    @Transactional
    public List<BuyExpPlan> save(BeanChangeVo<BuyExpPlan> rows) {
        List<BuyExpPlan> newUpdateDict = new ArrayList<>();
        List<BuyExpPlan> inserted = rows.getInserted();
        List<BuyExpPlan> updated = rows.getUpdated();
        List<BuyExpPlan> deleted = rows.getDeleted();
        if(inserted.size() > 0){
            for(BuyExpPlan dict:inserted){
                BuyExpPlan buyExpPlan = merge(dict);
                newUpdateDict.add(buyExpPlan);
            }
        }
        if (updated.size() > 0) {
            for (BuyExpPlan dict : updated) {
                BuyExpPlan buyExpPlan = merge(dict);
                newUpdateDict.add(buyExpPlan);
            }
        }
        if (deleted.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (BuyExpPlan dict : deleted) {
                ids.add(dict.getId());
            }
            this.removeByStringIds(BuyExpPlan.class,ids);
            newUpdateDict.addAll(deleted);
        }
        return newUpdateDict;
    }

    /**
     * 根据暂存单号查询暂存采购列表
     * @param buyId
     * @return
     */
    public List<BuyExpPlan> listTemp(String buyId){
        String hql = "from BuyExpPlan ep where ep.flag=0 and ep.buyId='"+buyId +"'";
        List resultList = this.entityManager.createQuery(hql).getResultList();
        return resultList;
    }

    /**
     * 根据上限生成采购数
     * @param inData
     * @return
     */
    public List<BuyExpPlan> generateNumUp(List<BuyExpPlan> inData,String storageCode) {
        if(null != inData && inData.size() > 0){
            Iterator<BuyExpPlan> ite = inData.iterator();
            while(ite.hasNext()){
                BuyExpPlan temp = ite.next();
                String sql = "SELECT nvl(sum(upper_level),0)\n" +
                        "\t\t\tFROM exp_storage_profile\n" +
                        "\t\t\tWHERE storage ='"+storageCode+"'\n" +
                        "\t\t\tAND EXP_code = '"+temp.getExpCode()+"'\n" +
                        "\t\t\tAnd exp_spec = '"+temp.getPackSpec()+"'";

                List result = super.createNativeQuery(sql).getResultList();
                if (result != null && result.size() > 0) {
                    BigDecimal up = (BigDecimal) result.get(0);
                    double upLevel = up.doubleValue();
//                    exportquantityRef
//                    temp.setExportquantityRef(upLevel);
                    temp.setWantNumber(upLevel);
                }

            }
        }
        return inData;
    }

    /**
     * 根据下限生成采购数
     * @param inData
     * @return
     */
    public List<BuyExpPlan> generateNumLow(List<BuyExpPlan> inData,String storageCode) {
        if (null != inData && inData.size() > 0) {
            Iterator<BuyExpPlan> ite = inData.iterator();
            while (ite.hasNext()) {
                BuyExpPlan temp = ite.next();
                String sql = "SELECT nvl(sum(low_level),0)\n" +
                        "\t\t\tFROM exp_storage_profile\n" +
                        "\t\t\tWHERE storage = '"+storageCode+"'\n" +
                        "\t\t\tAND EXP_code = '"+temp.getExpCode()+"'\n" +
                        "\t\t\tAnd exp_spec = '"+temp.getPackSpec()+"'";
                List result = super.createNativeQuery(sql).getResultList();
                if (result != null && result.size() > 0) {
                    BigDecimal up = (BigDecimal) result.get(0);
                    double upLevel = up.doubleValue();
//                    temp.setExportquantityRef(upLevel);
                    temp.setWantNumber(upLevel);
                }

            }
        }
        return inData;
    }

    /**
     * 根据消耗量生成采购数
     * @param inData
     * @return
     */
    public List<BuyExpPlan> generateNumSale(List<BuyExpPlan> inData) {

        if (null != inData && inData.size() > 0) {
            Iterator<BuyExpPlan> ite = inData.iterator();
            int day = 0;
            String startDate="";
            String endDate="";
            double consume = 0.00;
            double amount = 0.00;
            double quanty = 0.00;
            int dayIndex = 0;
            while (ite.hasNext()) {
                BuyExpPlan temp = ite.next();
//                if(temp.getStorage().equals("DAY")){
                    day = temp.getPlanNumber().intValue();
                    dayIndex = inData.indexOf(temp);

                    //根据传入的天数获得开始结束日期
                    Date dNow = new Date();   //当前时间
                    Date dBefore = new Date();

                    Calendar calendar = Calendar.getInstance();  //得到日历
                    calendar.setTime(dNow);//把当前时间赋给日历
                    calendar.add(Calendar.DAY_OF_MONTH, -day);  //设置为前n天
                    dBefore = calendar.getTime();   //得到前n天的时间

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
                    startDate = sdf.format(dBefore);    //格式化前n天
                    endDate = sdf.format(dNow); //格式化当前时间

//                }else{
                    //首先计算当前库存量
                    String stockSql = "SELECT nvl(sum(quantity),0)\n" +
                            "      FROM exp_stock\n" +
                            "      where exp_code = '"+temp.getExpCode()+"'\n" +
//                            "      And exp_spec = '"+temp.getExpSpec()+"'\n" +
                            "      And firm_id = '"+temp.getFirmId()+"'\n" +
                            "      And package_spec = '"+temp.getPackSpec()+"' and storage='"+temp.getStorage()+"'";
                    List quantityList = super.createNativeQuery(stockSql).getResultList();
                    if (quantityList != null && quantityList.size() > 0) {
                        double quantity = ((BigDecimal) quantityList.get(0)).doubleValue();
                        //然后计算这段时间内的消耗量
                        String consumeSql = "SELECT nvl(sum(exp_export_detail.quantity * exp_price_list.AMOUNT_PER_PACKAGE),0)\n" +
                                "\t\t\tFROM  exp_export_master,exp_export_detail,exp_price_list\n" +
                                "\t\t\tWHERE exp_export_detail.document_no= exp_export_master.document_no\n" +
                                "\t\t\tAND\texp_export_detail.EXP_code = exp_price_list.EXP_code\n" +
                                "\t\t\tAND\texp_export_detail.PACKAGE_SPEC = exp_price_list.EXP_spec\n" +
                                "\t\t\tand   exp_export_detail.firm_id = exp_price_list.firm_id  \t\t\tAND   exp_price_list.stop_date is NULL\n" +
                                "\t\t\tAND\tEXP_export_master.storage = '"+temp.getStorage()+"'\n" +
                                "\t\t\tAND\texp_export_detail.EXP_code = '"+temp.getExpCode()+"'\n" +
                                "\t\t\tAND   exp_export_detail.package_spec = '"+temp.getPackSpec()+"'\n" +
                                "\t\t\tAND   export_date >= to_date('"+ startDate+"','YYYY-MM-DD HH24:MI:SS')\n" +
                                "\t\t\tAnd   export_date < to_date('"+ endDate+"','YYYY-MM-DD HH24:MI:SS')";
                        List consumeList = super.createNativeQuery(consumeSql).getResultList();
                        if (consumeList != null && consumeList.size() > 0) {
                            consume = ((BigDecimal) consumeList.get(0)).doubleValue();
                        }
                        //查询出根据该消耗品的厂商、包装规格、包装单位查询出查询出其包装数量
                        String amountSql = "SELECT amount_per_package\n" +
                                "\t\t\tFROM EXP_price_list\n" +
                                "\t\t\tWHERE EXP_code = '"+temp.getExpCode()+"'\n" +
                                "\t\t\tAND EXP_spec = '"+temp.getPackSpec()+"'\n" +
                                "\t\t\tAND firm_id = '"+temp.getFirmId()+"'\n" +
                                "\t\t\tAND start_date < sysdate And (sysdate <= stop_date Or stop_date Is null)";
                        List amountList = super.createNativeQuery(amountSql).getResultList();
                        if (amountList != null && amountList.size() > 0) {
                            amount = ((BigDecimal) amountList.get(0)).doubleValue();
                        }
                        quanty = consume/amount;
                        quanty = quanty > 0.00 ? quanty : 0.00;
                        temp.setWantNumber(quanty);
                        temp.setExportquantityRef(quanty);
                        temp.setStockquantityRef(quantity);
                    }
                }
         }
         return inData;
    }

    /**
     * 查询待确认采购单
     * @param storageCode
     * @param loginName
     * @return
     */
    public List<BuyExpPlan> listConfirm(String storageCode, String loginName){
        String sql = "SELECT distinct buy_id,storer\n" +
                "\tFROM buy_exp_plan\n" +
                "\tWHERE ( (flag = 2 and  buyer = '"+loginName+"') or flag = 1 ) \n" +
                "\tand storage = '"+ storageCode+"'\t      \n" +
                "\tORDER BY buy_id desc";
        List<BuyExpPlan> result = super.createNativeQuery(sql, new ArrayList<Object>(), BuyExpPlan.class);
        return result;
    }

    /**
     * 查询待审核采购单
     * @param storageCode
     * @param loginName
     * @return
     */
    public List<BuyExpPlan> listAudit(String storageCode, String loginName){
        String sql = "SELECT distinct buy_id,buyer\n" +
                "\tFROM buy_exp_plan\n" +
                "\tWHERE flag = 3 or (flag = 4 and checker='"+loginName+"') or (flag = 8 and buyer='"+loginName+"')\n" +
                "\tand storage = '" + storageCode + "'\t      \n" +
                "\tORDER BY buy_id desc";
        List<BuyExpPlan> result = super.createNativeQuery(sql, new ArrayList<Object>(), BuyExpPlan.class);
        return result;
    }

    /**
     * 查询待执行采购单
     * @param storageCode
     * @param loginName
     * @return
     */
    public List<BuyExpPlan> listExecute(String storageCode, String loginName) {
        String sql = "SELECT distinct buy_id,checker \n" +
                "\tFROM buy_exp_plan\n" +
                "\tWHERE (flag = 5 and checker='"+loginName+"') or flag = 7 or (flag = 3 and buyer='"+loginName+"')\n" +
                "\tand storage = '" + storageCode + "'\t      \n" +
                "\tORDER BY buy_id desc";
        List<BuyExpPlan> result = super.createNativeQuery(sql, new ArrayList<Object>(), BuyExpPlan.class);
        return result;
    }

    /**
     * 根据采购单编号查询采购单列表详情
     * @param buyId
     * @return
     */
    public List<BuyExpPlan> getPlansById(String buyId){
        String sql = "SELECT ID,BUY_ID,   \n" +
                "         BUY_NO,   \n" +
                "         EXP_CODE,   \n" +
                "         EXP_NAME,   \n" +
                "         EXP_SPEC,   \n" +
                "         UNITS,   \n" +
                "         EXP_FORM,   \n" +
                "         TOXI_PROPERTY,   \n" +
                "         DOSE_PER_UNIT,   \n" +
                "         DOSE_UNITS,   \n" +
                "         EXP_INDICATOR,   \n" +
                "         INPUT_CODE,   \n" +
                "         WANT_NUMBER,   \n" +
                "         STORER,   \n" +
                "         STOCK_NUMBER,   \n" +
                "         STOCK_SUPPLIER,   \n" +
                "         BUYER,   \n" +
                "         CHECK_NUMBER,   \n" +
                "         CHECK_SUPPLIER,   \n" +
                "         CHECKER,   \n" +
                "         FLAG,   \n" +
                "         PACK_SPEC,   \n" +
                "         PACK_UNIT,   \n" +
                "         FIRM_ID,   \n" +
                "         PURCHASE_PRICE,   \n" +
                "         STORAGE,\n" +
                "\t\t\tSTOCK_NUMBER*PURCHASE_PRICE plan_number,\n" +
                "         STOCKQUANTITY_REF,   \n" +
                "         EXPORTQUANTITY_REF   \n" +
                "    FROM BUY_EXP_PLAN  \n" +
                "   WHERE ( BUY_ID = '"+buyId+"')" +
                "\tORDER BY buy_no asc";
        List<BuyExpPlan> result = super.createNativeQuery(sql, new ArrayList<Object>(), BuyExpPlan.class);

        return result;
    }

    public List<BuyExpPlanVo> listBuyExpPlanCaByInputCode(String q) {
        String sql = "select distinct a.firm_id,a.exp_code, a.retail_price,b.exp_name ,a.exp_spec pack_spec,a.units pack_unit,b.exp_form,a.min_spec exp_spec,a.min_units units,a.trade_price purchase_Price,b.input_code\n" +
                "  from exp_price_list a ,exp_dict b\n" +
                " where  a.exp_code = b.exp_code\n" +
                "   and a.min_spec = b.exp_spec\n" +
                "   and (start_date < sysdate and\n" +
                "       (stop_date >= sysdate or stop_date is null)) ";
        List<BuyExpPlanVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), BuyExpPlanVo.class);
        return nativeQuery;
    }
}
