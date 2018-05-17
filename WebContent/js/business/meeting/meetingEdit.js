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
    var selectFile = [];

    var handleDatePickers = function () {
        $("#messageNoticeTime,#meetingStartTime,#meetingEndTime").datetimepicker({
            language:'zh-CN',
            format: "yyyy-mm-dd hh:ii"
        });

        $("#meetingProposeTime").datepicker({
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

    var handleTimePickers = function () {
        if (jQuery().timepicker) {
            $('.timepicker-24').timepicker({
                minuteStep: 1,
                showSeconds: true,
                showMeridian: false
            });
        }
    }

    var handleSelect2 = function () {
        $.ajax({
            type:'post',
            dataType:"json",
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getEmployeeList',jsonString:null}),
            success:function(result){
                $('#chargerId').select2({
                    placeholder: "请选择主持人",
                    allowClear:true,
                    data:result
                });
                $('#proposerId').select2({
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
                $('#proposeDepartmentId').select2({
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
//                meetingSubject:{
//                    required: true
//                },
//                meetingRoomId:{
//                    required: true
//                },
//                meetingStartTime:{
//                    required: true
//                },
//                meetingEndTime:{
//                    required: true
//                },
//                chargerId:{
//                    required: true
//                },
//                meetingProposeTime:{
//                    required: true
//                },
//                proposerId:{
//                    required: true
//                },
//                proposeDepartmentId:{
//                    required: true
//                },
//                meetingJoinerNames:{
//                    required: true
//                }
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
                chargerId:{
                    required:"主持人不能为空！！！"
                },
                meetingProposeTime:{
                    required:"发起时间不能为空！！！"
                },
                proposerId:{
                    required:"发起人不能为空！！！"
                },
                proposeDepartmentId:{
                    required:"发起部门不能为空！！！"
                },
                meetingJoinerNames:{
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
                //隐藏域赋值
                $('#paaEmployeeId').attr("value", "20667");
                $('#paaUsername').attr("value", "曾庆越");
                var addData = DomUtil.getJSONObjectFromForm('createMeetingForm', null);
                var that =  $('#meetingFileUploadBtn').data('blueimp-fileupload') ||
                    $('#meetingFileUploadBtn').data('fileupload');
                var fileList = that.options.uploadCreateIds;
                console.log(fileList);
                if (fileList.length == 0) {
                    bootbox.alert({
                        className: 'span4 alert-error',
                        buttons: {
                            ok: {
                                label: '确定',
                                className: 'btn blue'
                            }
                        },
                        message: "附件不能为空",
                        callback: function () {

                        },
                        title: "错误提示"
                    });
                } else {
                    obj.push(StringUtil.decorateRequestData('MeetingDTO', addData));
                    obj.push(StringUtil.decorateRequestData('List', fileList));
                    //进度条
//                    $('#progressBar').modal('show', true);
//                    $.ajax({
//                        type: "POST",
//                        url: SMController.getUrl({
//                            controller: 'controllerProxy',
//                            method: 'callBack',
//                            proxyClass: 'meetingController',
//                            proxyMethod: meetingId == "" ? 'insertMeeting' : 'updateMeeting',
//                            jsonString: MyJsonUtil.obj2str(obj)
//                        }),
//                        dataType: "json",
//                        beforeSend: function(jqXHR, settings) {
//                            $.blockUI({
//                                message: '<div class="progress progress-lg progress-striped active" style="margin-bottom: 0px;">' +
//                                    '<div style="width: 100%" role="progressbar" class="progress-bar bg-color-darken">' +
//                                    '<span id="processStatus" style="position: relative; top: 5px;font-size:15px;">正在处理，请稍后...</span></div>' +
//                                    '</div>'
//                            });
//                        },
//                        success: function (result) {
//                            if (result.success) {
//                                $("#processStatus").text("提交成功，正在返回上一页面...");
//                                setTimeout(function(){
//                                    $.unblockUI();
//                                    history.back();
//                                }, 1500);
//
//                            } else {
//                                $.unblockUI();
//                                bootbox.alert({
//                                    title: '提示',//I18n.getI18nPropByKey("ProductionExecution.errorPrompt"),
//                                    message:result.msg,
//                                    className:'span4 alert-error',
//                                    buttons: {
//                                        ok: {
//                                            label: '关闭',//I18n.getI18nPropByKey("ProductionExecution.confirm"),
//                                            className: 'btn blue'
//                                        }
//                                    },
//                                    callback: function() {
//
//                                    }
//                                });
//                            }
//                        }
//                    });
                }
            }, // Do not change code below

            errorPlacement: function(error, element) {
                error.appendTo(element.parent());
            }
        });
    }

    var handleFileUpload = function () {
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
                        $('#imageError').removeClass('hidden').find("label").append("最大上传文件大小为 10.00 MB！");
                    }
                    if(data.files[0].error=="File type not allowed"){
                        $('#imageError').removeClass('hidden').find("label").append("上传文件类型不对！");
                    }
                }
            }).on('fileuploaddone',function (e, data) {
                var that = $(this).data('blueimp-fileupload') ||
                    $(this).data('fileupload');
                var file=data.result.files[0];
                that.options.uploadCreateIds.push(file['createId']);
                $('#imageError').addClass('hidden');
                var url =file.url;
                var date = new Date();
                var createTime =date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日 " +date.getHours()+":"+date.getMinutes();
                var fileContent ="<tr>"
                    +"<td><a href='"+$.url_root+"/issue/fileAttachementDownload.jspa?filePath="+url+"&fileName="+file.name+"'>"+file.name+"</a></td>"
                    +"<td width='100'>" + "曾庆越" + "</td>"
                    +"<td width='100'>" + "科技部" + "</td>"
                    +"<td width='160'>"+createTime+"</td>"
                    +"<td width='60' ><a class='deleteFile' data-fileId='"+file.id+"' data-fileName='"+file.name+"' href='javascript:void(0);'><i class='fa fa-trash-o'></i>"
                    +"</a></td>"
                    +"<input type='hidden' name='fileId' value='"+file.id+"'>"
                    +"<input type='hidden' name='createId' value='"+file.createId+"'>"
                    +"<input type='hidden' name='fileName' value='"+file.name+"'>"
                    +"<input type='hidden' name='mimeType' value="+file.type+">"
                    +"</tr>";
                $('#meetingRecordFiles tbody').append(fileContent);
            });

//        $('#meetingRecordFileUploadBtn').change(function(e) {
//            console.log("upload_change");
//            $(this).parent().next().val($(this).val());
//            e.preventDefault();
//        });

        $('#meetingRecordFiles').on("click", ".deleteFile", function(e) {
            var obj = [];
            obj.push(StringUtil.decorateRequestData('String',$(this).data("fileid")));
            var $that = $(this);
            $.ajax({
                type:'post',
                dataType:"json",
                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                    ,proxyClass:'attachmentController',proxyMethod:'deleteTempAttachmentFileById',
                    jsonString:MyJsonUtil.obj2str(obj)}),
                success :function(result)
                {
                    if(result) {
                        $that.closest("tr").remove();
                    }else {
                    }
                }
            });
        });

        $('#meetingFiles').on("click", ".deleteFile", function(e) {
            var obj = [];
            obj.push(StringUtil.decorateRequestData('String',$(this).data("fileid")));
            var $that = $(this);
            $.ajax({
                type:'post',
                dataType:"json",
                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                    ,proxyClass:'attachmentController',proxyMethod:'deleteTempAttachmentFileById',
                    jsonString:MyJsonUtil.obj2str(obj)}),
                success :function(result)
                {
                    if(result) {
                        $that.closest("tr").remove();
                    }else {
                    }
                }
            });
        });
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
                selectFile = [];
                $('#meetingFiles :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
                var tmpTableNodes = meetingFilesTable.fnGetNodes();
                for(var i = 0; i < tmpTableNodes.length; i++)
                    selectFile.push(meetingFilesTable.fnGetData(tmpTableNodes[i]).attachmentId);//fnGetData获取一行的数据
            }else{
                $('#meetingFiles :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
                selectFile = [];
            }
        });

        //复选框全选
        $('#meetingRecordFilesCheckAll').on('click', function (e) {
            var isCheck = $('#meetingRecordFilesCheckAll').prop('checked');
            if(isCheck){
                //先清空之前的选项
                selectFile = [];
                $('#meetingRecordFiles :checkbox').each(function(){
                    $(this).prop("checked","true");
                });
                var tmpTableNodes = meetingRecordFilesTable.fnGetNodes();
                for(var i = 0; i < tmpTableNodes.length; i++)
                    selectFile.push(meetingRecordFilesTable.fnGetData(tmpTableNodes[i]).attachmentId);//fnGetData获取一行的数据
            }else{
                $('#meetingRecordFiles :checkbox').each(function(){
                    $(this).removeAttr("checked");
                });
                selectFile = [];
            }
        });

        //根据复选框的值来获得行数据
        $('#meetingFiles tbody').on('click','tr', function () {
            var isCheck = this.getElementsByTagName('input').item(0).checked ;
            if(isCheck)
                selectFile.push(meetingFilesTable.fnGetData(this).attachmentId);
            else
                selectFile.remove(meetingFilesTable.fnGetData(this).attachmentId);
        });

        $('#meetingRecordFiles tbody').on('click','tr', function () {
            var isCheck = this.getElementsByTagName('input').item(0).checked ;
            if(isCheck)
                selectFile.push(meetingRecordFilesTable.fnGetData(this).attachmentId);
            else
                selectFile.remove(meetingRecordFilesTable.fnGetData(this).attachmentId);
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
            var tmpMeetingJoinerIds = '';
            var tmpMeetingJoinerNames = '';
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
                    if (tmpMeetingJoinerIds != '')
                        tmpMeetingJoinerIds += ',' + tmpTreeCheckedNodes[i].id;
                    else
                        tmpMeetingJoinerIds = tmpTreeCheckedNodes[i].id;
                }
            }

            var tmpMeetingJoinerNames = '';
            var tmpDepartmentEmployeeMapKeys = tmpDepartmentEmployeeMap.keys();
            for( var i = 0; i < tmpDepartmentEmployeeMapKeys.length; i++) {
                tmpMeetingJoinerNames += tmpDepartmentEmployeeMapKeys[i] + ":" + tmpDepartmentEmployeeMap.get(tmpDepartmentEmployeeMapKeys[i]) + "\n";
            }

            console.log(tmpMeetingJoinerNames);
            $('input[name="meetingJoinerIds"]').val(tmpMeetingJoinerIds);
            $('textarea[name="meetingJoinerNames"]').val(tmpMeetingJoinerNames);

            $('#meetingJoinerModal').modal('hide');
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
            var tmpTableNodes = meetingFilesTable.fnGetNodes();
            for(var i = 0; i < tmpTableNodes.length; i++)
                console.log(meetingFilesTable.fnGetData(tmpTableNodes[i]));//fnGetData获取一行的数据
        });

        $('#meetingFileDownloadBtn').on('click', function (e) {

        });

        $('#meetingFileDeleteBtn').on('click', function (e) {
            $('#meetingFiles :checkbox').each(function(){
                if($(this).prop("checked") && $(this).prop("id") == "" ) {
                    $(this).closest("tr").remove();
                    selectFile = [];
                }
            });
            var obj = [];
            obj.push(StringUtil.decorateRequestData('String',selectFile[0]));
            var $that = $(this);
//            $.ajax({
//                type:'post',
//                dataType:"json",
//                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
//                    ,proxyClass:'attachmentController',proxyMethod:'deleteTempAttachmentFileById',
//                    jsonString:MyJsonUtil.obj2str(obj)}),
//                success :function(result)
//                {
//                    if(result) {
//                        $that.closest("tr").remove();
//                    }else {
//                    }
//                }
//            });
        });
    }

    return {
        init: function () {
            handleSelect2();
            handleTree();
            handleForm();
//            handleFileUpload();
            handleDatePickers();
            handleTimePickers();
            handleTable();
            handleButton();
        }
    };
}();