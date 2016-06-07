package com.jims.his.domain.ieqm.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpPrepareDetail;
import com.jims.his.domain.ieqm.vo.ExpPrepareVo;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;
import com.jims.his.domain.ieqm.vo.ExpPrepareVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ExpPrepareDetailFacade extends BaseFacade {

    private EntityManager entityManager;

    @Inject
    public ExpPrepareDetailFacade (EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public List<ExpPrepareVo>findByBarCode(String barCode){
        String sql="select a.use_flag,a.master_id , \n"+
                "b.exp_id,b.prepare_date,b.prepare_person_name,b.phone,b.price,  \n"+
                "c.exp_name,c.exp_spec,c.units,  \n"+
                "d.supplier  \n"+
                "from exp_prepare_detail a,exp_prepare_master b,exp_dict c ,exp_supplier_catalog d  \n"+
                "where a.master_id=b.id and b.exp_id=c.exp_code and b.supplier_id=d.supplier_id \n"+
                "and a.exp_bar_code='"+barCode+"'";
        List<ExpPrepareVo> list=super.createNativeQuery(sql,new ArrayList<Object>(),ExpPrepareVo.class);
        if(list.size()==0){
            return null;
        }else{
            for(int i=0;i<list.size();i++){
                String a=list.get(i).getUseFlag();
                if(a.equals("0")){
                    list.get(i).setUseFlag("未使用");
                }else{
                    list.get(i).setUseFlag("已使用");
                }
            }
            return list;
        }
    }
     /**
     * chenxy
     * findMasterIdByBarCode
     * @param barCode
     * @return
     */
    public String findByExpBarCode(String barCode) {
        String sql="select masterId from ExpPrepareDetail where expBarCode='"+barCode+"'";
        List<String> list=entityManager.createQuery(sql).getResultList();
        if(list!=null&&!list.isEmpty())
            return list.get(0);
            return null;
     }

    /**
     *
     * @param barCode
     * @return
     */
    public ExpPrepareDetail findByCode(String barCode) {
          return (ExpPrepareDetail)entityManager.createQuery("from ExpPrepareDetail where expBarCode='"+barCode+"'").getSingleResult();
    }

    /**
     *
     * @param dept
     * @param supplerId
     * @return
     */
    public List<ExpPrepareVo> list(String dept, String supplerId) {
        String sql = "select d.exp_name,\n" +
                "       e.exp_spec,\n" +
                "       c.exp_bar_code,\n" +
                "       c.use_flag,\n" +
                "       c.use_date,\n" +
                "       c.use_patient_id,\n" +
                "       c.user_dept,\n" +
                "       c.operator\n" +
                "  from exp_prepare_master   a,\n" +
                "       exp_supplier_catalog b,\n" +
                "       exp_prepare_detail   c,\n" +
                "       exp_dict             d,\n" +
                "       exp_price_list       e\n" +
                " where a.supplier_id = b.id\n" +
                "   and c.master_id = a.id\n" +
                "   and a.exp_id = E.id\n" +
                "   and d.exp_code = e.exp_code\n" +
                "   and d.exp_spec = e.min_spec\n" +
                "   and b.id='"+supplerId+"'\n" +
                "   and e.exp_code='"+dept+"'\n";
        List<ExpPrepareVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpPrepareVo.class);
        return nativeQuery;
    }

    /**
     *
     * @param inputCode
     * @return
     */
    public List<ExpNameCaVo> listExpPrepareDetail(String inputCode) {
        String sql = "select distinct a.exp_code,\n" +
                "       a.exp_name,\n" +
                "       a.input_code\n" +
                "  from EXP_NAME_DICT a,exp_price_list b \n" +
                "  where upper(a.input_code) like upper('" + inputCode + "%')" +
                "   or(a.exp_code) like '"+inputCode+"%'" +
                "  and a.exp_code = b.exp_code and b.start_date <= sysdate\n" +
                "   AND (b.stop_date IS NULL OR\n" +
                "       b.stop_date > sysdate) ";
        List<ExpNameCaVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpNameCaVo.class);
        return nativeQuery;
    }
}

