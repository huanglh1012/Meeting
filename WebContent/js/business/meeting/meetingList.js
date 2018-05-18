/**
 * FileName: meetingList.js
 * File description: 用于加载会议列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * meetingList所有属性和方法定义
 * @type {meetingList}
 */
var meetingList = function () {
    var oTable = null;
    var selectTr = null;

    var handleSelect2 = function () {
        $('input[name="meetingCreator"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
            ,proxyClass:'securityController',proxyMethod:'getEmployeeList',jsonString:null});
        $('input[name="meetingParticipant"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
            ,proxyClass:'securityController',proxyMethod:'getEmployeeList',jsonString:null});
        $('input[name="meetingPresenter"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
            ,proxyClass:'securityController',proxyMethod:'getEmployeeList',jsonString:null});

        $('input[name="meetingCreatorDepartmentId"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
            ,proxyClass:'securityController',proxyMethod:'getDepartmentGroupList',jsonString:null});
        $('input[name="meetingParticipantDepartmentId"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
            ,proxyClass:'securityController',proxyMethod:'getDepartmentGroupList',jsonString:null});

        $('input[name="meetingRoomId"]')[0].dataset.url = SMController.getUrl({controller:'controllerProxy',method:'callBack'
            ,proxyClass:'meetingController',proxyMethod:'getMeetingRoomList',jsonString:null});
    }

    var handleButton = function () {
        $('#modifyMeetingBtn').on('click', function (e) {
            if (selectTr == null) {
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
                window.location.href='meeting_new.html?meetingId='+ selectTr.meetingId;
            }
        });

        $('#closeMeetingBtn').on('click', function (e) {
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
                                            text: '关闭成功'
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

        $('#deleteMeetingBtn').on('click', function (e) {
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
                    message: '确定删除该会议信息吗 ?',
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
            "ordering": true,
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
    }

    return {
        _select2InitValue: {
            meetingStatusId: [{
                "id": "0",
                "text": "已发起"
            }, {
                "id": "1",
                "text": "已结束"
            }]
        },
        init: function () {
            handleButton();
            handleSelect2();
            searchCommon.select2InitValue = this._select2InitValue;
            searchCommon.tableAjaxParam.proxyClass = "meetingController";
            searchCommon.tableAjaxParam.proxyMethod = "getMeetingListByCondition";
            searchCommon.bindingSearchEvent();
            searchCommon.init();
            common.loadDatatableSettings();
            handleDatePicker();
            handleTable();
        }
    };
}();