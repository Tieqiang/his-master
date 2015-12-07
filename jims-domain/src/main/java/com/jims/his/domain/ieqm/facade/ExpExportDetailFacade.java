package com.jims.his.domain.ieqm.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpExportDetail;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/10/23.
 */
public class ExpExportDetailFacade extends BaseFacade {

    private EntityManager entityManager ;

    @Inject
    public ExpExportDetailFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 根据医院和出库单号查找所有的数据
     * @param hospitalId
     * @param documentNo
     * @return
     */
    public List<ExpExportDetail> getDetail(String hospitalId, String documentNo) {

        String hql = "from ExpExportDetail as detail where detail.documentNo = '"+documentNo+"' and detail.hospitalId='"+hospitalId+"'" ;
        Query query = entityManager.createQuery(hql) ;
        return query.getResultList();
    }

    /**
     * 获取某一个
     * @param hospitalId
     * @param documentNo
     * @param itemNo
     * @return
     */
    public ExpExportDetail getDetail(String hospitalId, String documentNo, Short itemNo) {
        String hql = "from ExpExportDetail as detail where detail.documentNo = '"+documentNo+"' and detail.hospitalId='"+hospitalId+"'" +
                " and detail.itemNo = "+itemNo ;
        Query query = this.entityManager.createQuery(hql);
        List resultList = query.getResultList();
        if(resultList.size()>0){
            return (ExpExportDetail) resultList.get(0);
        }

        return null;
    }
}
