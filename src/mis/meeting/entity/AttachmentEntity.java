package mis.meeting.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the attachment database table.
 * 
 */
@Entity
@Table(name="ATTACHMENT")
public class AttachmentEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String attachmentId;
	private String attachmentCategoryId;
	private String attachmentName;
	private String attachmentOtherName;
	private String attachmentPath;
	private String employeeId;
	private String meetingId;
	private Timestamp uploadDatetime;

	public AttachmentEntity() {
	}


	@Id
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


	@Column(name="ATTACHMENT_NAME", nullable=false, length=256)
	public String getAttachmentName() {
		return this.attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}


	@Column(name="ATTACHMENT_OTHER_NAME", length=256)
	public String getAttachmentOtherName() {
		return this.attachmentOtherName;
	}

	public void setAttachmentOtherName(String attachmentOtherName) {
		this.attachmentOtherName = attachmentOtherName;
	}


	@Column(name="ATTACHMENT_PATH", nullable=false, length=1024)
	public String getAttachmentPath() {
		return this.attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
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


	@Column(name="UPLOAD_DATETIME", nullable=false)
	public Timestamp getUploadDatetime() {
		return this.uploadDatetime;
	}

	public void setUploadDatetime(Timestamp uploadDatetime) {
		this.uploadDatetime = uploadDatetime;
	}

}