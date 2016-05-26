package com.jims.his.domain.oauth2.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.oauth2.entity.App;

/**
 * Created by heren on 2016/5/26.
 */
public class AppFacade extends BaseFacade {

    public App getApp(String clientId){
        return get(App.class,clientId) ;
    }
}
