package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
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
public class FetchDataFacade extends BaseFacade{



    private IncomeItemDictFacade incomeItemDictFacade ;

    @Inject
    public FetchDataFacade(IncomeItemDictFacade incomeItemDictFacade) {
        this.incomeItemDictFacade = incomeItemDictFacade;
    }

    /**
     * 获取本月份数据，并将数据保存至数据库中
     *
     * @param id
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    @Transactional
    public List<CalcIncomeDetail> fetchFromHis( String hospitalId, String yearMonth,String fetchTypeId) {

        //计算月份
        String[] strings = yearMonth.split("-");
        String startDate="" ,endDate="" ;
        Integer month = Integer.parseInt(strings[1])  ;

        if(month==1||month==3||month==5||month==7||month==8){
            startDate = strings[0]+month+"-01 00:00:00" ;
            endDate = strings[0]+month+"-31 23:59:59" ;
        }else if(month==12){
            startDate = strings[0]+"-"+month+"-01 00:00:00" ;
            endDate = strings[0]+"-"+month+"-31 23:59:59" ;
        }else if(month==4||month==6||month==9){
            startDate = strings[0]+month+"-01 00:00:00" ;
            endDate = strings[0]+month+"-30 23:59:59" ;
        }else if(month==11){
            startDate = strings[0]+"-"+month+"-01 00:00:00" ;
            endDate = strings[0]+"-"+month+"-30 23:59:59" ;

        }

        //删除之前计算的数据
        removeCalcData(hospitalId,yearMonth) ;

        //根据选择的方式，获取对应的SQL；

        AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        String sql = acctParam.getParamSql() ;
        sql=sql.replace("${startDate}",startDate).replace("${endDate}", endDate);
        //重新计算结果
        //String sql = "\n" +
        //        "select a.item_code,\n" +
        //        "       a.item_name,\n" +
        //        "       a.class_on_reckoning,\n" +
        //        "       a.performed_by,\n" +
        //        "       a.performed_doctor,\n" +
        //        "       b.ordered_by_dept,\n" +
        //        "       b.ordered_by_doctor,\n" +
        //        "       a.ward_code,\n" +
        //        "       '0' outp_or_inp,\n" +
        //        "       sum(a.costs) costs\n" +
        //        "  from outpbill.outp_bill_items a, outpbill.outp_order_desc b\n" +
        //        " where a.rcpt_no = b.rcpt_no\n" +
        //        "   and a.visit_date >=\n" +
        //        "       to_date('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.visit_date <=\n" +
        //        "       to_date('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        " group by a.item_code,\n" +
        //        "          a.item_name,\n" +
        //        "          a.class_on_reckoning,\n" +
        //        "          a.performed_by,\n" +
        //        "          a.performed_doctor,\n" +
        //        "          b.ordered_by_dept,\n" +
        //        "          b.ordered_by_doctor,\n" +
        //        "          a.ward_code\n" +
        //        "union all\n" +
        //        "select a.item_code,\n" +
        //        "       a.item_name,\n" +
        //        "       a.class_on_reckoning,\n" +
        //        "       a.performed_by,\n" +
        //        "       a.perform_doctor,\n" +
        //        "       a.ordered_by,\n" +
        //        "       a.order_doctor,\n" +
        //        "       a.ward_code,\n" +
        //        "       '1' outp_or_inp,\n" +
        //        "       sum(a.costs) costs\n" +
        //        "  from inp_bill_detail a\n" +
        //        " where a.billing_date_time >=\n" +
        //        "       to_date('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.billing_date_time <=\n" +
        //        "       to_date('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        " group by a.item_code,\n" +
        //        "          a.item_name,\n" +
        //        "          a.class_on_reckoning,\n" +
        //        "          a.performed_by,\n" +
        //        "          a.perform_doctor,\n" +
        //        "          a.ordered_by,\n" +
        //        "          a.order_doctor,\n" +
        //        "          a.ward_code" ;
        //String sql = "select a.item_code,\n" +
        //        "       a.item_name,\n" +
        //        "       a.class_on_reckoning,\n" +
        //        "       nvl(c.dept_code, '*'),\n" +
        //        "       a.perform_doctor,\n" +
        //        "       nvl(e.dept_code, '*'),\n" +
        //        "       a.order_doctor,\n" +
        //        "       nvl(g.dept_code, '*'),\n" +
        //        "       '1' outp_or_inp,\n" +
        //        "       sum(a.costs) costs\n" +
        //        "  from inp_bill_detail             a,\n" +
        //        "       htca.acct_dept_dict         c,\n" +
        //        "       htca.acct_dept_vs_dept_dict d,\n" +
        //        "       htca.acct_dept_dict         e,\n" +
        //        "       htca.acct_dept_vs_dept_dict f,\n" +
        //        "       htca.acct_dept_dict         g,\n" +
        //        "       htca.acct_dept_vs_dept_dict h\n" +
        //        " where a.billing_date_time >=\n" +
        //        "       to_date('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.billing_date_time <=\n" +
        //        "       to_date('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.performed_by = d.dept_dict_id(+)\n" +
        //        "   and d.acct_dept_id = c.id(+)\n" +
        //        "   and a.ordered_by = f.dept_dict_id(+)\n" +
        //        "   and f.acct_dept_id = e.id(+)\n" +
        //        "   and a.ward_code = h.dept_dict_id(+)\n" +
        //        "   and h.acct_dept_id = g.id(+)\n" +
        //        " group by a.item_code,\n" +
        //        "          a.item_name,\n" +
        //        "          a.class_on_reckoning,\n" +
        //        "          nvl(c.dept_code, '*'),\n" +
        //        "          nvl(g.dept_code, '*'),\n" +
        //        "          nvl(e.dept_code, '*'),\n" +
        //        "          a.perform_doctor ,\n" +
        //        "          a.order_doctor\n" +
        //        "union all\n" +
        //        "select a.item_code,\n" +
        //        "       a.item_name,\n" +
        //        "       a.class_on_reckoning,\n" +
        //        "       nvl(c.dept_code, '*'),\n" +
        //        "       a.performed_doctor,\n" +
        //        "       nvl(e.dept_code, '*'),\n" +
        //        "       b.ordered_by_doctor,\n" +
        //        "       nvl(g.dept_code, '*'),\n" +
        //        "       '0' outp_or_inp,\n" +
        //        "       sum(a.costs) costs\n" +
        //        "  from outpbill.outp_bill_items    a,\n" +
        //        "       outpbill.outp_order_desc    b,\n" +
        //        "       htca.acct_dept_dict         c,\n" +
        //        "       htca.acct_dept_vs_dept_dict d,\n" +
        //        "       htca.acct_dept_dict         e,\n" +
        //        "       htca.acct_dept_vs_dept_dict f,\n" +
        //        "       htca.acct_dept_dict         g,\n" +
        //        "       htca.acct_dept_vs_dept_dict h\n" +
        //        " where a.rcpt_no = b.rcpt_no\n" +
        //        "   and a.visit_date >=\n" +
        //        "       to_date('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.visit_date <=\n" +
        //        "       to_date('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.performed_by = d.dept_dict_id(+)\n" +
        //        "   and d.acct_dept_id = c.id(+)\n" +
        //        "   and b.ordered_by_dept = f.dept_dict_id(+)\n" +
        //        "   and f.acct_dept_id = e.id(+)\n" +
        //        "   and a.ward_code = h.dept_dict_id(+)\n" +
        //        "   and h.acct_dept_id = g.id(+)\n" +
        //        " group by a.item_code,\n" +
        //        "          a.item_name,\n" +
        //        "          a.class_on_reckoning,\n" +
        //        "          nvl(c.dept_code, '*'),\n" +
        //        "          c.dept_code,\n" +
        //        "          a.performed_doctor,\n" +
        //        "          nvl(e.dept_code, '*'),\n" +
        //        "          b.ordered_by_doctor,\n" +
        //        "          nvl(g.dept_code, '*')" ;
        //String sql = "select a.item_code,\n" +
        //        "       a.item_name,\n" +
        //        "       a.class_on_reckoning,\n" +
        //        "       nvl(c.id, '*'),\n" +
        //        "       a.perform_doctor,\n" +
        //        "       nvl(e.id, '*'),\n" +
        //        "       a.order_doctor,\n" +
        //        "       nvl(g.id, '*'),\n" +
        //        "       '1' outp_or_inp,\n" +
        //        "       sum(a.costs) costs\n" +
        //        "  from inp_bill_detail             a,\n" +
        //        "       htca.acct_dept_dict         c,\n" +
        //        "       htca.acct_dept_vs_dept_dict d,\n" +
        //        "       htca.acct_dept_dict         e,\n" +
        //        "       htca.acct_dept_vs_dept_dict f,\n" +
        //        "       htca.acct_dept_dict         g,\n" +
        //        "       htca.acct_dept_vs_dept_dict h\n" +
        //        " where a.billing_date_time >=\n" +
        //        "       to_date('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.billing_date_time <=\n" +
        //        "       to_date('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.performed_by = d.dept_dict_id(+)\n" +
        //        "   and d.acct_dept_id = c.id(+)\n" +
        //        "   and a.ordered_by = f.dept_dict_id(+)\n" +
        //        "   and f.acct_dept_id = e.id(+)\n" +
        //        "   and a.ward_code = h.dept_dict_id(+)\n" +
        //        "   and h.acct_dept_id = g.id(+)\n" +
        //        " group by a.item_code,\n" +
        //        "          a.item_name,\n" +
        //        "          a.class_on_reckoning,\n" +
        //        "          nvl(c.id, '*'),\n" +
        //        "          nvl(g.id, '*'),\n" +
        //        "          nvl(e.id, '*'),\n" +
        //        "          a.perform_doctor ,\n" +
        //        "          a.order_doctor\n" +
        //        "union all\n" +
        //        "select a.item_code,\n" +
        //        "       a.item_name,\n" +
        //        "       a.class_on_reckoning,\n" +
        //        "       nvl(c.id, '*'),\n" +
        //        "       a.performed_doctor,\n" +
        //        "       nvl(e.id, '*'),\n" +
        //        "       b.ordered_by_doctor,\n" +
        //        "       nvl(g.id, '*'),\n" +
        //        "       '0' outp_or_inp,\n" +
        //        "       sum(a.costs) costs\n" +
        //        "  from outpbill.outp_bill_items    a,\n" +
        //        "       outpbill.outp_order_desc    b,\n" +
        //        "       htca.acct_dept_dict         c,\n" +
        //        "       htca.acct_dept_vs_dept_dict d,\n" +
        //        "       htca.acct_dept_dict         e,\n" +
        //        "       htca.acct_dept_vs_dept_dict f,\n" +
        //        "       htca.acct_dept_dict         g,\n" +
        //        "       htca.acct_dept_vs_dept_dict h\n" +
        //        " where a.rcpt_no = b.rcpt_no\n" +
        //        "   and a.visit_date >=\n" +
        //        "       to_date('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.visit_date <=\n" +
        //        "       to_date('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n" +
        //        "   and a.performed_by = d.dept_dict_id(+)\n" +
        //        "   and d.acct_dept_id = c.id(+)\n" +
        //        "   and b.ordered_by_dept = f.dept_dict_id(+)\n" +
        //        "   and f.acct_dept_id = e.id(+)\n" +
        //        "   and a.ward_code = h.dept_dict_id(+)\n" +
        //        "   and h.acct_dept_id = g.id(+)\n" +
        //        " group by a.item_code,\n" +
        //        "          a.item_name,\n" +
        //        "          a.class_on_reckoning,\n" +
        //        "          nvl(c.id, '*'),\n" +
        //        "          a.performed_doctor,\n" +
        //        "          nvl(e.id, '*'),\n" +
        //        "          b.ordered_by_doctor,\n" +
        //        "          nvl(g.id, '*')" ;
        Query query = createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<CalcIncomeDetail> incomeDetails = new ArrayList<>() ;
        for(Object[] objects:resultList){
            CalcIncomeDetail calcIncomeDetail = new CalcIncomeDetail() ;
            calcIncomeDetail.setHospitalId(hospitalId);
            calcIncomeDetail.setIncomeItemName((String) objects[1]);
            calcIncomeDetail.setIncomeItemCode((String) objects[0]);
            calcIncomeDetail.setClassOnRecking((String) objects[2]);
            calcIncomeDetail.setPerformedBy((String)objects[3]);
            calcIncomeDetail.setPerformedDoctor((String) objects[4]);
            calcIncomeDetail.setOrderedBy((String)objects[5]);
            calcIncomeDetail.setOrderDoctor((String) objects[6]);
            calcIncomeDetail.setWardCode((String) objects[7]);
            calcIncomeDetail.setInpOrOutp(objects[8].toString());
            calcIncomeDetail.setTotalCost((BigDecimal) objects[9]);
            calcIncomeDetail.setYearMonth(yearMonth);
            merge(calcIncomeDetail) ;
            incomeDetails.add(calcIncomeDetail) ;
        }
        return incomeDetails;
    }

    private AcctDeptDict getAcctDeptDict(String deptCode) {

        String hql = "select dept from AcctDeptDict as dept ,AcctDeptVsDeptDict vs where dept.id = vs.acctDeptId " +
                "and vs.deptDictId = '"+deptCode +"'" ;
        TypedQuery<AcctDeptDict> query = createQuery(AcctDeptDict.class, hql, new ArrayList<Object>());
        List<AcctDeptDict> resultList = query.getResultList();
        if(resultList.size()>0){
            return resultList.get(0) ;
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
        String hql = "delete from CalcIncomeDetail as calc where calc.yearMonth = '"+yearMonth+"' and " +
                "calc.hospitalId='"+hospitalId+"'" ;
        int i = this.getEntityManager().createQuery(hql).executeUpdate();
        return i ;
    }


    public PageEntity<CalcIncomeDetail> findByPages(String hospitalId, String yearMonth, String page, String rows) {

        String hql = "from CalcIncomeDetail as calc where calc.yearMonth = '"+yearMonth+"' and " +
                "calc.hospitalId = '"+hospitalId+"' order by calc.id" ;
        Integer iRow = Integer.parseInt(rows) ;
        Integer iPage = Integer.parseInt(page) ;

        TypedQuery<CalcIncomeDetail> query = createQuery(CalcIncomeDetail.class, hql, new ArrayList<Object>());
        query.setFirstResult((iPage-1)*iRow) ;
        query.setMaxResults(iRow) ;
        PageEntity<CalcIncomeDetail> pageEntity = new PageEntity<>() ;

        pageEntity.setRows(query.getResultList());
        pageEntity.setTotal(this.getCalcIncomeDetailCount(hospitalId,yearMonth));
        return pageEntity ;
    }

    /**
     * 获取某一个月份的总数据
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    public Long getCalcIncomeDetailCount(String hospitalId,String yearMonth){
        String hql = "select count(*) from CalcIncomeDetail as calc where calc.yearMonth = '"+yearMonth+"' and " +
                "calc.hospitalId = '"+hospitalId+"'" ;
        return createQuery(Long.class,hql,new ArrayList<Object>()).getResultList().get(0) ;
    }

    /**
     * 保存设置的对象
     * @param calcIncomeDetails
     * @return
     */
    @Transactional
    public List<CalcIncomeDetail> saveCalc(List<CalcIncomeDetail> calcIncomeDetails) {
        for(CalcIncomeDetail detail:calcIncomeDetails){
            merge(detail) ;
        }
        return calcIncomeDetails ;
    }

    @Transactional
    public List<CalcIncomeDetail> devideCalcIncome(String hospitalId, String yearMonth) {
        List<CalcIncomeDetail> incomeDetails = getCalcIncomeDetail(hospitalId, yearMonth);
        List<IncomeItemDict> incomeItemDicts =incomeItemDictFacade.findByHospitalId(hospitalId) ;

        for(CalcIncomeDetail detail:incomeDetails){
            for(IncomeItemDict incomeItemDict:incomeItemDicts){
                String incomeItemCode = detail.getIncomeItemCode();
                if("".equals(incomeItemCode)||incomeItemCode==null){
                    continue;
                }

                BigDecimal totalCost = detail.getTotalCost();
                if(incomeItemCode.equals(incomeItemDict.getPriceItemCode())){
                    if("1".equals(detail.getInpOrOutp())){
                        //住院
                        //detail.setOrderIncome(totalCost * ());
                        double orderReate = Double.parseDouble(incomeItemDict.getInpOrderedBy()) /100;
                        double performReate = Double.parseDouble(incomeItemDict.getInpPerformedBy()) /100;
                        double wardReate = Double.parseDouble(incomeItemDict.getInpWardCode()) /100;
                        double orderIncome = new BigDecimal(totalCost.doubleValue() * orderReate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        double perormIncome = new BigDecimal(totalCost.doubleValue() * performReate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        double wardIncome = new BigDecimal(totalCost.doubleValue() * wardReate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        detail.setOrderIncome(orderIncome);
                        detail.setPerformIncome(perormIncome);
                        detail.setWardIncome(wardIncome);
                        merge(detail) ;
                        break;
                    }

                    if("0".equals(detail.getInpOrOutp())){
                        //门诊
                        //detail.setOrderIncome(totalCost * ());
                        double orderReate = Double.parseDouble(incomeItemDict.getOutpOrderedBy()) /100;
                        double performReate = Double.parseDouble(incomeItemDict.getOutpPerformedBy()) /100;
                        double wardReate = Double.parseDouble(incomeItemDict.getOutpWardCode()) /100;
                        double orderIncome = new BigDecimal(totalCost.doubleValue() * orderReate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        double perormIncome = new BigDecimal(totalCost.doubleValue() * performReate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        double wardIncome = new BigDecimal(totalCost.doubleValue() * wardReate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        detail.setOrderIncome(orderIncome);
                        detail.setPerformIncome(perormIncome);
                        detail.setWardIncome(wardIncome);
                        merge(detail) ;
                        break;
                    }

                }

            }
        }
        return incomeDetails;
    }


    private List<AcctReckItemClassDict> getAcctReckItemClass(String hospitalId) {

        String hql = "from AcctReckItemClassDict as reck where reck.hospitalId='"+hospitalId+"'" ;

        return createQuery(AcctReckItemClassDict.class,hql,new ArrayList<Object>()).getResultList();
    }

    private List<CalcIncomeDetail> getCalcIncomeDetail(String hospitalId, String yearMonth) {
        String hql = "from CalcIncomeDetail as de where de.hospitalId='"+hospitalId+"' and " +
                "de.yearMonth='"+yearMonth+"'" ;
        TypedQuery<CalcIncomeDetail> query = createQuery(CalcIncomeDetail.class, hql, new ArrayList<Object>());
        return query.getResultList();
    }
}
