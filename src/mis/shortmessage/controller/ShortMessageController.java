package mis.shortmessage.controller;


import javax.annotation.Resource;

import mis.shortmessage.dto.ShortMessageCenterDTO;
import mis.shortmessage.service.ShortMessageService;

import org.springframework.stereotype.Controller;

import ecp.bsp.system.commons.dto.ActionResult;
import ecp.bsp.system.commons.dto.AjaxResult;

@Controller
public class ShortMessageController  {
	
	@Resource
	private ShortMessageService shortMessageService;
	
	/**
	 * 保存短信中心信息
	 * 
	 * @param inShortMessageCenterDTO
	 * 			短信中心信息
	 * @return
	 * 			返回短信中心保存情况
	 * @throws Exception
	 */
	public AjaxResult saveShortMessageCenter(ShortMessageCenterDTO inShortMessageCenterDTO) throws Exception {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.shortMessageService.saveShortMessageCenter(inShortMessageCenterDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 获取短信中心信息
	 * 
	 * @return
	 *     	返回短信中心信息
	 * @throws Exception 
	 */
	public Object getShortMessageCenterInfo() throws Exception {
		return this.shortMessageService.getShortMessageCenterInfo();
	}
}
