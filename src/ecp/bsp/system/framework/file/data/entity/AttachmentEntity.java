package ecp.bsp.system.framework.file.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ecp.bsp.system.core.BaseEntity;

/**
 * 闄勪欢瀹炰綋.
 * 
 * @since 2015-06-12 <br>
 * 
 * @author zengqingyue.
 */
@Entity
@Table(name = "ATTACHMENT", schema = "meeting")
public class AttachmentEntity extends BaseEntity {
	/**
	 * 鐗堟湰鍙�
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 闄勪欢鍚嶇О.
	 */
	private String attachmentId;
	/**
	 * 闄勪欢鍚嶇О.
	 */
	private String attachmentName;
	/**
	 * 闄勪欢鍚嶇О.
	 */
	private String attachmentRename;
	/**
	 * 闄勪欢璺緞.
	 */
	private String attachmentPath;
	/**
	 * 闄勪欢涓存椂璺緞.
	 */
	private String attachmentTempPath;
	/**
	 * 鍓嶅彴鍒涘缓鏃剁殑id,鐢ㄤ簬鍖哄埆涓嶅悓鐨勯檮浠�	 */
	private String attachmentCreateId;
	/**
	 * 闄勪欢鐘舵�
	 */
	private String attachmentStatusId;
	/**
	 * 闄勪欢鍒涘缓鏃堕棿
	 */
	private Date attachmentCreateTime;
	/**
	 * 闄勪欢绫诲瀷
	 */
	private String attachmentType;
	/**
	 * 闄勪欢澶у皬
	 */
	private Integer attachmentSize;
	/**
	 * 闄勪欢涓婁紶浜�	 */
	private String employeeId;
	/**
	 * 鑾峰彇闄勪欢ID.
	 * 
	 * @return 闄勪欢ID.
	 */
	@Id
	@Column(name = "ATTACHMENT_ID", unique = true, nullable = false, length = 32)
	public String getAttachmentId() {
		return this.getId(); 
	}

	/**
	 * 璁剧疆闄勪欢ID.
	 * 
	 * @param attachmentId
	 *             闄勪欢ID.
	 */
	public void setAttachmentId(String attachmentId) {
		this.setId(attachmentId);
	}

	/**
	 * 鑾峰彇闄勪欢鍚嶇О.
	 * 
	 * @return 闄勪欢鍚嶇О.
	 */
	@Column(name = "ATTACHMENT_NAME", nullable = false, length = 100)
	public String getAttachmentName() {
		return this.attachmentName;
	}

	/**
	 * 璁剧疆闄勪欢鍚嶇О.
	 * 
	 * @param attachmentName
	 *             闄勪欢鍚嶇О.
	 */
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	/**
	 * 鑾峰彇闄勪欢璺緞.
	 * 
	 * @return 闄勪欢璺緞.
	 */
	@Column(name = "ATTACHMENT_PATH", nullable = false, length = 1024)
	public String getAttachmentPath() {
		return this.attachmentPath;
	}

	/**
	 * 璁剧疆闄勪欢璺緞.
	 * 
	 * @param attachmentPath
	 *           闄勪欢璺緞.
	 */
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	/**
	 * 鑾峰彇涓存椂闄勪欢璺緞.
	 * 
	 * @return 闄勪欢璺緞.
	 */
	@Column(name = "ATTACHMENT_TEMP_PATH", nullable = false, length = 1024)
	public String getAttachmentTempPath() {
		return attachmentTempPath;
	}
	/**
	 * 璁剧疆涓存椂闄勪欢璺緞.
	 * 
	 * @param attachmentPath
	 *           闄勪欢璺緞.
	 */
	public void setAttachmentTempPath(String attachmentTempPath) {
		this.attachmentTempPath = attachmentTempPath;
	}

	/**
	 * 鑾峰彇闄勪欢鍒涘缓id
	 * @return
	 */
	@Column(name = "ATTACHMENT_CREATE_ID", nullable = false, length = 300)
	public String getAttachmentCreateId() {
		return attachmentCreateId;
	}

	/**
	 * 璁剧疆闄勪欢鍒涘缓id
	 * @param createId
	 */
	public void setAttachmentCreateId(String attachmentCreateId) {
		this.attachmentCreateId = attachmentCreateId;
	}
	/**
	 * 鑾峰彇闄勪欢鐘舵�
	 * @return
	 */
	@Column(name = "ATTACHMENT_STATUS_ID", nullable = false, length = 1)
	public String getAttachmentStatusId() {
		return attachmentStatusId;
	}

	/**
	 * 璁剧疆闄勪欢鐘舵�
	 * @param state
	 */
	public void setAttachmentStatusId(String attachmentStatusId) {
		this.attachmentStatusId = attachmentStatusId;
	}
	
	@Column(name = "ATTACHMENT_CREATE_TIME", length = 7)
	public Date getAttachmentCreateTime() {
		return attachmentCreateTime;
	}

	public void setAttachmentCreateTime(Date attachmentCreateTime) {
		this.attachmentCreateTime = attachmentCreateTime;
	}
	@Column(name = "ATTACHMENT_TYPE", nullable = false, length = 10)
	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	@Column(name = "ATTACHMENT_SIZE", nullable = false, precision = 32, scale = 0)
	public Integer getAttachmentSize() {
		return attachmentSize;
	}

	public void setAttachmentSize(Integer attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	@Column(name = "ATTACHMENT_RENAME", nullable = false, precision = 32, scale = 0)
	public String getAttachmentRename() {
		return attachmentRename;
	}

	public void setAttachmentRename(String attachmentRename) {
		this.attachmentRename = attachmentRename;
	}

	@Column(name = "EMPLOYEE_ID", nullable = false, precision = 32, scale = 0)
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
}