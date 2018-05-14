package mis.meeting.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the meeting_member_role database table.
 * 
 */
@Entity
@Table(name="MEETING_MEMBER_ROLE",schema="meeting")
public class MeetingMemberRoleEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String meetingMemberRoleId;
	private BigDecimal meetingMemberRoleCode;
	private String meetingMemberRoleName;

	public MeetingMemberRoleEntity() {
	}


	@Id
	@Column(name="MEETING_MEMBER_ROLE_ID", unique=true, nullable=false, length=32)
	public String getMeetingMemberRoleId() {
		return this.getId();
	}

	public void setMeetingMemberRoleId(String meetingMemberRoleId) {
		this.setId(meetingMemberRoleId);
	}


	@Column(name="MEETING_MEMBER_ROLE_CODE", nullable=false, precision=10)
	public BigDecimal getMeetingMemberRoleCode() {
		return this.meetingMemberRoleCode;
	}

	public void setMeetingMemberRoleCode(BigDecimal meetingMemberRoleCode) {
		this.meetingMemberRoleCode = meetingMemberRoleCode;
	}


	@Column(name="MEETING_MEMBER_ROLE_NAME", nullable=false, length=128)
	public String getMeetingMemberRoleName() {
		return this.meetingMemberRoleName;
	}

	public void setMeetingMemberRoleName(String meetingMemberRoleName) {
		this.meetingMemberRoleName = meetingMemberRoleName;
	}

}