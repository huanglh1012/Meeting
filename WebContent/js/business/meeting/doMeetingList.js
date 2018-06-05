/**
 * FileName: doMeetingList.js
 * File description: 用于加载待办会议列表页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
 */

/**
 * doMeetingList所有属性和方法定义
 * @type {doMeetingList}
 */
var doMeetingList = function () {
    var participantMeetingTable = null;
    var creatorMeetingTable = null;
    var participantMeetingSelectTr = null;
    var creatorMeetingSelectTr = null;
    var handleButton = function () {
        $('#viewJoinMeetingBtn').on('click', function (e) {
            if (participantMeetingSelectTr == null) {
                $.pnotify({
                    text: '请选择需要查看的会议信息'
                });
            }else{
                window.location.href='meeting_view.html?meetingId='+ participantMeetingSelectTr.meetingId;
            }
        });

        $('#modifyJoinMeetingBtn').on('click', function (e) {
            if (participantMeetingSelectTr == null) {
                $.pnotify({
                    text: '请选择需要修改的会议信息'
                });
            }else{
                window.location.href='meeting_new.html?meetingId='+ participantMeetingSelectTr.meetingId;
            }
        });

        $('#viewMeetingBtn').on('click', function (e) {
            if (creatorMeetingSelectTr == null) {
                $.pnotify({
                    text: '请选择需要查看的会议信息'
                });
            }else{
                window.location.href='meeting_view.html?meetingId='+ creatorMeetingSelectTr.meetingId;
            }
        });

        $('#modifyMeetingBtn').on('click', function (e) {
            if (creatorMeetingSelectTr == null) {
                $.pnotify({
                    text: '请选择需要修改的会议信息'
                });
            }else{
                if (creatorMeetingSelectTr.meetingStateId == '1') {
                    // 如果是管理员，则允许修改已关闭的会议
                    if(JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                        || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1) {
                        window.location.href='meeting_new.html?meetingId='+ creatorMeetingSelectTr.meetingId;
                    } else {
                        $.pnotify({
                            text: '该会议已关闭，不允许修改'
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
                    $.pnotify({
                        text: '该会议已关闭'
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
                                                delay: 4000
                                            });
                                        }
                                    }
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
            if(creatorMeetingSelectTr != null){
                if (creatorMeetingSelectTr.meetingStateId == '1') {
                    // 如果是管理员，则允许删除已关闭的会议
                    if(JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                        || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1) {
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
                            message: '确定删除【'+creatorMeetingSelectTr.meetingSubject+'】信息吗 ?',
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
                        message: '确定删除【'+creatorMeetingSelectTr.meetingSubject+'】会议吗 ?',
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
                                                delay: 4000
                                            });
                                        }
                                    }
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
        obj.push(StringUtil.decorateRequestData('String', JSON.parse(sessionStorage.getItem("EmployeeDTO")).employeeId));
        participantMeetingTable = $('#joinMeetingList').dataTable({
            //表头设置
            "aoColumns": tableHead,
            "aLengthMenu":[ 10, 25, 50,100],
            "bAutoWidth" : true,
            //默认显示的分页数
            "iDisplayLength": 10,
            "paging": true,
            "sDom": "<'dt-top-row'><'dt-wrapper't><'dt-row dt-bottom-row'<'row'<'col-sm-4'i><'col-sm-8 text-right'p>><'row'<'col-xs-12 col-sm-12 col-md-12 col-lg-12'l>>>",
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
            "iDisplayLength": 10,
            "paging": true,
            "sDom": "<'dt-top-row'><'dt-wrapper't><'dt-row dt-bottom-row'<'row'<'col-sm-4'i><'col-sm-8 text-right'p>><'row'<'col-xs-12 col-sm-12 col-md-12 col-lg-12'l>>>",
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

        $('#joinMeetingList tbody').on('click','tr', function () {
            if ($(this).hasClass("highlight")){
                $(this).removeClass("highlight");
                participantMeetingSelectTr = null;
            } else {
                participantMeetingTable.$('tr.highlight').removeClass("highlight");
                $(this).addClass("highlight");
                participantMeetingSelectTr = participantMeetingTable.fnGetData(this);
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

//        $('#joinMeetingList tbody').on('dblclick','tr', function () {
//            window.location.href='meeting_view.html?meetingId='+ participantMeetingTable.fnGetData(this).meetingId;
//        });
//
//        $('#myMeetingList tbody').on('dblclick','tr', function () {
//            window.location.href='meeting_view.html?meetingId='+ creatorMeetingTable.fnGetData(this).meetingId;
//        });
    }

    return {
        init: function () {
            handleButton();
            handleTable();
        }
    };
}();