package mis.meeting.dto;

import java.util.Date;

import mis.meeting.constant.MeetingEntityRegister;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.stereotype.Component;

import ecp.bsp.system.commons.utils.jackson.deserilizer.JsonDateDeSerializer;
import ecp.bsp.system.commons.utils.jackson.serializer.JsonDateSerializer;
import ecp.bsp.system.core.BaseDTO;
import ecp.bsp.system.framework.query.data.dto.DtoField2QueryField;
import ecp.bsp.system.framework.query.data.dto.ModelEntityAnnotation;

@Component
@ModelEntityAnnotation( propertiesFileName = "",
refEntity = MeetingEntityRegister.AttachmentCategoryEntity +
			MeetingEntityRegister.split +
            MeetingEntityRegister.MeetingAttachmentEntity +
            MeetingEntityRegister.split +
            MeetingEntityRegister.MeetingEntity +
            MeetingEntityRegister.split +
            MeetingEntityRegister.MeetingMemberRfEntity +
            MeetingEntityRegister.split +
            MeetingEntityRegister.MeetingMemberRoleEntity +
            MeetingEntityRegister.split +
            MeetingEntityRegister.MeetingRoomEntity +
            MeetingEntityRegister.split +
            MeetingEntityRegister.MeetingStateEntity+
            MeetingEntityRegister.split +
            MeetingEntityRegister.AttachmentEntity
            )
@DtoField2QueryField
public class MeetingAttachmentDTO extends BaseDTO {
	private String attachmentId;
	private String attachmentName;
	private String attachmentPath;
	private String meetingId;
	private String meetingStateId;
	private String meetingSubject;
	private String meetingStateName;
	private String meetingAttentions;
	@JsonSerialize(using=JsonDateSerializer.class)
	@JsonDeserialize(using=JsonDateDeSerializer.class)
	private Date attachmentCreateTime;
	@JsonSerialize(using=JsonDateSerializer.class)
	@JsonDeserialize(using=JsonDateDeSerializer.class)
	private Date meetingStartTime;
	@JsonSerialize(using=JsonDateSerializer.class)
	@JsonDeserialize(using=JsonDateDeSerializer.class)
	private Date meetingEndTime;
	@JsonSerialize(using=JsonDateSerializer.class)
	@JsonDeserialize(using=JsonDateDeSerializer.class)
	private Date meetingProposeTime;
	@JsonSerialize(using=JsonDateSerializer.class)
	@JsonDeserialize(using=JsonDateDeSerializer.class)
	private Date messageNoticeTime;
	@JsonSerialize(using=JsonDateSerializer.class)
	@JsonDeserialize(using=JsonDateDeSerializer.class)
	private Date meetingUploadEndTime;
	private String meetingRoomId;
	private String meetingRoomName;
	private String employeeId;
	private String employeeName;
	private String departmentId;
	private String departmentName;
	private String meetingCreator;
	private String meetingCreatorName;
	private String meetingCreatorDepartmentId;
	private String meetingCreatorDepartmentName;
	private String meetingPresenter;
	private String meetingPresenterName;
	private String meetingPresenterDepartmentId;
	private String meetingPresenterDepartmentName;
	private String meetingParticipant;
	private String meetingParticipantName;
	private String meetingParticipantDepartmentId;
	private String meetingParticipantDepartmentName;

	public String getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetingStateId() {
		return this.meetingStateId;
	}

	public void setMeetingStateId(String meetingStateId) {
		this.meetingStateId = meetingStateId;
	}

	public String getMeetingAttentions() {
		return this.meetingAttentions;
	}

	public void setMeetingAttentions(String meetingAttentions) {
		this.meetingAttentions = meetingAttentions;
	}

	public Date getMeetingEndTime() {
		return this.meetingEndTime;
	}

	public void setMeetingEndTime(Date meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}

	public Date getMeetingProposeTime() {
		return this.meetingProposeTime;
	}

	public void setMeetingProposeTime(Date meetingProposeTime) {
		this.meetingProposeTime = meetingProposeTime;
	}

	public String getMeetingRoomId() {
		return this.meetingRoomId;
	}

	public void setMeetingRoomId(String meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}

	public Date getMeetingStartTime() {
		return this.meetingStartTime;
	}

	public void setMeetingStartTime(Date meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}

	public String getMeetingSubject() {
		return this.meetingSubject;
	}

	public void setMeetingSubject(String meetingSubject) {
		this.meetingSubject = meetingSubject;
	}

	public String getMeetStateName() {
		return meetingStateName;
	}

	public void setMeetStateName(String meetStateName) {
		this.meetingStateName = meetStateName;
	}

	public Date getMessageNoticeTime() {
		return messageNoticeTime;
	}

	public void setMessageNoticeTime(Date messageNoticeTime) {
		this.messageNoticeTime = messageNoticeTime;
	}

	public Date getMeetingUploadEndTime() {
		return meetingUploadEndTime;
	}

	public void setMeetingUploadEndTime(Date meetingUploadEndTime) {
		this.meetingUploadEndTime = meetingUploadEndTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	@DtoField2QueryField(field="MEETING_CREATOR")
	public String getMeetingCreator() {
		return meetingCreator;
	}

	public void setMeetingCreator(String meetingCreator) {
		this.meetingCreator = meetingCreator;
	}

	@DtoField2QueryField(field="MEETING_PRESENTER")
	public String getMeetingPresenter() {
		return meetingPresenter;
	}

	public void setMeetingPresenter(String meetingPresenter) {
		this.meetingPresenter = meetingPresenter;
	}
	
	public String getMeetingStateName() {
		return meetingStateName;
	}

	public void setMeetingStateName(String meetingStateName) {
		this.meetingStateName = meetingStateName;
	}

	@DtoField2QueryField(field="MEETING_CREATOR_DEPARTMENT_ID")
	public String getMeetingCreatorDepartmentId() {
		return meetingCreatorDepartmentId;
	}

	public void setMeetingCreatorDepartmentId(String meetingCreatorDepartmentId) {
		this.meetingCreatorDepartmentId = meetingCreatorDepartmentId;
	}

	@DtoField2QueryField(field="MEETING_PRESENTER_DEPARTMENT_ID")
	public String getMeetingPresenterDepartmentId() {
		return meetingPresenterDepartmentId;
	}

	public void setMeetingPresenterDepartmentId(String meetingPresenterDepartmentId) {
		this.meetingPresenterDepartmentId = meetingPresenterDepartmentId;
	}

	@DtoField2QueryField(field="MEETING_PARTICIPANT")
	public String getMeetingParticipant() {
		return meetingParticipant;
	}

	public void setMeetingParticipant(String meetingParticipant) {
		this.meetingParticipant = meetingParticipant;
	}

	public String getMeetingParticipantDepartmentId() {
		return meetingParticipantDepartmentId;
	}

	@DtoField2QueryField(field="MEETING_PARTICIPANT_DEPARTMENT_ID")
	public void setMeetingParticipantDepartmentId(
			String meetingParticipantDepartmentId) {
		this.meetingParticipantDepartmentId = meetingParticipantDepartmentId;
	}

	public String getMeetingCreatorName() {
		return meetingCreatorName;
	}

	public void setMeetingCreatorName(String meetingCreatorName) {
		this.meetingCreatorName = meetingCreatorName;
	}

	public String getMeetingCreatorDepartmentName() {
		return meetingCreatorDepartmentName;
	}

	public void setMeetingCreatorDepartmentName(String meetingCreatorDepartmentName) {
		this.meetingCreatorDepartmentName = meetingCreatorDepartmentName;
	}

	public String getMeetingPresenterName() {
		return meetingPresenterName;
	}

	public void setMeetingPresenterName(String meetingPresenterName) {
		this.meetingPresenterName = meetingPresenterName;
	}

	public String getMeetingPresenterDepartmentName() {
		return meetingPresenterDepartmentName;
	}

	public void setMeetingPresenterDepartmentName(
			String meetingPresenterDepartmentName) {
		this.meetingPresenterDepartmentName = meetingPresenterDepartmentName;
	}

	public String getMeetingParticipantName() {
		return meetingParticipantName;
	}

	public void setMeetingParticipantName(String meetingParticipantName) {
		this.meetingParticipantName = meetingParticipantName;
	}

	public String getMeetingParticipantDepartmentName() {
		return meetingParticipantDepartmentName;
	}

	public void setMeetingParticipantDepartmentName(
			String meetingParticipantDepartmentName) {
		this.meetingParticipantDepartmentName = meetingParticipantDepartmentName;
	}

	public String getMeetingRoomName() {
		return meetingRoomName;
	}

	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public Date getAttachmentCreateTime() {
		return attachmentCreateTime;
	}

	public void setAttachmentCreateTime(Date attachmentCreateTime) {
		this.attachmentCreateTime = attachmentCreateTime;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
}
