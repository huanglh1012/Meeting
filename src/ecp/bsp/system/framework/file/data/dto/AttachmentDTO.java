package ecp.bsp.system.framework.file.data.dto;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

/**
 * 闄勪欢DTO
 * 
 * @since 2015-06-25
 * 
 * @author zengqingyue.
 */
@Component
public class AttachmentDTO extends BaseDTO {
	/**
	 * 闄勪欢ID.
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
	 * 闄勪欢鍒楄〃
	 */
	private List<AttachmentDTO> attachmentList;
	
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getAttachmentRename() {
		return attachmentRename;
	}
	public void setAttachmentRename(String attachmentRename) {
		this.attachmentRename = attachmentRename;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public String getAttachmentTempPath() {
		return attachmentTempPath;
	}
	public void setAttachmentTempPath(String attachmentTempPath) {
		this.attachmentTempPath = attachmentTempPath;
	}
	public String getAttachmentCreateId() {
		return attachmentCreateId;
	}
	public void setAttachmentCreateId(String attachmentCreateId) {
		this.attachmentCreateId = attachmentCreateId;
	}
	public String getAttachmentStatusId() {
		return attachmentStatusId;
	}
	public void setAttachmentStatusId(String attachmentStatusId) {
		this.attachmentStatusId = attachmentStatusId;
	}
	public Date getAttachmentCreateTime() {
		return attachmentCreateTime;
	}
	public void setAttachmentCreateTime(Date attachmentCreateTime) {
		this.attachmentCreateTime = attachmentCreateTime;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public Integer getAttachmentSize() {
		return attachmentSize;
	}
	public void setAttachmentSize(Integer attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public List<AttachmentDTO> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<AttachmentDTO> attachmentList) {
		this.attachmentList = attachmentList;
	}
	
}
	
