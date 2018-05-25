/**
 *  App全局用户信息类
 * @namespace CurrentLoginUser
 * @author  zengqingyue
 * @since   2015-06-01
 */
var CurrentLoginUser={
	/**
	 * 用户数据
	 * @property  data
	 */
	data:{},
	/**
	 * 获取用户信息
	 * @function {object} get
	 * @returns 用户信息
	 */
	get:_get
};

function _get(name){
	return CurrentLoginUser.data[name];
}

(function($){
    var tmpLoginDTO = JSON.parse(localStorage.getItem("LoginDTO"));
    if (tmpLoginDTO != null) {
        var obj = [];
        obj.push(StringUtil.decorateRequestData('String', tmpLoginDTO.employeeId));
        //获取当前用户任务节点
        $.ajax({
            type:'post',
            dataType:"json",
            async: false,
            url:SMController.getUrl({controller:'controllerProxy',method:'callBack'
                ,proxyClass:'securityController',proxyMethod:'getEmployeeDetailInfoByEmployeeId',jsonString:MyJsonUtil.obj2str(obj)}),
            success:function(result){
                localStorage.removeItem("EmployeeDTO");
                localStorage.setItem("EmployeeDTO", JSON.stringify(result));
            }
        });
    }
})(jQuery);