package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpPriceList;
import com.jims.his.domain.ieqm.vo.ExpPriceListVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangjing on 2015/10/10.
 */
public class ExpPriceListFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public ExpPriceListFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 根据expCode查询产品价格结果集
     * @param expCode
     * @return
     */
    public List<ExpPriceList> listExpPriceList(String expCode) {
        String hql = "from ExpPriceList as dict where dict.expCode='" + expCode + "'";
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }

    /**
     * 对EXP_PRICE_LIST和EXP_DICT联合查询，取出产品价格自定义对象ExpPriceListVo结果集
     * @param expCode
     * @return
     */
    public List<ExpPriceList> findExpPriceList(String expCode, String hospitalId,String flag) {
        String sql = "SELECT distinct a.ID,a.EXP_CODE,\n" +
                "         a.MATERIAL_CODE,   \n" +
                "         a.EXP_SPEC,   \n" +
                "         a.FIRM_ID,   \n" +
                "         a.UNITS,   \n" +
                "         a.TRADE_PRICE,   \n" +
                "         a.RETAIL_PRICE,   \n" +
                "         a.MAX_RETAIL_PRICE,   \n" +
                "         a.AMOUNT_PER_PACKAGE,   \n" +
                "         a.MIN_SPEC,   \n" +
                "         a.MIN_UNITS,   \n" +
                "         a.CLASS_ON_INP_RCPT,   \n" +
                "         a.CLASS_ON_OUTP_RCPT,   \n" +
                "         a.CLASS_ON_RECKONING,   \n" +
                "         a.SUBJ_CODE,   \n" +
                "         a.CLASS_ON_MR,   \n" +
                "         a.START_DATE,   \n" +
                "         a.STOP_DATE,   \n" +
                "         a.MEMOS,   \n" +
                "         b.EXP_NAME,\n" +
                "         a.PERMIT_NO,\n" +
                "         a.PERMIT_DATE,\n" +
                "         a.REGISTER_NO,\n" +
                "         a.REGISTER_DATE,\n" +
                "         a.FDA_OR_CE_NO,\n" +
                "         a.FDA_OR_CE_DATE,\n" +
                "         a.OTHER_NO,\n" +
                "         a.OTHER_DATE   \n" +
                "    FROM EXP_PRICE_LIST a,EXP_DICT b   \n" +
                "   WHERE  a.EXP_CODE = b.EXP_CODE and a.min_spec=b.exp_spec" ;

        if(null != expCode && !expCode.trim().equals("")){
            sql += " AND a.EXP_CODE ='" + expCode + "'";
        }
        if(null != hospitalId && !hospitalId.trim().equals("")){
            sql += " AND a.HOSPITAL_ID ='" + hospitalId + "'";
        }
        if("".equals(flag) || flag==null){
            sql+=" and ( a.STOP_DATE >= sysdate OR a.STOP_DATE is null ) " +
                    " and  a.START_DATE <= sysdate";
        }
        List<ExpPriceList> resultList = super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceList.class);
        return resultList;
    }

    /**
     * 保存产品价格
     * @param beanChangeVo
     * @return
     */
    @Transactional
    public List<ExpPriceList> saveExpPriceList(BeanChangeVo<ExpPriceListVo> beanChangeVo) {
        List<ExpPriceList> newUpdateDict = new ArrayList<>();

        List<ExpPriceListVo> inserted = beanChangeVo.getInserted();
        List<ExpPriceListVo> updated = beanChangeVo.getUpdated();
        List<ExpPriceListVo> deleted = beanChangeVo.getDeleted();

        if (inserted != null && inserted.size() > 0) {
            Iterator ite = inserted.iterator();
            ExpPriceList price;
            while (ite.hasNext()) {
                ExpPriceListVo vo = (ExpPriceListVo) ite.next();
                price = new ExpPriceList();
                price.setExpCode(vo.getExpCode());
                price.setExpSpec(vo.getExpSpec());
                price.setFirmId(vo.getFirmId());
                price.setUnits(vo.getUnits());
                price.setTradePrice(vo.getTradePrice());
                price.setRetailPrice(vo.getRetailPrice());
                price.setAmountPerPackage(vo.getAmountPerPackage());
                price.setMinSpec(vo.getMinSpec());
                price.setMinUnits(vo.getMinUnits());
                price.setClassOnInpRcpt(vo.getClassOnInpRcpt());
                price.setClassOnOutpRcpt(vo.getClassOnOutpRcpt());
                price.setClassOnReckoning(vo.getClassOnReckoning());
                price.setSubjCode(vo.getSubjCode());
                price.setClassOnMr(vo.getClassOnMr());
                price.setStartDate(new Date());
                price.setMemos(vo.getMemos());
                price.setMaxRetailPrice(vo.getMaxRetailPrice());
                price.setMaterialCode(vo.getMaterialCode());
                price.setOperator(vo.getOperator());
                price.setPermitNo(vo.getPermitNo());
                price.setPermitDate(new Date());
                price.setRegisterNo(vo.getRegisterNo());
                price.setRegisterDate(new Date());
                price.setFdaOrCeNo(vo.getFdaOrCeNo());
                price.setFdaOrCeDate(new Date());
                price.setOtherNo(vo.getOtherNo());
                price.setOtherDate(new Date());
                price.setHospitalId(vo.getHospitalId());
                merge(price);
                newUpdateDict.add(price);
            }
        }

        if (null != updated && updated.size() > 0) {
            Iterator ite = updated.iterator();
            ExpPriceList price;
            while (ite.hasNext()) {
                ExpPriceListVo vo = (ExpPriceListVo) ite.next();
                price = new ExpPriceList();
                price.setExpCode(vo.getExpCode());
                price.setExpSpec(vo.getExpSpec());
                price.setFirmId(vo.getFirmId());
                price.setUnits(vo.getUnits());
                price.setTradePrice(vo.getTradePrice());
                price.setRetailPrice(vo.getRetailPrice());
                price.setAmountPerPackage(vo.getAmountPerPackage());
                price.setMinSpec(vo.getMinSpec());
                price.setMinUnits(vo.getMinUnits());
                price.setClassOnInpRcpt(vo.getClassOnInpRcpt());
                price.setClassOnOutpRcpt(vo.getClassOnOutpRcpt());
                price.setClassOnReckoning(vo.getClassOnReckoning());
                price.setSubjCode(vo.getSubjCode());
                price.setClassOnMr(vo.getClassOnMr());
                price.setStartDate(new Date());
                price.setMemos(vo.getMemos());
                price.setMaxRetailPrice(vo.getMaxRetailPrice());
                price.setMaterialCode(vo.getMaterialCode());
                price.setOperator(vo.getOperator());
                price.setPermitNo(vo.getPermitNo());
                price.setPermitDate(new Date());
                price.setRegisterNo(vo.getRegisterNo());
                price.setRegisterDate(new Date());
                price.setFdaOrCeNo(vo.getFdaOrCeNo());
                price.setFdaOrCeDate(new Date());
                price.setOtherNo(vo.getOtherNo());
                price.setOtherDate(new Date());
                price.setHospitalId(vo.getHospitalId());
                merge(price);
                newUpdateDict.add(price);
            }
        }
        return newUpdateDict;
    }

    /**
     * 停价
     * @param price
     * @return
     */
    @Transactional
    public ExpPriceList stopPrice(ExpPriceList price){
        price = this.get(ExpPriceList.class, price.getId());
        price.setStopDate(new Date());
        price = merge(price);
        return price;
    }
    /**
     * 对exp_dict , exp_price_list ,exp_stock联合查询，取出产品价格自定义对象ExpPriceListVo结果集
     * @param inputCode
     * @param StorageCode
     * @return
     */
    public List<ExpPriceListVo> findExpList(String inputCode ,String StorageCode){
        String sql = " SELECT distinct B.EXP_NAME,\n" +
                "     c.EXP_CODE,\n" +
                "     c.EXP_SPEC,\n" +
                "     c.units,\n" +
                "     c.min_spec,\n" +
                "     c.min_UNITS,\n" +
                "     c.FIRM_ID,\n" +
                "     c.TRADE_PRICE,\n" +
                "     c.retail_price,\n" +
                "     c.Register_no,\n" +
                "     c.Permit_no,\n" +
                "     b.input_code,\n" +
                "     d.supply_indicator,\n" +
                "     d.quantity as amount_Per_Package\n" +
                "FROM exp_dict b, exp_price_list c,exp_stock d\n" +
                "WHERE b.EXP_CODE = c.EXP_CODE\n" +
                "AND   b.exp_spec = c.min_spec\n" +
                "and   c.EXP_CODE = d.exp_code(+)\n" +
                "and   c.min_SPEC = d.exp_spec(+)\n" +
                "and   c.firm_id = d.firm_id(+)\n" +
                "AND   c.start_date <= sysdate\n" +
                "AND   (c.stop_date IS NULL OR c.stop_date > sysdate)\n " +
                " and   d.package_spec=c.exp_spec " +
                " and   d.storage(+) like '" + StorageCode + "'||'%'\n" +
                //"AND   nvl(d.quantity,0) > 0" +

                "and   upper(b.input_code) like upper('%" + inputCode + "%')";/* +
                " and   d.quantity>0";*/
        return super.createNativeQuery(sql,new ArrayList<Object>(), ExpPriceListVo.class);
    }
    /**
     * 根据expCode，expSpec，firmId，units查询产品价格
     * @param expCode
     * @param expSpec
     * @param firmId
     * @param units
     * @return
     */
    public ExpPriceList getExpPriceList(String expCode, String expSpec, String firmId, String units){
        String hql = "from ExpPriceList pri where pri.expCode='" + expCode +"' and pri.expSpec='"
                + expSpec + "' and pri.firmId='" + firmId + "' and pri.units='" + units +"' and pri.stopDate is null ";
        List resultList = this.entityManager.createQuery(hql).getResultList();
        if (resultList.size() > 0) {
            return(ExpPriceList)resultList.get(0);
        }else{
            return null;
        }
    }

    public ExpPriceListVo findByCodeAndPackageSpec(String expCode, String packageSpec) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=sdf.format(new Date());
        String sql="select *  from exp_price_list where exp_code='"+expCode+"' and exp_spec='"+packageSpec+"' and (stop_date>=to_date('"+time+"','yyyy-MM-dd HH24:MI:SS') or stop_date is null)";
        List<ExpPriceListVo> list= super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceListVo.class);
        if(list!=null&&!list.isEmpty()){
            return list.get(0);
        }else{
            return null;
        }
    }


    /**
     * chenxy
     * @param expId
     * @return
     */
    public ExpPriceList findById(String expId) {
        return (ExpPriceList)entityManager.createQuery("from ExpPriceList where id='"+expId+"'").getSingleResult();
    }

    /**
     * 查找添加的价格信息是否已经存在
     * If exist true
     * else false
     * @param beanChangeVo
     * @return
     */
    public Map<String, Object> checkIsExist(BeanChangeVo<ExpPriceListVo> beanChangeVo) {
        Map<String,Object> map=new HashMap<>();
        List<ExpPriceListVo> list=beanChangeVo.getInserted();
        for(ExpPriceListVo expPriceListVo:list){
            boolean isExist=checkSingleIsExist(expPriceListVo);
            if(isExist){
                map.put("success",true);
                break;
            }
        }
        return map;
    }

    /**
     * 查找某个产品价格是否已经存在
     * @param expPriceListVo
     * @return
     */
    private boolean checkSingleIsExist(ExpPriceListVo expPriceListVo) {
        String expCode=expPriceListVo.getExpCode();
        String expSpec=expPriceListVo.getExpSpec();
        String units=expPriceListVo.getUnits();
        String firmId=expPriceListVo.getFirmId();
        Integer amountPerPackage=expPriceListVo.getAmountPerPackage();
        String  sql="from ExpPriceList where expCode='"+expCode+"' and expSpec='"+expSpec+"' and units='"+units+"' and firmId='"+firmId+"' and amountPerPackage="+amountPerPackage+"";
        List<ExpPriceList> list=entityManager.createQuery(sql).getResultList();
        if(list!=null&&!list.isEmpty()){
             return true;
        }
        return false;
    }


}
