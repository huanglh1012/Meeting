package mis.shortmessage.dto;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

@Component
public class ShortMessageCenterDTO extends BaseDTO {
	
	private String shortMessageCenterId;
	
	private String shortMessageCenterName;
	
	private String centerPhoneNumber;
	
	private String sendMessagePhoneNumber;
	
	private String messageModel;
	
	private String sendUrl;
	
	private String callerId;
	
	private String callerPassword;
	
	private String messageTemplateId;
	
	private String messageParam;

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

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getCallerPassword() {
		return callerPassword;
	}

	public void setCallerPassword(String callerPassword) {
		this.callerPassword = callerPassword;
	}

	public String getMessageTemplateId() {
		return messageTemplateId;
	}

	public void setMessageTemplateId(String messageTemplateId) {
		this.messageTemplateId = messageTemplateId;
	}

	public String getMessageParam() {
		return messageParam;
	}

	public void setMessageParam(String messageParam) {
		this.messageParam = messageParam;
	}
	
}
