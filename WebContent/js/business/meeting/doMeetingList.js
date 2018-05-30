/**
 * FileName: doMeetingList.js
 * File description: 用于加载待办会议列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * doMeetingList所有属性和方法定义
 * @type {doMeetingList}
 */
var doMeetingList = function () {
    var participantMeetingTable = null;
    var creatorMeetingTable = null;
    var creatorMeetingSelectTr = null;

    var handleButton = function () {
        $('#modifyMeetingBtn').on('click', function (e) {
            if (creatorMeetingSelectTr == null) {
                bootbox.alert({
                    className:'span4 alert-error',
                    buttons: {
                        ok: {
                            label: '确定',
                            className: 'btn blue'
                        }
                    },
                    message:'请选择需要修改的会议信息',
                    callback: function() {
                    },
                    title: "错误提示"
                });
            }else{
                if (creatorMeetingSelectTr.meetingStateId == '1') {
                    // 如果是管理员，则允许修改已关闭的会议
                    if(JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                        || JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1) {
                        window.location.href='meeting_new.html?meetingId='+ creatorMeetingSelectTr.meetingId;
                    } else {
                        bootbox.alert({
                            className:'span4 alert-error',
                            buttons: {
                                ok: {
                                    label: '确定',
                                    className: 'btn blue'
                                }
                            },
                            message:'该会议已关闭，不允许修改',
                            callback: function() {
                            },
                            title: "错误提示"
                        });
                    }
                } else {
                    window.location.href='meeting_new.html?meetingId='+ creatorMeetingSelectTr.meetingId;
                }
            }
        });

        $('#closeMeetingBtn').on('click', function (e) {
            if(creatorMeetingSelectTr != null){
                if (creatorMeetingSelectTr.meetingStateId == '1') {
                    bootbox.alert({
                        className:'span4 alert-error',
                        buttons: {
                            ok: {
                                label: '确定',
                                className: 'btn blue'
                            }
                        },
                        message:'该会议已关闭',
                        callback: function() {
                        },
                        title: "错误提示"
                    });
                } else {
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
                                obj.push(StringUtil.decorateRequestData('String',creatorMeetingSelectTr.meetingId));
                                $.ajax({
                                    type:'post',
                                    dataType:"json",
                                    async: false,
                                    url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                        ,proxyClass:'meetingController',proxyMethod:'closeMeeting',jsonString:MyJsonUtil.obj2str(obj)}),
                                    success:function(result){
                                        if(result.success){
                                            creatorMeetingTable.api().ajax.reload();
                                            $.pnotify({
                                                text: result.msg
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
            } else {
                bootbox.alert({
                    className:'span4 alert-error',
                    buttons: {
                        ok: {
                            label: '确定',
                            className: 'btn blue'
                        }
                    },
                    message:'请选择需要关闭的会议信息',
                    callback: function() {
                    },
                    title: "错误提示"
                });
            }
        });

        $('#deleteMeetingBtn').on('click', function (e) {
            if(creatorMeetingSelectTr != null){
                if (creatorMeetingSelectTr.meetingStateId == '1') {
                    // 如果是管理员，则允许删除已关闭的会议
                    if(JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                        || JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1) {
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
                            message: '确定删除该会议信息吗 ?',
                            title: "消息提示",
                            callback: function(result) {
                                if(result) {
                                    var obj = [];
                                    obj.push(StringUtil.decorateRequestData('String',creatorMeetingSelectTr.meetingId));
                                    $.ajax({
                                        type:'post',
                                        dataType:"json",
                                        async: false,
                                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                            ,proxyClass:'meetingController',proxyMethod:'deleteMeeting',jsonString:MyJsonUtil.obj2str(obj)}),
                                        success:function(result){
                                            if(result.success){
                                                creatorMeetingTable.api().ajax.reload();
                                                $.pnotify({
                                                    text: result.msg
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
                    } else {
                        bootbox.alert({
                            className:'span4 alert-error',
                            buttons: {
                                ok: {
                                    label: '确定',
                                    className: 'btn blue'
                                }
                            },
                            message:'该会议已关闭，不允许删除',
                            callback: function() {
                            },
                            title: "错误提示"
                        });
                    }
                } else {
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
                        message: '确定删除该会议信息吗 ?',
                        title: "消息提示",
                        callback: function(result) {
                            if(result) {
                                var obj = [];
                                obj.push(StringUtil.decorateRequestData('String',creatorMeetingSelectTr.meetingId));
                                $.ajax({
                                    type:'post',
                                    dataType:"json",
                                    async: false,
                                    url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                        ,proxyClass:'meetingController',proxyMethod:'deleteMeeting',jsonString:MyJsonUtil.obj2str(obj)}),
                                    success:function(result){
                                        if(result.success){
                                            creatorMeetingTable.api().ajax.reload();
                                            $.pnotify({
                                                text: result.msg
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
            } else {
                bootbox.alert({
                    className:'span4 alert-error',
                    buttons: {
                        ok: {
                            label: '确定',
                            className: 'btn blue'
                        }
                    },
                    message:'请选择需要删除的会议信息',
                    callback: function() {
                    },
                    title: "错误提示"
                });
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

        var obj = [];
        obj.push(StringUtil.decorateRequestData('String', JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId));
        participantMeetingTable = $('#joinMeetingList').dataTable({
            //表头设置
            "aoColumns": tableHead,
            "aLengthMenu":[ 10, 25, 50,100],
            "bAutoWidth" : true,
            //默认显示的分页数
            "iDisplayLength": 25,
            "oLanguage": { //国际化一些配置
                "sLoadingRecords" : "正在获取数据，请稍候...",
                "sLengthMenu" : "显示 _MENU_ 条",
                "sZeroRecords" : "没有您要搜索的内容",
                "sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
                "sInfoEmpty" : "记录数为0",
                "sInfoFiltered" : "(全部记录数 _MAX_ 条)",
                "sInfoPostFix" : "",
                "sSearch" : "搜索",
                "sUrl" : "",
                "oPaginate": {
                    "sFirst" : "第一页",
                    "sPrevious" : "上一页",
                    "sNext" : "下一页",
                    "sLast" : "最后一页"
                }
            },
            "ajax": {
                type:"POST",
                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                    ,proxyClass:'meetingController',proxyMethod:'getParticipantMeetingByEmployeeId',jsonString:MyJsonUtil.obj2str(obj)}),
                dataType:"json",
                success:function(data) {
                    participantMeetingTable.fnClearTable();
                    if (data.length > 0)
                        participantMeetingTable.fnAddData(data);
                }
            }
        });

        creatorMeetingTable = $('#myMeetingList').dataTable({
            //表头设置
            "aoColumns": tableHead,
            "aLengthMenu":[ 10, 25, 50,100],
            "bAutoWidth" : true,
            //默认显示的分页数
            "iDisplayLength": 25,
            "oLanguage": { //国际化一些配置
                "sLoadingRecords" : "正在获取数据，请稍候...",
                "sLengthMenu" : "显示 _MENU_ 条",
                "sZeroRecords" : "没有您要搜索的内容",
                "sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
                "sInfoEmpty" : "记录数为0",
                "sInfoFiltered" : "(全部记录数 _MAX_ 条)",
                "sInfoPostFix" : "",
                "sSearch" : "搜索",
                "sUrl" : "",
                "oPaginate": {
                    "sFirst" : "第一页",
                    "sPrevious" : "上一页",
                    "sNext" : "下一页",
                    "sLast" : "最后一页"
                }
            },
            "ajax": {
                type:"POST",
                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                    ,proxyClass:'meetingController',proxyMethod:'getCreatorMeetingByEmployeeId',jsonString:MyJsonUtil.obj2str(obj)}),
                dataType:"json",
                success:function(data) {
                    creatorMeetingTable.fnClearTable();
                    if (data.length > 0)
                        creatorMeetingTable.fnAddData(data);
                }
            }
        });

        $('#myMeetingList tbody').on('click','tr', function () {
            if ($(this).hasClass("highlight")){
                $(this).removeClass("highlight");
                creatorMeetingSelectTr = null;
            } else {
                creatorMeetingTable.$('tr.highlight').removeClass("highlight");
                $(this).addClass("highlight");
                creatorMeetingSelectTr = creatorMeetingTable.fnGetData(this);
            }
        });

        $('#joinMeetingList tbody').on('dblclick','tr', function () {
            window.location.href='meeting_view.html?meetingId='+ participantMeetingTable.fnGetData(this).meetingId;
        });

        $('#myMeetingList tbody').on('dblclick','tr', function () {
            window.location.href='meeting_view.html?meetingId='+ creatorMeetingTable.fnGetData(this).meetingId;
        });
    }

    return {
        init: function () {
            handleButton();
            handleTable();
        }
    };
}();