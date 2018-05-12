package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the attachment_category database table.
 * 
 */
@Entity
@Table(name="attachment_category",schema="meeting")
public class AttachmentCategoryEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String attachmentCategoryId;
	private BigDecimal attachmentCategoryCode;
	private String attachmentCategoryName;

	public AttachmentCategoryEntity() {
	}


	@Id
	@Column(name="ATTACHMENT_CATEGORY_ID", unique=true, nullable=false, length=32)
	public String getAttachmentCategoryId() {
		return this.getId();
	}

	public void setAttachmentCategoryId(String attachmentCategoryId) {
		this.setId(attachmentCategoryId);
	}


	@Column(name="ATTACHMENT_CATEGORY_CODE", nullable=false, precision=10)
	public BigDecimal getAttachmentCategoryCode() {
		return this.attachmentCategoryCode;
	}

	public void setAttachmentCategoryCode(BigDecimal attachmentCategoryCode) {
		this.attachmentCategoryCode = attachmentCategoryCode;
	}


	@Column(name="ATTACHMENT_CATEGORY_NAME", nullable=false, length=128)
	public String getAttachmentCategoryName() {
		return this.attachmentCategoryName;
	}

	public void setAttachmentCategoryName(String attachmentCategoryName) {
		this.attachmentCategoryName = attachmentCategoryName;
	}

}