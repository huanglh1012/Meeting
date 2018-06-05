package mis.shortmessage.dto;
import java.util.List;


public class ShortMessageResultDTO {
	private String code;
	private List<String> sId;
	private String messageSendResult;
	private String shortMessageCenterId;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getsId() {
		return sId;
	}
	public void setsId(List<String> sId) {
		this.sId = sId;
	}
	public String getMessageSendResult() {
		return messageSendResult;
	}
	public void setMessageSendResult(String messageSendResult) {
		this.messageSendResult = messageSendResult;
	}
	public String getShortMessageCenterId() {
		return shortMessageCenterId;
	}
	public void setShortMessageCenterId(String shortMessageCenterId) {
		this.shortMessageCenterId = shortMessageCenterId;
	}
	
}
