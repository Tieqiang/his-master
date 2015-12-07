/**
 * Created by heren on 2015/9/11.
 */
$(function () {

    $("#tt").treegrid({
        idField: 'id',
        treeField: 'menuName',
        title: '系统菜单维护',
        fit: true,
        footer: '#tb',
        columns: [[{
            title: '菜单名称',
            field: 'menuName',
            width: "20%"
        }, {
            title: '路径',
            field: 'href',
            width: "80%"
        }]],
        onLoadSuccess:function(node,data){
            //$(this).tree('collapseAll') ;
            //var roots = $(this).tree('getRoots') ;
            //for(var i = 0 ;i<roots.length ;i++){
            //    $(this).tree('collapseAll',roots[i].target) ;
            //}
        }
    });


    var loadMenu = function () {
        var menus = [];//菜单列表
        var menuTreeData = [];//菜单树的列表
        var menuPromise = $.get("/api/menu/list", function (data) {
            $.each(data,function(index,item){
                var menu ={} ;
                menu.id = item.id ;
                menu.menuName = item.menuName ;
                menu.href = item.href ;
                menu.parentId = item.parentId ;
                menu.children=[] ;
                menus.push(menu) ;
            });

            for(var i = 0 ;i<menus.length;i++){
                for(var j = 0 ;j<menus.length;j++){
                    if(menus[i].id ==menus[j].parentId){
                        menus[i].state='closed' ;
                        menus[j].state='open' ;
                        menus[i].children.push(menus[j]) ;
                    }
                }

                if(menus[i].children.length>0 && !menus[i].parentId){
                    menuTreeData.push(menus[i]) ;
                }

                if(!menus[i].parentId&&menus[i].children.length<=0){
                    menuTreeData.push(menus[i]) ;
                }
            }
        });

        menuPromise.done(function () {

            $("#tt").treegrid('loadData',menuTreeData) ;
            $("#tt").treegrid("selectRow", 1);
        });
    }


    loadMenu();



    $("#saveBtn").on('click', function () {
        if ($("#fm").form('validate')) {
            var menuDict = {};
            menuDict.menuName = $("#menuName").textbox('getValue');
            menuDict.href = $("#href").textbox('getValue');
            menuDict.parentId = $("#parentId").textbox('getValue');
            menuDict.id = $("#id").val();

            var title = $("#dlg").dialog('options').title;
            $.postJSON("/api/menu/add", menuDict, function (data) {

                $('#dlg').dialog('close');
                //清空数据
                //clearData();
                //重新加载菜单
                loadMenu();
            }, function (data, status) {
            })
        }

    })

    $("#addSameLevelBtn").on('click', function () {
        var node = $("#tt").treegrid('getSelected')
        if (!node) {
            $.messager.alert("系统提示", "请选择，所添加菜单的同一级的任意一个菜单");
            return;
        }
        if (node) {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', '添加同级菜单');
            //$('#fm').form('clear');
            $("#parentName").textbox('setValue', node.menuName);
            $("#parentId").textbox('setValue', node._parentId);
        }
    });

    /**
     * 添加下级菜单
     */
    $("#addNextLevelBtn").on('click', function () {
        var node = $("#tt").treegrid('getSelected')
        if (!node) {
            $.messager.alert("系统提示", "请选择，所添加菜单的同一级的任意一个菜单");
            return;
        }
        if (node) {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', '添加子菜单');
            //$('#fm').form('clear');
            $("#parentName").textbox('setValue', node.menuName);
            $("#parentId").textbox('setValue', node.id);
        }
    })

    /**
     * 删除某一个菜单
     */
    $("#removeBtn").on('click', function () {
        var row = $("#tt").datagrid('getSelected');
        if (row == null) {
            $.messager.alert("系统提示", "请选择要删除的菜单");
            return;
        }
        var children = $("#tt").treegrid('getChildren', row.id);
        if (children.length > 0) {
            $.messager.alert("系统提示", "请先删除子菜单");
            return;
        } else {

            $.messager.confirm("系统提示", "确认删除:【" + row.menuName + "】的菜单吗?", function (r) {
                if (r) {
                    $.post('/api/menu/del/' + row.id, function (data) {
                        $.messager.alert("系统提示", "删除成功");
                        $("#tt").treegrid('remove', row.id);
                    });
                }
            })

        }
    })

    /**
     * 修改一个菜单
     */
    $("#updateBtn").on('click', function () {

        var node = $("#tt").treegrid('getSelected');
        if (node == null) {
            $.messager.alert("系统提示", "请选中要修改的菜单");
            return;
        }
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', '修改菜单');
        //$('#fm').form('clear');
        $("#menuName").textbox('setValue', node.menuName);
        $("#href").textbox('setValue', node.href);
        $("#parentName").textbox('setValue', node.menuName);
        $("#parentId").textbox('setValue', node._parentId);
        $("#id").val(node.id);

    });

})