package com.jims.his.domain.ieqm.facade;


import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.vo.ExpMenuSearchVo;
import com.jims.his.domain.ieqm.vo.ExpPriceSearchVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangbinbin on 2015/10/8.
 */
public class ExpPriceSearchFacade extends BaseFacade {

    private EntityManager entityManager ;

    @Inject
    public ExpPriceSearchFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 产品价格查询SQL语句
     * @param expCode
     * @return
     */
    public List<ExpPriceSearchVo> findAll(String expCode ,String hospitalId ){
        String sql = "SELECT \n" +
                "      DISTINCT exp_price_list.exp_code,   Nvl(exp_name,exp_price_list.exp_code) exp_name,   exp_spec,   firm_id,units,    trade_price,    retail_price,    start_date,\n" +
                "\t\tstop_date,amount_per_package,min_spec,min_units,memos ,material_code \n" +
                "\n" +
                "    FROM exp_price_list,exp_name_dict   \n" +
                "\n" +
                "   WHERE exp_price_list.exp_code = exp_name_dict.exp_code AND" +
                "   std_indicator = 1 AND" +
                " exp_price_list.exp_code = '"+expCode+"' AND"+
                " exp_price_list.hospital_id = '"+hospitalId+"'";

        List<ExpPriceSearchVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceSearchVo.class);
        System.out.println("nativeQuery");
        return nativeQuery;
    }

    /**
     * 产品调价情况统计
     * @param startTime
     * @param stopTime
     * @return
     */
    public List<ExpPriceSearchVo> findCountAll(String startTime, String stopTime,String hospitalId) {
        String sql ="SELECT\n" +
                "        Nvl(exp_name,exp_price_list.exp_code) exp_name,exp_price_list.exp_spec,exp_price_list.units,firm_id,trade_price,retail_price,start_date,stop_date,memos,exp_price_list.exp_code   \n" +
                "    FROM\n" +
                "        exp_price_list,\n" +
                "        exp_name_dict  \n" +
                "    WHERE\n" +
                "        exp_price_list.exp_code = exp_name_dict.exp_code(+) \n" +
                "        AND    std_indicator = 1 \n" +
                "        AND    start_date >= TO_DATE('"+ startTime+"', 'YYYY-MM-DD HH24:MI:SS ') \n" +
                "        AND    start_date <= TO_DATE('"+ stopTime+"', 'YYYY-MM-DD HH24:MI:SS ') \n"+
                "        AND    exp_price_list.hospital_id = '"+hospitalId+"'";
        List<ExpPriceSearchVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpPriceSearchVo.class);
        System.out.println("nativeQuery");
        return nativeQuery;
    }
}
