package com.jims.his.domain.ieqm.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpPrepareDetail;
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
        String sql="select masterId from ExpPrepareDerail where expBarCode='"+barCode+"'";
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
}

