/**
 * FileName: meetingEdit.js
 * File description: 用于加载和初始化会议配置页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
 */

/**
 * meetingEdit
 * @type {meetingEdit}
 */
var meetingEdit = function () {
    var meetingId = null;
    var zTreeObj = null;
    var selectedTreeNode = null;
    var searchLocationIndex = 0;
    var meetingRecordFilesTable = null;
    var meetingFilesTable = null;

    var handlePageInfo = function () {
        var tmpUrl = document.URL;
        var tmpUrlParam = tmpUrl.split('?')[1];
        if (tmpUrlParam != undefined) {
            var tmpUrlParamValue= tmpUrlParam.split("=")[1];
            if (tmpUrlParamValue != undefined && tmpUrlParamValue != null) {
                var obj = [];
                obj.push(StringUtil.decorateRequestData('String',tmpUrlParamValue));
                $.ajax({
                    type:'post',
                    Type:"json",
                    async:false,
                    url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                        ,proxyClass:'meetingController',proxyMethod:'getMeetingInfoById',jsonString:MyJsonUtil.obj2str(obj)}),
                    success:function(result){
                        var tmpJsonObject = JSON.parse(result);
                        DomUtil.setFormElementsValueViaJSONObject('createMeetingForm',tmpJsonObject);
                        if (tmpJsonObject.isSendMessageNotice == 1)
                            $('input[name="isSendMessageNotice"]').prop('checked',true);
                        $('#meetingPresenter').select2('val',tmpJsonObject.meetingPresenter);
                        $('#meetingRoomId').select2('val',tmpJsonObject.meetingRoomId);
                        $('#meetingCreatorDepartmentId').select2('val',tmpJsonObject.meetingCreatorDepartmentId);
                        if (tmpJsonObject.meetingFileList.length > 0)
                            meetingFilesTable.fnAddData(tmpJsonObject.meetingFileList);
                        if (tmpJsonObject.meetingRecordFileList.length > 0)
                            meetingRecordFilesTable.fnAddData(tmpJsonObject.meetingRecordFileList);
                        // 判断操作人是否为发起人员或者管理员，如果不是，则只允许上传，删除和下载自己的会议材料，不允许修改会议内容，不允许下载其他部门会议材料
                        if(JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                            || JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1
                            || JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId == tmpJsonObject.meetingCreator) {
                        } else {
                            $('input,select,textarea',$('#createMeetingForm')).attr('readonly',true);
                            $("#messageNoticeTime,#meetingStartTime,#meetingEndTime,#meetingProposeTime,#meetingJoiner").attr('disabled',true);
                            $('input[name="isSendMessageNotice"]').attr('disabled',true);
                        }
                    }
                });
            }
        }
    }

    var handleDatePickers = function () {
        $("#messageNoticeTime,#meetingStartTime,#meetingEndTime,#meetingProposeTime").datetimepicker({
            language:'zh-CN',
            autoclose: true,
            format: "yyyy-mm-dd hh:ii"
        });
    }

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
    }

    var handleTree = function() {
        var setting = {
            view: {
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
        var tmpEmployeeDTO = JSON.parse(localStorage.getItem("EmployeeDTO"));
        // 隐藏域赋值
        $("#meetingProposeTime").datetimepicker('setDate', new Date());
        $('input[name="meetingCreatorName"]').val(tmpEmployeeDTO.employeeName);
        $('input[name="meetingCreatorDepartmentName"]').val(tmpEmployeeDTO.departmentName);
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
                meetingParticipantNames:{
                    required: true
                }
            },
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
                if ($('input[name="meetingCreator"]').val() == "")
                    $('input[name="meetingCreator"]').val(tmpEmployeeDTO.employeeId);
                $('input[name="employeeId"]').val(tmpEmployeeDTO.employeeId);
                var meetingId = $('input[name="meetingId"]').val();
                var isSendMessageNotice = $('input[name="isSendMessageNotice"]').prop('checked');
                var addData = DomUtil.getJSONObjectFromForm('createMeetingForm', null);
                if (isSendMessageNotice && addData.messageNoticeTime == '') {
                    bootbox.alert({
                        className: 'span4 alert-error',
                        buttons: {
                            ok: {
                                label: '确定',
                                className: 'btn blue'
                            }
                        },
                        message: "短信提醒时间不能为空",
                        callback: function () {

                        },
                        title: "错误提示"
                    });
                } else {
                    // 添加会议记录附件表格记录
                    var tmpMeetingRecordFileList = [];
                    var tmpMeetingRecordFilesTableNodes = meetingRecordFilesTable.fnGetNodes();
                    for(var i = 0; i < tmpMeetingRecordFilesTableNodes.length; i++) {
                        var tmpAttachmentId = meetingRecordFilesTable.fnGetData(tmpMeetingRecordFilesTableNodes[i]).attachmentId;
                        var tmpAttachmentDTO = {};
                        tmpAttachmentDTO.attachmentId = tmpAttachmentId;
                        tmpMeetingRecordFileList.push(tmpAttachmentDTO);
                    }
                    // 添加会议附件表格记录
                    var tmpMeetingFileList = [];
                    var tmpMeetingFilesTableNodes = meetingFilesTable.fnGetNodes();
                    for(var i = 0; i < tmpMeetingFilesTableNodes.length; i++) {
                        var tmpAttachmentId = meetingFilesTable.fnGetData(tmpMeetingFilesTableNodes[i]).attachmentId;
                        var tmpAttachmentDTO = {};
                        tmpAttachmentDTO.attachmentId = tmpAttachmentId;
                        tmpMeetingFileList.push(tmpAttachmentDTO);
                    }
                    var obj = [];
                    addData.meetingRecordFileList = tmpMeetingRecordFileList;
                    addData.meetingFileList = tmpMeetingFileList;
                    if (isSendMessageNotice)
                        addData.isSendMessageNotice = 1;
                    else
                        addData.isSendMessageNotice = 0;
                    // 设置时间值
                    if ($("#messageNoticeTime").val() != '')
                        addData.messageNoticeTime = $("#messageNoticeTime").val()+":00";
                    addData.meetingStartTime = $("#meetingStartTime").val()+":00";
                    addData.meetingEndTime = $("#meetingEndTime").val()+":00";
                    addData.meetingProposeTime = $("#meetingProposeTime").val()+":00";
                    obj.push(StringUtil.decorateRequestData('MeetingDTO', addData));
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
                                    message: result.msg + "<br>是否继续发起会议?",
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
                }
            },
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
                            '<span id="processStatus" style="position: relative; top: 5px;font-size:15px;">正在处理，请耐心等候...</span></div>' +
                            '</div>'
                    });
                },
                success: function (result) {
                    if (result.success) {
                        $("#processStatus").text("提交成功，正在返回上一页面...");
                        setTimeout(function(){
                            $.unblockUI();
                            history.back();
//                            window.location.href = 'meeting_list.html';
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
            { "sTitle": "上传部门", "mData": "departmentName"},
            { "sTitle": "上传时间", "mData": "attachmentCreateTime"}
        ];

        var meetingRecordFilesTableHead = [
            { "sTitle": '<input type="checkbox" id="meetingRecordFilesCheckAll"/>',"bSortable":false,"sWidth": "12px" },
            { "sTitle": "附件ID", "mData": "attachmentId","bVisible":false},
            { "sTitle": "材料名称","mData": "attachmentName"},
            { "sTitle": "上传人", "mData": "employeeName"},
            { "sTitle": "上传部门", "mData": "departmentName"},
            { "sTitle": "上传时间", "mData": "attachmentCreateTime"}
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
                }
            ]
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
                }
            ]
        });

        //复选框全选
        $('#meetingFilesCheckAll').on('click', function (e) {
            var isCheck = $('#meetingFilesCheckAll').prop('checked');
            if(isCheck){
                $('#meetingFiles :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
            }else{
                $('#meetingFiles :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
            }
        });

        //复选框全选
        $('#meetingRecordFilesCheckAll').on('click', function (e) {
            var isCheck = $('#meetingRecordFilesCheckAll').prop('checked');
            if(isCheck){
                $('#meetingRecordFiles :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
            }else{
                $('#meetingRecordFiles :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
            }
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
                if(data.files.error){
                    $('#imageError').find("label").empty();
                    if(data.files[0].error=="File is too large"){
                        $('#imageError').removeClass('hidden').find("label").append("最大上传文件大小为 1000 MB！");
                    }
                    if(data.files[0].error=="File type not allowed"){
                        $('#imageError').removeClass('hidden').find("label").append("上传文件类型不对！");
                    }
                }
                $.blockUI({
                    message: '<div class="progress progress-lg progress-striped active" style="margin-bottom: 0px;">' +
                        '<div style="width: 100%" role="progressbar" class="progress-bar bg-color-darken">' +
                        '<span id="processStatus" style="position: relative; top: 5px;font-size:15px;">正在上传文件，请稍候...</span></div>' +
                        '</div>'
                });
            }).on('fileuploaddone',function (e, data) {
                $.unblockUI();
                var tmpFileData = data.result.files[0];
                var tempAttachmentObject = {};
                tempAttachmentObject.attachmentId = tmpFileData.id;
                tempAttachmentObject.attachmentName = tmpFileData.name;
                tempAttachmentObject.employeeName = JSON.parse(localStorage.getItem("EmployeeDTO")).employeeName;
                tempAttachmentObject.departmentName = JSON.parse(localStorage.getItem("EmployeeDTO")).departmentName;
                tempAttachmentObject.attachmentCreateTime = new Date().pattern("yyyy-MM-dd hh:mm:ss");
                tempAttachmentObject.attachmentId = tmpFileData.id;
                tempAttachmentObject.employeeId = JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId;
                meetingRecordFilesTable.fnAddData(tempAttachmentObject);
            });

        $('#meetingRecordFileDeleteBtn').on('click', function (e) {
            var isDelete = true;
            $('#meetingRecordFiles :checkbox').each(function(){
                if($(this).prop("checked") && $(this).prop("id") == "") {
                    var tr = $(this).parents('tr');
                    var tmpRowData = meetingRecordFilesTable.fnGetData(tr);
                    // 如果不是发起人或者管理员，则只允许删除自己的会议材料
                    if(JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                        || JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1
                        || JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId == $('input[name="meetingCreator"]').val()) {
                    } else {
                        if (tmpRowData.employeeId != JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId) {
                            bootbox.alert({
                                className: 'span4 alert-error',
                                buttons: {
                                    ok: {
                                        label: '确定',
                                        className: 'btn blue'
                                    }
                                },
                                message: "只允许删除自己的会议材料",
                                callback: function () {

                                },
                                title: "错误提示"
                            });
                            isDelete = false;
                            return false;
                        }
                    }
                }
            });
            if (isDelete) {
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
                                ,proxyClass:'meetingController',proxyMethod:'deleteAttachmentByAttachmentId',
                                jsonString:MyJsonUtil.obj2str(obj)}),
                            success :function(result) {
                                if(result) {
                                    meetingRecordFilesTable.fnDeleteRow(tr);
                                    meetingRecordFilesTable.remove(tmpRowData.attachmentId);
                                }else {
                                }
                            }
                        });
                    }
                });
            }
        });

        $('#meetingFileUploadBtn').fileupload({
            myUrl: SMController.getUrl({controller:'controllerProxy',method:'callBackByRequest'
                ,proxyClass:'attachmentController',proxyMethod:'upload',jsonString:''}),
            dataType: 'json',
            autoUpload: true,
            maxFileSize: 1000000000// <1000 MB
        }).on('fileuploadprocessalways', function (e, data) {
            if(data.files.error){
                $('#imageError').find("label").empty();
                if(data.files[0].error=="File is too large"){
                    $('#imageError').removeClass('hidden').find("label").append("最大上传文件大小为 1000 MB！");
                }
                if(data.files[0].error=="File type not allowed"){
                    $('#imageError').removeClass('hidden').find("label").append("上传文件类型不对！");
                }
            }
            $.blockUI({
                message: '<div class="progress progress-lg progress-striped active" style="margin-bottom: 0px;">' +
                    '<div style="width: 100%" role="progressbar" class="progress-bar bg-color-darken">' +
                    '<span id="processStatus" style="position: relative; top: 5px;font-size:15px;">正在上传文件，请稍候...</span></div>' +
                    '</div>'
            });
        }).on('fileuploaddone',function (e, data) {
            $.unblockUI();
            var tmpFileData = data.result.files[0];
            var tempAttachmentObject = {};
            tempAttachmentObject.attachmentId = tmpFileData.id;
            tempAttachmentObject.attachmentName = tmpFileData.name;
            tempAttachmentObject.employeeName = JSON.parse(localStorage.getItem("EmployeeDTO")).employeeName;
            tempAttachmentObject.departmentName = JSON.parse(localStorage.getItem("EmployeeDTO")).departmentName;
            tempAttachmentObject.attachmentCreateTime = new Date().pattern("yyyy-MM-dd hh:mm:ss");;
            tempAttachmentObject.attachmentId = tmpFileData.id;
            tempAttachmentObject.employeeId = JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId;
            meetingFilesTable.fnAddData(tempAttachmentObject);
        });

        $('#meetingFileDeleteBtn').on('click', function (e) {
            var isDelete = true;
            $('#meetingFiles :checkbox').each(function(){
                if($(this).prop("checked") && $(this).prop("id") == "") {
                    var tr = $(this).parents('tr');
                    var tmpRowData = meetingFilesTable.fnGetData(tr);
                    // 如果不是发起人或者管理员，则只允许删除自己的会议材料
                    if(JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('-1') > -1
                        || JSON.parse(localStorage.getItem("EmployeeDTO")).roleIdList.indexOf('0') > -1
                        || JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId == $('input[name="meetingCreator"]').val()) {
                    } else {
                        if (tmpRowData.employeeId != JSON.parse(localStorage.getItem("EmployeeDTO")).employeeId) {
                            bootbox.alert({
                                className: 'span4 alert-error',
                                buttons: {
                                    ok: {
                                        label: '确定',
                                        className: 'btn blue'
                                    }
                                },
                                message: "只允许删除自己的会议材料",
                                callback: function () {

                                },
                                title: "错误提示"
                            });
                            isDelete = false;
                            return false;
                        }
                    }
                }
            });
            if (isDelete) {
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
                                ,proxyClass:'meetingController',proxyMethod:'deleteAttachmentByAttachmentId',
                                jsonString:MyJsonUtil.obj2str(obj)}),
                            success :function(result) {
                                if(result) {
                                    meetingFilesTable.fnDeleteRow(tr);
                                    meetingFilesTable.remove(tmpRowData.attachmentId);
                                }else {
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    return {
        init: function () {
            handleSelect2();
            handleTree();
            handleForm();
            handleDatePickers();
            handleTable();
            handleButton();
            handlePageInfo();
        }
    };
}();