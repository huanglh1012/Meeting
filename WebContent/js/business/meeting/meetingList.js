/**
 * FileName: meetingList.js
 * File description: 用于加载会议列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * meetingList所有属性和方法定义
 * @type {meetingList}
 */
var meetingList = function () {

    var handleDatePicker = function () {
        $("#meetingStartTime, #meetingEndTime").datepicker({
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

    var handleTable = function () {
        // 表头定义
        var tableHead = [
            { "sTitle": "会议ID", "mData": "meetingId","bVisible":false},
            { "sTitle": "会议主题","mData": "meetingSubject","type" :"string" },
            { "sTitle": "会议时间", "mData": "meetingStartTime","type":"date"},
            { "sTitle": "会议地点", "mData": "meetingRoomAddress","type" :"string" },
            { "sTitle": "发起人", "mData": "proposer","type" :"proposerCombo"},
            { "sTitle": "发起部门", "mData": "proposeDepartment","type" :"proposeDepartmentCombo"},
            { "sTitle": "会议状态", "mData": "meetingStatus","type" :"meetingStatusCombo"}
        ];
        var oTable =  $('#meetingList').dataTable({
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
            }, {
                "targets": [2],
                "render": function(data, type, full) {
                    data = data || "";
                    return '<a class="without-decoration font-default" title="' + data + '" href="javascript:;">' + data + '</a>';
                }
            }]
        });

        var table = $('#dt_issues').DataTable(),
            colvis = new $.fn.dataTable.ColVis(table, {
                buttonText: "显示 / 隐藏 列",
                bRestore: true,
                sRestore: "显示全部"
            });

        $("#dt_issues").resizableColumns({
            store: window.store
        });
    }

    return {
        _select2InitValue: {
            proposeDepartment: [{
                "id": "B2BIC",
                "text": "B2BIC"
            }, {
                "id": "E-MAIL",
                "text": "E-MAIL"
            }, {
                "id": "FTP专线",
                "text": "FTP专线"
            }],
            proposeEmployee: [{
                "id": "B2BIC",
                "text": "B2BIC"
            }, {
                "id": "E-MAIL",
                "text": "E-MAIL"
            }, {
                "id": "FTP专线",
                "text": "FTP专线"
            }],
            charger: [{
                "id": "B2BIC",
                "text": "B2BIC"
            }, {
                "id": "E-MAIL",
                "text": "E-MAIL"
            }, {
                "id": "FTP专线",
                "text": "FTP专线"
            }],
            meetingRoomAddress: [{
                "id": "B2BIC",
                "text": "B2BIC"
            }, {
                "id": "E-MAIL",
                "text": "E-MAIL"
            }, {
                "id": "FTP专线",
                "text": "FTP专线"
            }]
        },
        init: function () {
            searchCommon.select2InitValue = this._select2InitValue;
            searchCommon.tableAjaxParam.proxyClass = "fileController";
            searchCommon.tableAjaxParam.proxyMethod = "getReportTaskListByCondition";
            searchCommon.bindingSearchEvent();
            searchCommon.init();
            common.loadDatatableSettings();
            handleDatePicker();
            handleTable();
        }
    };
}();