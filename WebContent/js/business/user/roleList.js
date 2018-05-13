/**
 * FileName: roleList.js
 * File description: 用于加载角色列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * roleList所有属性和方法定义
 * @type {roleList}
 */
var roleList = function () {
    var zTreeObj = null;
    var setting = {
        view: {
//                addHoverDom: addHoverDom,
//                removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
        check: {
            enable: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        edit: {
            enable: false
        },
        callback : {
            onClick : zTreeOnClickRight
        }
    };

    // 树的单击事件
    function zTreeOnClickRight(event, treeId, treeNode) {
        // 取消全选
        $("input[name='checkbox']").removeAttr("checked");
        var obj = [];
        obj.push(StringUtil.decorateRequestData('String',zTreeObj.getSelectedNodes()[0].id));
        $.ajax({
            type: "POST",
            url: SMController.getUrl({
                controller: 'controllerProxy',
                method: 'callBack',
                proxyClass: 'securityController',
                proxyMethod: 'getRoleSecurity',
                jsonString: MyJsonUtil.obj2str(obj)
            }),
            dataType: "json",
            success: function (result) {
                for (var item in result){
                    var roleObj = result[item];
                    $("#"+roleObj.securityId).prop("checked","true");
                }
            }
        });
    }

    var handleCheckbox = function() {
        var tmpCheckboxGroup =  $("#securityList");
        $.ajax({
            type:'post',
            dataType:"json",
            async: false,
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getSecurityList',jsonString:null}),
            success:function(result){
                for (var item in result){
                    var securityObj = result[item];
                    var tmpCheckboxLabel = $("<label class='checkbox'>");
                    var tmpInput = $(" <input id='"+securityObj.securityId+"' name='checkbox' type='checkbox' value='"+securityObj.securityId+"'/>");
                    var tmpLabel = $(" <label>"+securityObj.securityName+"</label>");
                    $(tmpCheckboxLabel).appendTo(tmpCheckboxGroup);
                    $(tmpInput).appendTo(tmpCheckboxLabel);
                    $(tmpLabel).appendTo(tmpCheckboxLabel);
                }
            }
        });
    }

    var handleButton = function() {
        $('#add_role').on('click', function (e) {
            clearModalData();
            $('#roleModal').modal('show',true);
        });

        $('#modify_role').on('click', function (e) {
            clearModalData();
            var treeId = zTreeObj.getSelectedNodes()[0].id;
            var treeName = zTreeObj.getSelectedNodes()[0].name;

            $('input[name="roleId"]').val(treeId);
            $('input[name="roleName"]').val(treeName);
            $('#roleModal').modal('show',true);
        });

        $('#delete_role').on('click', function (e) {
            if(zTreeObj.getSelectedNodes()[0] != null){
                bootbox.confirm({
                    buttons: {
                        confirm: {
                            label: '确认',
                            className: 'btn green'
                        },
                        cancel: {
                            label: '取消',
                            className: 'btn'
                        }
                    },
                    message: '确定删除这一行吗 ?',
                    title: "消息提示",
                    callback: function(result) {
                        if(result) {
                            var obj = [];
                            obj.push(StringUtil.decorateRequestData('String',zTreeObj.getSelectedNodes()[0].id));
                            $.ajax({
                                type:'post',
                                dataType:"json",
                                async: false,
                                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                    ,proxyClass:'securityController',proxyMethod:'deleteRole',jsonString:MyJsonUtil.obj2str(obj)}),
                                success:function(result){
                                    if(result.success){
                                        $.ajax({
                                            type: "POST",
                                            url: SMController.getUrl({
                                                controller: 'controllerProxy',
                                                method: 'callBack',
                                                proxyClass: 'securityController',
                                                proxyMethod: 'getRoleTreeList',
                                                jsonString: null
                                            }),
                                            dataType: "json",
                                            success: function (result) {
                                                zTreeObj = $.fn.zTree.init($("#roleTree"), setting, result);
                                            }
                                        });
                                        $.pnotify({
                                            text: '删除成功'
                                        });
                                    }else{
                                        $.pnotify({
                                            type:'error',
                                            text: result.msg,
                                            delay: 8000
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        function clearModalData(){
            $('input[name="roleId"]').val('');
            $('input[name="roleName"]').val('');
        }

        $('#submitRole').on('click', function (e) {
            var roleId = $('input[name="roleId"]').val();
            var roleName = $('input[name="roleName"]').val();

            var obj = [];
            var addData = {};
            addData['roleId'] = roleId;
            addData['roleName'] = roleName;
            console.log(addData);

            if (roleName == '') {
                bootbox.alert({
                    className: 'span4 alert-error',
                    buttons: {
                        ok: {
                            label: '确定',
                            className: 'btn blue'
                        }
                    },
                    message: "角色名称不能为空",
                    callback: function () {

                    },
                    title: "错误提示"
                });
            } else {
                obj.push(StringUtil.decorateRequestData('RoleDTO', addData));
                //进度条
                $('#progressBar').modal('show', true);
                $.ajax({
                    type: "POST",
                    url: SMController.getUrl({
                        controller: 'controllerProxy',
                        method: 'callBack',
                        proxyClass: 'securityController',
                        proxyMethod: roleId == '' ? 'insertRole' : 'updateRole',
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
                        console.log(result);
                        if (result.success) {
                            $("#processStatus").text("提交成功，正在返回上一页面...");
                            setTimeout(function(){
                                $.unblockUI();
                                $('#roleModal').modal('hide');
                                $.ajax({
                                    type: "POST",
                                    url: SMController.getUrl({
                                        controller: 'controllerProxy',
                                        method: 'callBack',
                                        proxyClass: 'securityController',
                                        proxyMethod: 'getRoleTreeList',
                                        jsonString: null
                                    }),
                                    dataType: "json",
                                    success: function (result) {
                                        zTreeObj = $.fn.zTree.init($("#roleTree"), setting, result);
                                    }
                                });
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
        });

        $('#submitSecurity').on('click', function (e) {
            var obj = [];
            if (zTreeObj.getSelectedNodes()[0] == null) {
                bootbox.alert({
                    className: 'span4 alert-error',
                    buttons: {
                        ok: {
                            label: '确定',
                            className: 'btn blue'
                        }
                    },
                    message: "请选择角色",
                    callback: function () {

                    },
                    title: "错误提示"
                });
            } else {
                var roleId = zTreeObj.getSelectedNodes()[0].id;
                var securityList = [];
                $("input[name='checkbox']:checkbox:checked").each(function(){
                    securityList.push($(this).val());
                })
                console.log(securityList);
                obj.push(StringUtil.decorateRequestData('String', roleId));
                obj.push(StringUtil.decorateRequestData('List', securityList));
                //进度条
                $('#progressBar').modal('show', true);
                $.ajax({
                    type: "POST",
                    url: SMController.getUrl({
                        controller: 'controllerProxy',
                        method: 'callBack',
                        proxyClass: 'securityController',
                        proxyMethod: 'SaveRoleSecurity',
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
                        console.log(result);
                        if (result.success) {
                            $("#processStatus").text("提交成功...");
                            setTimeout(function(){
                                $.unblockUI();
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
        });

        $('#clearSecurity').on('click', function (e) {
            $("input[name='checkbox']").removeAttr("checked");
        });

        $('#selectAllSecurity').on('click', function (e) {
            $("input[name='checkbox']").prop("checked","true");
        });
    }

    var handleTree = function() {
        $.ajax({
            type: "POST",
            url: SMController.getUrl({
                controller: 'controllerProxy',
                method: 'callBack',
                proxyClass: 'securityController',
                proxyMethod: 'getRoleTreeList',
                jsonString: null
            }),
            dataType: "json",
            success: function (result) {
                zTreeObj = $.fn.zTree.init($("#roleTree"), setting, result);
            }
        });
    }

    return {
        init: function () {
            handleButton();
            handleTree();
            handleCheckbox();
        }
    };
}();