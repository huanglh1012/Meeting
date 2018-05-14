/**
 * FileName: meetingRoomList.js
 * File description: 用于加载会议材料列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * meetingRoomList所有属性和方法定义
 * @type {meetingRoomList}
 */
var meetingRoomList = function () {
    var handleButton = function() {
        $('#add').on('click', function (e) {
            $('#myModal').modal('show',true);
        });
    }

    var handleTable = function () {
        // 表头定义
        var tableHead = [
            { "sTitle": "会议室ID", "mData": "meetingRoomId","bVisible":false},
            { "sTitle": "会议室","mData": "meetingRoomName","type" :"string" },
            { "sTitle": "会议室地址", "mData": "meetingRoomAddress","type":"string"}
        ];
        var tableHead2 = [
            { "sTitle": "会议室ID", "mData": "meetingRoomId","bVisible":false},
            { "sTitle": "开始时间","mData": "meetingSubject","type" :"string" },
            { "sTitle": "结束时间", "mData": "meetingRoomAddress","type":"string"},
            { "sTitle": "发起人","mData": "proposer","type" :"string" },
            { "sTitle": "发起部门", "mData": "proposeDepartment","type":"string"}
        ];
        var oTable =  $('#meetingRoomList').dataTable({
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
        var oTable2 =  $('#meetingRoomTimeList').dataTable({
            "aoColumns": tableHead2,
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

    return {
        init: function () {
            handleButton();
            handleTable();
        }
    };
}();