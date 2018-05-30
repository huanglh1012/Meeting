package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="ROLE",schema="meeting")
public class RoleEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String roleId;
	private String roleName;

	public RoleEntity() {
	}


	@Id
	@Column(name="ROLE_ID", unique=true, nullable=false, length=32)
	public String getRoleId() {
		return this.getId();
	}

	public void setRoleId(String roleId) {
		this.setId(roleId);
	}


	@Column(name="ROLE_NAME", nullable=false, length=128)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}