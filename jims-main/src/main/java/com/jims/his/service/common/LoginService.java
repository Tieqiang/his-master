package com.jims.his.service.common;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.common.util.*;
import com.jims.his.domain.common.entity.*;
import com.jims.his.domain.common.facade.*;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.common.vo.Config;
import com.jims.his.domain.common.vo.ErrorMessager;
import com.jims.his.domain.htca.entity.AcctDeptVsDeptDict;
import com.jims.his.domain.htca.facade.AcctDeptDictFacade;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.facade.ExpStorageDeptFacade;
import org.eclipse.jetty.http.HttpStatus;


import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by heren on 2015/9/22.
 */
@Path("login")
@Produces("application/json")
public class LoginService {
    private ModuleDictFacade moduleDictFacade;
    private StaffDictFacade staffDictFacade ;
    private RoleDictFacade roleDictFacade ;
    private ExpStorageDeptFacade expStorageDeptFacade;
    private HttpServletResponse resp;
    private HttpServletRequest request;
    private AcctDeptDictFacade acctDeptDictFacade ;
    private ReportDictFacade reportDictFacade;
    private LocalProgramSettingFacade localProgramSettingFacade ;
    private KeyDictFacade keyDictFacade ;



    @Inject
    public LoginService(HttpServletRequest request, HttpServletResponse resp, ModuleDictFacade moduleDictFacade, StaffDictFacade staffDictFacade, RoleDictFacade roleDictFacade, ExpStorageDeptFacade expStorageDeptFacade, AcctDeptDictFacade acctDeptDictFacade, ReportDictFacade reportDictFacade, LocalProgramSettingFacade localProgramSettingFacade, KeyDictFacade keyDictFacade) {
        this.moduleDictFacade = moduleDictFacade;
        this.staffDictFacade = staffDictFacade;
        this.roleDictFacade = roleDictFacade;
        this.expStorageDeptFacade = expStorageDeptFacade;
        this.resp = resp;
        this.request = request;
        this.acctDeptDictFacade = acctDeptDictFacade;
        this.reportDictFacade = reportDictFacade;
        this.localProgramSettingFacade = localProgramSettingFacade;
        this.keyDictFacade = keyDictFacade;
    }


    @Path("sso")
    @GET
    public void ssoLogin(@QueryParam("username")String userName ,@QueryParam("password")String password,
                         @QueryParam("moduleId")String moduleId,@QueryParam("hospitalId")String hospitalId) throws Exception {
        if("".equals(userName)||"".equals(password)||"".equals(moduleId)||"".equals(hospitalId)){

            resp.sendRedirect("/login1.html?error=参数不允许为空");
        }else{
            if(userName.length()<6){
                int i = 6-userName.length() ;
                for(int j=0;j<i;j++){
                    userName = "0"+userName ;
                }
            }
            if(password.length()<6){
                int i = 6-password.length() ;
                for(int j=0;j<i;j++){
                    password = "0"+password ;
                }
            }

            Response response = this.login(userName, password) ;
            Object object = response.getEntity() ;
            if(object instanceof StaffDict){

                Config config = new Config() ;
                setAccessToken();
                config.setHospitalId(hospitalId);
                config.setModuleId(moduleId);
                config.setPassword(password);
                addLoginInfo(config);
                resp.sendRedirect("/index.html");
            }else{
                resp.sendRedirect("/login1.html");
            }
        }


    }

    /**
     * 根据用户名密码医院查找用户
     * @param loginName
     * @param password
     * @param hospitalId
     * @return
     */
    @POST
    @Path("login")
    public StaffDict loginStaffDict(@FormParam("loginName") String loginName, @FormParam("password") String password, @FormParam("validateCode") String validateCode,@FormParam("hospitalId")String hospitalId){
        HttpSession session = request.getSession();
        StaffDict staff = new StaffDict();
        return staff;
    }

    @POST
    @Path("check-login")
    public Response login(@QueryParam("loginName") String loginName, @QueryParam("password") String password){
        try{
            StaffDict staffDict= staffDictFacade.findByLoginName(loginName) ;
            HttpSession session = request.getSession() ;

            if(staffDict==null){
                ErrorMessager errorMessager = new ErrorMessager("错误的用户名", "系统登录");
                return Response.status(Response.Status.OK).entity(errorMessager).build() ;
            }
            String denPassword = EnscriptAndDenScript.denscriptFromHis(staffDict.getPassword()) ;
            if(!denPassword.equals(password)){
                ErrorMessager errorMessager = new ErrorMessager("错误的密码", "系统登录");
                return Response.status(Response.Status.OK).entity(errorMessager).build() ;
            }else{
                setAccessToken();
                session.setAttribute("loginName",staffDict.getLoginName());
                session.setAttribute("staffName", staffDict.getName());
                session.setAttribute("loginId",staffDict.getId());
                session.setAttribute("deptId",staffDict.getDeptDict().getId());
                session.setAttribute("password",password);
                return Response.status(Response.Status.OK).entity(staffDict).build() ;
            }
        }catch(Exception e){
            ErrorException errorException= new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build() ;
        }
    }
    /**
     * 根据staffId查询staff-vs-role表,查出用户的role-dict,再根据role查role-vs-menu表查出用户的menu集合
     * @param staffId
     * @return
     */
    public List<MenuDict> listMenuByStaffRole(String staffId){
        List<MenuDict> result = new ArrayList<>();
        //根据staffId查找出所有的staffVsRole
        List<StaffVsRole> staffRoleList = staffDictFacade.listRolesByStaff(staffId);
        if(null != staffRoleList && staffRoleList.size() > 0){
            Iterator<StaffVsRole> staffRoleListIte = staffRoleList.iterator();
            while(staffRoleListIte.hasNext()){
                StaffVsRole staffRole = staffRoleListIte.next();
                //根据roleId取出所有的RoleVsMenu
                List<RoleVsMenu> roleMenuList= roleDictFacade.listMenusByRole(staffRole.getRoleDict().getId());
                if(null != roleMenuList && roleMenuList.size() > 0){
                    Iterator<RoleVsMenu> roleMenuListIte = roleMenuList.iterator();
                    while (roleMenuListIte.hasNext()) {
                        RoleVsMenu roleMenu = roleMenuListIte.next();
                        //把得到的menu放到返回集合
                        result.add(roleMenu.getMenuDict());
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据模块查modul-vs-menu选择模块对应的menu集合
     * @param moduleId
     * @return
     */
    public List<MenuDict> listMenuByModule(String moduleId) {
        List<MenuDict> result = new ArrayList<>();
        List<ModuleVsMenu> moduleMenuList =moduleDictFacade.listMenusByModule(moduleId);
        if(null != moduleMenuList && moduleMenuList.size() > 0){
            Iterator<ModuleVsMenu> moduleMenuListIte = moduleMenuList.iterator();
            while(moduleMenuListIte.hasNext()){
                ModuleVsMenu moduleMenu = moduleMenuListIte.next();
                result.add(moduleMenu.getMenuDict());
            }
        }
        return result;
    }

    /**
     * 根据交集找出menus
     * @param staffId
     * @param moduleId
     * @return
     */
    @GET
    @Path("list-menu-by-staff")
    public List<MenuDict> listMenuByStaff(@QueryParam("staffId") String staffId, @QueryParam("moduleId") String moduleId) {
        List<MenuDict> byRole = listMenuByStaffRole(staffId);
        List<MenuDict> byModule = listMenuByModule(moduleId);
        if(null != byRole && null != byModule){
            byRole.retainAll(byModule);
        }
        return byRole;
    }


    @Path("add-login-info")
    @POST
    public Config addLoginInfo(Config config){
        HttpSession session = request.getSession();
        if(null != config.getHospitalId() && !config.getHospitalId().trim().equals("")){
            HospitalDict hospitalDict = staffDictFacade.get(HospitalDict.class, config.getHospitalId());
            session.setAttribute("hospitalId", hospitalDict.getId());
            session.setAttribute("hospitalName", hospitalDict.getHospitalName());
            config.setHospitalName(hospitalDict.getHospitalName());
        }
        if(null != config.getModuleId() && !config.getModuleId().trim().equals("")){
            ModulDict modulDict = staffDictFacade.get(ModulDict.class, config.getModuleId());
            session.setAttribute("moduleId", modulDict.getId());
            session.setAttribute("firstPage",modulDict.getModuleLoad());
            session.setAttribute("moduleName", modulDict.getModuleName());
            if("全成本核算系统".equals(modulDict.getModuleName())){
                AcctDeptVsDeptDict dict = acctDeptDictFacade.getAcctDeptVsDeptDict((String)session.getAttribute("deptId")) ;
                session.setAttribute("acctDeptId",dict.getAcctDeptId());
            }
            config.setModuleName(modulDict.getModuleName());
        }
        if (null != config.getStorageCode() && !config.getStorageCode().trim().equals("")) {
            ExpStorageDept storageDict = expStorageDeptFacade.get(ExpStorageDept.class, config.getStorageCode());
            session.setAttribute("storageName", storageDict.getStorageName());
            session.setAttribute("storageCode", storageDict.getStorageCode());
            config.setStorageName(storageDict.getStorageName());
        }

        return config ;

    }

    @Path("get-login-info")
    @GET
    public Response getLoginInfo() throws Exception {
        Config config = new Config() ;
        HttpSession session = request.getSession();
        //ReportDict reportDict = reportDictFacade.getByHospitalId((String) session.getAttribute("hospitalId"));
        config.setHospitalId((String) session.getAttribute("hospitalId"));
        config.setHospitalName((String) session.getAttribute("hospitalName"));
        config.setModuleName((String) session.getAttribute("moduleName"));
        config.setModuleId((String) session.getAttribute("moduleId"));
        config.setLoginId((String) session.getAttribute("loginId"));
        config.setLoginName((String) session.getAttribute("loginName"));
        config.setStaffName((String) session.getAttribute("staffName"));
        config.setStorageCode((String) session.getAttribute("storageCode"));
        config.setStorageName((String) session.getAttribute("storageName"));
        config.setAcctDeptId((String) session.getAttribute("acctDeptId"));
        config.setPassword((String) session.getAttribute("password"));
        config.setFirstPage((String)session.getAttribute("firstPage"));
        ReportDict reportDict = reportDictFacade.findByHospitalId((String) session.getAttribute("hospitalId")) ;
        config.setReportDict(reportDict);
        String hospitalName = (String) session.getAttribute("hospitalName");
        String key = EncryptUtil.encrypt(hospitalName.trim());
        KeyDict keyDict = keyDictFacade.findByKey(key) ;
        if(keyDict==null){
            config.setHospitalName("未经授权，仅限于试用");
            config.setModuleName("请及时授权");
            config.setLimitDays(30);
            return Response.status(Response.Status.OK).entity(config).build() ;
        }
        String keyCode = keyDict.getKeyCode() ;

        keyCode = EncryptUtil.decrypt(keyCode) ;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd") ;
        Date lastDate = dateFormat.parse(keyCode) ;
        Date now = new Date();
        long time = (lastDate.getTime() -now.getTime())/24/60/60/1000 ;
        if(time<=0){
            ErrorMessager errorMessager = new ErrorMessager() ;
            errorMessager.setErrorMessage("授权已过期，请重新授权");
            return Response.status(Response.Status.OK).entity(errorMessager).build() ;
        }else{
            config.setLimitDays((int)time);
        }

        return Response.status(Response.Status.OK).entity(config).build() ;
    }

    /**
     * 取消登录
     * @return
     */
    @GET
    @Path("log-out")
    @Produces("text/html")
    public String logOut(){
        HttpSession session = request.getSession();
        try {
            session.invalidate();
            return "ok";
        }catch (Exception e){
            e.printStackTrace();
            return  "false";
        }
    }


    @POST
    @Path("save-app")
    public Response saveLocalPragramSetting(BeanChangeVo<LocalProgramSetting> localProgramSettingBeanChangeVo){
        try {
            BeanChangeVo<LocalProgramSetting> localProgramSetting1 = localProgramSettingFacade.saveLocalApp(localProgramSettingBeanChangeVo) ;
            return Response.status(Response.Status.OK).entity(localProgramSetting1).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.OK).entity(errorException).build();
        }
    }

    @Path("list-app")
    @GET
    public List<LocalProgramSetting> findLocalProgramByLoginId(@QueryParam("loginId")String loginId){
        return localProgramSettingFacade.findLocalProgramByLoginId(loginId);
    }

    @Path("del-app")
    @POST
    public Response deleteProgramById(@QueryParam("id")String id){
        try {
            LocalProgramSetting localProgramSetting1 = localProgramSettingFacade.delLocalApp(id);
            return Response.status(Response.Status.OK).entity(localProgramSetting1).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.OK).entity(errorException).build();
        }
    }

    private void setAccessToken(){
        //用户名和密码校验成功

        try {
            String clientId = request.getRemoteHost() ;
            MD5Generator md5Generator = new MD5Generator();
            String accessToken = null;
            accessToken = md5Generator.generateValue();
            Cache cache= new Cache("code_"+clientId,accessToken,7200,true) ;
            CacheManager.putCache("code_" + clientId, cache);
            Cookie cookie = new Cookie("ACCESS_TOKEN","code_" + clientId) ;
            cookie.setMaxAge(3600);
            HttpSession session  = request.getSession();
            session.setAttribute("ACCESS_TOKEN",accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GET
    @Path("write-staff_id")
    public String writeStaffId(@QueryParam("staffId") String staffId){
        try {
            resp.sendRedirect("/views/his/common/module-select.html?sId="+staffId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}


