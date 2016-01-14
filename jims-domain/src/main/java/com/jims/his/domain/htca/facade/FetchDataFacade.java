package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.AppConfigerParameter;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

        if(month==1||month==3||month==5||month==7||month==8){
            startDate = strings[0]+month+"-01 00:00:00" ;
            endDate = strings[0]+month+"-31 23:59:59" ;
        }else if(month==12||month==10){
            startDate = strings[0]+"-"+month+"-01 00:00:00" ;
            endDate = strings[0]+"-"+month+"-31 23:59:59" ;
        }else if(month==4||month==6||month==9){
            startDate = strings[0]+month+"-01 00:00:00" ;
            endDate = strings[0]+month+"-30 23:59:59" ;
        }else if(month==11){
            startDate = strings[0]+"-"+month+"-01 00:00:00" ;
            endDate = strings[0]+"-"+month+"-30 23:59:59" ;

        }else {
            startDate = strings[0]+"-"+2+"-01 00:00:00" ;
            endDate = strings[0]+"-"+3+"-01 00:00:00" ;
        }

        //删除之前计算的数据
        removeCalcData(hospitalId, yearMonth);

        //根据选择的方式，获取对应的SQL；

        AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        String sql = acctParam.getParamSql();
        sql = sql.replace("${startDate}", startDate).replace("${endDate}", endDate);

        Query query = createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<CalcIncomeDetail> incomeDetails = new ArrayList<>();
        for (Object[] objects : resultList) {
            //if(((BigDecimal)objects[9]).intValue()==0){
            //    continue; //如果总金额为0 ，则不计入
            //}
            CalcIncomeDetail calcIncomeDetail = new CalcIncomeDetail();
            calcIncomeDetail.setHospitalId(hospitalId);
            calcIncomeDetail.setIncomeItemName((String) objects[1]);
            calcIncomeDetail.setIncomeItemCode((String) objects[0]);
            calcIncomeDetail.setClassOnRecking((String) objects[2]);
            calcIncomeDetail.setPerformedBy((String) objects[3]);
            calcIncomeDetail.setPerformedDoctor((String) objects[4]);
            calcIncomeDetail.setOrderedBy((String) objects[5]);
            calcIncomeDetail.setOrderDoctor((String) objects[6]);
            calcIncomeDetail.setWardCode((String) objects[7]);
            calcIncomeDetail.setInpOrOutp(objects[8].toString());
            calcIncomeDetail.setTotalCost((BigDecimal) objects[9]);
            calcIncomeDetail.setYearMonth(yearMonth);
            merge(calcIncomeDetail);
            incomeDetails.add(calcIncomeDetail);
        }
        return incomeDetails;
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
    public List<CalcIncomeDetail> devideCalcIncome(String hospitalId, String yearMonth) {

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
        String outpWardHql ="from AppConfigerParameter as config where config.appName='HTCA' and config.parameterName='OUTP_WARD_ID'" ;
        AppConfigerParameter outpWardParameter = createQuery(AppConfigerParameter.class,outpWardHql,new ArrayList<Object>()).getSingleResult() ;

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
                orderIncome = new BigDecimal(totalCost.doubleValue() * outpOrderReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                perormIncome = new BigDecimal(totalCost.doubleValue() * outpPerformReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                wardIncome = new BigDecimal(totalCost.doubleValue() * outpWardReate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                //判断如果是手术室且费用为材料费则按照护理单元的比例计入对应的手术室
                if (isOpratorDept(detail, parameter)) {
                    if("K01".equals(detail.getClassOnRecking())){
                        detail.setPerformIncome(wardIncome);
                        detail.setWardIncome(perormIncome);
                    }else{
                        detail.setPerformIncome(perormIncome);
                        detail.setWardIncome(wardIncome);
                    }
                } else {
                    if("K01".equals(detail.getClassOnRecking())){
                        detail.setPerformIncome(perormIncome);
                        detail.setWardCode(outpWardParameter.getParameterValue());//将护理单元设置成急诊科护理单元，从配置文件中取
                        detail.setWardIncome(wardIncome);
                    }else{
                        detail.setPerformIncome(perormIncome);
                        detail.setWardIncome(wardIncome);
                    }
                }
                detail.setOrderIncome(orderIncome);
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
