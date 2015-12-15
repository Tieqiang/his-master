/**
 * Created by heren on 2015/9/11.
 */
var config = new common() ;


window.addTab = function (title, href) {
    var tabs = $("#mainContent").tabs('tabs');
    console.log(tabs)
    if(tabs.length>10){
        $.messager.alert("系统提示","打开的Tab页面太多，请观不需要的，重新在打开",'info') ;
        return ;
    }
    if($("#mainContent").tabs('exists',title)){
        $("#mainContent").tabs('select',title);
    }else{
        var content = undefined ;
        if($.startWith(href,'http')){

            content= '<iframe scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>' ;
        }else{
            content= '<iframe scrolling="auto" frameborder="0"  src="views'+href+'.html" style="width:100%;height:100%;"></iframe>'
        }

        $("#mainContent").tabs('add',{
            title:title,
            content:content,
            closable:true
        });
    }
}
$(function(){

    $("#reLogin").on('click',function(){
        location.href="/login1.html"
    })
    $("#changeLogin").on('click',function(){
        location.href="/views/his/common/module-select.html"
    })
    $("#cc").layout() ;

    //所定窗口
    //$("#logwindow").window({
    //    title:'窗口锁定',
    //    width:400,
    //    height:200,
    //    modal:true,
    //    closed:true,
    //    closable:false
    //
    //})

    $("#lockWindow").on('click',function(){
        $("#logwindow").window("open") ;
    })

    var promise = $.get('/api/login/get-login-info',function(data){
        config =data ;
    })

    promise.done(function(){
        var menus=[] ;//菜单列表
        var menuTreeData = [] ;//菜单树的列表
        var url = "api/menu/list-login-module?moduleId="+config.moduleId+"&loginName="+config.loginName;
        var lisAll = "api/menu/list" ;
        $(".site_title").append("极目云软件-"+config.moduleName) ;

        //var url = "api/menu/list-login-module?moduleId=402886f350a6bd4f0150a6c0c4830001&loginName=000WL2"

        var menuPromise = $.get(url,function(data){
            $.each(data,function(index,item){
                var menu ={} ;
                menu.attributes={} ;
                menu.id = item.id ;
                menu.text = item.menuName ;
                menu.state = "open" ;
                menu.attributes.url = item.href ;
                menu.attributes.parentId = item.parentId ;
                menu.attributes.index = item.position;
                menu.children=[] ;
                menus.push(menu) ;
            })

            for(var i = 0 ;i<menus.length;i++){
                for(var j = 0 ;j<menus.length;j++){
                    if(menus[i].id ==menus[j].attributes.parentId){
                        menus[i].children.push(menus[j]) ;
                    }
                }

                if(menus[i].children.length>0 && !menus[i].attributes.parentId){
                    menuTreeData.push(menus[i]) ;
                }

                if(!menus[i].attributes.parentId&&menus[i].children.length<=0){
                    menuTreeData.push(menus[i]) ;
                }
            }

            menus.sort(function(a,b){
                return a.attributes.index- b.attributes.index;
            });
            console.log("asc menus:"+menus);
        }) ;

        menuTreeData.sort(function (a, b) {
            return a[0].attributes.index - b[0].attributes.index;
        });
        console.log("asc menuTreeData:" + menuTreeData);
        //$("#menuTree").accordion({
        //    fit:true
        //}) ;
        $("#uName").val(config.moduleName) ;
        //初始化菜单
        menuPromise.done(function(){

            $("#menuTree").tree({
                fit:true,
                lines:true,
                onClick:function(node){
                    if(node.attributes.url){
                        addTab(node.text,node.attributes.url) ;
                    }
                }
            }) ;

            $("#menuTree").tree('loadData',menuTreeData);
            $("#menuTree").tree('collapseAll')//默认折叠所有的选项
        }) ;
    })

}) ;


//config.defaultSupplier = "供应室"
//config.hospitalId = "4028862d4fcf2590014fcf9aef480016" ;
//config.hospitalName = "双滦区人民医院" ;
//config.storage = "五金库";
//config.storageCode='1503';
//config.loginName = '123';
//config.loginId = '11'
////config.moduleId = '402886f350a6bd4f0150a6c0c47c0000' ;
//config.moduleId = '402886f350a6bd4f0150a6c0c47c0000' ;
//config.moduleName = "消耗品管理系统"
//config.exportClass = "'发放出库','批量出库','退账入库'";
//config.reportServerIp = '192.168.6.68';
//config.reportServerPort = '8080';
//config.reportServerName = 'webReport';
//config.reportServerResousePath = 'ReportServer?reportlet=';
//config.defaultReportPath = "http://" + config.reportServerIp + ":" + config.reportServerPort + "/" + config.reportServerName + "/" + config.reportServerResousePath;