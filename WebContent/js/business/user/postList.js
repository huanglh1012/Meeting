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
    var oTable = null;
    var handleTable = function () {
        var selectTr = null;
        var tableHead = [
            { "sTitle": "职务ID", "mData": "meetingRoomId","bVisible":false},
            { "sTitle": "职务","mData": "postName","type" :"string" },
            { "sTitle": "职务描述", "mData": "postSummary","type":"string"}
        ];

        //算法配置
        oTable = $('#postList').dataTable({
            //表头设置
            "aoColumns": tableHead,
            "aLengthMenu":[ 10, 25, 50,100],
            "bAutoWidth" : true,
            //默认显示的分页数
            "iDisplayLength": 25,
            "oLanguage": { //国际化一些配置
                "sLoadingRecords" : "正在获取数据，请稍候...",
                "sLengthMenu" : "显示 _MENU_ 条",
                "sZeroRecords" : "没有您要搜索的内容",
                "sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
                "sInfoEmpty" : "记录数为0",
                "sInfoFiltered" : "(全部记录数 _MAX_ 条)",
                "sInfoPostFix" : "",
                "sSearch" : "搜索",
                "sUrl" : "",
                "oPaginate": {
                    "sFirst" : "第一页",
                    "sPrevious" : "上一页",
                    "sNext" : "下一页",
                    "sLast" : "最后一页"
                }
            },
            "ajax": {
                type:"POST",
                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                    ,proxyClass:'securityController',proxyMethod:'getPostList',jsonString:null}),
                dataType:"json",
                success:function(data) {
                    oTable.fnClearTable();
                    oTable.fnAddData(data);
                }
            }
        });

        //单击每一行的操作（根据复选框的值来获得行数据）
        $('#postList tbody').on('click','tr', function () {
            // 复选框只能"单选"
            console.log("tr click")
            var data = oTable.fnGetData(this);
            console.log(data);
            if ($(this).hasClass("highlight")){
                $(this).removeClass("highlight");
            } else {
                oTable.$('tr.highlight').removeClass("highlight");
                $(this).addClass("highlight");
            }
        });

        //双击每一行,查看数据
        $('#postList tbody').on('dblclick', 'tr', function () {
            var data = oTable.fnGetData( this );
            $('#myModal').modal('show',true);
            window.location.href='viewAlgorithm.html?encryptionAlgorithmId='+ data.encryptionAlgorithmId;
        });

        // "添加"按钮
        $('#post_add').click(function (e) {
            clearModalData();
            $('#myModal').modal('show',true);
        });

        //"修改"按钮
        $('#post_modify').click(function (e) {
            clearModalData();
            if(selectTr!=null){
                var selectData = oTable.fnGetData(selectTr);
                $('#myModal').modal('show',true);
                window.location.href='modifyAlgorithm.html?encryptionAlgorithmId='+ selectData.encryptionAlgorithmId;
            }
        });

        // “删除” 按钮
        $('#post_delete').click(function (e) {
            if(selectTr!=null){
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
                    message: '确定删除这一行吗 ?',
                    title: "消息提示",
                    callback: function(result) {
                        if(result) {
                            var obj = [];
                            var deleteData = oTable.fnGetData(selectTr);
                            obj.push(StringUtil.decorateRequestData('String',deleteData.encryptionAlgorithmId));
                            $.ajax({
                                type:'post',
                                dataType:"json",
                                async: false,
                                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                    ,proxyClass:'fileController',proxyMethod:'deleteEncryptionAlgorithm',jsonString:MyJsonUtil.obj2str(obj)}),
                                success:function(result){
                                    if(result.success){
                                        $.pnotify({
                                            text: result.msg.actionResultMessage
                                        });
                                        //客户端”删除“
                                        oTable.fnDeleteRow(selectTr);
                                    }else{
                                        $.pnotify({
                                            type:'error',
                                            text: result.msg,
                                            delay: 8000
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });


        function clearModalData(){
            $("#myModal").on("hidden.bs.modal", function() {
                $(this).removeData("bs.modal");
            });
        }

    };

    var handleTable2 = function () {
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
                                $('#myModal').modal('hide');
                                oTable.api().ajax.reload();
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