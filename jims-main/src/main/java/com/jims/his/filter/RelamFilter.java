package com.jims.his.filter;

import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;
import com.jims.his.common.util.Base64;
import com.jims.his.domain.common.entity.HospitalDict;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.common.facade.StaffDictFacade;
import com.sun.xml.internal.ws.resources.HttpserverMessages;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限认真
 * Created by heren on 2015/11/2.
 */
@Singleton
public class RelamFilter implements Filter {


    private StaffDictFacade staffDictFacade ;

    @Inject
    public RelamFilter(StaffDictFacade staffDictFacade) {
        this.staffDictFacade = staffDictFacade;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request ;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response ;
        String authorization = httpServletRequest.getHeader("Authorization");

        if(authorization==null||"".equals(authorization)){
            httpServletResponse.setStatus(401);
            httpServletResponse.setHeader("WWW-authenticate", "Basic realm=api");
            return ;
        }
        String fromBase64 = Base64.getFromBase64(authorization);
        String[] split = fromBase64.split(":");
        String userName = split[0] ;
        String password =split[1] ;

        StaffDict byLogin = staffDictFacade.findByLogin(userName, password);

        if(byLogin !=null){
            chain.doFilter(request, response);
        }else{
            httpServletResponse.setStatus(403);
            return ;
        }
    }

    @Override
    public void destroy() {

    }
}
