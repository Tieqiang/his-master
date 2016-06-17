package com.jims.his.service;


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


