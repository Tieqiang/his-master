/**
 * Created by heren on 2015/11/27.
 */
$(function(){

    $("#win").window({
        modal:true,
        width:350,
        height:400,
        title:'登录内容选择',
        onOpen:function(){
            $("#win").window('center') ;
        },
        footer:"#ft"
    }) ;

    //医院选择
    $("#hospital").combobox({
        method:'GET',
        url:"/api/hospital-dict/list",
        valueField:'id',
        textField:"hospitalName",
        onLoadSuccess:function(){
            var data = $(this).combobox('getData') ;
            if(data){
                $(this).combobox('select',data[0].id) ;
            }
        },
        onSelect:function(item){
            //库房选择
            $('#stock').combobox({
                panelHeight: 'auto',
                url: '/api/exp-storage-dept/list?hospitalId=' + item.id,
                method: 'GET',
                valueField: 'storageCode',
                textField: 'storageName',
                onLoadSuccess: function () {
                    var data = $(this).combobox('getData');
                    if (data.length>0) {
                        $(this).combobox('select', data[0].storageName);
                    }
                }
            });
        }
    }) ;

    //模块选择
    $("#modual").combobox({
        method:'GET',
        url:'/api/module-dict/list',
        valueField:'id',
        textField:'moduleName',
        onLoadSuccess:function(){
            var data = $(this).combobox('getData') ;
            if(data){
                $(this).combobox('select',data[0].id) ;
            }
        },
        onSelect: function (item) {
            if(item.moduleName=="消耗品管理系统"){
                $("#stockDiv").show();
            }else{
                $("#stockDiv").hide();
            }
        }
    }) ;


    $("#saveItemBtn").on('click',function(){
        var hospitalId = $("#hospital").combobox('getValue') ;
        var moduleId = $("#modual").combobox('getValue') ;
        var storageCode = $("#stock").combobox('getValue');

        var config ={} ;
        config.hospitalId = hospitalId ;
        config.moduleId = moduleId ;
        config.storageCode = storageCode;

        $.postJSON("/api/login/add-login-info",config,function(data){
            //登录成功跳转至index.html
            location.href="/index.html"
        },function(data){

        })

    })
})