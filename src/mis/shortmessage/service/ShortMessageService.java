package mis.shortmessage.service;

import java.util.List;

import javax.annotation.Resource;

import mis.shortmessage.dao.ShortMessageDAO;
import mis.shortmessage.dto.ShortMessageCenterDTO;
import mis.shortmessage.entity.ShortMessageCenterEntity;

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
}
