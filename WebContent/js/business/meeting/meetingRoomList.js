/**
 * FileName: meetingRoomList.js
 * File description: 用于加载会议材料列表页面的组件及内容
 * Copyright (c) 2018 Kia, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:kiatsang@163.com">kia</a>
 * @DateTime: 2018-05-21
 */

/**
 * meetingRoomList所有属性和方法定义
 * @type {meetingRoomList}
 */
var meetingRoomList = function () {
    var meetingRoomTable = null;
    var meetingRoomBookingTable = null;
    var selectTr = null;

    var handleButton = function() {
        // "添加"按钮
        $('#addMeetingRoomBtn').click(function (e) {
            clearModalData();
            $('#meetingRoomModal').modal('show',true);
        });

        //"修改"按钮
        $('#modifyMeetingRoomBtn').click(function (e) {
            clearModalData();
            if(selectTr != null){
                $('#meetingRoomModal').modal('show',true);
                $('input[name=meetingRoomId]').val(selectTr.meetingRoomId);
                $('input[name=meetingRoomName]').val(selectTr.meetingRoomName);
                $('input[name=meetingRoomAddress]').val(selectTr.meetingRoomAddress);
            } else {
                $.pnotify({
                    text: '请选择需要修改的会议室信息'
                });
            }
        });

        // “删除” 按钮
        $('#deleteMeetingRoomBtn').click(function (e) {
            if (selectTr != null) {
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
                    message: '确定删除【'+selectTr.meetingRoomName+'】会议室吗 ?',
                    title: "消息提示",
                    callback: function(result) {
                        if(result) {
                            var obj = [];
                            obj.push(StringUtil.decorateRequestData('String',selectTr.meetingRoomId));
                            $.ajax({
                                type:'post',
                                dataType:"json",
                                async: false,
                                url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                                    ,proxyClass:'meetingController',proxyMethod:'deleteMeetingRoom',jsonString:MyJsonUtil.obj2str(obj)}),
                                success:function(result){
                                    if(result.success){
                                        selectTr = null;
                                        meetingRoomTable.api().ajax.reload();
                                        $.pnotify({
                                            text: result.msg
                                        });
                                    }else{
                                        $.pnotify({
                                            text: "会议室已被使用，无法删除.",
                                            delay: 4000
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                $.pnotify({
                    text: '请选择需要删除的会议室信息'
                });
            }
        });

        $('#submitMeetingRoom').on('click', function (e) {
            var meetingRoomId = $('input[name=meetingRoomId]').val();
            var meetingRoomName = $('input[name=meetingRoomName]').val();
            var meetingRoomAddress = $('input[name=meetingRoomAddress]').val();

            var obj = [];
            var addData = {};
            addData['meetingRoomId'] = meetingRoomId;
            addData['meetingRoomName'] = meetingRoomName;
            addData['meetingRoomAddress'] = meetingRoomAddress;

            if (meetingRoomName == '' || meetingRoomAddress == '') {
                $('#errorTips').text("会议室名称和地址不能为空");
            } else {
                obj.push(StringUtil.decorateRequestData('MeetingRoomDTO', addData));
                $.ajax({
                    type: "POST",
                    url: SMController.getUrl({
                        controller: 'controllerProxy',
                        method: 'callBack',
                        proxyClass: 'meetingController',
                        proxyMethod: meetingRoomId == '' ? 'insertMeetingRoom' : 'updateMeetingRoom',
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
                                selectTr = null;
                                $('#meetingRoomModal').modal('hide');
                                meetingRoomTable.api().ajax.reload();
                                $.pnotify({
                                    text: result.msg.actionResultMessage
                                });
                            }, 1500);

                        } else {
                            $.unblockUI();
                            $('#errorTips').text(result.msg);
                        }
                    }
                });
            }
        });

        function clearModalData(){
            $('#errorTips').text("");
            $('input[name=meetingRoomId]').val('');
            $('input[name=meetingRoomName]').val('');
            $('input[name=meetingRoomAddress]').val('');
        }
    }

    var handleTable = function () {
        // 表头定义
        var tmpMeetingRoomTableHead = [
            { "sTitle": "会议室ID", "mData": "meetingRoomId","bVisible":false},
            { "sTitle": "会议室","mData": "meetingRoomName","type" :"string" },
            { "sTitle": "会议室地址", "mData": "meetingRoomAddress","type":"string"}
        ];
        var tmpMeetingRoomBookingListTableHead = [
            { "sTitle": "开始时间","mData": "meetingStartTime","type" :"string" },
            { "sTitle": "结束时间", "mData": "meetingEndTime","type":"string"},
            { "sTitle": "发起人","mData": "employeeName","type" :"string" },
            { "sTitle": "发起部门", "mData": "departmentName","type":"string"}
        ];

        meetingRoomTable = $('#meetingRoomList').dataTable({
            //表头设置
            "aoColumns": tmpMeetingRoomTableHead,
            "aLengthMenu":[ 10, 25, 50,100],
            "bAutoWidth" : true,
            //默认显示的分页数
            "iDisplayLength": 10,
            "paging": true,
            "sDom": "<'dt-top-row'><'dt-wrapper't><'dt-row dt-bottom-row'<'row'<'col-sm-4'i><'col-sm-8 text-right'p>><'row'<'col-xs-12 col-sm-12 col-md-12 col-lg-12'l>>>",
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
                    ,proxyClass:'meetingController',proxyMethod:'getMeetingRoomList',jsonString:null}),
                dataType:"json",
                success:function(data) {
                    meetingRoomTable.fnClearTable();
                    if (data.length > 0)
                        meetingRoomTable.fnAddData(data);
                }
            }
        });

        //单击每一行的操作（根据复选框的值来获得行数据）
        $('#meetingRoomList tbody').on('click','tr', function () {
            if ($(this).hasClass("highlight")){
                $(this).removeClass("highlight");
                selectTr = null;
            } else {
                meetingRoomTable.$('tr.highlight').removeClass("highlight");
                $(this).addClass("highlight");
                selectTr = meetingRoomTable.fnGetData(this);
                var obj = [];
                obj.push(StringUtil.decorateRequestData('String',selectTr.meetingRoomId));
                $.ajax({
                    type: "POST",
                    url: SMController.getUrl({
                        controller: 'controllerProxy',
                        method: 'callBack',
                        proxyClass: 'meetingController',
                        proxyMethod: 'getMeetingRoomBookingListByRoomId',
                        jsonString: MyJsonUtil.obj2str(obj)
                    }),
                    dataType: "json",
                    success: function (result) {
                        meetingRoomBookingTable.fnClearTable();
                        if (result.length > 0)
                            meetingRoomBookingTable.fnAddData(result);
                    }
                });
            }
        });

        meetingRoomBookingTable =  $('#meetingRoomBookingList').dataTable({
            "aoColumns": tmpMeetingRoomBookingListTableHead,
            "aLengthMenu":[ 10, 25, 50,100],
            "bAutoWidth" : true,
            //默认显示的分页数
            "iDisplayLength": 10,
            "paging": true,
            "sDom": "<'dt-top-row'><'dt-wrapper't><'dt-row dt-bottom-row'<'row'<'col-sm-4'i><'col-sm-8 text-right'p>><'row'<'col-xs-12 col-sm-12 col-md-12 col-lg-12'l>>>",
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
            }
        });
    }

    return {
        init: function () {
            handleButton();
            handleTable();
        }
    };
}();