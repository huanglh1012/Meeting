/**
 * FileName: departmentList.js
 * File description: 用于加载部门列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * departmentList所有属性和方法定义
 * @type {departmentList}
 */
var departmentList = function () {
    var zTreeObj = null;
    var handleButton = function() {
        $('#add').on('click', function (e) {
            $('#myModal').modal('show',true);
        });
    }

    var handleTree = function() {
        var setting = {
            view: {
//                addHoverDom: addHoverDom,
//                removeHoverDom: removeHoverDom,
                selectedMulti: false
            },
            check: {
                enable: false
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            edit: {
                enable: false
            },
            callback : {
                onClick : zTreeOnClickRight
            }
        };

        // 树的单击事件
        function zTreeOnClickRight(event, treeId, treeNode) {
            clearModalData();
            var treeId = zTreeObj.getSelectedNodes()[0].id;
            var treePid = zTreeObj.getSelectedNodes()[0].pId;
            var treeName = zTreeObj.getSelectedNodes()[0].name;
            $('input[name="parentDepartmentName"]').val(treeName);
            $('input[name="parentDepartmentId"]').val(treePid);
            $('input[name="departmentName"]').val();
        }

        function clearModalData(){
            $('input[name="departmentId"]').val('');
            $('input[name="parentDepartmentId"]').val('');
            $('input[name="parentDepartmentName"]').val('');
            $('input[name="departmentName"]').val('');
        }

        var newCount = 1;
        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_"+treeNode.tId);
            if (btn) btn.bind("click", function(){
                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
                return false;
            });
        };
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
        };

        $.ajax({
            type: "POST",
            url: SMController.getUrl({
                controller: 'controllerProxy',
                method: 'callBack',
                proxyClass: 'securityController',
                proxyMethod: 'getDepartmentTreeList',
                jsonString: null
            }),
            dataType: "json",
            success: function (result) {
                zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, result);
            }
        });
    }

    return {
        init: function () {
            handleButton();
            handleTree();
        }
    };
}();