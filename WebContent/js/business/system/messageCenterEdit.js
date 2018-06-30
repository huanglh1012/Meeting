/**
 * FileName: messageCenterEdit.js
 * File description: 用于加载消息中心设置页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
 */

/**
 * messageCenterEdit所有属性和方法定义
 * @type {messageCenterEdit}
 */
var messageCenterEdit = function () {
    var handleButton = function () {
        $('#testMessageSendBtn').on('click', function (e) {
            var obj = [];
            var tmpFormData = DomUtil.getJSONObjectFromForm('shortMessageCenterForm', null);
            obj.push(StringUtil.decorateRequestData('ShortMessageCenterDTO', tmpFormData));
            $.ajax({
                type: "POST",
                url: SMController.getUrl({
                    controller: 'controllerProxy',
                    method: 'callBack',
                    proxyClass: 'shortMessageController',
                    proxyMethod: 'testSendMessage',
                    jsonString: MyJsonUtil.obj2str(obj)
                }),
                dataType: "json",
                beforeSend: function(jqXHR, settings) {
                    $.blockUI({
                        message: '<div class="progress progress-lg progress-striped active" style="margin-bottom: 0px;">' +
                            '<div style="width: 100%" role="progressbar" class="progress-bar bg-color-darken">' +
                            '<span id="processStatus" style="position: relative; top: 5px;font-size:15px;">正在发送测试短信...</span></div>' +
                            '</div>'
                    });
                },
                success: function (result) {
                    if (result.success) {
                        $("#processStatus").text("测试短息发送完成...");
                        $.unblockUI();
                        $('#messageSendResult').text("测试短息发送完成,返回结果："+result.msg.entityKeyValue.messageSendResult);

                    } else {
                        $.unblockUI();
                        $('#messageSendResult').text(result.msg);
//                        $('#messageSendResult').text("测试短息发送失败,请检查短信模板参数和消息电话号码是否不为空，或分割符号是否为英文逗号");
                    }
                }
            });
        });

        //"报表规则"添加对话框
        $('#addTemplateBtn').on('click', function (e) {
            $('#templateModal').modal('show',true);
            $('#builder').queryBuilder('reset');
        });

        $('#confirmTemplateBtn').on('click', function() {
            var tmpRules = $('#builder').queryBuilder('getRules').rules;
            var tmpTemplateRuleExpression = '';
            for(var i = 0;i < tmpRules.length; i++){
                if (tmpRules[i].value != '0') {
                    if (tmpTemplateRuleExpression != '')
                        tmpTemplateRuleExpression +=','
                    tmpTemplateRuleExpression = tmpTemplateRuleExpression + '{@' +tmpRules[i].value + '}';
                }
            }
            $('input[name="messageModel"]').val(tmpTemplateRuleExpression);
            $('#builder').queryBuilder('reset');
            $('#templateModal').modal('hide');
        });
    }

    var handleJqueryBuilder = function () {
        var rules_basic = {
            condition: 'AND',
            rules: [{
                id: 'variableValue',
                operator: 'equal',
                value: '0'
            }]
        };
        $('#builder').queryBuilder({
            plugins: ['bt-tooltip-errors'],
            filters: [{
                id: 'variableValue',
                label: '变量值',
                type: 'string',
                input: 'select',
                values: {
                    '0': '----',
                    'meetingSubject': '会议主题',
                    'meetingRoomName': '会议地点',
                    'meetingStartTime': '会议开始时间',
                    'meetingEndTime': '会议结束时间',
                    'meetingProposeTime': '会议发起时间',
                    'meetingCreatorName': '发起人',
                    'meetingCreatorDepartmentName': '发起部门',
                    'meetingAttentions': '会议注意事项'
                },
                operators: ['equal']
            }],
            allow_groups:false,
            lang_code:'cn',
            conditions:['AND'],
            rules: rules_basic
        });
    }

    var handlePageInfo = function () {
        $.ajax({
            type:'post',
            Type:"json",
            async:false,
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'shortMessageController',proxyMethod:'getShortMessageCenterInfo',jsonString:null}),
            success:function(result){
                if (result != '')
                    DomUtil.setFormElementsValueViaJSONObject('shortMessageCenterForm',JSON.parse(result));
            }
        });
    }

    var handleForm = function () {
        $("#shortMessageCenterForm").validate({
            // 指定验证时要忽略哪些元素，默认是hidden，支持jQuery的伪类选择器，需要为应用该验证器的元素加上.required-validation
            errorElement: "strong",
            ignore: ":not('.required-validation')",
            errorClass: "note_error text-danger",
            focusCleanup: true,
            focusInvalid: false,
            rules: {
                shortMessageCenterName: {
                    required: true
                },
                sendUrl: {
                    required: true
                },
                callerId: {
                    required: true
                },
                callerPassword: {
                    required: true
                },
                messageTemplateId: {
                    required: true
                },
                messageModel: {
                    required: true
                }
            }, // Messages for form
            messages: {
                shortMessageCenterName:{
                    required:"短信中心名称不能为空"
                },
                sendUrl:{
                    required:"短信中心发送接口不能为空"
                },
                callerId:{
                    required:"调用者ID不能为空"
                },
                callerPassword:{
                    required:"调用者密码不能为空"
                },
                messageTemplateId:{
                    required:"消息发送模板ID不能为空"
                },
                messageModel:{
                    required:"短信模板变量不能为空"
                }
            },

            highlight: function(element, errorClass) {
                $(element).parent().addClass("has-error");
            },

            unhighlight: function(element, errorClass) {
                $(element).parent().removeClass("has-error");
            },

            submitHandler: function(form) { //验证通过时触发
                var obj = [];
                var tmpFormData = DomUtil.getJSONObjectFromForm('shortMessageCenterForm', null);
                obj.push(StringUtil.decorateRequestData('ShortMessageCenterDTO', tmpFormData));
                $.ajax({
                    type: "POST",
                    url: SMController.getUrl({
                        controller: 'controllerProxy',
                        method: 'callBack',
                        proxyClass: 'shortMessageController',
                        proxyMethod: 'saveShortMessageCenter',
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
                                $.pnotify({
                                    text: result.msg
                                });
                                handlePageInfo();
                            }, 1500);

                        } else {
                            $.unblockUI();
                            $.pnotify({
                                text: result.msg
                            });
                        }
                    }
                });
            }, // Do not change code below

            errorPlacement: function(error, element) {
                error.appendTo(element.parent());
            }
        });
    }

    return {
        init: function () {
            handleButton();
            handleJqueryBuilder();
            handleForm();
            handlePageInfo();
        }
    };
}();