/**
 * Created by Administrator on 2017/6/1.
 */
/**
 * Created by heren on 2015/9/11.
 */
var config = new common() ;

var isPublicIp=function(ip){
    if(ip=="localhost"){
        return false ;
    }
    var ips=ip.split(".") ;
    if(ips.length<4){
        return false ;
    }else{
        if(ips[0]=="10" || (ips[0]=="172" && ips[1]>='16' && ips[1]<="31") ||(ips[0]=="192" && ips[1]=="168")){
            return false ;
        }else{
            return true ;
        }
    }
}

var buildTree=function(dom,data){
    for(var i=0;i<data.length;i++){
        var li = $("<li></li>");
        if(data[i].children.length){
            var a =$("<a href=\"#\">\n    <i class=\"fa fa-home\"></i>\n   <span class=\"nav-label\">"+data[i].text+"</span>\n    <span class=\"fa arrow\"></span>\n</a>")
            var ul =$("<ul class=\"nav nav-second-level\"></ul>")
            buildTree(ul,data[i].children);
            li.append(a);
            li.append(ul);
        }else{
            var aherf = $("<a class=\"J_menuItem\" href='views"+data[i].attributes.url+".html'><i class=\"fa fa-columns\"></i> <span class=\"nav-label\">"+data[i].text+"</span></a>")
            li.append(aherf);
        }

        dom.append(li);
    }
}

$(function () {
    var localApps = [];

    $("#reLogin").on('click', function () {
        $.get("/api/login/log-out", function (data, status) {
            if (data == "ok") {
                location.href = "/login1.html"
            } else {
                $.messager.alert("退出登录失败！");
            }
        })
    })


    var promise = $.get('/api/login/get-login-info?date=' + new Date(), function (data) {
        if(data.errorMessage){
            $.messager.alert("系统提示",data.errorMessage,'error');
        }
        config = data;
        console.log(config);
        $("#moduleName").html(config.moduleName);
        $("#loginName").html("登陆者："+config.staffName);

        if(config.limitDays<30){
            $.messager.alert("系统提示","系统将在"+config.limitDays+"天后过期",'info');
        }
    })

    promise.done(function () {
        var menus = [];//菜单列表
        var menuTreeData = [];//菜单树的列表
        var url = "api/menu/list-login-module?moduleId=" + config.moduleId + "&loginName=" + config.loginName;
        console.log(url);
        var lisAll = "api/menu/list";
        $(".site_title").append(config.hospitalName + "-" + config.moduleName);
        var menuPromise = $.get(url, function (data) {

            console.log(data);
            $.each(data, function (index, item) {
                var menu = {};
                menu.attributes = {};
                menu.id = item.id;
                menu.text = item.menuName;
                menu.state = "open";
                menu.attributes.url = item.href;
                menu.attributes.parentId = item.parentId;
                menu.attributes.index = item.position;
                menu.children = [];
                menus.push(menu);
            })

            for (var i = 0; i < menus.length; i++) {
                for (var j = 0; j < menus.length; j++) {
                    if (menus[i].id == menus[j].attributes.parentId) {
                        menus[i].children.push(menus[j]);
                        menus[i].state = 'closed';
                    }
                }

                if (menus[i].children.length > 0 && !menus[i].attributes.parentId) {
                    menuTreeData.push(menus[i]);
                }

                if (!menus[i].attributes.parentId && menus[i].children.length <= 0) {
                    menuTreeData.push(menus[i]);
                }
            }
            if (menuTreeData.length) {
                menuTreeData[0].state = 'open';
            }

            menus.sort(function (a, b) {
                return a.attributes.index - b.attributes.index;
            });
        });
        //初始化菜单
        menuPromise.done(function () {
            console.log(menuTreeData);
            buildTree($("#side-menu"),menuTreeData[0].children);

            contabs();
            readyfunction();
        });
    })



});


