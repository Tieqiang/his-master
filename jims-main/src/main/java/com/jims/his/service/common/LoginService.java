package com.jims.his.service.common;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.common.util.EnscriptAndDenScript;
import com.jims.his.domain.common.entity.*;
import com.jims.his.domain.common.facade.ModuleDictFacade;
import com.jims.his.domain.common.facade.RoleDictFacade;
import com.jims.his.domain.common.facade.StaffDictFacade;
import com.jims.his.domain.common.vo.Config;
import com.jims.his.domain.common.vo.ErrorMessager;
import com.jims.his.domain.htca.entity.AcctDeptVsDeptDict;
import com.jims.his.domain.htca.facade.AcctDeptDictFacade;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.facade.ExpStorageDeptFacade;
import org.codehaus.jettison.json.JSONArray;


import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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



    @Inject
    public LoginService(HttpServletRequest request, HttpServletResponse resp, ModuleDictFacade moduleDictFacade, StaffDictFacade staffDictFacade, RoleDictFacade roleDictFacade, ExpStorageDeptFacade expStorageDeptFacade, AcctDeptDictFacade acctDeptDictFacade) {
        this.moduleDictFacade = moduleDictFacade;
        this.staffDictFacade = staffDictFacade;
        this.roleDictFacade = roleDictFacade;
        this.expStorageDeptFacade = expStorageDeptFacade;
        this.resp = resp;
        this.request = request;
        this.acctDeptDictFacade = acctDeptDictFacade;
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
    public Response loing(@QueryParam("loginName")String loginName ,@QueryParam("password")String password){
        try{
            StaffDict staffDict= staffDictFacade.findByLoginName(loginName) ;
            if(staffDict==null){
                ErrorMessager errorMessager = new ErrorMessager("错误的用户名", "系统登录");
                return Response.status(Response.Status.OK).entity(errorMessager).build() ;
            }
            String denPassword = EnscriptAndDenScript.denscriptFromHis(staffDict.getPassword()) ;
            if(!denPassword.equals(password)){
                ErrorMessager errorMessager = new ErrorMessager("错误的密码", "系统登录");
                return Response.status(Response.Status.OK).entity(errorMessager).build() ;
            }else{
                HttpSession session = request.getSession() ;
                session.setAttribute("loginName",staffDict.getLoginName());
                session.setAttribute("staffName", staffDict.getName());
                session.setAttribute("loginId",staffDict.getId());
                session.setAttribute("deptId",staffDict.getDeptDict().getId());
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


    @GET
    @Path("code")
    public void getValidateCode(@QueryParam("temp") String temp) throws ServletException, IOException {
        int width = 200;
        int height = 30;
        int codeCount = 4;

        int x = width / (codeCount + 1);
        int fontHeight = height - 2;
        int codeY = height - 4;
        //定义图像buffer
        BufferedImage buffImg = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        //创建一个随机数生成器类
        Random random = new Random();

        //将图像填充为白色
        g.setColor(Color.getColor("#EF02FF"));
        g.fillRect(0, 0, width, height);

        //创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        //设置字体。
        g.setFont(font);

        //画边框。
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setBackground(Color.getColor("#EAF2FF"));
        //随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(Color.BLACK);
        for (int i = 0; i < 160;
             i++) {
            x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        //randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;

        //随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            //得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
            //产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            //用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i+1) * (width / (codeCount + 1)), codeY);

            //将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }

        HttpSession session = request.getSession();
        session.setAttribute("validateCode", randomCode.toString());
        System.out.println("random:" + randomCode.toString());

        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);

        resp.setContentType("image/jpeg");

//将图像输出到Servlet输出流中。
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
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
    public Config getLoginInfo(){
        Config config = new Config() ;
        HttpSession session = request.getSession();
        config.setHospitalId((String) session.getAttribute("hospitalId"));
        config.setHospitalName((String)session.getAttribute("hospitalName"));
        config.setModuleName((String)session.getAttribute("moduleName"));
        config.setModuleId((String)session.getAttribute("moduleId"));
        config.setLoginId((String)session.getAttribute("loginId"));
        config.setLoginName((String)session.getAttribute("loginName"));
        config.setStaffName((String) session.getAttribute("staffName"));
        config.setStorageCode((String) session.getAttribute("storageCode"));
        config.setStorageName((String) session.getAttribute("storageName"));
        config.setAcctDeptId((String)session.getAttribute("acctDeptId"));
        config.setDefaultReportPath("http://192.168.6.68:8080/webReport/ReportServer?reportlet=");
        return config ;
    }
}


