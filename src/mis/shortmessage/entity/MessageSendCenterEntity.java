package mis.shortmessage.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the message_send_center database table.
 * 
 */
@Entity
@Table(name="message_send_center",schema="meeting")
public class MessageSendCenterEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String messageSendCenterId;
	private String meetingId;
	private String messageSendStateId;
	private Date sendDatetime;
	private String sendMessage;

	public MessageSendCenterEntity() {
	}


	@Id
	@Column(name="MESSAGE_SEND_CENTER_ID", unique=true, nullable=false, length=32)
	public String getMessageSendCenterId() {
		return this.getId();
	}

	public void setMessageSendCenterId(String messageSendCenterId) {
		this.setId(messageSendCenterId);
	}


	@Column(name="MEETING_ID", nullable=false, length=32)
	public String getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}


	@Column(name="MESSAGE_SEND_STATE_ID", length=32)
	public String getMessageSendStateId() {
		return this.messageSendStateId;
	}

	public void setMessageSendStateId(String messageSendStateId) {
		this.messageSendStateId = messageSendStateId;
	}

	@Column(name="SEND_DATETIME", nullable=false)
	public Date getSendDatetime() {
		return this.sendDatetime;
	}

	public void setSendDatetime(Date sendDatetime) {
		this.sendDatetime = sendDatetime;
	}


	@Lob
	@Column(name="SEND_MESSAGE", nullable=false)
	public String getSendMessage() {
		return this.sendMessage;
	}

	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}

}