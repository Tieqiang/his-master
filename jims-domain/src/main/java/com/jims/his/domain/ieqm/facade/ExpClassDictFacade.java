package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpClassDict;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by wangjing on 2015/9/23.
 */
public class ExpClassDictFacade extends BaseFacade {



    private EntityManager entityManager ;

    @Inject
    public ExpClassDictFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
