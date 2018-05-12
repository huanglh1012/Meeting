package mis.meeting.dao;

import java.sql.Timestamp;
import java.util.List;

import mis.meeting.constant.MeetingConstant;
import mis.meeting.dto.MeetingRoomBookingDTO;
import mis.meeting.entity.MeetingEntity;
import mis.meeting.entity.MeetingStateEntity;
import mis.meeting.myenum.MeetingMemberRoleEnum;
import mis.meeting.myenum.MeetingStateEnum;
import ecp.bsp.system.core.BaseDAO;

public class MeetingDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public MeetingEntity getMeetingByPlanDatetimeRang(String inMeetingRoomId, Timestamp inMeetingStartTime, Timestamp inMeetingEndTime) {
		List<MeetingEntity> tmpMeetingEntityList = (List<MeetingEntity>) this.query(MeetingConstant.SQL_GET_MEETING_BY_PAN_DATETIME_RANG, 
				new Object[] {inMeetingRoomId, inMeetingStartTime, inMeetingStartTime, inMeetingEndTime, inMeetingEndTime}, MeetingEntity.class);
		if (tmpMeetingEntityList == null || tmpMeetingEntityList.isEmpty()) {
			return null;
		}
		
		return tmpMeetingEntityList.get(0);
	}

	public void CloseMeeting(String inMeetingId) throws Exception {
		MeetingStateEntity tmpMeetingStateEntity = this.getEntity(MeetingStateEntity.class, "meetingStateCode", MeetingStateEnum.MEETING_CLOSE.ordinal());
		this.excuteSQL(MeetingConstant.SQL_UPDATE_MEETING_STATE, new Object[] { tmpMeetingStateEntity.getMeetingStateId(), inMeetingId });
	}

	public void deleteMeetingMember(String inMeetingId) throws Exception {
		this.excuteSQL(MeetingConstant.SQL_DELETE_MEETING_MEMBER_RF, new Object[] { inMeetingId });
	}

	@SuppressWarnings("unchecked")
	public List<MeetingRoomBookingDTO> getMeetingRoomBookingListByRoomId(String inMeetingRoomId) {
		return (List<MeetingRoomBookingDTO>) this.query(MeetingConstant.SQL_GET_MEETING_ROOM_BOOKING_LIST_BY_ROOM_ID, new Object[] { 
				inMeetingRoomId, MeetingMemberRoleEnum.MEETING_CREATOR.ordinal() }, MeetingRoomBookingDTO.class);
	}

}
