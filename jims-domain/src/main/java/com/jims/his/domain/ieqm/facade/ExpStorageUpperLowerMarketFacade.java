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
        String sql = "SELECT distinct EXP_STORAGE_PROFILE.STORAGE,\n" +
                "       EXP_STORAGE_PROFILE.ID,\n" +
                "       EXP_STORAGE_PROFILE.LOCATION,\n" +
                "       EXP_STORAGE_PROFILE.SUPPLIER,\n" +
                "       EXP_STORAGE_PROFILE.EXP_CODE,\n" +
                "       EXP_DICT.EXP_NAME,\n" +
                "       EXP_STORAGE_PROFILE.EXP_SPEC,\n" +
                "       EXP_STORAGE_PROFILE.UNITS,\n" +
                "       EXP_STORAGE_PROFILE.AMOUNT_PER_PACKAGE,\n" +
                "       EXP_STORAGE_PROFILE.PACKAGE_UNITS,\n" +
                "       EXP_STORAGE_PROFILE.UPPER_LEVEL,\n" +
                "       EXP_STORAGE_PROFILE.LOW_LEVEL\n" +
                 "  FROM exp_dict, EXP_STORAGE_PROFILE\n" +
                " WHERE (exp_storage_profile.exp_code = exp_dict.exp_code)\n" +
                 "and EXP_STORAGE_PROFILE.STORAGE = '"+storageCode+"'" ;
        List<ExpStorageProfileVo> expStorageProfileVos = super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageProfileVo.class);
        for(ExpStorageProfileVo e:expStorageProfileVos){
            String sql2="select nvl(b.quantity,0)  from exp_export_master a,exp_export_detail b WHERE a.document_no=b.document_no  and " +
                    "a.storage='"+storageCode+"' and a.export_date >=\n" +
                    "                           to_date('"+startTime+"',\n" +
                    "                                   'yyyy-MM-dd HH24:MI:SS')\n" +
                    "                       AND export_date <\n" +
                    "                           to_date('"+stopTime+"',\n" +
                    "                                   'yyyy-MM-dd HH24:MI:SS') and " +
                    " a.hospital_id='"+hospitalId+"' and b.exp_code='"+e.getExpCode()+"' and b.package_spec='"+e.getExpSpec()+"' ";
            List<String> obj = super.createNativeQuery(sql2).getResultList();
           if(obj!=null&&!obj.isEmpty()){
               e.setStockQuantity(Integer.parseInt(obj.get(0).toString()));
           }else{
               e.setStockQuantity(0);
           }
         }
        return expStorageProfileVos;
    }
}
