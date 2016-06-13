/**
 * 科室单项成本录入功能
 * Created by heren on 2015/11/30.
 */
$(function(){
    var acctDeptDict=[] ;
    $.get("/api/acct-dept-dict/acct-list?hospitalId="+parent.config.hospitalId,function(data){
        acctDeptDict = data ;
    }) ;

    var staffs = [] ;

    $.get("/api/staff-dict/list-by-hospital?hospitalId="+parent.config.hospitalId,function(data){
        staffs = data ;
    })
    var editRow = undefined ;
    var p = $('#fetchDate').datebox('panel');//日期选择对象
    var tds = false; //日期选择对象中月份
    var span = p.find('span.calendar-text'); //显示月份层的触发控件
    $("#fetchDate").datebox({
        onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
            span.trigger('click'); //触发click事件弹出月份层
            if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                tds = p.find('div.calendar-menu-month-inner td');
                tds.click(function (e) {
                    $(this).addClass("calendar-selected")
                    e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                    var year = /\d{4}/.exec(span.html())[0]//得到年份
                    var month = parseInt($(this).attr('abbr'), 10) + 1; //月份
                    $("#fetchDate").datebox('hidePanel').datebox('setValue', year + "-" + month)
                    setButton(year,month);
                });
            }, 0)
        },
        parser: function (s) {//配置parser，返回选择的日期
            if (!s) return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        formatter: function (d) {
            var year = d.getFullYear();
            var month = d.getMonth();
            if (month == 0) {
                month = 12;
                year = year - 1;
            }
            if(month<10){
                return year + '-0' + month;
            }
            return year + '-' + month;

        }//配置formatter，只返回年月
    });
    //设置按钮的使用状态
    var setButton = function(year,month){
        var yearM = undefined ;
        if(month==0){
            year = year -1 ;
            month = 12 ;
        }else{
            month = month -1 ;
        }

        if(month< 10 ){
            yearM = year+"-0"+month ;
        }else{
            yearM = year +"-"+month ;
        }

        $.get("/api/acct-save-record/get?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearM,function(data){

            if(data=="failure"){//尚未结存
                $(".easyui-linkbutton").linkbutton("enable") ;
            }
            if(data=="success"){
                $(".easyui-linkbutton").linkbutton("disable") ;
            }
        })
        //查询结存记录
    }
    $("#acctCostTable").datagrid({
        fit:true,
        fitColumns:true,
        method:'GET',
        checkOnSelect:true,
        striped: true,
        singleSelect: false,
        toolbar: '#ft',
        method: 'GET',
        rownumbers:true,
        pagination: true,
        ctrlSelect:true,
        loadMsg:'数据正在加载，请稍后......',
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'核算单元',
            field:'acctDeptId',
            width:'20%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<acctDeptDict.length ;i++){
                    if(value==acctDeptDict[i].id){
                        return acctDeptDict[i].deptName ;
                    }
                }
                return value ;
            }
        },{
            title:'单项奖金额',
            field:'rewardNum',
            width:'10%'
        },{
            title:'操作人',
            field:'operator',
            width:'10%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<staffs.length ;i++){
                    if(staffs[i].id==value){
                        return staffs[i].name ;
                    }
                }
                return value ;
            }
        },{
            title:'操作时间',
            field:'operatorDate',
            width:'40%',
            hidden:true,
            formatter:function(value,row,index){
                var date = new Date(value) ;
                return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate() ;
            }
        }]],
        onDblClickRow:function(rowIndex,rowData){
            if(editRow==0||editRow){
                stopEdit() ;
            }
            editRow = rowIndex ;
            $(this).datagrid('checkRow',rowIndex) ;
            beginEdit() ;
        }
    }) ;

    $("#rewardItem").combobox({
        method:'GET',
        url:'/api/single-reward-dict/list-all?hospitalId='+parent.config.hospitalId ,
        valueField:'id',
        textField:'rewardName',
        onLoadSuccess:function(data){
            var data =$(this).combobox('getData') ;
            if(data.length){
                $(this).combobox('setValue',data[0].id) ;
            }
        }
    }) ;

    //查询按钮
    $("#searchBtn").on('click',function(){
        var yearMonth = $("#fetchDate").datebox('getValue') ;
        if(!yearMonth){
            $.messager.alert("系统提示","查询月份不能为空",'info') ;
            return ;
        }
        var costItemId = $("#rewardItem").combobox('getValue') ;

        if(!costItemId){
            $.messager.alert("系统提示","专项奖项目不能为空",'info') ;
            return ;
        }

        var options = $("#acctCostTable").datagrid('options') ;
        options.url = "/api/single-reward-record/list?hospitalId="+parent.config.hospitalId+"&rewardDictId="+costItemId+"&yearMonth="+yearMonth ;
        $("#acctCostTable").datagrid('reload') ;

    })


    //停止编辑行
    var stopEdit = function(){
        if(editRow || editRow==0){

            $("#acctCostTable").datagrid('endEdit', editRow);
            editRow = undefined ;
            return true ;
        }else{
            editRow=undefined ;
            return true ;
        }
    }

    //开始编辑行
        var beginEdit = function(){
            if(editRow||editRow==0){
                $("#acctCostTable").datagrid('beginEdit',editRow) ;
            }
        }


        //保存按钮
        $("#saveBtn").on('click',function(){
            var rows = $("#acctCostTable").datagrid('getRows') ;
            console.log(rows) ;
            if(!rows.length){
                $.messager.alert("系统提示","请选择要保存的记录","info") ;
                return  ;
            }
            var yearMonth = $("#fetchDate").datebox('getValue') ;
            var costItemId = $("#rewardItem").combobox('getValue') ;

            for(var i = 0 ;i<rows.length ;i++){
                rows[i].yearMonth = yearMonth ;
                rows[i].rewardDictId = costItemId ;
                rows[i].hospitalId= parent.config.hospitalId;
                rows[i].operator = parent.config.loginId ;
                rows[i].operatorDate = new Date() ;
            }

            if(stopEdit()){
                $.postJSON("/api/single-reward-record/save",rows,function(data){
                    $("#searchBtn").trigger('click') ;
                    $.messager.alert("系统提示","保存成功","info") ;
                },function(data){
                    $.messager.alert("系统提示","保存失败","info") ;
                })
            }

        })  ;


        //删除某一个成本项目
        $("#removeBtn").on('click',function(){
            var rows = $("#acctCostTable").datagrid('getSelections') ;
            if(!rows.length){
                $.messager.alert("系统提示","请选择要删除的项目","info") ;
                return  ;
            }

            var deleteIds = [] ;
            for(var i = 0 ;i<rows.length ;i++){
                if(rows[i].id){
                    deleteIds.push(rows[i].id) ;
                }
            }
            //如果需要删除的
            if(deleteIds.length){
                $.messager.confirm('系统提示','确定要进行删除操作吗',function(r){
                    if(r){
                        $.postJSON("/api/single-reward-record/delete",deleteIds,function(data){
                            $.messager.alert('系统提示','删除成功','info') ;
                            $("#searchBtn").trigger('click') ;
                        },function(data){
                        })
                    }
                });

            }else{
                $.messager.alert("系统提示","本页数据未存在在数据库中，不能删除","info") ;
            }
    })
}) ;