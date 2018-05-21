package mis.shortmessage.dao;

import org.springframework.stereotype.Repository;

import mis.shortmessage.constant.ShortMessageConst;
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

}
