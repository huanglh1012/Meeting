package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the security database table.
 * 
 */
@Entity
@Table(name="security")
public class SecurityEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String securityId;
	private String securityCode;
	private String securityName;

	public SecurityEntity() {
	}


	@Id
	@Column(name="SECURITY_ID", unique=true, nullable=false, length=32)
	public String getSecurityId() {
		return this.securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}


	@Column(name="SECURITY_CODE", nullable=false, length=32)
	public String getSecurityCode() {
		return this.securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}


	@Column(name="SECURITY_NAME", nullable=false, length=128)
	public String getSecurityName() {
		return this.securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

}