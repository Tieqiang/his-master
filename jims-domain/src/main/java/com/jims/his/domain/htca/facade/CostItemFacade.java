package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.CostItemClassDict;
import com.jims.his.domain.htca.entity.CostItemDict;

/**
 * Created by heren on 2015/11/24.
 */
public class CostItemFacade extends BaseFacade {


    @Transactional
    public void saveCostItemClass(CostItemClassDict costItemClassDict) {
        merge(costItemClassDict) ;
    }

    @Transactional
    public void delClassItem(String id) {
        CostItemClassDict costItemClassDict = get(CostItemClassDict.class, id);
        remove(costItemClassDict);
    }

    @Transactional
    public void saveCostItem(CostItemDict costItemDict) {
        merge(costItemDict) ;
    }

    @Transactional
    public CostItemDict deleteCostItemById(String id) {
        CostItemDict dict = get(CostItemDict.class, id);
        remove(dict);
        return dict;
    }
}
