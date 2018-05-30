package mis.meeting.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the attachment database table.
 * 
 */
@Entity
@Table(name="MEETING_ATTACHMENT",schema="meeting")
public class MeetingAttachmentEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String meetingAttachmentId;
	private String attachmentId;
	private String attachmentCategoryId;
	private String meetingId;

	public MeetingAttachmentEntity() {
	}

	@Id
	@Column(name="MEETING_ATTACHMENT_ID", unique=true, nullable=false, length=32)
	public String getMeetingAttachmentId() {
		return this.getId();
	}

	public void setMeetingAttachmentId(String meetingAttachmentId) {
		this.setId(meetingAttachmentId);
	}
	
	@Column(name="ATTACHMENT_ID", unique=true, nullable=false, length=32)
	public String getAttachmentId() {
		return this.attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}


	@Column(name="ATTACHMENT_CATEGORY_ID", nullable=false, length=32)
	public String getAttachmentCategoryId() {
		return this.attachmentCategoryId;
	}

	public void setAttachmentCategoryId(String attachmentCategoryId) {
		this.attachmentCategoryId = attachmentCategoryId;
	}

	@Column(name="MEETING_ID", nullable=false, length=32)
	public String getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

}