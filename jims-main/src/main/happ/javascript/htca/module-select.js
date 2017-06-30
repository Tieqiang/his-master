/**
 * Created by Administrator on 2017/6/30.
 */
$(function(){

    var modules = [] ;
    var hospitals=[] ;

    var hospitalPromise = $.get("/api/hospital-dict/list",function(data){
        hospitals = data ;
    });

    var getHospitalModules=function(hospital){
        console.log(hospital);

        var module = {} ;
        var promise  = $.get("/api/module-dict/list-by-staff?hospitalId="+hospital.id,function (data) {
            module.items = data ;
            module.hospital =hospital;
            var row = $("<div class='row'></div>")
            for(var i = 0;i<data.length ;i++){
                var column=undefined ;
                if(i%2==0){
                    column = $("<div data='"+data[i].id+"' class=\'col-md-4\' style=\'cursor:pointer\'>\n    <div class=\"widget red-bg p-lg text-center\">\n        <div class=\"m-b-md\">\n            <i class=\"fa fa-bell fa-4x\"></i>\n            <h1 class=\"font-bold no-margins\">\n"+data[i].moduleName+"            </h1>\n            <small>点击登录。</small>\n        </div>\n    </div>\n</div>\n")
                }else{
                    column = $("<div  data='"+data[i].id+"' class=\'col-md-4\' style=\'cursor:pointer\'>\n    <div class=\"widget blue-bg p-lg text-center\">\n        <div class=\"m-b-md\">\n            <i class=\"fa fa-shield fa-4x\"></i>\n            <h1 class=\"font-bold no-margins\">\n"+data[i].moduleName+"            </h1>\n            <small>点击登录。</small>\n        </div>\n    </div>\n</div>\n")
                }
                column.on("click",function(e){
                    var moduleId = $(e.currentTarget).attr("data");

                    var config ={} ;
                    config.hospitalId = hospital.id ;
                    config.moduleId = moduleId ;
                    $.postJSON("/api/login/add-login-info", config, function (data) {
                        //登录成功跳转至index.html
                        location.href = "/index.html";
                    }, function (data) {

                    })
                })
                row.append(column);
                $("#moduleContent").append(row);
            }

        });

        promise.done(function(data){
            console.log(module);
        })
    }

    hospitalPromise.done(function(data){
        for(var i =0 ;i<hospitals.length;i++){
            var hospital = hospitals[i] ;
            getHospitalModules(hospital);
        }
    })

})