package mis.security.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

@Component
public class DepartmentDTO extends BaseDTO {
	private String departmentId;
	private String parentDepartmentId;
	private String departmentName;
	private String id;
	private String text;
	private List<DepartmentDTO> children;
	
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

	public List<DepartmentDTO> getChildren() {
		return children;
	}

	public void setChildren(List<DepartmentDTO> children) {
		this.children = children;
	}
	
}