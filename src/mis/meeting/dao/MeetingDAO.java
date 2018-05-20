package mis.meeting.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import mis.meeting.constant.MeetingConstant;
import mis.meeting.dto.MeetingDTO;
import mis.meeting.dto.MeetingMemberRfDTO;
import mis.meeting.dto.MeetingRoomBookingDTO;
import mis.meeting.dto.MeetingRoomDTO;
import mis.meeting.entity.MeetingEntity;
import mis.meeting.entity.MeetingStateEntity;
import mis.meeting.myenum.MeetingMemberRoleEnum;
import mis.meeting.myenum.MeetingStateEnum;
import ecp.bsp.system.core.BaseDAO;
import ecp.bsp.system.framework.file.data.dto.AttachmentDTO;

@Repository
public class MeetingDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public MeetingEntity getMeetingByPlanDatetimeRang(String inMeetingRoomId, Date inMeetingStartTime, Date inMeetingEndTime) {
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

	/**
	 * 根据会议ID获取会议信息
	 * 
	 * @param inMeetingId
	 *     会议ID
	 * @return
	 *     返回会议信息
	 */
	@SuppressWarnings("unchecked")
	public MeetingDTO getMeetingInfoById(String inMeetingId) throws Exception {
		List<MeetingDTO> tmpMeetingDTOist = (List<MeetingDTO>) this.query(MeetingConstant.SQL_GET_MEETING_INFO_BY_MEETING_ID, 
				new Object[] {inMeetingId}, MeetingDTO.class);
		return tmpMeetingDTOist.size() > 0 ? tmpMeetingDTOist.get(0) : null;
	}
	
	/**
	 * 根据用户ID获取用户发起的会议列表
	 * 
	 * @param inEmployeeId
	 * 			用户ID
	 * @return
	 * 			返回用户发起的会议列表
	 */
	@SuppressWarnings("unchecked")
	public List<MeetingDTO> getCreatorMeetingByEmployeeId(String inEmployeeId) {
		return (List<MeetingDTO>) this.query(MeetingConstant.SQL_GET_CREATOR_MEETING_INFO_BY_EMPLOYEE_ID, new Object[] {inEmployeeId}, MeetingDTO.class);
	}
	
	/**
	 * 根据用户ID获取用户参与的会议列表
	 * 
	 * @param inEmployeeId
	 * 			用户ID
	 * @return
	 * 			返回用户擦米的会议列表
	 */
	@SuppressWarnings("unchecked")
	public List<MeetingDTO> getParticipantMeetingByEmployeeId(String inEmployeeId) {
		return (List<MeetingDTO>) this.query(MeetingConstant.SQL_GET_PARTICIPANT_MEETING_INFO_BY_EMPLOYEE_ID, new Object[] {inEmployeeId}, MeetingDTO.class);
	}
	
	/**
	 * 根据会议ID获取会议信息
	 * 
	 * @param inMeetingId
	 *     会议ID
	 * @return
	 *     返回会议信息
	 */
	@SuppressWarnings("unchecked")
	public List<MeetingMemberRfDTO> getMeetingMemberRfInfoByMeetingId(String inMeetingId) throws Exception {
		return (List<MeetingMemberRfDTO>) this.query(MeetingConstant.SQL_GET_MEETING_MEMBER_RF_LIST, 
				new Object[] {inMeetingId}, MeetingMemberRfDTO.class);
	}
	
	/**
	 * 根据会议ID获取会议附件信息
	 * 
	 * @param inMeetingId
	 *     会议ID
	 * @return
	 *     返回会议信息
	 */
	@SuppressWarnings("unchecked")
	public List<AttachmentDTO> getMeetingAttachmentInfoByMeetingId(String inMeetingId, String inAttachmentCategoryId) throws Exception {
		return (List<AttachmentDTO>) this.query(MeetingConstant.SQL_GET_MEETING_ATTACHMENT_LIST_BY_MEETING_ID, 
				new Object[] {inMeetingId,inAttachmentCategoryId}, AttachmentDTO.class);
	}
	
	/**
	 * 获取会议室信息列表
	 * 
	 * @return
	 *     返回会议室信息列表
	 */
	@SuppressWarnings("unchecked")
	public List<MeetingRoomDTO> getMeetingRoomList() throws Exception  {
		return (List<MeetingRoomDTO>) this.query(MeetingConstant.SQL_GET_MEETING_ROOM_LIST, MeetingRoomDTO.class);
	}
	
	public void deleteMeetingMember(String inMeetingId) throws Exception {
		this.excuteSQL(MeetingConstant.SQL_DELETE_MEETING_MEMBER_RF, new Object[] { inMeetingId });
	}

	@SuppressWarnings("unchecked")
	public List<MeetingRoomBookingDTO> getMeetingRoomBookingListByRoomId(String inMeetingRoomId) {
		return (List<MeetingRoomBookingDTO>) this.query(MeetingConstant.SQL_GET_MEETING_ROOM_BOOKING_LIST_BY_ROOM_ID, new Object[] { 
				inMeetingRoomId}, MeetingRoomBookingDTO.class);
	}

}
