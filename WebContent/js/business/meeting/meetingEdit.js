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

    var handleDatePickers = function () {
        $("#messageNoticeTime,#meetingStartTime").datetimepicker({
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
        $("#create-issue-form").validate({
            // 指定验证时要忽略哪些元素，默认是hidden，支持jQuery的伪类选择器，需要为应用该验证器的元素加上.required-validation
            errorElement: "strong",
            ignore: ":not('.required-validation')",
            errorClass: "note_error text-danger",
            focusCleanup: true,
            focusInvalid: false,
            rules: {
                taskStrategyId: {
                    required: true
                }
            }, // Messages for form
            messages: {
                taskStrategyId:{
                    required:"策略不能为空！！！"
                }
            },

            highlight: function(element, errorClass) {
                $(element).parent().addClass("has-error");
            },

            unhighlight: function(element, errorClass) {
                $(element).parent().removeClass("has-error");
            },

            submitHandler: function(form) { //验证通过时触发
                var taskStrategyId = $('#taskStrategyId').attr("value");
                var obj = [];
                //隐藏域赋值
                $('#paaEmployeeId').attr("value", "20667");
                $('#paaUsername').attr("value", "曾庆越");
                var addData = DomUtil.getJSONObjectFromForm('create-issue-form', null);
                console.log(addData);
                var fileList = $('#file-upload').data('blueimp-fileupload').options.uploadCreateIds;
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
                    obj.push(StringUtil.decorateRequestData('ReportTaskDTO', addData));
                    obj.push(StringUtil.decorateRequestData('List', fileList));
                    //进度条
                    $('#progressBar').modal('show', true);
                    $.ajax({
                        type: "POST",
                        url: SMController.getUrl({
                            controller: 'controllerProxy',
                            method: 'callBack',
                            proxyClass: 'fileController',
                            proxyMethod: 'submitReportTask',
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
                                    history.back();
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
            }, // Do not change code below

            errorPlacement: function(error, element) {
                error.appendTo(element.parent());
            }
        });
    }

    var handleFileUpload = function () {
        $('#file-upload').fileupload({
            myUrl: SMController.getUrl({controller:'controllerProxy',method:'callBackByRequest'
                ,proxyClass:'attachmentController',proxyMethod:'upload',jsonString:''}),
            dataType: 'json',
            autoUpload: true,
            maxFileSize: 10000000// <1 MB
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
                    +"<td width='100'>"+file.size+" B</td>"
                    +"<td width='160'>"+createTime+"</td>"
                    +"<td width='60' ><a class='deleteFile' data-fileId='"+file.id+"' data-fileName='"+file.name+"' href='javascript:void(0);'><i class='fa fa-trash-o'></i>"
                    +"</a></td>"
                    +"<input type='hidden' name='fileId' value='"+file.id+"'>"
                    +"<input type='hidden' name='createId' value='"+file.createId+"'>"
                    +"<input type='hidden' name='fileName' value='"+file.name+"'>"
                    +"<input type='hidden' name='mimeType' value="+file.type+">"
                    +"</tr>";
                $('#fileAttachements tbody').append(fileContent);
            });

        $('#file-upload').change(function(e) {
            console.log("upload_change");
            $(this).parent().next().val($(this).val());
            e.preventDefault();
        });

        $('#fileAttachements').on("click", ".deleteFile", function(e) {
            console.log($(this).data("fileid"));
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
    }

    return {
        init: function () {
            handleSelect2();
            handleTree();
            handleForm();
            handleFileUpload();
            handleDatePickers();
            handleTimePickers();
            handleButton();
        }
    };
}();