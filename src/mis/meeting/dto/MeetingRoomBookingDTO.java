package mis.meeting.dto;

import java.sql.Timestamp;

import ecp.bsp.system.core.BaseDTO;

public class MeetingRoomBookingDTO extends BaseDTO {
	private String meetingRoomId;
	private String meetingRoomAddress;
	private String meetingId;
	private String meetingSubject;
	private Timestamp meetingStartTime;
	private Timestamp meetingEndTime;
	private String employeeId;
	private String employeeName;
	private String departmentId;
	private String departmentName;
	public String getMeetingRoomId() {
		return meetingRoomId;
	}
	public void setMeetingRoomId(String meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}
	public String getMeetingRoomAddress() {
		return meetingRoomAddress;
	}
	public void setMeetingRoomAddress(String meetingRoomAddress) {
		this.meetingRoomAddress = meetingRoomAddress;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getMeetingSubject() {
		return meetingSubject;
	}
	public void setMeetingSubject(String meetingSubject) {
		this.meetingSubject = meetingSubject;
	}
	public Timestamp getMeetingStartTime() {
		return meetingStartTime;
	}
	public void setMeetingStartTime(Timestamp meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}
	public Timestamp getMeetingEndTime() {
		return meetingEndTime;
	}
	public void setMeetingEndTime(Timestamp meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
