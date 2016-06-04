package com.jims.his.domain.ieqm.facade;

import com.jims.his.common.BaseFacade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ExpPrepareDetailFacade extends BaseFacade {

    private EntityManager entityManager;

    @Inject
    public ExpPrepareDetailFacade (EntityManager entityManager){
        this.entityManager=entityManager;
    }
}

