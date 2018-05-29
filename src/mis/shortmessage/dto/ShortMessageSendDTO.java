package mis.shortmessage.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

@Component
public class ShortMessageSendDTO extends BaseDTO implements Serializable {
	private static final long serialVersionUID = 5251376728651894834L;
	private String meetingId; 
	private String meetingStateId;
	private String meetingSubject;
	private String meetingStateName;
	private String meetingAttentions;
	private String meetingRoomId;
	private String meetingRoomName;
	private String employeeId;
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getMeetingStateId() {
		return meetingStateId;
	}
	public void setMeetingStateId(String meetingStateId) {
		this.meetingStateId = meetingStateId;
	}
	public String getMeetingSubject() {
		return meetingSubject;
	}
	public void setMeetingSubject(String meetingSubject) {
		this.meetingSubject = meetingSubject;
	}
	public String getMeetingStateName() {
		return meetingStateName;
	}
	public void setMeetingStateName(String meetingStateName) {
		this.meetingStateName = meetingStateName;
	}
	public String getMeetingAttentions() {
		return meetingAttentions;
	}
	public void setMeetingAttentions(String meetingAttentions) {
		this.meetingAttentions = meetingAttentions;
	}
	public String getMeetingRoomId() {
		return meetingRoomId;
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
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
}
