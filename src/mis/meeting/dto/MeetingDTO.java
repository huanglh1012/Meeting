package mis.meeting.dto;

import java.sql.Timestamp;
import java.util.List;

import ecp.bsp.system.core.BaseDTO;

public class MeetingDTO extends BaseDTO {
	private String meetingId;
	private String meetStateId;
	private String meetingAttentions;
	private Timestamp meetingEndTime;
	private Timestamp meetingProposeTime;
	private String meetingRoomId;
	private Timestamp meetingStartTime;
	private String meetingSubject;
	private List<List<MeetingMemberRfDTO>> meetingMemberList;

	public String getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetStateId() {
		return this.meetStateId;
	}

	public void setMeetStateId(String meetStateId) {
		this.meetStateId = meetStateId;
	}

	public String getMeetingAttentions() {
		return this.meetingAttentions;
	}

	public void setMeetingAttentions(String meetingAttentions) {
		this.meetingAttentions = meetingAttentions;
	}

	public Timestamp getMeetingEndTime() {
		return this.meetingEndTime;
	}

	public void setMeetingEndTime(Timestamp meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}

	public Timestamp getMeetingProposeTime() {
		return this.meetingProposeTime;
	}

	public void setMeetingProposeTime(Timestamp meetingProposeTime) {
		this.meetingProposeTime = meetingProposeTime;
	}

	public String getMeetingRoomId() {
		return this.meetingRoomId;
	}

	public void setMeetingRoomId(String meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}

	public Timestamp getMeetingStartTime() {
		return this.meetingStartTime;
	}

	public void setMeetingStartTime(Timestamp meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}

	public String getMeetingSubject() {
		return this.meetingSubject;
	}

	public void setMeetingSubject(String meetingSubject) {
		this.meetingSubject = meetingSubject;
	}
	
	public List<List<MeetingMemberRfDTO>> getMeetingMemberList() {
		return this.meetingMemberList;
	}

	public void setMeetingMemberList(List<List<MeetingMemberRfDTO>> meetingMemberList) {
		this.meetingMemberList = meetingMemberList;
	}
}
