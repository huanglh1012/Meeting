/**
 * FileName: departmentList.js
 * File description: 用于加载部门列表页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
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
    };

    var handleButton = function() {
        $('#add_department').on('click', function (e) {
            clearModalData();
            if(zTreeObj.getSelectedNodes().length > 0) {
                var treeId = zTreeObj.getSelectedNodes()[0].id;
                var treeName = zTreeObj.getSelectedNodes()[0].name;
                $('input[name="parentDepartmentId"]').val(treeId);
                $('input[name="parentDepartmentName"]').val(treeName);
                $('#departmentModal').modal('show',true);
            } else {
                $.pnotify({
                    text: '请选择任意部门进行操作'
                });
            }
        });

        $('#modify_department').on('click', function (e) {
            clearModalData();
            if(zTreeObj.getSelectedNodes().length > 0) {
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
            } else {
                $.pnotify({
                    text: '请选择需要修改的部门'
                });
            }
        });

        $('#delete_department').on('click', function (e) {
            if(zTreeObj.getSelectedNodes()[0] != null) {
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
                    message: '确定删除【'+zTreeObj.getSelectedNodes()[0].name+'】部门吗 ?',
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
                                            text: result.msg
                                        });
                                    }else{
                                        $.pnotify({
                                            text: "部门已被使用，无法删除.",
                                            delay: 4000
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                $.pnotify({
                    text: '请选择需要删除的部门'
                });
            }
        });

        function clearModalData(){
            $('#errorTips').text("");
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
            if (departmentName == '') {
                $('#errorTips').text("部门名称不能为空");
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
                                $.pnotify({
                                    text: result.msg
                                });
                            }, 1500);

                        } else {
                            $.unblockUI();
                            $('#errorTips').text(result.msg);
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