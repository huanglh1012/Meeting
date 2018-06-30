/**
 * FileName: meetingAttachmentList.js
 * File description: 用于加载会议材料列表页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
 */

/**
 * meetingAttachmentList所有属性和方法定义
 * @type {meetingAttachmentList}
 */
var meetingAttachmentList = function () {
    var selectTr = null;
    var oTable = null;

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
                $('input[name="employeeId"]').select2({
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
                $('input[name="departmentId"]').select2({
                    multiple: true,
                    allowClear:true,
                    width:'100%',
                    data:result
                });
            }
        });
    }

    var handleDatePicker = function () {
        $('input[name="meetingStartTimeDateStart"],input[name="meetingStartTimeDateEnd"],input[name="attachmentCreateTimeDateStart"],input[name="attachmentCreateTimeDateEnd"]').datepicker({
            format: "yyyy-mm-dd",
            language:'zh-CN',
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
            { "sTitle": '<input type="checkbox" id="filesCheckAll"/>',"bSortable":false,"sWidth": "30px" },
            { "sTitle": "附件ID", "mData": "attachmentId","bVisible":false},
            { "sTitle": "材料名称","mData": "attachmentName"},
            { "sTitle": "上传人", "mData": "employeeName"},
            { "sTitle": "上传部门", "mData": "departmentName"},
            { "sTitle": "会议主题", "mData": "meetingSubject"},
            { "sTitle": "会议时间", "mData": "meetingStartTime"},
            { "sTitle": "会议状态", "mData": "meetingStateName"}
        ];

        oTable =  $('#dt_issues').dataTable({
            "aoColumns": tableHead,
            "serverSide": true,
            "bAutoWidth": false,
            "responsive": true,
//            "ordering": true,
            "lengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
            "lengthChange": true,
            "paging": true,
            "sDom": "<'dt-top-row'><'dt-wrapper't><'dt-row dt-bottom-row'<'row'<'col-sm-4'i><'col-sm-8 text-right'p>><'row'<'col-xs-12 col-sm-12 col-md-12 col-lg-12'l>>>",
            "ajax": {
                url: "../../controllerProxy.do?method=callBack",
                type: "POST",
                dataSrc: "data",
                data: $.proxy(searchCommon.setDatatableData, searchCommon)
            },
            "columnDefs": [{
                "aTargets": [0],
                "mRender": function (data, type, full ) {
                    return'<input type="checkbox" class="checkboxes"/>';
                }
            }]
        });

        //复选框全选
        $('#filesCheckAll').on('click', function (e) {
            var isCheck = $('#filesCheckAll').prop('checked');
            if(isCheck){
                $('#dt_issues :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
            }else{
                $('#dt_issues :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
            }
        });
    }

    var handleButton = function () {
        //“下载”按钮
        $('#attachmentDownloadBtn').on('click', function (e) {
            var isDownload = true;
            var selectMeetingRecordFiles = [];
            var tmpLastSelectMeetingRecordFile = null;
            $('#dt_issues :checkbox').each(function(){
                if($(this).prop("checked") && $(this).prop("id") == "") {
                    var tr = $(this).parents('tr');
                    var tmpRowData = oTable.fnGetData(tr);
                    var obj = [];
                    obj.push(StringUtil.decorateRequestData('String',tmpRowData.meetingId));
                    $.ajax({
                        type:'post',
                        Type:"json",
                        async:false,
                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                            ,proxyClass:'meetingController',proxyMethod:'getMeetingInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
                        success:function(result){
                            var tmpJsonObject = JSON.parse(result);
                            // 如果不是发起人、管理员、领导，则只允许下载自己的会议材料
                            if(JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                                || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1
                                || JSON.parse(sessionStorage.getItem("EmployeeDTO")).roleIdList.indexOf('1') > -1
                                || JSON.parse(sessionStorage.getItem("EmployeeDTO")).employeeId == tmpJsonObject.meetingCreator
                                || JSON.parse(sessionStorage.getItem("EmployeeDTO")).employeeId == tmpRowData.employeeId) {
                                selectMeetingRecordFiles.push(tmpRowData.attachmentId);
                                tmpLastSelectMeetingRecordFile = tmpRowData.attachmentName;
                            } else {
                                $.pnotify({
                                    text: '只允许下载自己的会议材料'
                                });
                                isDownload = false;
                                return false;
                            }
                        }
                    });
                }
            });
            if (isDownload) {
                if (selectMeetingRecordFiles.length == 0) {
                    $.pnotify({
                        text: '请勾选需要下载的记录'
                    });
                }else{
                    $.blockUI({
                        message: '<div class="progress progress-lg progress-striped active" style="margin-bottom: 0px;">' +
                            '<div style="width: 100%" role="progressbar" class="progress-bar bg-color-darken">' +
                            '<span id="processStatus" style="position: relative; top: 5px;font-size:15px;">正在下载文件，请稍候...</span></div>' +
                            '</div>'
                    });
                    var obj = [];
                    obj.push(StringUtil.decorateRequestData('List',selectMeetingRecordFiles));
                    // 下载文件数是否为1、文件名后缀是否为文本类型,使用文件流返回的方式下载文件（解决浏览器下载时直接打开文件的问题）
                    if (selectMeetingRecordFiles.length == 1 && isTextType(tmpLastSelectMeetingRecordFile)) {
                        var url = SMController.getUrl({controller:'controllerProxy',method:'callBackByRequestAndResponse'
                            ,proxyClass:'attachmentController',proxyMethod:'downloadFile',
                            jsonString:MyJsonUtil.obj2str(obj)},"../../");
                        var temp = document.createElement("form");
                        temp.action = url;
                        temp.method = "post";
                        temp.style.display = "none";
                        document.body.appendChild(temp);
                        temp.submit();
                        $.unblockUI();
                    } else {
                        $.ajax({
                            type:'post',
                            dataType:"json",
                            url: SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                ,proxyClass:'attachmentController',proxyMethod:'downloadFile',
                                jsonString:MyJsonUtil.obj2str(obj)}),
                            success:function(result){
                                $.unblockUI();
                                window.location.href = '../../../'+result;
                            }
                        });
                    }
                }
            }
        });

        $('#viewMeetingBtn').on('click', function (e) {
            var selectMeetingRecordFiles = [];
            $('#dt_issues :checkbox').each(function(){
                if($(this).prop("checked") && $(this).prop("id") == "") {
                    var tr = $(this).parents('tr');
                    var tmpRowData = oTable.fnGetData(tr);
                    selectMeetingRecordFiles.push(tmpRowData);
                }
            });

            if (selectMeetingRecordFiles.length != 1 ) {
                $.pnotify({
                    text: '请勾选一条需要查看的记录'
                });
            }else{
                var obj = [];
                obj.push(StringUtil.decorateRequestData('String',selectMeetingRecordFiles[0].meetingId));
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
                                text: '你不是管理员和领导角色或者不是该会议的参与者和发起人，没有权限查看该会议'
                            });
                        }
                    }
                });
            }
        });
    }
    return {
        init: function () {
            searchCommon.tableAjaxParam.proxyClass = "meetingController";
            searchCommon.tableAjaxParam.proxyMethod = "getMeetingAttachmentListByCondition";
            searchCommon.bindingSearchEvent();
            common.loadDatatableSettings();
            handleSelect2();
            handleDatePicker();
            handleTable();
            handleButton();
        }
    };
}();