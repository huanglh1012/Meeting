package mis.shortmessage.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;

import mis.shortmessage.constant.ShortMessageConst;
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
import ecp.bsp.system.commons.utils.PropertiesUtil;
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
	
	public ShortMessageResultDTO sendMessage(String inSendTelePhones, String inMessageParam) {
		try {
			PropertiesUtil tmpPropertiesUtil = new PropertiesUtil(ShortMessageConst.PROPERTIES_NAME_FILE_DIR_PATH);
			String tmpSendUrl= tmpPropertiesUtil.readPropertiesByName(ShortMessageConst.PROPERTIES_KEY_SEND_URL);
			String tmpCallerId = tmpPropertiesUtil.readPropertiesByName(ShortMessageConst.PROPERTIES_KEY_CALLER_ID);
			String tmpPassword = tmpPropertiesUtil.readPropertiesByName(ShortMessageConst.PROPERTIES_KEY_PASSOWRD);
			String tmpTmplateId = tmpPropertiesUtil.readPropertiesByName(ShortMessageConst.PROPERTIES_KEY_TEMPLATE_ID);
			String tmpCollectPostSMS = "<SMSEntity callerId=\"" + tmpCallerId + "\">" + "<password>" + tmpPassword + "</password>" + 
				    "<templateId>" + tmpTmplateId + "</templateId>" + "<param>" + inMessageParam + "</param>" 
					+ "<phone>" + inSendTelePhones + "</phone>"+ "</SMSEntity>";
			
			// 第一步：建立连接
			URL tmpPostUrl = new URL(tmpSendUrl);
			
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
			
			return tmpShortMessageResultDTO;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
