package mis.shortmessage.dto;
import java.util.List;


public class ShortMessageResultDTO {
	private String code;
	private List<String> sId;
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
}
