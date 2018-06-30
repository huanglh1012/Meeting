/**
 * FileName: userList.js
 * File description: 用于加载用户列表页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
 */

/**
 * userList所有属性和方法定义
 * @type {userList}
 */
var userList = function () {
    var oTable = null;
    var selectTr = null;

    var handleSelect2 = function () {
        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getDepartmentGroupList',jsonString:null}),
            success:function(result){
                $('input[name="departmentId"]').select2({
                    multiple: true,
                    allowClear:true,
                    width:'100%',
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
                $('input[name="postId"]').select2({
                    multiple: true,
                    allowClear:true,
                    width:'100%',
                    data:result
                });
            }
        });
//        $('input[name="departmentId"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
//            ,proxyClass:'securityController',proxyMethod:'getDepartmentGroupList',jsonString:null});
//        $('input[name="postId"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
//            ,proxyClass:'securityController',proxyMethod:'getPostList',jsonString:null});
    }

    var handleDatePicker = function () {
        $("#startDate, #endDate").datepicker({
            format: "yyyy-mm-dd",
            minViewMode: "days",
            todayHighlight : 1,
            autoclose: true
        }).on("changeDate", function(e) {
                if ($(e.target).data("type") === "start") {
                    $("#endDate").datepicker("setStartDate", e.date);
                } else {
                    $("#startDate").datepicker("setEndDate", e.date);
                }
            });
    }

    var handleButton = function () {
        $('#modifyUserBtn').on('click', function (e) {
            if (selectTr == null) {
                $.pnotify({
                    text: '请选择需要修改的用户信息'
                });
            }else{
                window.location.href='user_new.html?employeeId='+ selectTr.employeeId;
            }
        });

        $('#deleteUserBtn').on('click', function (e) {
            if(selectTr != null){
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
                    message: '确定删除【'+selectTr.employeeName+'】用户吗 ?',
                    title: "消息提示",
                    callback: function(result) {
                        if(result) {
                            var obj = [];
                            obj.push(StringUtil.decorateRequestData('String',selectTr.employeeId));
                            $.ajax({
                                type:'post',
                                dataType:"json",
                                async: false,
                                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                    ,proxyClass:'securityController',proxyMethod:'deleteEmployee',jsonString:MyJsonUtil.obj2str(obj)}),
                                success:function(result){
                                    if(result.success){
                                        selectTr = null;
                                        oTable.api().ajax.reload();
                                        $.pnotify({
                                            text: result.msg
                                        });
                                    }else{
                                        $.pnotify({
                                            text: "该用户已被使用，无法删除",
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
                    text: '请选择需要删除的用户信息'
                });
            }
        });

        $('#modifyPasswordBtn').on('click', function (e) {
            if (selectTr == null) {
                $.pnotify({
                    text: '请选择需要修改密码的用户信息'
                });
            }else{
                clearModalData();
                $('#modifyUserPasswordModal').modal('show',true);
            }
        });

        $('#modifyPasswordConfirmBtn').on('click', function (e) {
            var newPassword = $('input[name="newEmployeePassword"]').val();
            var passwordConfirm = $('input[name="employeePasswordConfirm"]').val();
            if (newPassword == '' || passwordConfirm == '') {
                $('#errorTips').text("新密码和确认密码不能为空");
            } else {
                if (newPassword != passwordConfirm) {
                    $('#errorTips').text("密码不一致, 请确保确认密码和新密码一致");
                } else {
                    var obj = [];
                    var addData = {};
                    addData.newPassword = newPassword;
                    addData.passwordConfirm = passwordConfirm;
                    addData.employeeId = selectTr.employeeId;
                    obj.push(StringUtil.decorateRequestData('EmployeeDTO', addData));

                    $.ajax({
                        type: "POST",
                        url: SMController.getUrl({
                            controller: 'controllerProxy',
                            method: 'callBack',
                            proxyClass: 'securityController',
                            proxyMethod: 'modifyPassword',
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
                                $("#processStatus").text("密码修改成功，正在返回上一页面...");
                                setTimeout(function(){
                                    $.unblockUI();
                                    $('#modifyUserPasswordModal').modal('hide');
                                    $.pnotify({
                                        text: result.msg
                                    });
                                }, 1500);
                            } else {
                                $.unblockUI();
                                bootbox.alert({
                                    className: 'span4 alert-error',
                                    buttons: {
                                        ok: {
                                            label: '确定',
                                            className: 'btn blue'
                                        }
                                    },
                                    message: result.msg,
                                    callback: function () {

                                    },
                                    title: "错误提示"
                                });
                            }
                        }
                    });
                }
            }
        });

        function clearModalData(){
            $('#errorTips').text("");
            $('input[name="newEmployeePassword"]').val('');
            $('input[name="employeePasswordConfirm"]').val('');
        }
    }

    var handleTable = function () {
        // 表头定义
        var tableHead = [
            { "sTitle": "用户ID", "mData": "employeeId","bVisible":false},
            { "sTitle": "姓名","mData": "employeeName","type" :"string" },
            { "sTitle": "身份证号", "mData": "identifyCardNumber","type":"string"},
            { "sTitle": "性别", "mData": "sexName","type" :"string" },
            { "sTitle": "账号", "mData": "login","type" :"string"},
            { "sTitle": "部门", "mData": "departmentName","type" :"departmentCombo"},
            { "sTitle": "职务", "mData": "postName","type":"postCombo" },
            { "sTitle": "角色", "mData": "roleName" ,"type" :"roleCombo"},
            { "sTitle": "联系电话", "mData": "telephone","type" :"string"}
        ];

        oTable =  $('#dt_issues').dataTable({
            "aoColumns": tableHead,
            "serverSide": true,
            "aLengthMenu":[ 10, 25, 50,100],
            "bAutoWidth" : true,
//            "bSort": true,
//            ordering: true,
            searching: true,
            order: [[2, 'asc']],
            //默认显示的分页数
            "iDisplayLength": 10,
            "lengthChange": true,
            "paging": true,
            "sDom": "<'dt-top-row'><'dt-wrapper't><'dt-row dt-bottom-row'<'row'<'col-sm-4'i><'col-sm-8 text-right'p>><'row'<'col-xs-12 col-sm-12 col-md-12 col-lg-12'l>>>",
            "ajax": {
                url: "../../controllerProxy.do?method=callBack",
                type: "POST",
                dataSrc: "data",
                data: $.proxy(searchCommon.setDatatableData, searchCommon)
            }
        });

        $('#dt_issues tbody').on('click','tr', function () {
            if ($(this).hasClass("highlight")){
                $(this).removeClass("highlight");
                selectTr = null;
            } else {
                oTable.$('tr.highlight').removeClass("highlight");
                $(this).addClass("highlight");
                selectTr = oTable.fnGetData(this);
            }
        });
    }

    return {
        _select2InitValue: {
            sexId: [{
                "id": "0",
                "text": "男"
            }, {
                "id": "1",
                "text": "女"
            }]
        },
        init: function () {
            handleButton();
            handleSelect2();
            searchCommon.select2InitValue = this._select2InitValue;
            searchCommon.tableAjaxParam.proxyClass = "securityController";
            searchCommon.tableAjaxParam.proxyMethod = "getEmployeeListByCondition";
            searchCommon.bindingSearchEvent();
            searchCommon.init();
            common.loadDatatableSettings();
            handleDatePicker();
            handleTable();
        }
    };
}();