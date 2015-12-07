package com.jims.his.service;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by heren on 2015/8/24.
 */
@Path("time")
@Produces(MediaType.APPLICATION_JSON)
public class TimeService {

    @GET
    public Date getDate(){
        return new Date() ;
    }




    @GET
    @Path("get-req")
    @Produces("application/xml")
    @Consumes("application/xml")
    public String reqInfo(String req){
        return req ;
    }




}
