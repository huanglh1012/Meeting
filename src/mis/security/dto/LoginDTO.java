package mis.security.dto;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

@Component
public class LoginDTO extends BaseDTO {
	
	private String login;
	
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
