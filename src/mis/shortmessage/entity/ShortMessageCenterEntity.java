package mis.shortmessage.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the short_message_center database table.
 * 
 */
@Entity
@Table(name="SHORT_MESSAGE_CENTER",schema="meeting")
public class ShortMessageCenterEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String shortMessageCenterId;
	private String shortMessageCenterName;
	private String centerPhoneNumber;
	private String sendMessagePhoneNumber;
	private String messageModel;
	private String sendUrl;
	private String callerId;
	private String callerPassword;
	private String messageTemplateId;


	@Id
	@Column(name="SHORT_MESSAGE_CENTER_ID", unique=true, nullable=false, length=32)
	public String getShortMessageCenterId() {
		return this.getId();
	}

	public void setShortMessageCenterId(String shortMessageCenterId) {
		this.setId(shortMessageCenterId);
	}

	@Column(name="SHORT_MESSAGE_CENTER_NAME", nullable=false, length=128)
	public String getShortMessageCenterName() {
		return shortMessageCenterName;
	}


	public void setShortMessageCenterName(String shortMessageCenterName) {
		this.shortMessageCenterName = shortMessageCenterName;
	}


	@Column(name="CENTER_PHONE_NUMBER", nullable=false, length=128)
	public String getCenterPhoneNumber() {
		return this.centerPhoneNumber;
	}

	public void setCenterPhoneNumber(String centerPhoneNumber) {
		this.centerPhoneNumber = centerPhoneNumber;
	}


	@Column(name="SEND_MESSAGE_PHONE_NUMBER", nullable=false, length=128)
	public String getSendMessagePhoneNumber() {
		return this.sendMessagePhoneNumber;
	}

	public void setSendMessagePhoneNumber(String sendMessagePhoneNumber) {
		this.sendMessagePhoneNumber = sendMessagePhoneNumber;
	}

	@Column(name="MESSAGE_MODEL", nullable=false, length=1024)
	public String getMessageModel() {
		return messageModel;
	}

	public void setMessageModel(String messageModel) {
		this.messageModel = messageModel;
	}

	@Column(name="SEND_URL", nullable=false, length=256)
	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	@Column(name="CALLER_ID", nullable=false, length=256)
	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	@Column(name="CALLER_PASSWORD", nullable=false, length=256)
	public String getCallerPassword() {
		return callerPassword;
	}

	public void setCallerPassword(String callerPassword) {
		this.callerPassword = callerPassword;
	}

	@Column(name="MESSAGE_TEMPLATE_ID", nullable=false, length=256)
	public String getMessageTemplateId() {
		return messageTemplateId;
	}

	public void setMessageTemplateId(String messageTemplateId) {
		this.messageTemplateId = messageTemplateId;
	}
	
}