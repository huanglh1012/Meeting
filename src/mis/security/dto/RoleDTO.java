package mis.security.dto;

import ecp.bsp.system.core.BaseDTO;

public class RoleDTO extends BaseDTO {
	private String roleId;
	private String roleName;
	
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
