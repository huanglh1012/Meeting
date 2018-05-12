/**
 * FileName: roleList.js
 * File description: 用于加载角色列表页面的组件及内容
 * Copyright (c) 2017 Eastcompeace, Inc. All Rights Reserved.
 *
 * @author <a href="mailto:zengqingyue@eastcompeace.com">zengqingyue</a>
 * @DateTime: 2017-11-21
 */

/**
 * roleList所有属性和方法定义
 * @type {roleList}
 */
var roleList = function () {
    var handleCheckbox = function() {
        var tmpCheckboxGroup =  $("#securityList");
        $.ajax({
            type:'post',
            dataType:"json",
            async: false,
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getSecurityList',jsonString:null}),
            success:function(result){
                for (var item in result){
                    var securityObj = result[item];
                    var tmpCheckboxLabel = $("<label class='checkbox'>");
                    var tmpInput = $(" <input id='"+securityObj.securityId+"' type='checkbox' value='"+securityObj.securityCode+"'/>");
                    var tmpLabel = $(" <label>"+securityObj.securityName+"</label>");
                    $(tmpCheckboxLabel).appendTo(tmpCheckboxGroup);
                    $(tmpInput).appendTo(tmpCheckboxLabel);
                    $(tmpLabel).appendTo(tmpCheckboxLabel);
                }
            }
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
            }
        };

        var zNodes =[
            { id:1, pId:0, name:"管理员"},
            { id:2, pId:0, name:"公司领导"},
            { id:3, pId:0, name:"普通员工"}
        ];

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

        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    }

    return {
        init: function () {
            handleTree();
            handleCheckbox();
        }
    };
}();