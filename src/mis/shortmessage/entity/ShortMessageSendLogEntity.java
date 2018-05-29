package mis.shortmessage.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the short_message_send_log database table.
 * 
 */
@Entity
@Table(name="SHORT_MESSAGE_SEND_LOG",schema="meeting")
public class ShortMessageSendLogEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String shortMessageSendLogId;
	private String messageSendCenterId;
	private String messageSendStateId;
	private String shortMessageCenterId;
	private String messageSendTelephone;
	private String messageSendParam;
	private Date messageSendTime;
	private String messageSendResult;
	private int messageSendCount;
	public ShortMessageSendLogEntity() {
	}


	@Id
	@Column(name="SHORT_MESSAGE_SEND_LOG_ID", unique=true, nullable=false, length=32)
	public String getShortMessageSendLogId() {
		return this.getId();
	}

	public void setShortMessageSendLogId(String shortMessageSendLogId) {
		this.setId(shortMessageSendLogId);
	}


	@Column(name="MESSAGE_SEND_CENTER_ID", nullable=false, length=32)
	public String getMessageSendCenterId() {
		return this.messageSendCenterId;
	}

	public void setMessageSendCenterId(String messageSendCenterId) {
		this.messageSendCenterId = messageSendCenterId;
	}


	@Column(name="MESSAGE_SEND_STATE_ID", nullable=false, length=32)
	public String getMessageSendStateId() {
		return this.messageSendStateId;
	}

	public void setMessageSendStateId(String messageSendStateId) {
		this.messageSendStateId = messageSendStateId;
	}


	@Column(name="SHORT_MESSAGE_CENTER_ID", nullable=false, length=32)
	public String getShortMessageCenterId() {
		return this.shortMessageCenterId;
	}

	public void setShortMessageCenterId(String shortMessageCenterId) {
		this.shortMessageCenterId = shortMessageCenterId;
	}

	@Lob
	@Column(name="MESSAGE_SEND_TELEPHONE", nullable=false)
	public String getMessageSendTelephone() {
		return messageSendTelephone;
	}


	public void setMessageSendTelephone(String messageSendTelephone) {
		this.messageSendTelephone = messageSendTelephone;
	}

	@Lob
	@Column(name="MESSAGE_SEND_PARAM", nullable=false)
	public String getMessageSendParam() {
		return messageSendParam;
	}

	public void setMessageSendParam(String messageSendParam) {
		this.messageSendParam = messageSendParam;
	}

	@Column(name="MESSAGE_SEND_TIME", nullable=false)
	public Date getMessageSendTime() {
		return messageSendTime;
	}

	public void setMessageSendTime(Date messageSendTime) {
		this.messageSendTime = messageSendTime;
	}

	@Lob
	@Column(name="MESSAGE_SEND_RESULT", nullable=false)
	public String getMessageSendResult() {
		return messageSendResult;
	}


	public void setMessageSendResult(String messageSendResult) {
		this.messageSendResult = messageSendResult;
	}

	@Column(name="MESSAGE_SEND_COUNT", nullable=false)
	public int getMessageSendCount() {
		return messageSendCount;
	}


	public void setMessageSendCount(int messageSendCount) {
		this.messageSendCount = messageSendCount;
	}

	
}