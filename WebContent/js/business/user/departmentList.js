/**
 * FileName: departmentList.js
 * File description: 用于加载部门列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * departmentList所有属性和方法定义
 * @type {departmentList}
 */
var departmentList = function () {
    var zTreeObj = null;
    var selectedTreeNode = null;
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
        }
//        callback : {
//            onClick : zTreeOnClickRight
//        }
    };

    var handleButton = function() {
        $('#add_department').on('click', function (e) {
            clearModalData();
            var treeId = zTreeObj.getSelectedNodes()[0].id;
            var treeName = zTreeObj.getSelectedNodes()[0].name;

            $('input[name="parentDepartmentId"]').val(treeId);
            $('input[name="parentDepartmentName"]').val(treeName);
            $('#departmentModal').modal('show',true);
        });

        $('#modify_department').on('click', function (e) {
            clearModalData();
            var treeId = zTreeObj.getSelectedNodes()[0].id;
            var treePid = zTreeObj.getSelectedNodes()[0].pId;
            var treeName = zTreeObj.getSelectedNodes()[0].name;
            var isParent = zTreeObj.getSelectedNodes()[0].isParent;

            var parentNode = zTreeObj.getSelectedNodes()[0].getParentNode();
            if (parentNode != null){
                $('input[name="parentDepartmentName"]').val(parentNode.name);
            }

            $('input[name="departmentId"]').val(treeId);
            $('input[name="parentDepartmentId"]').val(treePid);
            $('input[name="departmentName"]').val(treeName);
            $('input[name="isParent"]').val(isParent);
            $('#departmentModal').modal('show',true);
        });

        $('#delete_department').on('click', function (e) {
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
                                    ,proxyClass:'securityController',proxyMethod:'deleteDepartment',jsonString:MyJsonUtil.obj2str(obj)}),
                                success:function(result){
                                    if(result.success){
                                        $.ajax({
                                            type: "POST",
                                            url: SMController.getUrl({
                                                controller: 'controllerProxy',
                                                method: 'callBack',
                                                proxyClass: 'securityController',
                                                proxyMethod: 'getDepartmentTreeList',
                                                jsonString: null
                                            }),
                                            dataType: "json",
                                            success: function (result) {
                                                zTreeObj = $.fn.zTree.init($("#departmentTree"), setting, result);
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
            $('input[name="departmentId"]').val('');
            $('input[name="parentDepartmentId"]').val('');
            $('input[name="parentDepartmentName"]').val('');
            $('input[name="departmentName"]').val('');
            $('input[name="isParent"]').val('');
        }

        $('#submitDepartment').on('click', function (e) {
            var departmentId = $('input[name="departmentId"]').val();
            var parentDepartmentId = $('input[name="parentDepartmentId"]').val();
            var departmentName = $('input[name="departmentName"]').val();
            var isParent = $('input[name="isParent"]').val();

            var obj = [];
            var addData = {};
            addData['departmentId'] = departmentId;
            addData['parentDepartmentId'] = parentDepartmentId;
            addData['departmentName'] = departmentName;
            addData['isParent'] = isParent;
            console.log(addData);

            if (departmentName == '') {
                bootbox.alert({
                    className: 'span4 alert-error',
                    buttons: {
                        ok: {
                            label: '确定',
                            className: 'btn blue'
                        }
                    },
                    message: "部门名称不能为空",
                    callback: function () {

                    },
                    title: "错误提示"
                });
            } else {
                obj.push(StringUtil.decorateRequestData('DepartmentDTO', addData));
                //进度条
                $('#progressBar').modal('show', true);
                $.ajax({
                    type: "POST",
                    url: SMController.getUrl({
                        controller: 'controllerProxy',
                        method: 'callBack',
                        proxyClass: 'securityController',
                        proxyMethod: departmentId == '' ? 'insertDepartment' : 'updateDepartment',
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
                                $('#departmentModal').modal('hide');
                                $.ajax({
                                    type: "POST",
                                    url: SMController.getUrl({
                                        controller: 'controllerProxy',
                                        method: 'callBack',
                                        proxyClass: 'securityController',
                                        proxyMethod: 'getDepartmentTreeList',
                                        jsonString: null
                                    }),
                                    dataType: "json",
                                    success: function (result) {
                                        zTreeObj = $.fn.zTree.init($("#departmentTree"), setting, result);
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
    }

    var handleTree = function() {
        function clearModalData(){
            $('input[name="departmentId"]').val('');
            $('input[name="parentDepartmentId"]').val('');
            $('input[name="parentDepartmentName"]').val('');
            $('input[name="departmentName"]').val('');
            $('input[name="isParent"]').val('');
        }

        $.ajax({
            type: "POST",
            url: SMController.getUrl({
                controller: 'controllerProxy',
                method: 'callBack',
                proxyClass: 'securityController',
                proxyMethod: 'getDepartmentTreeList',
                jsonString: null
            }),
            dataType: "json",
            success: function (result) {
                zTreeObj = $.fn.zTree.init($("#departmentTree"), setting, result);
            }
        });
    }

    return {
        init: function () {
            handleButton();
            handleTree();
        }
    };
}();