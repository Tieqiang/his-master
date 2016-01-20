package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AcctProfitChangeDict;
import com.jims.his.domain.htca.entity.AcctProfitChangeRecord;
import com.jims.his.domain.htca.entity.IncomeTypeDict;
import com.jims.his.domain.htca.facade.IncomeTypeDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/10/28.
 */
@Produces("application/json")
@Path("acct-proft-chage-record")
public class AcctProfitChangeRecordService {

    private IncomeTypeDictFacade incomeTypeDictFacade ;

    @Inject
    public AcctProfitChangeRecordService(IncomeTypeDictFacade incomeTypeDictFacade) {
        this.incomeTypeDictFacade = incomeTypeDictFacade;
    }

    @GET
    @Path("list")
    public List<AcctProfitChangeRecord> listByHospital(){

        return incomeTypeDictFacade.findProftByHospitalId() ;

    }

    @POST
    @Path("save-update")
    public Response saveOrUpdate(BeanChangeVo<AcctProfitChangeRecord> acctProftChageRecordBeanChangeVo){
        try {
            incomeTypeDictFacade.saveacctProftChageRecordBeanChangeVo(acctProftChageRecordBeanChangeVo);
            return Response.status(Response.Status.OK).entity(acctProftChageRecordBeanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @GET
    @Path("change-record-search")
    public List<AcctProfitChangeRecord> listAcctProfitChangeRecord(@QueryParam("yearMonth") String yearMonth, @QueryParam("incomeOrCost") String incomeOrCost,
                                                   @QueryParam("acctDeptId") String acctDeptId) {
        String hql = "from AcctProfitChangeRecord as a where a.yearMonth = '" + yearMonth + "'and a.acctDeptId='"+acctDeptId+"' and a.incomeOrCost = '"+incomeOrCost+"'";
        List<AcctProfitChangeRecord> resultList = incomeTypeDictFacade.createQuery(AcctProfitChangeRecord.class, hql, new ArrayList<Object>()).getResultList();
        return resultList;


    }

}
