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
    var selectFiles = [];
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
            { "sTitle": '<input type="checkbox" id="filesCheckAll"/>',"bSortable":false,"sWidth": "12px" },
            { "sTitle": "附件ID", "mData": "attachmentId","bVisible":false},
            { "sTitle": "材料名称","mData": "attachmentName"},
            { "sTitle": "上传人", "mData": "employeeName"},
            { "sTitle": "上传部门", "mData": "departmentName"},
            { "sTitle": "会议主题", "mData": "meetingSubject"},
            { "sTitle": "会议时间", "mData": "meetingStartTime"},
            { "sTitle": "会议状态", "mData": "meetingStateName"}
        ];
        var oTable =  $('#dt_issues').dataTable({
            "aoColumns": tableHead,
            "serverSide": true,
            "bAutoWidth": false,
            "responsive": true,
            "ordering": true,
            "lengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
            "lengthChange": true,
            "paging": true,
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
                //先清空之前的选项
                selectFiles = [];
                $('#dt_issues :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
                var tmpTableNodes = oTable.fnGetNodes();
                for(var i = 0; i < tmpTableNodes.length; i++)
                    selectFiles.push(oTable.fnGetData(tmpTableNodes[i]).attachmentId);//fnGetData获取一行的数据
            }else{
                $('#dt_issues :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
                selectFiles = [];
            }
        });

        //根据复选框的值来获得行数据
        $('#dt_issues tbody').on('click','tr', function () {
            var isCheck = this.getElementsByTagName('input').item(0).checked ;
            if(isCheck)
                selectFiles.push(oTable.fnGetData(this).attachmentId);
            else
                selectFiles.remove(oTable.fnGetData(this).attachmentId);
        });
    }

    var handleButton = function () {
        //“下载”按钮
        $('#attachmentDownloadBtn').on('click', function (e) {
            var obj = [];
            obj.push(StringUtil.decorateRequestData('List',selectFiles));
            $.ajax({
                type:'post',
                dataType:"json",
                url: SMController.getUrl({controller:'controllerProxy',method:'callBack'
                    ,proxyClass:'attachmentController',proxyMethod:'downloadFile',
                    jsonString:MyJsonUtil.obj2str(obj)}),
                success:function(result){
                    console.log(result);
                    window.location.href = '../../../'+result;
                }
            });
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