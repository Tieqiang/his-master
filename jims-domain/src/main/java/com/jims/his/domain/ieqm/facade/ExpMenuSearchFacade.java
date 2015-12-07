package com.jims.his.domain.ieqm.facade;


import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.vo.ExpMenuSearchVo;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangbinbin on 2015/10/8.
 */
public class ExpMenuSearchFacade extends BaseFacade {

    private EntityManager entityManager ;

    @Inject
    public ExpMenuSearchFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 产品目录查询SQL语句
     * @param storageCode
     * @param hospitalId
     * @return
     */
    public List<ExpMenuSearchVo> findAll(String storageCode,String hospitalId){
        Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String sysTime=formatter.format(time);
        String sql = "select a.exp_name,a.exp_form,a.exp_indicator \n" +
                ",b.exp_code,b.exp_spec,b.firm_id,b.units,b.trade_price,b.retail_price,exp_quantity.quantity\n" +
                "from exp_dict a, exp_price_list b,  \n" +
                "         (SELECT exp_code, PACKAGE_SPEC, firm_id, sum(QUANTITY) quantity " +
                "         FROM exp_stock \n" +
                "\t\t\t   WHERE STORAGE = '"+storageCode+"' and \n" +
                "               HOSPITAL_ID='"+hospitalId+"'"+
                "\t\t\t   GROUP BY exp_code, PACKAGE_SPEC, firm_id) exp_quantity\n" +
                "where ( b.exp_code = a.exp_code  ) and  \n" +
                "      ( a.exp_spec = b.exp_spec ) and  \n" +
                "\t    ( b.exp_code = exp_quantity.exp_code(+) ) and \n" +
                "\t    ( b.exp_spec = exp_quantity.PACKAGE_SPEC(+) ) and\n" +
                "\t    ( b.firm_id = exp_quantity.firm_id(+) ) and\n" +
                "\t    ( start_date <to_date ( '"+sysTime +"' , 'yyyy-MM-dd HH24:MI:SS' ) ) AND \n" +
                "      ( stop_date >= to_date ( '"+sysTime +"' , 'yyyy-MM-dd HH24:MI:SS' )  OR stop_date IS NULL ) and\n" +
                "\t    ( a.storage_code = '"+storageCode+"' )";
        List<ExpMenuSearchVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpMenuSearchVo.class);
        return nativeQuery;
    }
}
