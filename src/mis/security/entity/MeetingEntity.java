package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the meeting database table.
 * 
 */
@Entity
@Table(name="meeting",schema="meeting")
public class MeetingEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String meetingId;
	private String meetStateId;
	private String meetingAttentions;
	private Timestamp meetingEndTime;
	private Timestamp meetingProposeTime;
	private String meetingRoomId;
	private Timestamp meetingStartTime;
	private String meetingSubject;

	public MeetingEntity() {
	}


	@Id
	@Column(name="MEETING_ID", unique=true, nullable=false, length=32)
	public String getMeetingId() {
		return this.getId();
	}

	public void setMeetingId(String meetingId) {
		this.setId(meetingId);
	}


	@Column(name="MEET_STATE_ID", nullable=false, length=32)
	public String getMeetStateId() {
		return this.meetStateId;
	}

	public void setMeetStateId(String meetStateId) {
		this.meetStateId = meetStateId;
	}


	@Lob
	@Column(name="MEETING_ATTENTIONS")
	public String getMeetingAttentions() {
		return this.meetingAttentions;
	}

	public void setMeetingAttentions(String meetingAttentions) {
		this.meetingAttentions = meetingAttentions;
	}


	@Column(name="MEETING_END_TIME", nullable=false)
	public Timestamp getMeetingEndTime() {
		return this.meetingEndTime;
	}

	public void setMeetingEndTime(Timestamp meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}


	@Column(name="MEETING_PROPOSE_TIME", nullable=false)
	public Timestamp getMeetingProposeTime() {
		return this.meetingProposeTime;
	}

	public void setMeetingProposeTime(Timestamp meetingProposeTime) {
		this.meetingProposeTime = meetingProposeTime;
	}


	@Column(name="MEETING_ROOM_ID", nullable=false, length=32)
	public String getMeetingRoomId() {
		return this.meetingRoomId;
	}

	public void setMeetingRoomId(String meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}


	@Column(name="MEETING_START_TIME", nullable=false)
	public Timestamp getMeetingStartTime() {
		return this.meetingStartTime;
	}

	public void setMeetingStartTime(Timestamp meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}


	@Column(name="MEETING_SUBJECT", nullable=false, length=512)
	public String getMeetingSubject() {
		return this.meetingSubject;
	}

	public void setMeetingSubject(String meetingSubject) {
		this.meetingSubject = meetingSubject;
	}

}