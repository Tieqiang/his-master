package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.AppConfigerParameter;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heren on 2015/11/18.
 * 提取数据用的Facade
 */
public class FetchDataFacade extends BaseFacade {


    private IncomeItemDictFacade incomeItemDictFacade;

    @Inject
    public FetchDataFacade(IncomeItemDictFacade incomeItemDictFacade) {
        this.incomeItemDictFacade = incomeItemDictFacade;
    }

    /**
     * 获取本月份数据，并将数据保存至数据库中
     *
     * @param
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    @Transactional
    public List<CalcIncomeDetail> fetchFromHis(String hospitalId, String yearMonth, String fetchTypeId) {

        //计算月份
        String[] strings = yearMonth.split("-");
        String startDate = "", endDate = "";
        Integer month = Integer.parseInt(strings[1]);
        Integer year = Integer.parseInt(strings[0]) ;
        String performDept = null ;//定义开单科室
        String orderDept  = null ;//定义执行科室

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8) {
            startDate = strings[0] + "-" + month + "-01 00:00:00";
            endDate = strings[0] + "-" + month + "-31 23:59:59";
        } else if (month == 12 || month == 10) {
            startDate = strings[0] + "-" + month + "-01 00:00:00";
            endDate = strings[0] + "-" + month + "-31 23:59:59";
        } else if (month == 4 || month == 6 || month == 9) {
            startDate = strings[0] + month + "-01 00:00:00";
            endDate = strings[0] + month + "-30 23:59:59";
        } else if (month == 11) {
            startDate = strings[0] + "-" + month + "-01 00:00:00";
            endDate = strings[0] + "-" + month + "-30 23:59:59";

        } else {
            startDate = strings[0] + "-" + 2 + "-01 00:00:00";
            if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
                endDate = strings[0] + "-02-29 23:59:59";
            }else{
                endDate = strings[0] + "-02-28 23:59:59";
            }
        }

        //删除之前计算的数据
        removeCalcData(hospitalId, yearMonth);
        //根据选择的方式，获取对应的SQL；
        //AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        String hql = "from AcctParam as param  where param.paramName='" + fetchTypeId + "'";
        List<AcctParam> acctParams = createQuery(AcctParam.class, hql, new ArrayList<Object>()).getResultList();
        List<CalcIncomeDetail> incomeDetails = new ArrayList<>();


        String staffHql = "from StaffDict as staff where staff.hospitalId='"+hospitalId+"'" ;
        List<StaffDict> staffDicts = createQuery(StaffDict.class,staffHql,new ArrayList<Object>()).getResultList() ;

        for (AcctParam acctParam : acctParams) {
            String sql = acctParam.getParamSql();
            sql = sql.replace("${startDate}", startDate).replace("${endDate}", endDate);
            Query query = createNativeQuery(sql);
            List<Object[]> resultList = query.getResultList();
            for (Object[] objects : resultList) {

                CalcIncomeDetail calcIncomeDetail = new CalcIncomeDetail();
                calcIncomeDetail.setHospitalId(hospitalId);
                calcIncomeDetail.setIncomeItemName((String) objects[1]);
                calcIncomeDetail.setIncomeItemCode((String) objects[0]);
                calcIncomeDetail.setClassOnRecking((String) objects[2]);
                //设置执行科室
                //performDept = getAcctDeptDictId((String)objects[4],staffDicts) ;
                calcIncomeDetail.setPerformedBy((String) objects[3]);
                //if(performDept==null || "".equals(performDept)){
                //    calcIncomeDetail.setPerformedBy((String) objects[3]);
                //}else{
                //    calcIncomeDetail.setPerformedBy(performDept);
                //    performDept = null ;
                //}
                calcIncomeDetail.setPerformedDoctor((String) objects[4]);

                //设置开单科室
                orderDept =  getAcctDeptDictId((String)objects[6],staffDicts) ;
                if(orderDept==null || "".equals(orderDept)){
                    if(objects[5]==null || "".equals(objects[5])){
                        System.out.println("---------------------------------------------------------");
                        calcIncomeDetail.setOrderedBy("*");
                    }else{
                        calcIncomeDetail.setOrderedBy((String) objects[5]);
                    }
                }else{
                    calcIncomeDetail.setOrderedBy(orderDept);
                    orderDept = null ;
                }
                calcIncomeDetail.setOrderDoctor((String) objects[6]);
                calcIncomeDetail.setWardCode((String) objects[7]);
                calcIncomeDetail.setInpOrOutp(objects[8].toString());
                calcIncomeDetail.setTotalCost((BigDecimal) objects[9]);
                calcIncomeDetail.setYearMonth(yearMonth);
                merge(calcIncomeDetail);
                incomeDetails.add(calcIncomeDetail);
            }
        }
        return incomeDetails;
    }

    /**
     * 根据 empno name 或者loginname获取核算单元信息
     * @param object
     * @param staffDicts
     * @return
     */
    private String getAcctDeptDictId(String object, List<StaffDict> staffDicts) {
        if(object==null){
            return null ;
        }

        for(StaffDict staffDict:staffDicts){
            if(object.equals(staffDict.getLoginName())||object.equals(staffDict.getEmpNo())||object.equals(staffDict.getName())){
                return staffDict.getAcctDeptId() ;
            }
        }

        return null;
    }

    private AcctDeptDict getAcctDeptDict(String deptCode) {

        String hql = "select dept from AcctDeptDict as dept ,AcctDeptVsDeptDict vs where dept.id = vs.acctDeptId " +
                "and vs.deptDictId = '" + deptCode + "'";
        TypedQuery<AcctDeptDict> query = createQuery(AcctDeptDict.class, hql, new ArrayList<Object>());
        List<AcctDeptDict> resultList = query.getResultList();
        if (resultList.size() > 0) {
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 删除某一个医院某一个月份的收入信息
     *
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    private int removeCalcData(String hospitalId, String yearMonth) {
        String hql = "delete from CalcIncomeDetail as calc where calc.yearMonth = '" + yearMonth + "' and " +
                "calc.hospitalId='" + hospitalId + "'";
        int i = this.getEntityManager().createQuery(hql).executeUpdate();
        return i;
    }


    public PageEntity<CalcIncomeDetail> findByPages(String hospitalId, String yearMonth, String page, String rows) {

        String hql = "from CalcIncomeDetail as calc where calc.yearMonth = '" + yearMonth + "' and " +
                "calc.hospitalId = '" + hospitalId + "' order by calc.id";
        Integer iRow = Integer.parseInt(rows);
        Integer iPage = Integer.parseInt(page);

        TypedQuery<CalcIncomeDetail> query = createQuery(CalcIncomeDetail.class, hql, new ArrayList<Object>());
        query.setFirstResult((iPage - 1) * iRow);
        query.setMaxResults(iRow);
        PageEntity<CalcIncomeDetail> pageEntity = new PageEntity<>();

        pageEntity.setRows(query.getResultList());
        pageEntity.setTotal(this.getCalcIncomeDetailCount(hospitalId, yearMonth));
        return pageEntity;
    }

    /**
     * 获取某一个月份的总数据
     *
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    public Long getCalcIncomeDetailCount(String hospitalId, String yearMonth) {
        String hql = "select count(*) from CalcIncomeDetail as calc where calc.yearMonth = '" + yearMonth + "' and " +
                "calc.hospitalId = '" + hospitalId + "'";
        return createQuery(Long.class, hql, new ArrayList<Object>()).getResultList().get(0);
    }

    /**
     * 保存设置的对象
     *
     * @param calcIncomeDetails
     * @return
     */
    @Transactional
    public List<CalcIncomeDetail> saveCalc(List<CalcIncomeDetail> calcIncomeDetails) {
        for (CalcIncomeDetail detail : calcIncomeDetails) {
            merge(detail);
        }
        return calcIncomeDetails;
    }

    /**
     * 收入分割。
     * 对于门诊费用，如果护理单元的分割比例不为0 ，且护理单元为空的时候
     * 需要将这部分费用分担给门急诊护理单元（从参数表中取）
     * <p/>
     * 对于材料费用，如果执行科室费手术组，按照通常方法的比例将护理收入计入对应的护理单元
     * 如果对应的执行科室为手术组，则将收入比例按照护理单元的比例计入给手术室组
     *
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    @Transactional
    public List<CalcIncomeDetail> devideCalcIncome(String hospitalId, String yearMonth) throws Exception {

        double inpOrderReate = 0;
        double inpPerformReate = 0;
        double inpWardReate = 0;
        double outpOrderReate = 0;
        double outpPerformReate = 0;
        double outpWardReate = 0;
        double orderIncome = 0;
        double perormIncome = 0;
        double wardIncome = 0;
        List<CalcIncomeDetail> incomeDetails = getCalcIncomeDetail(hospitalId, yearMonth);
        List<IncomeItemDict> incomeItemDicts = incomeItemDictFacade.findByHospitalId(hospitalId);

        //读取参数获取手手术室信息
        String appHql = "from AppConfigerParameter as config where config.appName='HTCA' and config.parameterName='OPERATION_ACCT_DEPT'";
        AppConfigerParameter parameter = createQuery(AppConfigerParameter.class, appHql, new ArrayList<Object>()).getSingleResult();

        //读取参数获取门诊护理单元
        String outpWardHql = "from AppConfigerParameter as config where config.appName='HTCA' and config.parameterName='OUTP_WARD_ID'";
        AppConfigerParameter outpWardParameter = createQuery(AppConfigerParameter.class, outpWardHql, new ArrayList<Object>()).getSingleResult();

        //读取按照住院比例计算的门诊科室
        String outpDevideBaseInpHql = "from AppConfigerParameter as config where config.appName='HTCA' and config.parameterName='OUTP_DEVIDE_BASE_INP'";
        AppConfigerParameter outpDevideBaseInpParamter = createQuery(AppConfigerParameter.class, outpDevideBaseInpHql, new ArrayList<Object>()).getSingleResult();

        Map<String, String> outpWard = new HashMap<>();
        if (outpDevideBaseInpParamter != null) {
            String temp = outpDevideBaseInpParamter.getParameterValue();
            if (temp != null) {
                String[] temp1 = temp.split("|");
                for (String str : temp1) {
                    String[] temp2 = str.split(";");
                    if (temp2.length == 2) {
                        outpWard.put(temp2[0], temp2[1]);
                    }
                }
            }
        }


        for (CalcIncomeDetail detail : incomeDetails) {
            BigDecimal totalCost = detail.getTotalCost();
            IncomeItemDict incomeItemDict = getIncomeDict(detail, incomeItemDicts);
            if (incomeItemDict != null) {
                inpOrderReate = Double.parseDouble(incomeItemDict.getInpOrderedBy() == null ? "0" : incomeItemDict.getInpOrderedBy()) / 100;
                inpPerformReate = Double.parseDouble(incomeItemDict.getInpPerformedBy() == null ? "0" : incomeItemDict.getInpPerformedBy()) / 100;
                inpWardReate = Double.parseDouble(incomeItemDict.getInpWardCode() == null ? "0" : incomeItemDict.getInpWardCode()) / 100;
                outpOrderReate = Double.parseDouble(incomeItemDict.getOutpOrderedBy() == null ? "0" : incomeItemDict.getOutpOrderedBy()) / 100;
                outpPerformReate = Double.parseDouble(incomeItemDict.getOutpPerformedBy() == null ? "0" : incomeItemDict.getOutpPerformedBy()) / 100;
                outpWardReate = Double.parseDouble(incomeItemDict.getOutpWardCode() == null ? "0" : incomeItemDict.getOutpWardCode()) / 100;
            } else {
                String reckingCode = detail.getClassOnRecking();
                String hql = "from AcctReckItemClassDict as dict where dict.hospitalId='" + hospitalId + "' and dict.reckItemCode='" + reckingCode + "'";
                List<AcctReckItemClassDict> acctReckItemClassDicts = createQuery(AcctReckItemClassDict.class, hql, new ArrayList<Object>()).getResultList();
                if (acctReckItemClassDicts.size() > 0) {
                    AcctReckItemClassDict acctReckItemClassDict = acctReckItemClassDicts.get(0);
                    inpOrderReate = Double.parseDouble(acctReckItemClassDict.getInpOrderedBy() == null ? "0" : acctReckItemClassDict.getInpOrderedBy()) / 100;
                    inpPerformReate = Double.parseDouble(acctReckItemClassDict.getInpPerformedBy() == null ? "0" : acctReckItemClassDict.getInpPerformedBy()) / 100;
                    inpWardReate = Double.parseDouble(acctReckItemClassDict.getInpWardCode() == null ? "0" : acctReckItemClassDict.getInpWardCode()) / 100;
                    outpOrderReate = Double.parseDouble(acctReckItemClassDict.getOutpOrderedBy() == null ? "0" : acctReckItemClassDict.getOutpOrderedBy()) / 100;
                    outpPerformReate = Double.parseDouble(acctReckItemClassDict.getOutpPerformedBy() == null ? "0" : acctReckItemClassDict.getOutpPerformedBy()) / 100;
                    outpWardReate = Double.parseDouble(acctReckItemClassDict.getOutpWardCode() == null ? "0" : acctReckItemClassDict.getOutpWardCode()) / 100;
                }else{
                    Exception exception = new Exception("未能找到名称为：【"+detail.getIncomeItemName()+"】，编码为：【"+detail.getIncomeItemCode()+"】的分割系数") ;
                    throw exception ;
                }
            }

            if ("1".equals(detail.getInpOrOutp())) {
                //住院
                orderIncome = new BigDecimal(totalCost.doubleValue() * inpOrderReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                perormIncome = new BigDecimal(totalCost.doubleValue() * inpPerformReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                wardIncome = new BigDecimal(totalCost.doubleValue() * inpWardReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                //判断如果是手术室且费用为材料费则按照护理单元的比例计入对应的手术室
                if (isOpratorDept(detail, parameter) && ("K01".equals(detail.getClassOnRecking())||"K02".equals(detail.getClassOnRecking()))) {
                    detail.setPerformIncome(wardIncome);
                    detail.setWardIncome(perormIncome);
                } else {
                    detail.setPerformIncome(perormIncome);
                    detail.setWardIncome(wardIncome);
                }
                detail.setOrderIncome(orderIncome);
                merge(detail);
            }

            if ("0".equals(detail.getInpOrOutp())) {
                //门诊
                //
                String emgWArd = outpWardParameter.getParameterValue() ;
                if(emgWArd !=null && emgWArd.equals(detail.getWardCode())) {
                    //判断护理单元如果是急诊护理，则按照住院进行计算
                    //住院
                    orderIncome = new BigDecimal(totalCost.doubleValue() * inpOrderReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    perormIncome = new BigDecimal(totalCost.doubleValue() * inpPerformReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wardIncome = new BigDecimal(totalCost.doubleValue() * inpWardReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    //如果是执行科室是手术室且是材料费，则将材料费按照护理单元比例计入手术室否则则将对应的护理收入计入急诊科
                    if (isOpratorDept(detail, parameter) && ("K01".equals(detail.getClassOnRecking())||"K02".equals(detail.getClassOnRecking()))) {
                        detail.setPerformIncome(wardIncome);
                        detail.setWardIncome(perormIncome);
                    } else {
                        detail.setPerformIncome(perormIncome);
                        detail.setWardIncome(wardIncome);
                    }
                    detail.setOrderIncome(orderIncome);
                }else if (outpWard.containsKey(detail.getOrderedBy())) {
                    //住院
                    //判断此开单科室是否按按照住院的比例进行分割收入
                    orderIncome = new BigDecimal(totalCost.doubleValue() * inpOrderReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    perormIncome = new BigDecimal(totalCost.doubleValue() * inpPerformReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wardIncome = new BigDecimal(totalCost.doubleValue() * inpWardReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    detail.setOrderIncome(orderIncome);
                    //如果是执行科室是手术室且是材料费，则将材料费按照护理单元比例计入手术室否则则将对应的护理收入计入急诊科
                    if (isOpratorDept(detail, parameter) && ("K01".equals(detail.getClassOnRecking())||"K02".equals(detail.getClassOnRecking()))) {
                        detail.setPerformIncome(wardIncome);
                        detail.setWardIncome(perormIncome);
                    } else {
                        detail.setPerformIncome(perormIncome);
                        detail.setWardIncome(wardIncome);
                    }
                    detail.setWardCode(outpWard.get(detail.getOrderedBy()));//设置护理单元为对应的护理单元
                } else {
                    orderIncome = new BigDecimal(totalCost.doubleValue() * outpOrderReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    perormIncome = new BigDecimal(totalCost.doubleValue() * outpPerformReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wardIncome = new BigDecimal(totalCost.doubleValue() * outpWardReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    /**
                     * 说明：如果非急诊护理，且不是按照住院比例进行收入分割的开单科室
                     *      则价格开单的收入给开单，执行的收入给执行科室。
                     */
                    detail.setOrderIncome(orderIncome);
                    detail.setPerformIncome(perormIncome);
                    if("*".equals(detail.getWardCode())&&null !=detail.getWardIncome() && detail.getWardIncome() !=0){//如果护理部分有钱，且护理单元没有指定则将此部分钱归入急诊护理
                        detail.setWardCode(outpWardParameter.getParameterValue());
                    }
                    detail.setWardIncome(wardIncome);
                    //注释部分主要是为了判断，如果是材料费，对于普通门诊如果执行科室是手术室，则材料收入按照门诊护理单元的比例计入手术室
                    //如果不是则将此部分材料费按照护理单元的收入计入急诊护理
                    //if (isOpratorDept(detail, parameter)) {
                    //    if (("K01".equals(detail.getClassOnRecking())||"K02".equals(detail.getClassOnRecking()))) {
                    //        detail.setPerformIncome(wardIncome);
                    //        detail.setWardIncome(perormIncome);
                    //    } else {
                    //        detail.setPerformIncome(perormIncome);
                    //        detail.setWardIncome(wardIncome);
                    //    }
                    //} else {
                    //    if (("K01".equals(detail.getClassOnRecking())||"K02".equals(detail.getClassOnRecking()))) {
                    //        detail.setPerformIncome(perormIncome);
                    //        detail.setWardCode(outpWardParameter.getParameterValue());//将护理单元设置成急诊科护理单元，从配置文件中取
                    //        detail.setWardIncome(wardIncome);
                    //    } else {
                    //        detail.setPerformIncome(perormIncome);
                    //        detail.setWardIncome(wardIncome);
                    //    }
                    //}
                    //if("*".equals(detail.getWardCode())){
                    //    detail.setWardCode(outpWardParameter.getParameterValue());//如果门诊费用的护理单元为空，则将护理单元设置为急诊护理，从配置文件中获取
                    //}
                    //detail.setOrderIncome(orderIncome);
                }
                merge(detail);
            }
        }
        return incomeDetails;
    }

    /**
     * 判断是否为手术类科室
     *
     * @param detail
     * @param parameter
     * @return
     */
    private boolean isOpratorDept(CalcIncomeDetail detail, AppConfigerParameter parameter) {
        //如果收入名为空或者获取的参数为空
        if (detail == null || parameter == null) {
            return false;
        }
        String perform = detail.getPerformedBy();
        String operators = parameter.getParameterValue();
        if (operators == null || perform == null) {
            return false;
        }

        return operators.contains(perform);
    }

    /**
     * 根据待计算项目查找比例
     *
     * @param detail
     * @param incomeItemDicts
     * @return
     */
    private IncomeItemDict getIncomeDict(CalcIncomeDetail detail, List<IncomeItemDict> incomeItemDicts) {
        for (IncomeItemDict incomeItemDict : incomeItemDicts) {
            String incomeItemCode = detail.getIncomeItemCode();
            if ("".equals(incomeItemCode) || incomeItemCode == null) {
                continue;
            }
            if (incomeItemCode.equals(incomeItemDict.getPriceItemCode())) {
                return incomeItemDict;
            } else {
                continue;
            }
        }
        return null;
    }


    private List<AcctReckItemClassDict> getAcctReckItemClass(String hospitalId) {

        String hql = "from AcctReckItemClassDict as reck where reck.hospitalId='" + hospitalId + "'";

        return createQuery(AcctReckItemClassDict.class, hql, new ArrayList<Object>()).getResultList();
    }

    private List<CalcIncomeDetail> getCalcIncomeDetail(String hospitalId, String yearMonth) {
        String hql = "from CalcIncomeDetail as de where de.hospitalId='" + hospitalId + "' and " +
                "de.yearMonth='" + yearMonth + "'";
        TypedQuery<CalcIncomeDetail> query = createQuery(CalcIncomeDetail.class, hql, new ArrayList<Object>());
        return query.getResultList();
    }
}
