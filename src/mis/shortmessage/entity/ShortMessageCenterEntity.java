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
}