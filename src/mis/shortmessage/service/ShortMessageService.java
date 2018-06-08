package mis.shortmessage.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import mis.shortmessage.dao.ShortMessageDAO;
import mis.shortmessage.dto.ShortMessageCenterDTO;
import mis.shortmessage.dto.ShortMessageResultDTO;
import mis.shortmessage.entity.ShortMessageCenterEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import ecp.bsp.system.commons.constant.ExceptionCodeConst;
import ecp.bsp.system.commons.dto.ActionResult;
import ecp.bsp.system.commons.utils.ActionResultUtil;
import ecp.bsp.system.commons.utils.LoggerUtil;
import ecp.bsp.system.core.BaseService;

@Service
public class ShortMessageService extends BaseService {
	@Resource
	private ShortMessageDAO shortMessageDAO;
	
	private ActionResult insertShortMessageCenter(ShortMessageCenterDTO inShortMessageCenterDTO) throws Exception {
		ShortMessageCenterEntity tmpNewShortMessageCenterEntity = new ShortMessageCenterEntity();
        ShortMessageCenterDTO.dtoToEntity(inShortMessageCenterDTO, tmpNewShortMessageCenterEntity);
		this.shortMessageDAO.insert(tmpNewShortMessageCenterEntity);
		
		return ActionResultUtil.getActionResult(tmpNewShortMessageCenterEntity.getId(), "短信中心新建成功");
	}
	
	private ActionResult updateShortMessageCenter(ShortMessageCenterDTO inShortMessageCenterDTO) throws Exception {
		ShortMessageCenterEntity tmpShortMessageCenterEntity = this.shortMessageDAO.getEntity(ShortMessageCenterEntity.class, inShortMessageCenterDTO.getShortMessageCenterId());
        ShortMessageCenterDTO.dtoToEntity(inShortMessageCenterDTO, tmpShortMessageCenterEntity);
		this.shortMessageDAO.update(tmpShortMessageCenterEntity);
		
		return ActionResultUtil.getActionResult(tmpShortMessageCenterEntity.getId(), "短信中心修改成功");
	}
	
	public ActionResult saveShortMessageCenter(ShortMessageCenterDTO inShortMessageCenterDTO) throws Exception {
		ActionResult tmpActionResult = null;
		ShortMessageCenterEntity tmpShortMessageCenterEntity = this.shortMessageDAO.getEntity(ShortMessageCenterEntity.class, inShortMessageCenterDTO.getShortMessageCenterId());
		if (tmpShortMessageCenterEntity == null) {
			List<ShortMessageCenterEntity> tmpShortMessageCenterEntityList = this.shortMessageDAO.getEntityList(ShortMessageCenterEntity.class, "");
			if (tmpShortMessageCenterEntityList != null && !tmpShortMessageCenterEntityList.isEmpty()) {
				String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "数据中心信息有误，请联系技术支持处理";
				LoggerUtil.instance(this.getClass()).error(exceptionMessage);
				throw new RuntimeException(exceptionMessage);
			}
			
			tmpActionResult = this.insertShortMessageCenter(inShortMessageCenterDTO);
		} else {
			tmpActionResult = this.updateShortMessageCenter(inShortMessageCenterDTO);
		}
		
		return tmpActionResult;
	}
	
	public ShortMessageCenterDTO getShortMessageCenterInfo() throws Exception {
		return this.shortMessageDAO.getShortMessageCenterInfo();
	}
	
	public ActionResult testSendMessage(ShortMessageCenterDTO inShortMessageCenterDTO) throws Exception {	
		// 判断输入数据合法性
		if (inShortMessageCenterDTO.getMessageParam() == null || inShortMessageCenterDTO.getMessageModel() == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "模板内容和模板参数不能为空，请重新输入.";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		if (inShortMessageCenterDTO.getSendMessagePhoneNumber() == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "信息发送号码不能为空，请重新输入.";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		// 检查模板变量和模板参数合法性
		Pattern tmpPattern = Pattern.compile("\\{@(\\w+)\\}");  
		Matcher tmpMatcher = tmpPattern.matcher(inShortMessageCenterDTO.getMessageModel());
		List<String> tmpVariableParamList = new ArrayList<String>();
		while(tmpMatcher.find()){
			tmpVariableParamList.add(tmpMatcher.group(1));
		}
		String[] tmpMessageParam = inShortMessageCenterDTO.getMessageParam().split(",");
		if (tmpMessageParam.length != tmpVariableParamList.size()) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "模板内容的变量值个数与模板参数的参数个数不一致，请重新输入.";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		// 将模板变量替换成模板参数值
		for (int i = 0; i < tmpMessageParam.length; i++) {
			String tmpVariableParam = "{@" + tmpVariableParamList.get(i) + "}";
			inShortMessageCenterDTO.setMessageModel(inShortMessageCenterDTO.getMessageModel().replace(tmpVariableParam, tmpMessageParam[i]));
		}
		inShortMessageCenterDTO.setMessageParam(inShortMessageCenterDTO.getMessageModel());
		
		// 发送信息
		ShortMessageResultDTO tmpShortMessageResultDTO = this.sendMessage(inShortMessageCenterDTO);
		
		return ActionResultUtil.getActionResult(tmpShortMessageResultDTO, "发送测试短信成功");
	}
	
	public ShortMessageResultDTO sendMessage(String inSendTelePhones, String inMessageParam) throws Exception {
		ShortMessageCenterDTO tmpShortMessageCenterDTO  = this.getShortMessageCenterInfo();
		tmpShortMessageCenterDTO.setSendMessagePhoneNumber(inSendTelePhones);
		tmpShortMessageCenterDTO.setMessageParam(inMessageParam);
		return this.sendMessage(tmpShortMessageCenterDTO);
	}
	
	public ShortMessageResultDTO sendMessage(ShortMessageCenterDTO inShortMessageCenterDTO) throws Exception {
		try {
			String tmpCollectPostSMS = "<SMSEntity callerId=\"" + inShortMessageCenterDTO.getCallerId() + "\">" 
					+ "<password>" + inShortMessageCenterDTO.getCallerPassword() + "</password>" 
					+ "<templateId>" + inShortMessageCenterDTO.getMessageTemplateId() + "</templateId>" 
					+ "<param>" + inShortMessageCenterDTO.getMessageParam() + "</param>" 
					+ "<phone>" + inShortMessageCenterDTO.getSendMessagePhoneNumber() + "</phone>"+ "</SMSEntity>";
			
			// 第一步：建立连接
			URL tmpPostUrl = new URL(inShortMessageCenterDTO.getSendUrl());
			
			// 第二步：根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型
			HttpURLConnection tmpHttpURLConnection = (HttpURLConnection) tmpPostUrl.openConnection();
			
			// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到 
			tmpHttpURLConnection.setDoOutput(true);
			tmpHttpURLConnection.setInstanceFollowRedirects(false);
			tmpHttpURLConnection.setRequestMethod("POST");
			tmpHttpURLConnection.setRequestProperty("Content-Type", "application/xml");
			
			// 第四步：读取输入流,得到响应结果
			OutputStream tmpOutputStream = tmpHttpURLConnection.getOutputStream();
			//设置编码,否则中文乱码
			tmpOutputStream.write(tmpCollectPostSMS.getBytes("utf-8"));
			tmpOutputStream.flush();
			BufferedReader tmpBufferedReader = new BufferedReader(new InputStreamReader(tmpHttpURLConnection.getInputStream()));
			String tmpLine = tmpBufferedReader.readLine();
			String tmpResult = StringUtils.EMPTY;
			while (tmpLine != null) {
				tmpResult += tmpLine;
			    tmpLine = tmpBufferedReader.readLine();
		    }
			
			// 第五步:断开连接 
			tmpHttpURLConnection.disconnect();
			
			// 返回发送结果
			ShortMessageResultDTO tmpShortMessageResultDTO = new ShortMessageResultDTO();
			if (tmpResult.length() > 2) {
				JSONArray jsonArray=JSONArray.fromObject(tmpResult);
				Object tmpObject = jsonArray.get(0);
				JSONObject tmpJSONObject = JSONObject.fromObject(tmpObject);
				tmpShortMessageResultDTO = (ShortMessageResultDTO)JSONObject.toBean(tmpJSONObject, ShortMessageResultDTO.class);
			} else {
				tmpShortMessageResultDTO.setCode(tmpResult);
			}
			tmpShortMessageResultDTO.setMessageSendResult(tmpResult);
			tmpShortMessageResultDTO.setShortMessageCenterId(inShortMessageCenterDTO.getShortMessageCenterId());
			
			return tmpShortMessageResultDTO;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
