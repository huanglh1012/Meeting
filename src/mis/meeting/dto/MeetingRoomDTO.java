package mis.meeting.dto;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

@Component
public class MeetingRoomDTO extends BaseDTO {
	private String meetingRoomId;
	private String meetingRoomName;
	private String meetingRoomAddress;
	public String getMeetingRoomId() {
		return this.meetingRoomId;
	}
	public void setMeetingRoomId(String meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}
	public String getMeetingRoomName() {
		return meetingRoomName;
	}
	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}
	public String getMeetingRoomAddress() {
		return this.meetingRoomAddress;
	}
	public void setMeetingRoomAddress(String meetingRoomAddress) {
		this.meetingRoomAddress = meetingRoomAddress;
	}
	
}
