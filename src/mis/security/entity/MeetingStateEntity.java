package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the meeting_state database table.
 * 
 */
@Entity
@Table(name="meeting_state",schema="meeting")
public class MeetingStateEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String meetingStateId;
	private BigDecimal meetingStateCode;
	private String meetingStateName;

	public MeetingStateEntity() {
	}


	@Id
	@Column(name="MEETING_STATE_ID", unique=true, nullable=false, length=32)
	public String getMeetingStateId() {
		return this.getId();
	}

	public void setMeetingStateId(String meetingStateId) {
		this.setId(meetingStateId);
	}


	@Column(name="MEETING_STATE_CODE", nullable=false, precision=10)
	public BigDecimal getMeetingStateCode() {
		return this.meetingStateCode;
	}

	public void setMeetingStateCode(BigDecimal meetingStateCode) {
		this.meetingStateCode = meetingStateCode;
	}


	@Column(name="MEETING_STATE_NAME", nullable=false, length=128)
	public String getMeetingStateName() {
		return this.meetingStateName;
	}

	public void setMeetingStateName(String meetingStateName) {
		this.meetingStateName = meetingStateName;
	}

}