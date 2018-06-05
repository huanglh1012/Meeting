/**
 * FileName: meetingList.js
 * File description: 用于加载会议列表页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
 */

/**
 * meetingList所有属性和方法定义
 * @type {meetingList}
 */
var meetingList = function () {
    var oTable = null;
    var selectTr = null;

    var handleSelect2 = function () {
        var meetingStateData = [{
            "id": "0",
            "text": "已发起"
        }, {
            "id": "1",
            "text": "已结束"
        }];

        $('input[name="meetingStateId"]').select2({
            allowClear:true,
            width:'100%',
            data:meetingStateData
        });

        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'meetingController',proxyMethod:'getMeetingRoomList',jsonString:null}),
            success:function(result){
                $('input[name="meetingRoomId"]').select2({
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
                ,proxyClass:'securityController',proxyMethod:'getEmployeeList',jsonString:null}),
            success:function(result){
                $('input[name="meetingCreator"],input[name="meetingParticipant"],input[name="meetingPresenter"]').select2({
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
                ,proxyClass:'securityController',proxyMethod:'getDepartmentGroupList',jsonString:null}),
            success:function(result){
                $('input[name="meetingCreatorDepartmentId"],input[name="meetingParticipantDepartmentId"]').select2({
                    multiple: true,
                    allowClear:true,
                    width:'100%',
                    data:result
                });
            }
        });
    }

    var handleButton = function () {
        var tmpEmployeeDTO = JSON.parse(sessionStorage.getItem("EmployeeDTO"));

        $('#modifyMeetingBtn').on('click', function (e) {
            if (selectTr == null) {
                $.pnotify({
                    text: '请选择需要修改的会议信息'
                });
            }else{
                if (selectTr.meetingStateId == '1') {
                    // 如果是管理员，则允许修改已关闭的会议
                    if(tmpEmployeeDTO.roleIdList.indexOf('-1') > -1
                        || tmpEmployeeDTO.roleIdList.indexOf('0') > -1) {
                        window.location.href='meeting_new.html?meetingId='+ selectTr.meetingId;
                    } else {
                        $.pnotify({
                            text: '该会议已关闭，不允许修改'
                        });
                    }
                } else {
                    var obj = [];
                    obj.push(StringUtil.decorateRequestData('String',selectTr.meetingId));
                    $.ajax({
                        type:'post',
                        Type:"json",
                        async:false,
                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                            ,proxyClass:'meetingController',proxyMethod:'getMeetingInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
                        success:function(result){
                            var tmpJsonObject = JSON.parse(result);
                            // 判断是否为参与人或发起人或管理员
                            if(tmpEmployeeDTO.roleIdList.indexOf('-1') > -1 || tmpEmployeeDTO.roleIdList.indexOf('0') > -1
                                || tmpJsonObject.meetingParticipants.indexOf(tmpEmployeeDTO.employeeId) > -1
                                || tmpEmployeeDTO.employeeId == tmpJsonObject.meetingCreator) {
                                window.location.href='meeting_new.html?meetingId='+ selectTr.meetingId;
                            } else {
                                $.pnotify({
                                    text: '你不是管理员角色或者不是该会议的发起人和参会人，没有权限修改会议信息.'
                                });
                            }
                        }
                    });
                }
            }
        });

        $('#closeMeetingBtn').on('click', function (e) {
            if(selectTr != null){
                if (selectTr.meetingStateId == '1') {
                    $.pnotify({
                        text: '该会议已关闭，无法操作.'
                    });
                } else {
                    var obj = [];
                    obj.push(StringUtil.decorateRequestData('String',selectTr.meetingId));
                    $.ajax({
                        type:'post',
                        Type:"json",
                        async:false,
                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                            ,proxyClass:'meetingController',proxyMethod:'getMeetingInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
                        success:function(result){
                            var tmpJsonObject = JSON.parse(result);
                            // 判断是否为管理员或发起人
                            if(tmpEmployeeDTO.roleIdList.indexOf('-1') > -1 || tmpEmployeeDTO.roleIdList.indexOf('0') > -1
                                || tmpEmployeeDTO.employeeId == tmpJsonObject.meetingCreator) {
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
                                    message: '确定关闭该会议信息吗 ?',
                                    title: "消息提示",
                                    callback: function(result) {
                                        if(result) {
                                            var obj = [];
                                            obj.push(StringUtil.decorateRequestData('String',selectTr.meetingId));
                                            $.ajax({
                                                type:'post',
                                                dataType:"json",
                                                async: false,
                                                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                                    ,proxyClass:'meetingController',proxyMethod:'closeMeeting',jsonString:MyJsonUtil.obj2str(obj)}),
                                                success:function(result){
                                                    if(result.success){
                                                        oTable.api().ajax.reload();
                                                        $.pnotify({
                                                            text: '会议关闭成功'
                                                        });
                                                    }else{
                                                        $.pnotify({
                                                            type:'error',
                                                            text: result.msg,
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
                                    text: '你不是管理员角色或者不是该会议的发起人，没有权限关闭会议信息.'
                                });
                            }
                        }
                    });
                }
            } else {
                $.pnotify({
                    text: '请选择需要关闭的会议信息'
                });
            }
        });

        $('#deleteMeetingBtn').on('click', function (e) {
            if(selectTr != null){
                if (selectTr.meetingStateId == '1') {
                    // 如果是管理员，则允许删除已关闭的会议
                    if(tmpEmployeeDTO.roleIdList.indexOf('-1') > -1 || tmpEmployeeDTO.roleIdList.indexOf('0') > -1) {
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
                            message: '确定删除【'+selectTr.meetingSubject+'】会议吗 ?',
                            title: "消息提示",
                            callback: function(result) {
                                if(result) {
                                    var obj = [];
                                    obj.push(StringUtil.decorateRequestData('String',selectTr.meetingId));
                                    $.ajax({
                                        type:'post',
                                        dataType:"json",
                                        async: false,
                                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                            ,proxyClass:'meetingController',proxyMethod:'deleteMeeting',jsonString:MyJsonUtil.obj2str(obj)}),
                                        success:function(result){
                                            if(result.success){
                                                oTable.api().ajax.reload();
                                                $.pnotify({
                                                    text: '会议删除成功'
                                                });
                                            }else{
                                                $.pnotify({
                                                    type:'error',
                                                    text: result.msg,
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
                            text: '该会议已关闭，不允许删除'
                        });
                    }
                } else {
                    var obj = [];
                    obj.push(StringUtil.decorateRequestData('String',selectTr.meetingId));
                    $.ajax({
                        type:'post',
                        Type:"json",
                        async:false,
                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                            ,proxyClass:'meetingController',proxyMethod:'getMeetingInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
                        success:function(result){
                            var tmpJsonObject = JSON.parse(result);
                            // 判断是否为管理员或发起人
                            if(tmpEmployeeDTO.roleIdList.indexOf('-1') > -1 || tmpEmployeeDTO.roleIdList.indexOf('0') > -1
                                || tmpEmployeeDTO.employeeId == tmpJsonObject.meetingCreator) {
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
                                    message: '确定删除【'+selectTr.meetingSubject+'】会议吗 ?',
                                    title: "消息提示",
                                    callback: function(result) {
                                        if(result) {
                                            var obj = [];
                                            obj.push(StringUtil.decorateRequestData('String',selectTr.meetingId));
                                            $.ajax({
                                                type:'post',
                                                dataType:"json",
                                                async: false,
                                                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                                    ,proxyClass:'meetingController',proxyMethod:'deleteMeeting',jsonString:MyJsonUtil.obj2str(obj)}),
                                                success:function(result){
                                                    if(result.success){
                                                        oTable.api().ajax.reload();
                                                        $.pnotify({
                                                            text: '会议删除成功'
                                                        });
                                                    }else{
                                                        $.pnotify({
                                                            type:'error',
                                                            text: result.msg,
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
                                    text: '你不是管理员角色或者不是该会议的发起人，没有权限删除会议信息'
                                });
                            }
                        }
                    });
                }
            } else {
                $.pnotify({
                    text: '请选择需要删除的会议信息'
                });
            }
        });

        $('#viewMeetingBtn').on('click', function (e) {
            if (selectTr == null) {
                $.pnotify({
                    text: '请选择需要查看的会议信息'
                });
            }else{
                var obj = [];
                obj.push(StringUtil.decorateRequestData('String',selectTr.meetingId));
                $.ajax({
                    type:'post',
                    Type:"json",
                    async:false,
                    url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                        ,proxyClass:'meetingController',proxyMethod:'getMeetingInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
                    success:function(result){
                        var tmpJsonObject = JSON.parse(result);
                        // 管理员、领导、发起人、参与人可以查看会议信息
                        if(JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                            || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1
                            || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('1') > -1
                            || tmpJsonObject.meetingParticipants.indexOf(JSON.parse(sessionStorage.getItem("EmployeeDTO")).employeeId) > -1
                            || JSON.parse(sessionStorage.getItem("EmployeeDTO")).employeeId == tmpJsonObject.meetingCreator) {
                            window.location.href='meeting_view.html?meetingId='+ tmpJsonObject.meetingId;
                        } else {
                            $.pnotify({
                                text: '你不是管理员和领导角色或者不是该会议的发起人和参会人，没有权限查看该会议'
                            });
                        }
                    }
                });
            }
        });
    }

    var handleDatePicker = function () {
        $("#meetingStartTime, #meetingEndTime").datepicker({
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

    var handleTable = function () {
        // 表头定义
        var tableHead = [
            { "sTitle": "会议ID", "mData": "meetingId","bVisible":false},
            { "sTitle": "会议主题","mData": "meetingSubject"},
            { "sTitle": "会议时间", "mData": "meetingStartTime"},
            { "sTitle": "会议地点", "mData": "meetingRoomName"},
            { "sTitle": "发起人", "mData": "meetingCreatorName"},
            { "sTitle": "发起部门", "mData": "meetingCreatorDepartmentName"},
            { "sTitle": "会议状态", "mData": "meetingStateName"}
        ];

        oTable =  $('#dt_issues').dataTable({
            "aoColumns": tableHead,
            "serverSide": true,
            "bAutoWidth": false,
            "responsive": true,
//            "ordering": true,
            "order": [[ 2, "ASC" ]],
            "lengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
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

//        $('#dt_issues tbody').on('dblclick','tr', function () {
//            var obj = [];
//            obj.push(StringUtil.decorateRequestData('String',oTable.fnGetData(this).meetingId));
//            $.ajax({
//                type:'post',
//                Type:"json",
//                async:false,
//                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
//                    ,proxyClass:'meetingController',proxyMethod:'getMeetingInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
//                success:function(result){
//                    var tmpJsonObject = JSON.parse(result);
//                    // 管理员、领导、发起人、参与人可以查看会议信息
//                    if(JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
//                        || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1
//                        || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('1') > -1
//                        || tmpJsonObject.meetingParticipants.indexOf(JSON.parse(sessionStorage.getItem("EmployeeDTO")).employeeId) > -1
//                        || JSON.parse(sessionStorage.getItem("EmployeeDTO")).employeeId == tmpJsonObject.meetingCreator) {
//                        window.location.href='meeting_view.html?meetingId='+ tmpJsonObject.meetingId;
//                    } else {
//                        bootbox.alert({
//                            className:'span4 alert-error',
//                            buttons: {
//                                ok: {
//                                    label: '确定',
//                                    className: 'btn blue'
//                                }
//                            },
//                            message:'你不是该会议的参与者或发起人或管理员，没有权限查看该会议',
//                            callback: function() {
//                            },
//                            title: "错误提示"
//                        });
//                    }
//                }
//            });
//        });
    }

    return {
        init: function () {
            handleButton();
            handleSelect2();
            searchCommon.tableAjaxParam.proxyClass = "meetingController";
            searchCommon.tableAjaxParam.proxyMethod = "getMeetingListByCondition";
            searchCommon.bindingSearchEvent();
            common.loadDatatableSettings();
            handleDatePicker();
            handleTable();
        }
    };
}();