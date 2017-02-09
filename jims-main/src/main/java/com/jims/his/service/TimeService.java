package com.jims.his.service;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.secnod.shiro.jaxrs.Auth;
import sun.security.util.Password;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by heren on 2015/8/24.
 */
@Path("time")
@Produces(MediaType.APPLICATION_JSON)
public class TimeService {

    private List<DemoUser>  demoUsers = new ArrayList<>() ;
    @GET
    public Date getDate(){
        return new Date() ;
    }

    public TimeService() {

        for(int i = 0;i<10;i++){
            demoUsers.add(new DemoUser("username", "passord")) ;
        }
    }

    @GET
    @Path("get-req")
    @Produces("application/xml")
    @Consumes("application/xml")
    public String reqInfo(String req){
        return req ;
    }

    @GET
    @Path("list")
    public List<DemoUser> listDemoUsers(){
        return this.demoUsers ;
    }


    @GET
    @Path("auth")
    public List<DemoUser> testString(@Auth Subject user){
        DemoUser demoUser = new DemoUser(user.getPrincipal().toString(),user.getSession().getHost()) ;
        UsernamePasswordToken token   = new UsernamePasswordToken("zhao","123") ;
        try {
            user.login(token);
            System.out.println("认证成功");
        } catch (AuthenticationException e) {
            System.out.println("认证失败");
            e.printStackTrace();
        }
        this.demoUsers.add(demoUser);
        return this.demoUsers;
    }

    @XmlRootElement
    class DemoUser{
        private String userName ;
        private String password ;

        public DemoUser(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}


