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
        String sql = "SELECT EXP_PRICE_LIST.EXP_CODE,   \n" +
                "         EXP_DICT.EXP_NAME,   \n" +
                "         EXP_PRICE_LIST.EXP_SPEC,   \n" +
                "         EXP_PRICE_LIST.FIRM_ID,   \n" +
                "         EXP_PRICE_LIST.UNITS,   \n" +
                "         EXP_DICT.EXP_FORM,   \n" +
                "         EXP_PRICE_LIST.TRADE_PRICE,   \n" +
                "         EXP_PRICE_LIST.RETAIL_PRICE,\n" +
                "      EXP_DICT.EXP_INDICATOR,\n" +
                "      exp_quantity.quantity\n" +
                "    FROM EXP_DICT,   \n" +
                "         EXP_PRICE_LIST,(SELECT exp_code, PACKAGE_SPEC, firm_id, sum(QUANTITY) quantity FROM exp_stock \n" +
                "         WHERE STORAGE = '" + storageCode + "' and \n" +
                "               HOSPITAL_ID='"+hospitalId+"'"+
                "      GROUP BY exp_code, PACKAGE_SPEC, firm_id) exp_quantity\n" +
                "   WHERE  EXP_PRICE_LIST.EXP_CODE = EXP_DICT.EXP_CODE  and  \n" +
                "          EXP_DICT.EXP_SPEC = EXP_PRICE_LIST.EXP_SPEC  and  \n" +
                "       EXP_PRICE_LIST.EXP_CODE = exp_quantity.exp_code(+)  and \n" +
                "       EXP_PRICE_LIST.EXP_SPEC = exp_quantity.PACKAGE_SPEC(+)  and\n" +
                "       EXP_PRICE_LIST.FIRM_ID = exp_quantity.firm_id(+)  and\n" +
                "       start_date < SYSDATE  AND \n" +
                "         ( stop_date >= SYSDATE OR stop_date IS NULL ) and\n" +
                "       EXP_DICT.storage_code = '"+storageCode + "' and \n" +
                "       EXP_PRICE_LIST.HOSPITAL_ID='" + hospitalId + "'";
        List<ExpMenuSearchVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpMenuSearchVo.class);
        return nativeQuery;
    }
}
