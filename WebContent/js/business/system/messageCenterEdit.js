/**
 * FileName: messageCenterEdit.js
 * File description: 用于加载消息中心设置页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * messageCenterEdit所有属性和方法定义
 * @type {messageCenterEdit}
 */
var messageCenterEdit = function () {
    var handlePageInfo = function () {
        $.ajax({
            type:'post',
            Type:"json",
            async:false,
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'shortMessageController',proxyMethod:'getShortMessageCenterInfo',jsonString:null}),
            success:function(result){
                console.log(result);
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
                centerPhoneNumber: {
                    required: true
                },
                sendMessagePhoneNumber: {
                    required: true
                }
            }, // Messages for form
            messages: {
                shortMessageCenterName:{
                    required:"短信中心名称！！！"
                },
                centerPhoneNumber:{
                    required:"短信中心号码！！！"
                },
                sendMessagePhoneNumber:{
                    required:"信息发送号码！！！"
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
            handleForm();
            handlePageInfo();
        }
    };
}();