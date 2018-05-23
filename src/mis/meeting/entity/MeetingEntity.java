package mis.meeting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the meeting database table.
 * 
 */
@Entity
@Table(name="MEETING",schema="meeting")
public class MeetingEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String meetingId;
	private String meetingStateId;
	private String meetingAttentions;
	private Date meetingEndTime;
	private Date meetingProposeTime;
	private String meetingRoomId;
	private Date meetingStartTime;
	private String meetingSubject;
	private Date meetingUploadEndTime;
	private Integer isSendMessageNotice;
	
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


	@Column(name="MEETING_STATE_ID", nullable=false, length=32)
	public String getMeetingStateId() {
		return this.meetingStateId;
	}

	public void setMeetingStateId(String meetingStateId) {
		this.meetingStateId = meetingStateId;
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
	public Date getMeetingEndTime() {
		return this.meetingEndTime;
	}

	public void setMeetingEndTime(Date meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}


	@Column(name="MEETING_PROPOSE_TIME", nullable=false)
	public Date getMeetingProposeTime() {
		return this.meetingProposeTime;
	}

	public void setMeetingProposeTime(Date meetingProposeTime) {
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
	public Date getMeetingStartTime() {
		return this.meetingStartTime;
	}

	public void setMeetingStartTime(Date meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}


	@Column(name="MEETING_SUBJECT", nullable=false, length=512)
	public String getMeetingSubject() {
		return this.meetingSubject;
	}

	public void setMeetingSubject(String meetingSubject) {
		this.meetingSubject = meetingSubject;
	}

	@Column(name="MEETING_UPLOAD_END_TIME", nullable=false)
	public Date getMeetingUploadEndTime() {
		return meetingUploadEndTime;
	}

	public void setMeetingUploadEndTime(Date meetingUploadEndTime) {
		this.meetingUploadEndTime = meetingUploadEndTime;
	}

	@Column(name="IS_SEND_MESSAGE_NOTICE", nullable=false)
	public Integer getIsSendMessageNotice() {
		return isSendMessageNotice;
	}

	public void setIsSendMessageNotice(Integer isSendMessageNotice) {
		this.isSendMessageNotice = isSendMessageNotice;
	}
	
}