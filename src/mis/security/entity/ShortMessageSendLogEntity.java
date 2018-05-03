package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the short_message_send_log database table.
 * 
 */
@Entity
@Table(name="short_message_send_log")
public class ShortMessageSendLogEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String shortMessageSendLogId;
	private String messageSendCenterId;
	private String messageSendStateId;
	private String shortMessageCenterId;

	public ShortMessageSendLogEntity() {
	}


	@Id
	@Column(name="SHORT_MESSAGE_SEND_LOG_ID", unique=true, nullable=false, length=32)
	public String getShortMessageSendLogId() {
		return this.shortMessageSendLogId;
	}

	public void setShortMessageSendLogId(String shortMessageSendLogId) {
		this.shortMessageSendLogId = shortMessageSendLogId;
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

}