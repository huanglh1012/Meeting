package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the sex database table.
 * 
 */
@Entity
@Table(name="sex")
public class SexEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sexId;
	private BigDecimal sexCode;
	private String sexName;

	public SexEntity() {
	}


	@Id
	@Column(name="SEX_ID", unique=true, nullable=false, length=32)
	public String getSexId() {
		return this.sexId;
	}

	public void setSexId(String sexId) {
		this.sexId = sexId;
	}


	@Column(name="SEX_CODE", nullable=false, precision=10)
	public BigDecimal getSexCode() {
		return this.sexCode;
	}

	public void setSexCode(BigDecimal sexCode) {
		this.sexCode = sexCode;
	}


	@Column(name="SEX_NAME", nullable=false, length=16)
	public String getSexName() {
		return this.sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

}