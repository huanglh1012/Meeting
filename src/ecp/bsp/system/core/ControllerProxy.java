package ecp.bsp.system.core;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ecp.bsp.system.commons.dto.DataTypeDTO;
import ecp.bsp.system.commons.utils.jackson.JSONUtil;
import ecp.bsp.system.framework.context.ContextBeanFactory;

/**
 * 鎺у埗浠ｇ悊灞�
 *
 * @since 2015-04-22    <br>
 * @author zengqingyue.
 */
@Controller
@RequestMapping("/controllerProxy.do")
public class ControllerProxy {
	
	/**
	 * 璋冪敤瀵瑰簲controller鍜屾柟娉�
	 * @param className
	 * @param methodName
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(params = "method=callBack",method = RequestMethod.POST)
	public @ResponseBody Object callBack(@RequestParam("proxyClass")String className,
			@RequestParam("proxyMethod")String methodName,@RequestParam("jsonString")String jsonString) throws Exception {
		String tmpJsonString = java.net.URLDecoder.decode(jsonString,"UTF-8");
		String[] paramArray = JSONUtil.fromJSON(tmpJsonString, String[].class);
		Object obj = ContextBeanFactory.getBeanByName(className);
		
		if (paramArray != null) {
			if (paramArray.length > 0) {
				Class[] classArray = new Class[paramArray.length];
				Object[] paramValueArray = new Object[paramArray.length];
				for (int i=0;i<paramArray.length;i++) {
					Class cls = null;
					DataTypeDTO dataTypeDTO = JSONUtil.fromJSON(paramArray[i], DataTypeDTO.class);
					if (dataTypeDTO != null) {
						if (dataTypeDTO.getDataTypeName().equals("String")) {
							cls = String.class;
						} else if (dataTypeDTO.getDataTypeName().equals("Integer")) {
							cls = Integer.class;
						} else if (dataTypeDTO.getDataTypeName().equals("Boolean")) {
							cls = Boolean.class;
						} else if (dataTypeDTO.getDataTypeName().equals("Float")) {
							cls = Float.class;
						} else if (dataTypeDTO.getDataTypeName().equals("Double")) {
							cls = Double.class;
						} else if (dataTypeDTO.getDataTypeName().equals("List")) {
							cls = List.class;
						} else {
							String dataTypeName = dataTypeDTO.getDataTypeName().substring(0,1).toLowerCase() + dataTypeDTO.getDataTypeName().substring(1);
							cls = ContextBeanFactory.getBeanByName(dataTypeName).getClass();
						}
						
						Object paramValue = JSONUtil.fromJSON(dataTypeDTO.getDataTypeValue(),cls);
						classArray[i] = cls;
						paramValueArray[i] = paramValue;
					} else {
						String exceptionMessage = "系统异常，请联系管理员";
						throw new RuntimeException(exceptionMessage);
					}
				}
				Method method = obj.getClass().getMethod(methodName,classArray); 
				return method.invoke(obj,paramValueArray);
			}
		}
		Method method = obj.getClass().getMethod(methodName); 
		return method.invoke(obj);
	}
	
	/**
	 * 璋冪敤瀵瑰簲controller鍜屾柟娉曞苟浼犻�request瀵硅薄
	 * @param className
	 * @param methodName
	 * @param jsonString
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(params = "method=callBackByRequest",method = RequestMethod.POST)
	public @ResponseBody Object callBackByRequest(@RequestParam("proxyClass")String className,@RequestParam("proxyMethod")String methodName,
			@RequestParam("jsonString")String jsonString,HttpServletRequest request) throws Exception {
		String tmpJsonString = java.net.URLDecoder.decode(jsonString,"UTF-8");
		String[] paramArray = JSONUtil.fromJSON(tmpJsonString, String[].class);
		Class[] classArray = null;
		Object[] paramValueArray = null;
		
		if (paramArray != null ) {
			classArray = new Class[paramArray.length+1];
			paramValueArray = new Object[paramArray.length+1];
			for (int i=0;i<paramArray.length;i++) {
				Class cls = null;
				DataTypeDTO dataTypeDTO = JSONUtil.fromJSON(paramArray[i], DataTypeDTO.class);
				if (dataTypeDTO != null) {
					if (dataTypeDTO.getDataTypeName().equals("String")) {
						cls = String.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Integer")) {
						cls = Integer.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Boolean")) {
						cls = Boolean.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Float")) {
						cls = Float.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Double")) {
						cls = Double.class;
					} else if (dataTypeDTO.getDataTypeName().equals("List")) {
						cls = List.class;
					} else {
						String dataTypeName = dataTypeDTO.getDataTypeName().substring(0,1).toLowerCase() + dataTypeDTO.getDataTypeName().substring(1);
						cls = ContextBeanFactory.getBeanByName(dataTypeName).getClass();
					}
					
					Object paramValue = JSONUtil.fromJSON(dataTypeDTO.getDataTypeValue(),cls);
					classArray[i] = cls;
					paramValueArray[i] = paramValue;
				} else {
					String exceptionMessage = "系统异常，请联系管理员";
					throw new RuntimeException(exceptionMessage);
				}
			}
			classArray[paramArray.length] = HttpServletRequest.class;
			paramValueArray[paramArray.length] = request;
		} else {
			classArray = new Class[1];
			paramValueArray = new Object[1];
			classArray[0] = HttpServletRequest.class;
			paramValueArray[0] = request;
		}
		
		Object obj = ContextBeanFactory.getBeanByName(className);
		Method method = obj.getClass().getMethod(methodName,classArray); 
		return method.invoke(obj,paramValueArray);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(params = "method=callBackByRequestAndResponse",method = RequestMethod.POST)
	public @ResponseBody Object callBackByRequestAndResponse(@RequestParam("proxyClass")String className,@RequestParam("proxyMethod")String methodName,
			@RequestParam("jsonString")String jsonString, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmpJsonString = java.net.URLDecoder.decode(jsonString,"UTF-8");
		String[] paramArray = JSONUtil.fromJSON(tmpJsonString, String[].class);
		Class[] classArray = null;
		Object[] paramValueArray = null;
		
		if (paramArray != null ) {
			classArray = new Class[paramArray.length+2];
			paramValueArray = new Object[paramArray.length+2];
			for (int i=0;i<paramArray.length;i++) {
				Class cls = null;
				DataTypeDTO dataTypeDTO = JSONUtil.fromJSON(paramArray[i], DataTypeDTO.class);
				if (dataTypeDTO != null) {
					if (dataTypeDTO.getDataTypeName().equals("String")) {
						cls = String.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Integer")) {
						cls = Integer.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Boolean")) {
						cls = Boolean.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Float")) {
						cls = Float.class;
					} else if (dataTypeDTO.getDataTypeName().equals("Double")) {
						cls = Double.class;
					} else if (dataTypeDTO.getDataTypeName().equals("List")) {
						cls = List.class;
					} else {
						String dataTypeName = dataTypeDTO.getDataTypeName().substring(0,1).toLowerCase() + dataTypeDTO.getDataTypeName().substring(1);
						cls = ContextBeanFactory.getBeanByName(dataTypeName).getClass();
					}
					
					Object paramValue = JSONUtil.fromJSON(dataTypeDTO.getDataTypeValue(),cls);
					classArray[i] = cls;
					paramValueArray[i] = paramValue;
				} else {
					String exceptionMessage = "系统异常，请联系管理员";
					throw new RuntimeException(exceptionMessage);
				}
			}
			classArray[paramArray.length] = HttpServletRequest.class;
			classArray[paramArray.length + 1] = HttpServletResponse.class;
			paramValueArray[paramArray.length] = request;
			paramValueArray[paramArray.length + 1] = response;
		} else {
			classArray = new Class[2];
			paramValueArray = new Object[2];
			classArray[0] = HttpServletRequest.class;
			classArray[1] = HttpServletRequest.class;
			paramValueArray[0] = request;
			paramValueArray[1] = request;
		}
		
		Object obj = ContextBeanFactory.getBeanByName(className);
		Method method = obj.getClass().getMethod(methodName,classArray); 
		return method.invoke(obj,paramValueArray);
	}
}