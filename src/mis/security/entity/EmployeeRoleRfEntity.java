package mis.security.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEE_ROLE_RF",schema="meeting")
public class EmployeeRoleRfEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String employeeId;
	private String roleId;
	
	@Id
	@Column(name="EMPLOYEE_ROLE_RF_ID", unique=true, nullable=false, length=32)
	public String getEmployeeRoleRfId() {
		return this.getId();
	}

	public void setEmployeeRoleRfId(String employeeId) {
		this.setId(employeeId);
	}
	
	@Column(name="EMPLOYEE_ID", nullable=false, length=32)
	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	@Column(name="ROLE_ID", nullable=false, length=32)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
