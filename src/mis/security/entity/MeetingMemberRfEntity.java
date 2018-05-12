package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the meeting_member_rf database table.
 * 
 */
@Entity
@Table(name="meeting_member_rf",schema="meeting")
public class MeetingMemberRfEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String meetingMemberRfId;
	private String employeeId;
	private String meetingId;
	private String meetingMemberRoleId;

	public MeetingMemberRfEntity() {
	}


	@Id
	@Column(name="MEETING_MEMBER_RF_ID", unique=true, nullable=false, length=32)
	public String getMeetingMemberRfId() {
		return this.getId();
	}

	public void setMeetingMemberRfId(String meetingMemberRfId) {
		this.setId(meetingMemberRfId);
	}


	@Column(name="EMPLOYEE_ID", nullable=false, length=32)
	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	@Column(name="MEETING_ID", nullable=false, length=32)
	public String getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}


	@Column(name="MEETING_MEMBER_ROLE_ID", nullable=false, length=32)
	public String getMeetingMemberRoleId() {
		return this.meetingMemberRoleId;
	}

	public void setMeetingMemberRoleId(String meetingMemberRoleId) {
		this.meetingMemberRoleId = meetingMemberRoleId;
	}

}