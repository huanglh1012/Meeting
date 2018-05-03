package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the employee database table.
 * 
 */
@Entity
@Table(name="employee")
public class EmployeeEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String employeeId;
	private String departmentId;
	private String employeeName;
	private String identifyCardNumber;
	private String login;
	private String password;
	private String postId;
	private String roleId;
	private String sexId;
	private String telephone;

	@Id
	@Column(name="EMPLOYEE_ID", unique=true, nullable=false, length=32)
	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	@Column(name="DEPARTMENT_ID", length=32)
	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}


	@Column(name="EMPLOYEE_NAME", nullable=false, length=128)
	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	@Column(name="IDENTIFY_CARD_NUMBER", nullable=false, length=24)
	public String getIdentifyCardNumber() {
		return this.identifyCardNumber;
	}

	public void setIdentifyCardNumber(String identifyCardNumber) {
		this.identifyCardNumber = identifyCardNumber;
	}


	@Column(name="LOGIN", length=128)
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	@Column(name="PASSWORD", length=128)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Column(name="POST_ID", length=32)
	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}


	@Column(name="ROLE_ID", length=32)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	@Column(name="SEX_ID", length=32)
	public String getSexId() {
		return this.sexId;
	}

	public void setSexId(String sexId) {
		this.sexId = sexId;
	}


	@Column(length=128)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}