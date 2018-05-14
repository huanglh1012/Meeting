/**
 * FileName: userEdit.js
 * File description: 用于加载和初始化自动化规则配置页面的组件及内容
 * Copyright (c) 2016 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2016-10-18
 */

/**
 * userEdit所有属性和方法定义
 * @type {userEdit}
 */
var userEdit = function () {

    var handlePageInfo = function () {
        var tmpUrl = document.URL;
        var tmpUrlParam = tmpUrl.split('?')[1];
        if (tmpUrlParam != undefined) {
            var tmpUrlParamValue= tmpUrlParam.split("=")[1];
            if (tmpUrlParamValue != undefined && tmpUrlParamValue != null) {
                var obj = [];
                obj.push(StringUtil.decorateRequestData('String',tmpUrlParamValue));
                $.ajax({
                    type:'post',
                    Type:"json",
                    async:false,
                    url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                        ,proxyClass:'securityController',proxyMethod:'getEmployeeInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
                    success:function(result){
                        console.log(result);
                        DomUtil.setFormElementsValueViaJSONObject('userForm',JSON.parse(result));
                    }
                });
            }
        }


    }

    var handleSelect2 = function () {
        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getRoleList',jsonString:null}),
            success:function(result){
                $('#roleId').select2({
                    placeholder: "请选择一个或多个角色",
                    allowClear:true,
                    multiple: true,
                    data:result
                });
            }
        });

        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getDepartmentGroupList',jsonString:null}),
            success:function(result){
                $('#departmentId').select2({
                    placeholder: "请选择部门",
                    allowClear:true,
                    data:result
                });
            }
        });

        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getPostList',jsonString:null}),
            success:function(result){
                $('#postId').select2({
                    placeholder: "请选择职务",
                    allowClear:true,
                    data:result
                });
            }
        });
    }

    var handleForm = function () {
        $("#userForm").validate({
            // 指定验证时要忽略哪些元素，默认是hidden，支持jQuery的伪类选择器，需要为应用该验证器的元素加上.required-validation
            errorElement: "strong",
            ignore: ":not('.required-validation')",
            errorClass: "note_error text-danger",
            focusCleanup: true,
            focusInvalid: false,
            rules: {
                employeeName: {
                    required: true
                },
                login: {
                    required: true
                },
                password: {
                    required: true
                },
                telephone: {
                    required: true
                },
                departmentId: {
                    required: true
                },
                roleId: {
                    required: true
                },
                postId: {
                    required: true
                }
            }, // Messages for form
            messages: {
                employeeName:{
                    required:"姓名不能为空！！！"
                },
                login:{
                    required:"账号不能为空！！！"
                },
                password:{
                    required:"密码不能为空！！！"
                },
                telephone:{
                    required:"电话不能为空！！！"
                },
                departmentId:{
                    required:"部门不能为空！！！"
                },
                roleId:{
                    required:"角色不能为空！！！"
                },
                postId:{
                    required:"职务不能为空！！！"
                }
            },

            highlight: function(element, errorClass) {
                $(element).parent().addClass("has-error");
            },

            unhighlight: function(element, errorClass) {
                $(element).parent().removeClass("has-error");
            },

            submitHandler: function(form) { //验证通过时触发
                var employeeId = $('input[name="employeeId"]').val();
                var obj = [];
                var addData = DomUtil.getJSONObjectFromForm('userForm', null);
                addData.sexId =  $("input[name='sexId']:checked").val();
                if ( addData.password != addData.confirmPassword) {
                    bootbox.alert({
                        className: 'span4 alert-error',
                        buttons: {
                            ok: {
                                label: '确定',
                                className: 'btn blue'
                            }
                        },
                        message: "密码不一致，请重新输入",
                        callback: function () {

                        },
                        title: "错误提示"
                    });
                } else {
                    obj.push(StringUtil.decorateRequestData('EmployeeDTO', addData));
                    $.ajax({
                        type: "POST",
                        url: SMController.getUrl({
                            controller: 'controllerProxy',
                            method: 'callBack',
                            proxyClass: 'securityController',
                            proxyMethod: employeeId == '' ? 'insertEmployee' : 'updateEmployee',
                            jsonString: MyJsonUtil.obj2str(obj)
                        }),
                        dataType: "json",
                        beforeSend: function(jqXHR, settings) {
                            $.blockUI({
                                message: '<div class="progress progress-lg progress-striped active" style="margin-bottom: 0px;">' +
                                    '<div style="width: 100%" role="progressbar" class="progress-bar bg-color-darken">' +
                                    '<span id="processStatus" style="position: relative; top: 5px;font-size:15px;">正在处理，请稍后...</span></div>' +
                                    '</div>'
                            });
                        },
                        success: function (result) {
                            if (result.success) {
                                $("#processStatus").text("提交成功，正在返回上一页面...");
                                setTimeout(function(){
                                    $.unblockUI();
                                    history.back();
                                }, 1500);

                            } else {
                                $.unblockUI();
                                bootbox.alert({
                                    title: '提示',//I18n.getI18nPropByKey("ProductionExecution.errorPrompt"),
                                    message:result.msg,
                                    className:'span4 alert-error',
                                    buttons: {
                                        ok: {
                                            label: '关闭',//I18n.getI18nPropByKey("ProductionExecution.confirm"),
                                            className: 'btn blue'
                                        }
                                    },
                                    callback: function() {

                                    }
                                });
                            }
                        }
                    });
                }
            }, // Do not change code below

            errorPlacement: function(error, element) {
                error.appendTo(element.parent());
            }
        });
    }

    return {
        init: function () {
            handlePageInfo();
            handleSelect2();
            handleForm();
        }
    };
}();