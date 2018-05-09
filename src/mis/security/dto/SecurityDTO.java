package mis.security.dto;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;
@Component
public class SecurityDTO extends BaseDTO {
	private String securityId;
	private String securityCode;
	private String securityName;

	public String getSecurityId() {
		return this.securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getSecurityCode() {
		return this.securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getSecurityName() {
		return this.securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

}
