package mis.shortmessage.dto;

import ecp.bsp.system.core.BaseDTO;

public class ShortMessageCenterDTO extends BaseDTO {
	
	private String shortMessageCenterId;
	
	private String shortMessageCenterName;
	
	private String centerPhoneNumber;
	
	private String sendMessagePhoneNumber;
	
	private String messageModel;

	public String getShortMessageCenterId() {
		return shortMessageCenterId;
	}

	public void setShortMessageCenterId(String shortMessageCenterId) {
		this.shortMessageCenterId = shortMessageCenterId;
	}

	public String getCenterPhoneNumber() {
		return this.centerPhoneNumber;
	}

	public String getShortMessageCenterName() {
		return this.shortMessageCenterName;
	}

	public void setShortMessageCenterName(String shortMessageCenterName) {
		this.shortMessageCenterName = shortMessageCenterName;
	}

	public void setCenterPhoneNumber(String centerPhoneNumber) {
		this.centerPhoneNumber = centerPhoneNumber;
	}

	public String getSendMessagePhoneNumber() {
		return this.sendMessagePhoneNumber;
	}

	public void setSendMessagePhoneNumber(String sendMessagePhoneNumber) {
		this.sendMessagePhoneNumber = sendMessagePhoneNumber;
	}

	public String getMessageModel() {
		return this.messageModel;
	}

	public void setMessageModel(String messageModel) {
		this.messageModel = messageModel;
	}
}
