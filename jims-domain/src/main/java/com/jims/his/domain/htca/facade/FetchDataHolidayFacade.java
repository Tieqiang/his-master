package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.AppConfigerParameter;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.*;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by heren on 2015/11/18.
 * 提取数据用的Facade
 */
public class FetchDataHolidayFacade extends BaseFacade {


    private IncomeItemDictFacade incomeItemDictFacade;

    @Inject
    public FetchDataHolidayFacade(IncomeItemDictFacade incomeItemDictFacade) {
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
    public List<CalcIncomeDetailForHoliday> fetchFromHis(String hospitalId, String yearMonth, String fetchTypeId) throws ParseException {

        ////计算月份
        String performDept = null ;//定义开单科室
        String orderDept  = null ;//定义执行科室


        //删除之前计算的数据
        removeCalcData(hospitalId, yearMonth);
        //根据选择的方式，获取对应的SQL；
        //AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        String hql = "from AcctParam as param  where param.paramName='" + fetchTypeId + "'";
        List<AcctParam> acctParams = createQuery(AcctParam.class, hql, new ArrayList<Object>()).getResultList();
        List<CalcIncomeDetailForHoliday> incomeDetails = new ArrayList<>();


        String staffHql = "from StaffDict as staff where staff.hospitalId='"+hospitalId+"'" ;
        List<StaffDict> staffDicts = createQuery(StaffDict.class,staffHql,new ArrayList<Object>()).getResultList() ;
        String hqlHoliday = "from HolidayDict as dict where substr(dict.holiday,1,7)='"+yearMonth+"'" ;
        List<HolidayDict> holidayDicts = createQuery(HolidayDict.class,hqlHoliday,new ArrayList<Object>()).getResultList() ;
        String startDate = "" ;
        String endDate = "" ;
        for(HolidayDict holidayDict:holidayDicts){
            String holiday = holidayDict.getHoliday() ;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();

            c.setTime(format.parse(holiday));

            int dayForWeek = 0;



            int i = c.get(Calendar.DAY_OF_WEEK) ;
            if(i==7 && "0".equals(holidayDict.getFullDay())){
                startDate = holiday+" 12:00:00" ;
            }else{
                startDate = holiday +" 00:00:00" ;
            }
            endDate = holiday +" 23:59:59" ;
            for (AcctParam acctParam : acctParams) {
                String sql = acctParam.getParamSql();
                sql = sql.replace("${startDate}", startDate).replace("${endDate}", endDate);
                Query query = createNativeQuery(sql);
                List<Object[]> resultList = query.getResultList();
                for (Object[] objects : resultList) {

                    CalcIncomeDetailForHoliday calcIncomeDetail = new CalcIncomeDetailForHoliday();
                    calcIncomeDetail.setHospitalId(hospitalId);
                    calcIncomeDetail.setIncomeItemName((String) objects[1]);
                    calcIncomeDetail.setIncomeItemCode((String) objects[0]);
                    calcIncomeDetail.setClassOnRecking((String) objects[2]);
                    //设置执行科室
                    calcIncomeDetail.setPerformedBy((String) objects[3]);
                    calcIncomeDetail.setPerformedDoctor((String) objects[4]);

                    //设置开单科室
                    orderDept =  getAcctDeptDictId((String)objects[6],staffDicts) ;
                    if(orderDept==null || "".equals(orderDept)){
                        calcIncomeDetail.setOrderedBy((String) objects[5]);
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
        String hql = "delete from CalcIncomeDetailForHoliday as calc where calc.yearMonth = '" + yearMonth + "' and " +
                "calc.hospitalId='" + hospitalId + "'";
        int i = this.getEntityManager().createQuery(hql).executeUpdate();
        return i;
    }


    public PageEntity<CalcIncomeDetailForHoliday> findByPages(String hospitalId, String yearMonth, String page, String rows) {

        String hql = "from CalcIncomeDetailForHoliday as calc where calc.yearMonth = '" + yearMonth + "' and " +
                "calc.hospitalId = '" + hospitalId + "' order by calc.id";
        Integer iRow = Integer.parseInt(rows);
        Integer iPage = Integer.parseInt(page);

        TypedQuery<CalcIncomeDetailForHoliday> query = createQuery(CalcIncomeDetailForHoliday.class, hql, new ArrayList<Object>());
        query.setFirstResult((iPage - 1) * iRow);
        query.setMaxResults(iRow);
        PageEntity<CalcIncomeDetailForHoliday> pageEntity = new PageEntity<>();

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
        String hql = "select count(*) from CalcIncomeDetailForHoliday as calc where calc.yearMonth = '" + yearMonth + "' and " +
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
    public List<CalcIncomeDetailForHoliday> saveCalc(List<CalcIncomeDetailForHoliday> calcIncomeDetails) {
        for (CalcIncomeDetailForHoliday detail : calcIncomeDetails) {
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
    public List<CalcIncomeDetailForHoliday> devideCalcIncome(String hospitalId, String yearMonth) {

        double inpOrderReate = 0;
        double inpPerformReate = 0;
        double inpWardReate = 0;
        double outpOrderReate = 0;
        double outpPerformReate = 0;
        double outpWardReate = 0;
        double orderIncome = 0;
        double perormIncome = 0;
        double wardIncome = 0;
        List<CalcIncomeDetailForHoliday> incomeDetails = getCalcIncomeDetail(hospitalId, yearMonth);
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


        for (CalcIncomeDetailForHoliday detail : incomeDetails) {
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
                }
            }

            if ("1".equals(detail.getInpOrOutp())) {
                //住院
                orderIncome = new BigDecimal(totalCost.doubleValue() * inpOrderReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                perormIncome = new BigDecimal(totalCost.doubleValue() * inpPerformReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                wardIncome = new BigDecimal(totalCost.doubleValue() * inpWardReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                //判断如果是手术室且费用为材料费则按照护理单元的比例计入对应的手术室
                if (isOpratorDept(detail, parameter) && "K01".equals(detail.getClassOnRecking())) {
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
                //如果比例按照住院比例进行计算收入的话
                String emgWArd = outpWardParameter.getParameterValue() ;
                if(emgWArd !=null && emgWArd.equals(detail.getWardCode())) {
                    //判断护理单元如果是急诊护理，则按照住院进行计算
                    //住院
                    orderIncome = new BigDecimal(totalCost.doubleValue() * inpOrderReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    perormIncome = new BigDecimal(totalCost.doubleValue() * inpPerformReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wardIncome = new BigDecimal(totalCost.doubleValue() * inpWardReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    //如果是执行科室是手术室且是材料费，则将材料费按照护理单元比例计入手术室否则则将对应的护理收入计入急诊科
                    if (isOpratorDept(detail, parameter) && "K01".equals(detail.getClassOnRecking())) {
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

                    //如果是执行科室是手术室且是材料费，则将材料费按照护理单元比例计入手术室否则则将对应的护理收入计入急诊科
                    if (isOpratorDept(detail, parameter) && "K01".equals(detail.getClassOnRecking())) {
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
                    //判断如果是手术室且费用为材料费则按照护理单元的比例计入对应的手术室
                    detail.setOrderIncome(orderIncome);
                    detail.setOrderIncome(orderIncome);
                    detail.setPerformIncome(perormIncome);
                    if("*".equals(detail.getWardCode())&&null !=detail.getWardIncome() && detail.getWardIncome()>0){//如果护理部分有钱，且护理单元没有指定则将此部分钱归入急诊护理
                        detail.setWardCode(outpWardParameter.getParameterValue());
                    }
                    detail.setWardIncome(wardIncome);
                    //if (isOpratorDept(detail, parameter)) {
                    //    if ("K01".equals(detail.getClassOnRecking())) {
                    //        detail.setPerformIncome(wardIncome);
                    //        detail.setWardIncome(perormIncome);
                    //    } else {
                    //        detail.setPerformIncome(perormIncome);
                    //        detail.setWardIncome(wardIncome);
                    //    }
                    //} else {
                    //    if ("K01".equals(detail.getClassOnRecking())) {
                    //        detail.setPerformIncome(perormIncome);
                    //        detail.setWardCode(outpWardParameter.getParameterValue());//将护理单元设置成急诊科护理单元，从配置文件中取
                    //        detail.setWardIncome(wardIncome);
                    //    } else {
                    //        detail.setPerformIncome(perormIncome);
                    //        detail.setWardIncome(wardIncome);
                    //    }
                    //}
                    ////if("*".equals(detail.getWardCode())){
                    ////    detail.setWardCode(outpWardParameter.getParameterValue());//如果门诊费用的护理单元为空，则将护理单元设置为急诊护理，从配置文件中获取
                    ////}
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
    private boolean isOpratorDept(CalcIncomeDetailForHoliday detail, AppConfigerParameter parameter) {
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
    private IncomeItemDict getIncomeDict(CalcIncomeDetailForHoliday detail, List<IncomeItemDict> incomeItemDicts) {
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

    private List<CalcIncomeDetailForHoliday> getCalcIncomeDetail(String hospitalId, String yearMonth) {
        String hql = "from CalcIncomeDetailForHoliday as de where de.hospitalId='" + hospitalId + "' and " +
                "de.yearMonth='" + yearMonth + "'";
        TypedQuery<CalcIncomeDetailForHoliday> query = createQuery(CalcIncomeDetailForHoliday.class, hql, new ArrayList<Object>());
        return query.getResultList();
    }
}
