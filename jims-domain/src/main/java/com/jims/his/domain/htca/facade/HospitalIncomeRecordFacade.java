package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctDeptDict;
import com.jims.his.domain.htca.entity.CalcIncomeDetail;
import com.jims.his.domain.htca.entity.HospitalIncomeRecord;
import com.jims.his.domain.htca.vo.HospitalIncomeRecordVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/10/29.
 */
public class HospitalIncomeRecordFacade extends BaseFacade {


    /**
     * 查询医院但阅读的收入情况
     *
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    public List<HospitalIncomeRecordVo> findByHospitalId(String hospitalId, String yearMonth) {
        String sql = "select b.dept_name,\n" +
                "       b.dept_code,\n" +
                "       a.income_year_month,\n" +
                "       a.income_amount,\n" +
                "       a.income_item_code,\n" +
                "       a.income_item_name,\n" +
                "       a.id,\n" +
                "       a.hospital_id\n" +
                "  from htca.hospital_income_record a, htca.acct_dept_dict b\n" +
                " where a.dept_id = b.id and a.income_year_month = '"+yearMonth+"'\n" +
                " and a.hospital_id = '"+hospitalId+"' and a.income_state='0'" +
                " and a.hospital_id = b.hospital_id" ;
        List<HospitalIncomeRecordVo> hospitalIncomeRecordVos = createNativeQuery(sql, new ArrayList<Object>(), HospitalIncomeRecordVo.class);
        return hospitalIncomeRecordVos;
    }

    /**
     * 计算某一个月的科室收入
     * @param hospitalId 医院ID
     * @param yearMonth 核算年月
     * @return
     */
    public List<HospitalIncomeRecord> calculateHospitalIncomeRecord(String hospitalId, String yearMonth) {

        String sql = " select '' id,\n" +
                "        year_month income_year_month,\n" +
                "        b.id dept_id,\n" +
                "        sum(total_cost) income_amount,\n" +
                "        income_item_name,\n" +
                "        class_on_recking income_item_code,\n" +
                "        a.hospital_id\n" +
                "   from htca.calc_income_detail a, htca.acct_dept_dict b\n" +
                "  where a.performed_by = b.dept_code\n" +
                "    and b.hospital_id = '"+hospitalId+"'\n" +
                "    and a.hospital_id = b.hospital_id\n" +
                "    and a.year_month = '"+yearMonth+"'\n" +
                "  group by b.id,\n" +
                "           income_item_name,\n" +
                "           class_on_recking,\n" +
                "           year_month,\n" +
                "           a.hospital_id" ;
        System.out.println(sql);

        List<HospitalIncomeRecordVo> incomeRecordVos = createNativeQuery(sql, new ArrayList<Object>(), HospitalIncomeRecordVo.class);
        List<HospitalIncomeRecord> incomeRecords = new ArrayList<>() ;
        for(HospitalIncomeRecordVo vo:incomeRecordVos){
            String deptId = vo.getDeptId();
            AcctDeptDict dict = get(AcctDeptDict.class, deptId);
            HospitalIncomeRecord record= new HospitalIncomeRecord() ;
            record.setId(vo.getId());
            record.setHospitalId(vo.getHospitalId());
            record.setAcctDeptDict(dict);
            record.setIncomeAmount(vo.getIncomeAmount());
            record.setIncomeItemCode(vo.getIncomeItemCode());
            record.setIncomeItemName(vo.getIncomeItemName());
            record.setIncomeState("-1");
            record.setIncomeYearMonth(yearMonth);
            incomeRecords.add(record) ;
        }
        return incomeRecords ;
    }

    /**
     * 从outp_bill_items和inp_bill_items中计算收费明细
     * 费用归集功能
     * @param hospitalId
     * @param yearMonth
     */
    @Transactional
    public void calculateIncomeDetail(String hospitalId,String yearMonth){
        String sql="select '' id,\n" +
                "       a.hospital_id,\n" +
                "       a.ordered_by,\n" +
                "       c.dept_name ordered_by_dept_name,\n" +
                "       a.performed_by,\n" +
                "       d.dept_name performed_by_dept_name,\n" +
                "       sum(a.costs),\n" +
                "       a.class_on_reckoning,\n" +
                "       b.income_item_name,\n" +
                "       a.perform_doctor,\n" +
                "       a.order_doctor,\n" +
                "       a.ward_code\n" +
                "  from htca.inp_bill_detail  a,\n" +
                "       htca.income_item_dict b,\n" +
                "       htca.acct_dept_dict   c,\n" +
                "       htca.acct_dept_dict   d\n" +
                " where a.class_on_reckoning = b.income_item_code(+)\n" +
                "   and a.ordered_by = c.dept_code\n" +
                "   and a.performed_by = d.dept_code having sum(a.costs) <> 0\n" +
                "   and a.hospital_id = '"+hospitalId+"'\n" +
                "   and to_char(a.billing_date_time,'YYYY-MM')='"+yearMonth+"'\n" +
                " group by a.ordered_by,\n" +
                "          a.performed_by,\n" +
                "          a.class_on_reckoning,\n" +
                "          b.income_item_name,\n" +
                "          c.dept_name,\n" +
                "          d.dept_name,\n" +
                "          a.perform_doctor,\n" +
                "          a.order_doctor,\n" +
                "          a.ward_code,\n" +
                "          a.hospital_id\n" +
                "union\n" +
                "select '' id,\n" +
                "       a.hospital_id,\n" +
                "       a.order_dept,\n" +
                "       c.dept_name ordered_by_dept_name,\n" +
                "       a.performed_by,\n" +
                "       d.dept_name performed_by_dept_name,\n" +
                "       sum(a.costs),\n" +
                "       a.class_on_reckoning,\n" +
                "       b.income_item_name,\n" +
                "       a.order_doctor,\n" +
                "       a.performed_doctor,\n" +
                "       a.ward_code\n" +
                "  from htca.outp_bill_items  a,\n" +
                "       htca.income_item_dict b,\n" +
                "       htca.acct_dept_dict   c,\n" +
                "       htca.acct_dept_dict   d\n" +
                " where a.class_on_reckoning = b.income_item_code(+)\n" +
                "   and a.order_dept = c.dept_code\n" +
                "   and a.performed_by = d.dept_code having sum(a.costs) <> 0\n" +
                "   and a.hospital_id = '"+hospitalId+"'\n" +
                "   and to_char(a.billing_date_time,'YYYY-MM')='"+yearMonth+"'\n" +
                " group by a.order_dept,\n" +
                "          a.performed_by,\n" +
                "          a.class_on_reckoning,\n" +
                "          b.income_item_name,\n" +
                "          c.dept_name,\n" +
                "          d.dept_name,\n" +
                "          a.order_doctor,\n" +
                "          a.performed_doctor,\n" +
                "          a.ward_code,\n" +
                "          a.hospital_id" ;

        List<CalcIncomeDetail> nativeQuery = createNativeQuery(sql, new ArrayList<Object>(), CalcIncomeDetail.class);
        List<CalcIncomeDetail> calcIncomeDetail = findCalcIncomeDetail(hospitalId, yearMonth);
        List<String> ids = new ArrayList<>() ;

        for(CalcIncomeDetail detail:calcIncomeDetail){
            ids.add(detail.getId()) ;
        }
        if(ids.size()>0){
            removeByStringIds(CalcIncomeDetail.class,ids);
        }

        for(CalcIncomeDetail detail:nativeQuery){
            merge(detail) ;
        }
    }


    /***
     * 获取某一个月份的计算明细
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    public List<CalcIncomeDetail> findCalcIncomeDetail(String hospitalId,String yearMonth){
        String hql = "from CalcIncomeDetail as d where d.hospitalId='"+hospitalId+"' and a.yearMonth='"+yearMonth+"'" ;
        return createQuery(CalcIncomeDetail.class,hql,new ArrayList<Object>()).getResultList() ;
    }

}
