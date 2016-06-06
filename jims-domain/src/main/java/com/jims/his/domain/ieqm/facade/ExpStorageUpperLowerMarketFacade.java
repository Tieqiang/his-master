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
        String sql = "SELECT EXP_STORAGE_PROFILE.STORAGE,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.ID,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.LOCATION,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.SUPPLIER,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.EXP_CODE,\n" +
                "\t\t\t\tEXP_DICT.EXP_NAME,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.EXP_SPEC,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.UNITS,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.AMOUNT_PER_PACKAGE,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.PACKAGE_UNITS,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.UPPER_LEVEL,\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.LOW_LEVEL,\n" +
                "\t\t\t\t(SELECT sum(quantity * EXP_PRICE_LIST.AMOUNT_PER_PACKAGE) stockQuantity\n" +
                "\t\t\t\t\t FROM exp_export_master, exp_export_detail, exp_price_list\n" +
                "\t\t\t\t\tWHERE exp_export_detail.document_no =\n" +
                "\t\t\t\t\t\t\t\texp_export_master.document_no\n" +
                "\t\t\t\t\t\tAND exp_export_detail.exp_code = exp_price_list.exp_code\n" +
                "\t\t\t\t\t\tAND exp_export_detail.PACKAGE_SPEC = exp_price_list.exp_spec\n" +
                "\t\t\t\t\t\tAND exp_export_master.storage = '"+storageCode+"'\n" +
                "\t\t\t\t\t\tAND exp_export_detail.exp_code = exp_storage_profile.exp_code\n" +
                "\t\t\t\t\t\tAND exp_export_detail.package_spec =\n" +
                "\t\t\t\t\t\t\t\texp_storage_profile.exp_spec\n" +
                "\t\t\t\t\t\tAND export_date >=\n" +
                "\t\t\t\t\t\t\t\tto_date('"+startTime+"', 'yyyy-MM-dd HH24:MI:SS')\n" +
                "\t\t\t\t\t\tAND export_date <\n" +
                "\t\t\t\t\t\t\t\tto_date('"+stopTime+"', 'yyyy-MM-dd HH24:MI:SS')\n" +
                "\t\t\t\t\t\tAND exp_export_master.hospital_id =\n" +
                "\t\t\t\t\t\t\t\t'"+hospitalId+"') /\n" +
                "\t\t\t\tEXP_STORAGE_PROFILE.AMOUNT_PER_PACKAGE stock_quantity\n" +
                "\t FROM EXP_price_list, exp_dict, EXP_STORAGE_PROFILE\n" +
                "\tWHERE (exp_storage_profile.exp_code = exp_price_list.exp_code)\n" +
                "\t\tand (exp_dict.Exp_Code = Exp_Price_List.exp_code)\n" +
                "\t\tand (exp_storage_profile.exp_spec = exp_price_list.exp_spec)\n" +
                "\t\tand EXP_STORAGE_PROFILE.STORAGE = '"+storageCode+"'" ;

        List<ExpStorageProfileVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageProfileVo.class);
        return nativeQuery;
    }
}
