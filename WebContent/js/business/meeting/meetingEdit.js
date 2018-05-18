/**
 * FileName: meetingEdit.js
 * File description: 用于加载和初始化会议配置页面的组件及内容
 * Copyright (c) 2016 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2016-10-18
 */

/**
 * meetingEdit
 * @type {meetingEdit}
 */
var meetingEdit = function () {
    var zTreeObj = null;
    var selectedTreeNode = null;
    var searchLocationIndex = 0;
    var meetingRecordFilesTable = null;
    var meetingFilesTable = null;
    var selectMeetingRecordFiles = [];
    var selectMeetingFiles = [];

    var handleDatePickers = function () {
        $("#messageNoticeTime,#meetingStartTime,#meetingEndTime,#meetingProposeTime").datetimepicker({
            language:'zh-CN',
            format: "yyyy-mm-dd hh:ii:ss"
        });

//        $("#meetingProposeTime").datepicker({
//            format: "yyyy-mm-dd",
//            minViewMode: "days",
//            todayHighlight : 1,
//            autoclose: true
//        }).on("changeDate", function(e) {
//                if ($(e.target).data("type") === "start") {
//                    $("#endDate").datepicker("setStartDate", e.date);
//                } else {
//                    $("#startDate").datepicker("setEndDate", e.date);
//                }
//            });
    }

//    var handleTimePickers = function () {
//        if (jQuery().timepicker) {
//            $('.timepicker-24').timepicker({
//                minuteStep: 1,
//                showSeconds: true,
//                showMeridian: false
//            });
//        }
//    }

    var handleSelect2 = function () {
        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getEmployeeList',jsonString:null}),
            success:function(result){
                $('#meetingPresenter').select2({
                    placeholder: "请选择主持人",
                    allowClear:true,
                    data:result
                });
                $('#meetingCreator').select2({
                    placeholder: "请选择发起人",
                    allowClear:true,
                    data:result
                });
            }
        });

        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'meetingController',proxyMethod:'getMeetingRoomList',jsonString:null}),
            success:function(result){
                $('#meetingRoomId').select2({
                    placeholder: "请选择会议室",
                    allowClear:true,
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
                $('#meetingCreatorDepartmentId').select2({
                    placeholder: "请选择部门",
                    allowClear:true,
                    data:result
                });
            }
        });
    }

    var handleTree = function() {
        var setting = {
            view: {
//                addHoverDom: addHoverDom,
//                removeHoverDom: removeHoverDom,
                selectedMulti: false,
                fontCss: function (treeId, treeNode) {
                    return treeNode.highlight == true ? {color:"red", "font-weight": "bold"} : {color:"#000", "font-weight": "normal"};
                }
            },
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            edit: {
                enable: false
            }
//            callback : {
//                onClick : zTreeOnClickRight
//            }
        };

        $.ajax({
            type: "POST",
            url: SMController.getUrl({
                controller: 'controllerProxy',
                method: 'callBack',
                proxyClass: 'securityController',
                proxyMethod: 'getDepartmentEmployeeTreeList',
                jsonString: null
            }),
            dataType: "json",
            success: function (result) {
                zTreeObj = $.fn.zTree.init($("#departmentEmployeeTree"), setting, result);
            }
        });
    }

    var handleForm = function () {
        $("#createMeetingForm").validate({
            errorElement: "strong",
            ignore: ":not('.required-validation')",
            errorClass: "note_error text-danger",
            focusCleanup: true,
            focusInvalid: false,
            rules: {
                meetingSubject:{
                    required: true
                },
                meetingRoomId:{
                    required: true
                },
                meetingStartTime:{
                    required: true
                },
                meetingEndTime:{
                    required: true
                },
                meetingPresenter:{
                    required: true
                },
                meetingProposeTime:{
                    required: true
                },
                meetingCreator:{
                    required: true
                },
//                meetingCreatorDepartmentId:{
//                    required: true
//                },
                meetingParticipantNames:{
                    required: true
                }
            }, // Messages for form
            messages: {
                meetingSubject:{
                    required:"会议主题不能为空！！！"
                },
                meetingRoomId:{
                    required:"会议地点不能为空！！！"
                },
                meetingStartTime:{
                    required:"会议开始时间不能为空！！！"
                },
                meetingEndTime:{
                    required:"会议结束时间不能为空！！！"
                },
                meetingPresenter:{
                    required:"主持人不能为空！！！"
                },
                meetingProposeTime:{
                    required:"发起时间不能为空！！！"
                },
                meetingCreator:{
                    required:"发起人不能为空！！！"
                },
                meetingCreatorDepartmentId:{
                    required:"发起部门不能为空！！！"
                },
                meetingParticipantNames:{
                    required:"参会部门或人员不能为空！！！"
                }
            },

            highlight: function(element, errorClass) {
                $(element).parent().addClass("has-error");
            },

            unhighlight: function(element, errorClass) {
                $(element).parent().removeClass("has-error");
            },

            submitHandler: function(form) { //验证通过时触发
                var meetingId = $('input[name="meetingId"]').val();
                var obj = [];
                // 隐藏域赋值
                $('input[name="employeeId"]').val("B1B573CA788041688384814FFD286D3F");
                var addData = DomUtil.getJSONObjectFromForm('createMeetingForm', null);
                // 添加会议记录附件表格记录
                var tmpMeetingRecordFileList = [];
                var tmpMeetingRecordFilesTableNodes = meetingRecordFilesTable.fnGetNodes();
                for(var i = 0; i < tmpMeetingRecordFilesTableNodes.length; i++) {
                    var tmpAttachmentId = meetingRecordFilesTable.fnGetData(tmpMeetingRecordFilesTableNodes[i]).attachmentId;
                    tmpMeetingRecordFileList.push(tmpAttachmentId);
                }
                // 添加会议附件表格记录
                var tmpMeetingFileList = [];
                var tmpMeetingFilesTableNodes = meetingFilesTable.fnGetNodes();
                for(var i = 0; i < tmpMeetingFilesTableNodes.length; i++) {
                    var tmpAttachmentId = meetingFilesTable.fnGetData(tmpMeetingFilesTableNodes[i]).attachmentId;
                    tmpMeetingFileList.push(tmpAttachmentId);
                }
                addData.meetingRecordFileList = tmpMeetingRecordFileList;
                addData.meetingFileList = tmpMeetingFileList;
                obj.push(StringUtil.decorateRequestData('MeetingDTO', addData));
                console.log(addData);
                // 会议时间重复时需要询问用户是否确认发起会议
                $.ajax({
                    type:'post',
                    dataType:"json",
                    url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                        ,proxyClass:'meetingController',proxyMethod:'isMeetingExistByPlanDatetimeRang',jsonString:MyJsonUtil.obj2str(obj)}),
                    success:function(result){
                        console.log(result);
                        if (!result.success) {
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
                                message: result.msg.actionResultMessage,
                                title: "消息提示",
                                callback: function(result) {
                                    if(result) {
                                        submitMeeting(addData);
                                    }
                                }
                            });
                        } else {
                            submitMeeting(addData);
                        }
                    }
                });
            }, // Do not change code below

            errorPlacement: function(error, element) {
                error.appendTo(element.parent());
            }
        });

        function submitMeeting(inMeetingDTO) {
            var obj = [];
            obj.push(StringUtil.decorateRequestData('MeetingDTO', inMeetingDTO));
            $.ajax({
                type: "POST",
                url: SMController.getUrl({
                    controller: 'controllerProxy',
                    method: 'callBack',
                    proxyClass: 'meetingController',
                    proxyMethod: inMeetingDTO.meetingId == "" ? 'insertMeeting' : 'updateMeeting',
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
                            window.location.href = 'meeting_list.html';
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
    }

    var handleTable = function () {
        // 表头定义
        var meetingFilesTableHead = [
            { "sTitle": '<input type="checkbox" id="meetingFilesCheckAll"/>',"bSortable":false,"sWidth": "12px" },
            { "sTitle": "附件ID", "mData": "attachmentId","bVisible":false},
            { "sTitle": "材料名称","mData": "attachmentName"},
            { "sTitle": "上传人", "mData": "employeeName"},
            { "sTitle": "上传部门", "mData": "employeeDepartmentName"},
            { "sTitle": "上传时间", "mData": "attachmentCreateTime"}//,
//            { "sTitle": "操作"}
        ];

        var meetingRecordFilesTableHead = [
            { "sTitle": '<input type="checkbox" id="meetingRecordFilesCheckAll"/>',"bSortable":false,"sWidth": "12px" },
            { "sTitle": "附件ID", "mData": "attachmentId","bVisible":false},
            { "sTitle": "材料名称","mData": "attachmentName"},
            { "sTitle": "上传人", "mData": "employeeName"},
            { "sTitle": "上传部门", "mData": "employeeDepartmentName"},
            { "sTitle": "上传时间", "mData": "attachmentCreateTime"}//,
//            { "sTitle": "操作"}
        ];

        meetingFilesTable = $('#meetingFiles').dataTable({
            //表头设置
            "aoColumns": meetingFilesTableHead,
            "bAutoWidth" : true,
            "paging": false,
            "info": false,
            "oLanguage": { //国际化一些配置
                "sLoadingRecords" : "正在获取数据，请稍候...",
                "sZeroRecords" : "没有您要搜索的内容"
            },
            "aoColumnDefs": [
                {   //第一列的值为combox
                    "aTargets": [0],
                    "mRender": function (data, type, full ) {
                        return'<input type="checkbox" class="checkboxes"/>';
                    }
                }/*, {   //第七列的值为链接
                    "aTargets": [6],
                    "mRender": function (data, type, full ) {
                        return'<a class="delete btn mini green" ><i class="fa fa-trash-o"></i> 删除</a>';
                    }
                }*/
            ]
            //,
//            "ajax": {
//                type:"POST",
//                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
//                    ,proxyClass:'securityController',proxyMethod:'getPostList',jsonString:null}),
//                dataType:"json",
//                success:function(data) {
//                    meetingFilesTable.fnClearTable();
//                    meetingFilesTable.fnAddData(data);
//                }
//            }
        });

        meetingRecordFilesTable = $('#meetingRecordFiles').dataTable({
            //表头设置
            "aoColumns": meetingRecordFilesTableHead,
            "bAutoWidth" : true,
            "paging": false,
            "info": false,
            "oLanguage": { //国际化一些配置
                "sLoadingRecords" : "正在获取数据，请稍候...",
                "sZeroRecords" : "没有您要搜索的内容"
            },
            "aoColumnDefs": [
                {   //第一列的值为combox
                    "aTargets": [0],
                    "mRender": function (data, type, full ) {
                        return'<input type="checkbox" class="checkboxes"/>';
                    }
                }/*, {   //第七列的值为链接
                 "aTargets": [6],
                 "mRender": function (data, type, full ) {
                 return'<a class="delete btn mini green" ><i class="fa fa-trash-o"></i> 删除</a>';
                 }
                 }*/
            ]
            //,
//            "ajax": {
//                type:"POST",
//                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
//                    ,proxyClass:'securityController',proxyMethod:'getPostList',jsonString:null}),
//                dataType:"json",
//                success:function(data) {
//                    meetingFilesTable.fnClearTable();
//                    meetingFilesTable.fnAddData(data);
//                }
//            }
        });

        //复选框全选
        $('#meetingFilesCheckAll').on('click', function (e) {
            var isCheck = $('#meetingFilesCheckAll').prop('checked');
            if(isCheck){
                //先清空之前的选项
                selectMeetingFiles = [];
                $('#meetingFiles :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
                var tmpTableNodes = meetingFilesTable.fnGetNodes();
                for(var i = 0; i < tmpTableNodes.length; i++)
                    selectMeetingFiles.push(meetingFilesTable.fnGetData(tmpTableNodes[i]).attachmentId);//fnGetData获取一行的数据
            }else{
                $('#meetingFiles :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
                selectMeetingFiles = [];
            }
        });

        //复选框全选
        $('#meetingRecordFilesCheckAll').on('click', function (e) {
            var isCheck = $('#meetingRecordFilesCheckAll').prop('checked');
            if(isCheck){
                //先清空之前的选项
                selectMeetingRecordFiles = [];
                $('#meetingRecordFiles :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
                var tmpTableNodes = meetingRecordFilesTable.fnGetNodes();
                for(var i = 0; i < tmpTableNodes.length; i++)
                    selectMeetingRecordFiles.push(meetingRecordFilesTable.fnGetData(tmpTableNodes[i]).attachmentId);//fnGetData获取一行的数据
            }else{
                $('#meetingRecordFiles :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
                selectMeetingRecordFiles = [];
            }
        });

        //根据复选框的值来获得行数据
        $('#meetingFiles tbody').on('click','tr', function () {
            var isCheck = this.getElementsByTagName('input').item(0).checked ;
            if(isCheck)
                selectMeetingFiles.push(meetingFilesTable.fnGetData(this).attachmentId);
            else
                selectMeetingFiles.remove(meetingFilesTable.fnGetData(this).attachmentId);
        });

        $('#meetingRecordFiles tbody').on('click','tr', function () {
            var isCheck = this.getElementsByTagName('input').item(0).checked ;
            if(isCheck)
                selectMeetingRecordFiles.push(meetingRecordFilesTable.fnGetData(this).attachmentId);
            else
                selectMeetingRecordFiles.remove(meetingRecordFilesTable.fnGetData(this).attachmentId);
        });
    }

    var handleButton = function() {
        $('#meetingJoiner').on('click', function (e) {
            $('input[name="searchEmployeeText"]').val('');
            $('#meetingJoinerModal').modal('show',true);
        });

        $('#searchEmployeeBtn').on('click', function (e) {
            var tmpKeywords = $('input[name="searchEmployeeText"]').val();
            if (tmpKeywords != '')
            {
                var tmpNodeList = zTreeObj.getNodesByParamFuzzy("name", tmpKeywords, null);
                if (searchLocationIndex > tmpNodeList.length -1)
                    searchLocationIndex = 0;
                if (tmpNodeList.length > 0)
                    zTreeObj.selectNode(tmpNodeList[searchLocationIndex]);
            }
            searchLocationIndex++;
        });

        $('input[name="searchEmployeeText"]').bind('input propertychange', function() {
            searchLocationIndex = 0;
            // 先把全部节点更新为普通样式
            var tmpTreeAllNodes = zTreeObj.transformToArray(zTreeObj.getNodes());
            for(var i = 0; i < tmpTreeAllNodes.length; i++) {
                tmpTreeAllNodes[i].highlight = false;
                zTreeObj.updateNode(tmpTreeAllNodes[i]);
            }
            // 指定节点的样式更新为高亮显示，并展开
            var tmpKeywords = $('input[name="searchEmployeeText"]').val();
            if (tmpKeywords != '')
            {
                var tmpNodeList = zTreeObj.getNodesByParamFuzzy("name", tmpKeywords, null);
                for( var i = 0; i < tmpNodeList.length; i++) {
//                zTreeObj.selectNode(tmpNodeList[i]);
                    tmpNodeList[i].highlight = true;
                    zTreeObj.updateNode(tmpNodeList[i]);
                }
            }
            zTreeObj.expandAll(true);
        });

        $('#departmentEmployeeConfirmBtn').on('click', function (e) {
            var tmpMeetingParticipantIds = '';
            var tmpMeetingParticipantNames = '';
            var tmpDepartmentEmployeeMap = new Map();
            var tmpTreeCheckedNodes = zTreeObj.getCheckedNodes(true);
            for( var i = 0; i < tmpTreeCheckedNodes.length; i++) {
                if (!tmpTreeCheckedNodes[i].isParent) {
                    var tmpParentNode = tmpTreeCheckedNodes[i].getParentNode();
                    // 拼接用户名称
                    if (tmpDepartmentEmployeeMap.containsKey(tmpParentNode.name)) {
                        var tmpEmployeeNames = tmpDepartmentEmployeeMap.get(tmpParentNode.name);
                        tmpDepartmentEmployeeMap.remove(tmpParentNode.name);
                        tmpDepartmentEmployeeMap.put(tmpParentNode.name, tmpEmployeeNames + ',' + tmpTreeCheckedNodes[i].name);
                    } else {
                        tmpDepartmentEmployeeMap.put(tmpParentNode.name, tmpTreeCheckedNodes[i].name);
                    }
                    // 拼接用户ID
                    if (tmpMeetingParticipantIds != '')
                        tmpMeetingParticipantIds += ',' + tmpTreeCheckedNodes[i].id;
                    else
                        tmpMeetingParticipantIds = tmpTreeCheckedNodes[i].id;
                }
            }

            var tmpMeetingParticipantNames = '';
            var tmpDepartmentEmployeeMapKeys = tmpDepartmentEmployeeMap.keys();
            for( var i = 0; i < tmpDepartmentEmployeeMapKeys.length; i++) {
                tmpMeetingParticipantNames += tmpDepartmentEmployeeMapKeys[i] + ":" + tmpDepartmentEmployeeMap.get(tmpDepartmentEmployeeMapKeys[i]) + "\n";
            }

            console.log(tmpMeetingParticipantNames);
            $('input[name="meetingParticipants"]').val(tmpMeetingParticipantIds);
            $('textarea[name="meetingParticipantNames"]').val(tmpMeetingParticipantNames);

            $('#meetingJoinerModal').modal('hide');
        });

        $('#meetingRecordFileUploadBtn').fileupload({
            myUrl: SMController.getUrl({controller:'controllerProxy',method:'callBackByRequest'
                ,proxyClass:'attachmentController',proxyMethod:'upload',jsonString:''}),
            dataType: 'json',
            autoUpload: true,
            maxFileSize: 1000000000// <1000 MB
        }).on('fileuploadprocessalways', function (e, data) {
                console.log("fileuploadprocessalways");
                if(data.files.error){
                    $('#imageError').find("label").empty();
                    if(data.files[0].error=="File is too large"){
                        $('#imageError').removeClass('hidden').find("label").append("最大上传文件大小为 1000 MB！");
                    }
                    if(data.files[0].error=="File type not allowed"){
                        $('#imageError').removeClass('hidden').find("label").append("上传文件类型不对！");
                    }
                }
            }).on('fileuploaddone',function (e, data) {
                var date = new Date();
                var createTime =date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日 " +date.getHours()+":"+date.getMinutes();
                var tmpFileData = data.result.files[0];
                var tempAttachmentObject = {};
                tempAttachmentObject.attachmentId = tmpFileData.id;
                tempAttachmentObject.attachmentName = tmpFileData.name;
                tempAttachmentObject.employeeName = "曾庆越";
                tempAttachmentObject.employeeDepartmentName = "科技部";
                tempAttachmentObject.attachmentCreateTime = createTime;
                tempAttachmentObject.attachmentId = tmpFileData.id;
                meetingRecordFilesTable.fnAddData(tempAttachmentObject);
            });

        $('#meetingRecordFileDeleteBtn').on('click', function (e) {
            $('#meetingRecordFiles :checkbox').each(function(){
                if($(this).prop("checked") && $(this).prop("id") == "") {
                    var tr = $(this).parents('tr');
                    var tmpRowData = meetingRecordFilesTable.fnGetData(tr);
                    var obj = [];
                    obj.push(StringUtil.decorateRequestData('String',tmpRowData.attachmentId));
                    $.ajax({
                        type:'post',
                        dataType:"json",
                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                            ,proxyClass:'attachmentController',proxyMethod:'deleteTempAttachmentFileById',
                            jsonString:MyJsonUtil.obj2str(obj)}),
                        success :function(result)
                        {
                            if(result) {
                                meetingRecordFilesTable.fnDeleteRow(tr);
                                meetingRecordFilesTable.remove(tmpRowData.attachmentId);
                            }else {
                            }
                        }
                    });
                }
            });
        });

        $('#meetingFileUploadBtn').fileupload({
            myUrl: SMController.getUrl({controller:'controllerProxy',method:'callBackByRequest'
                ,proxyClass:'attachmentController',proxyMethod:'upload',jsonString:''}),
            dataType: 'json',
            autoUpload: true,
            maxFileSize: 1000000000// <1000 MB
        }).on('fileuploadprocessalways', function (e, data) {
            console.log("fileuploadprocessalways");
            if(data.files.error){
                $('#imageError').find("label").empty();
                if(data.files[0].error=="File is too large"){
                    $('#imageError').removeClass('hidden').find("label").append("最大上传文件大小为 1000 MB！");
                }
                if(data.files[0].error=="File type not allowed"){
                    $('#imageError').removeClass('hidden').find("label").append("上传文件类型不对！");
                }
            }
        }).on('fileuploaddone',function (e, data) {
            var date = new Date();
            var createTime =date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日 " +date.getHours()+":"+date.getMinutes();
            var tmpFileData = data.result.files[0];
            var tempAttachmentObject = {};
            tempAttachmentObject.attachmentId = tmpFileData.id;
            tempAttachmentObject.attachmentName = tmpFileData.name;
            tempAttachmentObject.employeeName = "曾庆越";
            tempAttachmentObject.employeeDepartmentName = "科技部";
            tempAttachmentObject.attachmentCreateTime = createTime;
            tempAttachmentObject.attachmentId = tmpFileData.id;
            meetingFilesTable.fnAddData(tempAttachmentObject);
//            var tmpTableNodes = meetingFilesTable.fnGetNodes();
//            for(var i = 0; i < tmpTableNodes.length; i++)
//                console.log(meetingFilesTable.fnGetData(tmpTableNodes[i]));//fnGetData获取一行的数据
        });

        $('#meetingFileDeleteBtn').on('click', function (e) {
            $('#meetingFiles :checkbox').each(function(){
                if($(this).prop("checked") && $(this).prop("id") == "") {
                    var tr = $(this).parents('tr');
                    var tmpRowData = meetingFilesTable.fnGetData(tr);
                    var obj = [];
                    obj.push(StringUtil.decorateRequestData('String',tmpRowData.attachmentId));
                    $.ajax({
                        type:'post',
                        dataType:"json",
                        url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                            ,proxyClass:'attachmentController',proxyMethod:'deleteTempAttachmentFileById',
                            jsonString:MyJsonUtil.obj2str(obj)}),
                        success :function(result)
                        {
                            if(result) {
                                meetingFilesTable.fnDeleteRow(tr);
                                meetingFilesTable.remove(tmpRowData.attachmentId);
                            }else {
                            }
                        }
                    });
                }
            });
        });
    }

    return {
        init: function () {
            handleSelect2();
            handleTree();
            handleForm();
            handleDatePickers();
//            handleTimePickers();
            handleTable();
            handleButton();
        }
    };
}();