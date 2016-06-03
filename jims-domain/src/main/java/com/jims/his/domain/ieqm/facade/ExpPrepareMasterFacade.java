package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpPrepareDetail;
import com.jims.his.domain.ieqm.entity.ExpPrepareMaster;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ExpPrepareMasterFacade extends BaseFacade {
    private EntityManager entityManager;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH;mm:ss");

    @Inject
    public ExpPrepareMasterFacade (EntityManager entityManager){
        this.entityManager=entityManager;
    }

    /**
     *备货方法
     * @param expId   产品Id
     * @param supplierId   供货商 Id
     * @param amount      备货数量
     * @return
     */
    @Transactional
    public List<ExpPrepareDetail> save(String expId, String supplierId, String amount,String staffName,double price,List<ExpPrepareDetail> list) {
        /**
         * 在 exp_prepare_master 表中写入数据
         */
        ExpPrepareMaster expPrepareMaster=new ExpPrepareMaster();
        expPrepareMaster.setExpId(expId);
        expPrepareMaster.setSupplierId(supplierId);
        expPrepareMaster.setPrepareCount(amount);
        expPrepareMaster.setPrepareDate(sdf.format(new Date()));
        expPrepareMaster.setOperator(staffName);
        expPrepareMaster.setPrice(price);
        expPrepareMaster=entityManager.merge(expPrepareMaster);
        /**
         * 在 exp_prepare_detail 表中写入数据
         */
        Integer amountInt=Integer.parseInt(amount);
        for(int i=0;i<amountInt;i++){
            String barCode=new Date().getTime()+i+"";
            ExpPrepareDetail expPrepareDetail=new ExpPrepareDetail();
            expPrepareDetail.setOperator(staffName);
            expPrepareDetail.setMasterId(expPrepareMaster.getId());
            expPrepareDetail.setExpBarCode(barCode);
            expPrepareDetail.setUseFlag("0");//0 未使用
            expPrepareDetail.setPrintFlag("0");//0 未打印
            expPrepareDetail=entityManager.merge(expPrepareDetail);
            if(list!=null&&!list.isEmpty()&&!list.contains(expPrepareDetail)){
                list.add(expPrepareDetail);
            }
            if(list.isEmpty()){
                list.add(expPrepareDetail);
            }
          }
        return list;
    }
}
