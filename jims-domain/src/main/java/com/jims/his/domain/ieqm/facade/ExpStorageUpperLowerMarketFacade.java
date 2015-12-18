package com.jims.his.domain.ieqm.facade;


import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.vo.ExpPriceSearchVo;
import com.jims.his.domain.ieqm.vo.ExpStorageProfileVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangbinbin on 2015/10/8.
 */
public class ExpStorageUpperLowerMarketFacade extends BaseFacade {

    private EntityManager entityManager ;

    @Inject
    public ExpStorageUpperLowerMarketFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 根据消耗量定义库存量
     * @param storageCode
     * @param startTime
     * @param stopTime
     * @return
     */
    public List<ExpStorageProfileVo> findAll(String storageCode,String startTime,String stopTime,String hospitalId){
        String sql = "SELECT EXP_STORAGE_PROFILE.STORAGE ,   \n" +
                "         EXP_STORAGE_PROFILE.ID,   \n" +
                "         EXP_STORAGE_PROFILE.LOCATION,   \n" +
                "         EXP_STORAGE_PROFILE.SUPPLIER,   \n" +
                "         EXP_STORAGE_PROFILE.EXP_CODE,   \n" +
                "         EXP_DICT.EXP_NAME,  \n" +
                "         EXP_STORAGE_PROFILE.EXP_SPEC,   \n" +
                "         EXP_STORAGE_PROFILE.UNITS,   \n" +
                "         EXP_STORAGE_PROFILE.AMOUNT_PER_PACKAGE,   \n" +
                "         EXP_STORAGE_PROFILE.PACKAGE_UNITS,   \n" +
                "         EXP_STORAGE_PROFILE.UPPER_LEVEL,   \n" +
                "         EXP_STORAGE_PROFILE.LOW_LEVEL,\n" +
                "          (SELECT sum ( quantity *EXP_PRICE_LIST.AMOUNT_PER_PACKAGE ) stockQuantity\n" +
                "          FROM exp_export_master , exp_export_detail , exp_price_list \n" +
                "          WHERE exp_export_detail.document_no =exp_export_master.document_no \n" +
                "                AND exp_export_detail.exp_code =exp_price_list.exp_code \n" +
                "                AND exp_export_detail.PACKAGE_SPEC =exp_price_list.exp_spec \n" +
                "                AND exp_export_master.storage ='"+storageCode+"' \n" +
                "                AND exp_export_detail.exp_code =exp_storage_profile.exp_code \n" +
                "                AND exp_export_detail.exp_spec =exp_storage_profile.exp_spec \n" +
                "                AND export_date >= to_date ( '"+startTime+"' , 'yyyy-MM-dd HH24:MI:SS' ) \n" +
                "                AND export_date < to_date ( '"+stopTime+"' , 'yyyy-MM-dd HH24:MI:SS' ) \n " +
                "                AND exp_export_master.hospital_id='"+hospitalId+"' )  stock_quantity     \n" +

                "    FROM EXP_DICT,   \n" +
                "         EXP_STORAGE_PROFILE  \n" +
                "     WHERE ( exp_storage_profile.exp_code = exp_dict.exp_code ) and  \n" +
                "         ( exp_storage_profile.exp_spec = exp_dict.exp_spec ) and \n" +
                "\t\t\tEXP_STORAGE_PROFILE.STORAGE = '"+storageCode+"'  " ;

        List<ExpStorageProfileVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageProfileVo.class);
        return nativeQuery;
    }
}
