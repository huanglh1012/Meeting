/**
 * FileName: meetingAttachmentList.js
 * File description: 用于加载会议材料列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * meetingAttachmentList所有属性和方法定义
 * @type {meetingAttachmentList}
 */
var meetingAttachmentList = function () {
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
            { "sTitle": "附件ID", "mData": "attachmentId","bVisible":false},
            { "sTitle": "附件路径", "mData": "attachmentPath","bVisible":false},
            { "sTitle": "材料名称","mData": "attachmentName"},
            { "sTitle": "上传人", "mData": "employeeName"},
            { "sTitle": "上传部门", "mData": "departmentName"},
            { "sTitle": "会议主题", "mData": "meetingSubject"},
            { "sTitle": "会议时间", "mData": "meetingStartTime"},
            { "sTitle": "会议状态", "mData": "meetingStateName"}
        ];
        var oTable =  $('#meetingAttachmentList').dataTable({
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
            },
            "columnDefs": [{
                "targets": [0],
                "render": function(data, type, full) {
                    return '<a title="' + data + '" target="_blank" href="' + $.url_root + '/issue/viewIssueOfCard.jspa?issueId=' + full.issueId + '">' + data + '</a>';
                }
            }, {
                "targets": [1],
                "render": function(data, type, full) {
                    return '<a class="without-decoration font-default" title="' + data + '" href="javascript:;">' + data + '</a>';
                }
            }, {
                "targets": [2],
                "render": function(data, type, full) {
                    data = data || "";
                    return '<a class="without-decoration font-default" title="' + data + '" href="javascript:;">' + data + '</a>';
                }
            }]
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
        init: function () {
            searchCommon.tableAjaxParam.proxyClass = "meetingController";
            searchCommon.tableAjaxParam.proxyMethod = "getMeetingAttachmentListByCondition";
            searchCommon.bindingSearchEvent();
            common.loadDatatableSettings();
            handleSelect2();
            handleDatePicker();
            handleTable();
        }
    };
}();