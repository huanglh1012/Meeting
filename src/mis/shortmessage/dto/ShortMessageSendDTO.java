package mis.shortmessage.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.stereotype.Component;

import ecp.bsp.system.commons.utils.jackson.deserilizer.JsonDateDeSerializer;
import ecp.bsp.system.commons.utils.jackson.serializer.JsonDateSerializer;
import ecp.bsp.system.core.BaseDTO;

@Component
public class ShortMessageSendDTO extends BaseDTO implements Serializable {
	private static final long serialVersionUID = 5251376728651894834L;
	private String messageSendCenterId;
	private String meetingId; 
	private String meetingStateId;
	private String meetingSubject;
	private String meetingStateName;
	private String meetingAttentions;
	private String meetingRoomId;
	private String meetingRoomName;
	private String employeeId;
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
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getMeetingStateId() {
		return meetingStateId;
	}
	public void setMeetingStateId(String meetingStateId) {
		this.meetingStateId = meetingStateId;
	}
	public String getMeetingSubject() {
		return meetingSubject;
	}
	public void setMeetingSubject(String meetingSubject) {
		this.meetingSubject = meetingSubject;
	}
	public String getMeetingStateName() {
		return meetingStateName;
	}
	public void setMeetingStateName(String meetingStateName) {
		this.meetingStateName = meetingStateName;
	}
	public String getMeetingAttentions() {
		return meetingAttentions;
	}
	public void setMeetingAttentions(String meetingAttentions) {
		this.meetingAttentions = meetingAttentions;
	}
	public String getMeetingRoomId() {
		return meetingRoomId;
	}
	public void setMeetingRoomId(String meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}
	public String getMeetingRoomName() {
		return meetingRoomName;
	}
	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getMessageSendCenterId() {
		return messageSendCenterId;
	}
	public void setMessageSendCenterId(String messageSendCenterId) {
		this.messageSendCenterId = messageSendCenterId;
	}
	public Date getMeetingStartTime() {
		return meetingStartTime;
	}
	public void setMeetingStartTime(Date meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}
	public Date getMeetingEndTime() {
		return meetingEndTime;
	}
	public void setMeetingEndTime(Date meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}
	public Date getMeetingProposeTime() {
		return meetingProposeTime;
	}
	public void setMeetingProposeTime(Date meetingProposeTime) {
		this.meetingProposeTime = meetingProposeTime;
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
	
}
