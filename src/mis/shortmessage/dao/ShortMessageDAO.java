package mis.shortmessage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import mis.shortmessage.constant.ShortMessageConst;
import mis.shortmessage.dto.ShortMessageCenterDTO;
import ecp.bsp.system.core.BaseDAO;

@Repository
public class ShortMessageDAO extends BaseDAO {

	public void setWaitingSendingMessageMeetingNull(String inMeetingId) throws Exception {
		// 设置信息发送中心表会议外键指定会议ID为空
		this.excuteSQL(ShortMessageConst.SQL_SET_MESSAGE_SEND_CENTER_NULL_BY_MEETING_ID, new Object[] {inMeetingId});
	}

	public void setWaitingSendingMessageDisactive(String inMeetingId) throws Exception {
		// 设置信息发送中心表会议外键指定会议ID为空
		this.excuteSQL(ShortMessageConst.SQL_SET_MESSAGE_SEND_CENTER_DISACTIVE_BY_MEETING_ID, new Object[] {inMeetingId});
	}

	public void deleteWaitingSendingMessage(String inMeetingId) throws Exception {
		this.excuteSQL(ShortMessageConst.SQL_DELETE_MESSAGE_SEND_CENTER_WAITING_MESSAGE_BY_MEETING_ID, new Object[] {inMeetingId, inMeetingId});
	}

	@SuppressWarnings("unchecked")
	public ShortMessageCenterDTO getShortMessageCenterInfo() {
		List<ShortMessageCenterDTO> tmpShortMessageCenterDTOist = (List<ShortMessageCenterDTO>) this.query(ShortMessageConst.SQL_GET_SHORT_MESSAGE_CENTER_INFO, ShortMessageCenterDTO.class);
		return tmpShortMessageCenterDTOist.size() > 0 ? tmpShortMessageCenterDTOist.get(0) : null;
	}

}
