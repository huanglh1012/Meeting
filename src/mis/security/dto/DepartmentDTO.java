package mis.security.dto;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

@Component
public class DepartmentDTO extends BaseDTO {
	private String departmentId;
	private String parentDepartmentId;
	private String departmentName;
	private int isParent;

	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getParentDepartmentId() {
		return this.parentDepartmentId;
	}

	public void setParentDepartmentId(String parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}
	
	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int isParent() {
		return isParent;
	}

	public void setParent(int isParent) {
		this.isParent = isParent;
	}

}