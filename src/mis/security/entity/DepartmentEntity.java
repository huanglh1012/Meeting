package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the department database table.
 * 
 */
@Entity
@Table(name="department",schema="meeting")
public class DepartmentEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String departmentName;
	private String parentDepartmentId;
	private int isParent;

	@Id
	@Column(name="DEPARTMENT_ID", unique=true, nullable=false, length=32)
	public String getDepartmentId() {
		return this.getId();
	}

	public void setDepartmentId(String departmentId) {
		this.setId(departmentId);
	}

	@Column(name="PARENT_DEPARTMENT_ID", length=128)
	public String getParentDepartmentId() {
		return this.parentDepartmentId;
	}

	public void setParentDepartmentId(String parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}

	@Column(name="DEPARTMENT_NAME", nullable=false, length=128)
	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Column(name="IS_PARENT", nullable=false, length=128)
	public int isParent() {
		return isParent;
	}

	public void setParent(int isParent) {
		this.isParent = isParent;
	}

	
}