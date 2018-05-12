package mis.meeting.dto;

import ecp.bsp.system.core.BaseDTO;

public class MeetingMemberRfDTO extends BaseDTO {
	private String employeeId;
	private String employeeName;
	private String departmentId;
	private String departmentName;
	private String meetingId;
	private String meetingMemberRoleId;
	
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
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getMeetingMemberRoleId() {
		return meetingMemberRoleId;
	}
	public void setMeetingMemberRoleId(String meetingMemberRoleId) {
		this.meetingMemberRoleId = meetingMemberRoleId;
	}
}
