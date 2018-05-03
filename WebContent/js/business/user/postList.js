/**
 * FileName: postList.js
 * File description: 用于加载职务列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * postList所有属性和方法定义
 * @type {postList}
 */
var postList = function () {

    var handleTable = function () {
        $('#add').on('click', function (e) {
            $('#myModal').modal('show',true);
        });

        // 表头定义
        var tableHead = [
            { "sTitle": "职务ID", "mData": "meetingRoomId","bVisible":false},
            { "sTitle": "职务","mData": "postName","type" :"string" },
            { "sTitle": "职务描述", "mData": "postSummary","type":"string"}
        ];
        var oTable =  $('#postList').dataTable({
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
            }]
        });
    }

    var handleButton = function() {
        $('#submitPost').on('click', function (e) {
            var postName = $('#postName').val();
            var postSummary = $('#postSummary').val();

            var obj = [];
            var addData = {};
            addData['postName'] = postName
            addData['postSummary'] = postSummary;
            console.log(addData);
            if (postName == '') {
                bootbox.alert({
                    className: 'span4 alert-error',
                    buttons: {
                        ok: {
                            label: '确定',
                            className: 'btn blue'
                        }
                    },
                    message: "职务名称不能为空",
                    callback: function () {

                    },
                    title: "错误提示"
                });
            } else {
                obj.push(StringUtil.decorateRequestData('PostDTO', addData));
                //进度条
                $('#progressBar').modal('show', true);
                $.ajax({
                    type: "POST",
                    url: SMController.getUrl({
                        controller: 'controllerProxy',
                        method: 'callBack',
                        proxyClass: 'securityController',
                        proxyMethod: 'insertPost',
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
                        console.log(result);
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
        });
    }

    return {
        init: function () {
            handleTable();
            handleButton();
        }
    };
}();