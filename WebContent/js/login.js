/**
 * FileName: login.js
 * File description: 用于加载和初始化登录页面的组件及内容
 * Copyright (c) 2016 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2016-11-02
 */

/**
 * login所有属性和方法定义
 * @type {login}
 */
var login = function () {

    var handleForm = function () {
        $("#loginForm").validate({
            rules:{
                login:{
                    required:true
                },
                password:{
                    required:true
                }
            },
            messages: {
                login:{
                    required:"用户名不能为空！！！"
                },
                password:{
                    required:"密码不能为空！！！"
                }
            },
            submitHandler: function (form) {
                var obj = [];
                var tmpFormData = DomUtil.getJSONObjectFromForm('loginForm', null);
                obj.push(StringUtil.decorateRequestData('LoginDTO', tmpFormData));
                $.ajax({
                    type:"POST",
                    url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                        ,proxyClass:'securityController',proxyMethod:'login',jsonString:MyJsonUtil.obj2str(obj)},
                        '/Meeting/'),
                    dataType:"json",
                    success:function(result) {
                        if (result.success) {
                            localStorage.setItem("LoginDTO", JSON.stringify(result.msg.entityKeyValue));
                            window.location.href='index.html';
                        } else {
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
        });

        document.onkeydown = function(e) {
            if(!e) e = window.event;
            if((e.keyCode || e.which) == 13){
                document.getElementById("btnSubmit").click();
            }
        };
    };

    var handleTop = function () {
        initTopEvent();
        intSecurity();
        validateFrom();

        // 初始化表单提交事件
        function initTopEvent() {
            var tmpLoginDTO= JSON.parse(localStorage.getItem("LoginDTO"));
            if (tmpLoginDTO == null)
                window.location.href = '../../login.html';

            // 设置登录名
            document.getElementById("loginUser").innerHTML = tmpLoginDTO.employeeName;

            // 退出登录
            $("#logout").click(function(e) {
                // 将所有保存的数据删除
                localStorage.clear();
            });

            // 提交表单
            $("#btn-modifiedPassword").on('click', function(e){
                // 清除界面上的弹出框
                clearSmallBox();

            });

            //取消提交表单,表单填充域清空
            $("#btn-cancel , .close").on('click',function(e){
                resetOrClearForm();
            });
        }

        // 初始化用户权限
        function intSecurity() {
            var tmpEmployeeDTO = JSON.parse(localStorage.getItem("EmployeeDTO"));
            var tmpSecurityCodeList = tmpEmployeeDTO.securityCodeList;
            // 设置按钮权限
            $("span[data-security-code],a[data-security-code]").each(function(i, v) {
                if (tmpSecurityCodeList.indexOf(""+$(this).data('securityCode')) == -1)
                    $(this).hide();
            });
        }

        //为表单提供验证规则
        function validateFrom(){
            //为validator添加自定义方法;
            $.validator.addMethod("rightFormate", function (value, element) {
                var tel = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,60}$/;
                return this.optional(element) || (tel.test(value));
            }, '正确的密码格式是由字母和数字组成!');

            //验证表单
            $("#password-form").validate({
                rules: {
                    password:{
                        required: true
                    },
                    newPassword: {
                        required: true,
                        rangelength: [8,60],
                        rightFormate : true
                    },
                    passwordConfirm: {
                        required: true,
                        equalTo: '#newPassword'
                    }
                },
                messages: {
                    password:{
                        required: '请输入密码!'
                    },
                    newPassword: {
                        required: '请输入密码!',
                        rangelength: "请输入 一个长度介于{0}至{1} 之间的字符串",
                        rightFormate : '正确的密码格式是由字母和数字组成!'

                    },
                    passwordConfirm: {
                        required: '请再次输入密码',
                        equalTo: '输入与上次不一致, 请确保确认密码和新密码一致!'
                    }
                },
                errorPlacement: function(error, element) {
                    error.insertAfter(element.parent());
                }
            });
        }

        //重置或清空表单
        function resetOrClearForm(){
            $("#password-form")[0].reset();
            $("#password, #newPassword, #passwordConfirm").parent().removeClass("state-success state-error").next().remove();
        }
    }

    return {
        init: function () {
            handleForm();
        },
        initTop: function() {
            handleTop();
        }
    };
}();