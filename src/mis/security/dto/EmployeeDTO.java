package mis.security.dto;

import java.util.List;

import mis.security.constant.SecurityEntityRegister;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;
import ecp.bsp.system.framework.query.data.dto.ModelEntityAnnotation;

@Component
@ModelEntityAnnotation( propertiesFileName = "",
refEntity = SecurityEntityRegister.EmployeeEntity +
			SecurityEntityRegister.split +
            SecurityEntityRegister.DepartmentEntity +
            SecurityEntityRegister.split +
            SecurityEntityRegister.PostEntity +
            SecurityEntityRegister.split +
            SecurityEntityRegister.RoleEntity +
            SecurityEntityRegister.split +
            SecurityEntityRegister.SexEntity
            )
public class EmployeeDTO extends BaseDTO {
	
	private String employeeId;
	
    private String postId;
    
    private String postName;
	
	private String departmentId;
	
	private String departmentName;
	
	private String roleId;
	
	private String roleName;
	
	private String login;
	
	private String password;
	
	private String confirmPassword;
	
	private String employeeName;
	
	private String sexId;
	
	private String sexName;
	
	private String telephone;
	
	private String identifyCardNumber;

	private List<String> roleIdList;
	
	private String id;
	
	private String text;
	
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
