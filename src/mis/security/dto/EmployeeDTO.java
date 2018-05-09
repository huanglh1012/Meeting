package mis.security.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;
@Component
public class EmployeeDTO extends BaseDTO {
	
	private String employeeId;
	
    private String postId;
	
	private String departmentId;
	
	private String roleId;
	
	private String login;
	
	private String password;
	
	private String employeeName;
	
	private String sexId;
	
	private String telephone;
	
	private String identifyCardNumber;

	private List<String> roleIdList;
	
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String postId) {
		this.employeeId = postId;
	}
	
	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getSexId() {
		return sexId;
	}

	public void setSexId(String sexId) {
		this.sexId = sexId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getIdentifyCardNumber() {
		return identifyCardNumber;
	}

	public void setIdentifyCardNumber(String identifyCardNumber) {
		this.identifyCardNumber = identifyCardNumber;
	}
	
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}
}
