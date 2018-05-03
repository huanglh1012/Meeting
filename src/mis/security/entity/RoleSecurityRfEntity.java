package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the role_security_rf database table.
 * 
 */
@Entity
@Table(name="ROLE_SECURITY_RF")
public class RoleSecurityRfEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String roleSecurityRfId;
	private String roleId;
	private String securityId;

	public RoleSecurityRfEntity() {
	}


	@Id
	@Column(name="ROLE_SECURITY_RF_ID", unique=true, nullable=false, length=32)
	public String getRoleSecurityRfId() {
		return this.roleSecurityRfId;
	}

	public void setRoleSecurityRfId(String roleSecurityRfId) {
		this.roleSecurityRfId = roleSecurityRfId;
	}


	@Column(name="ROLE_ID", nullable=false, length=32)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	@Column(name="SECURITY_ID", nullable=false, length=32)
	public String getSecurityId() {
		return this.securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

}