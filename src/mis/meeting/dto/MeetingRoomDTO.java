package mis.meeting.dto;

import ecp.bsp.system.core.BaseDTO;

public class MeetingRoomDTO extends BaseDTO {
	private String meetingRoomId;
	private String meetingRoomAddress;
	public String getMeetingRoomId() {
		return this.meetingRoomId;
	}
	public void setMeetingRoomId(String meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}
	public String getMeetingRoomAddress() {
		return this.meetingRoomAddress;
	}
	public void setMeetingRoomAddress(String meetingRoomAddress) {
		this.meetingRoomAddress = meetingRoomAddress;
	}
	
}
