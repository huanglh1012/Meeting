package mis.meeting.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import mis.meeting.dao.MeetingDAO;
import mis.meeting.dto.MeetingDTO;
import mis.meeting.dto.MeetingMemberRfDTO;
import mis.meeting.dto.MeetingRoomBookingDTO;
import mis.meeting.dto.MeetingRoomDTO;
import mis.meeting.entity.MeetingAttachmentEntity;
import mis.meeting.entity.MeetingEntity;
import mis.meeting.entity.MeetingMemberRfEntity;
import mis.meeting.entity.MeetingRoomEntity;
import mis.meeting.job.MeetingMessageSendJob;
import mis.meeting.myenum.AttachmentCategoryEnum;
import mis.meeting.myenum.MeetingMemberRoleEnum;
import mis.meeting.myenum.MeetingStateEnum;
import mis.shortmessage.dto.ShortMessageResultDTO;
import mis.shortmessage.dto.ShortMessageSendDTO;
import mis.shortmessage.entity.MessageSendCenterEntity;
import mis.shortmessage.entity.ShortMessageSendLogEntity;
import mis.shortmessage.myenum.MessageSendStateEnum;
import mis.shortmessage.service.ShortMessageService;

import org.aspectj.util.FileUtil;
import org.quartz.Trigger;
import org.quartz.DateBuilder.IntervalUnit;
import org.springframework.stereotype.Service;

import com.eastcompeace.system.quartz.core.QuarzManager;
import com.eastcompeace.system.quartz.entity.JobEntity;
import com.eastcompeace.system.quartz.util.MyTriggerUtils;

import ecp.bsp.system.commons.constant.ExceptionCodeConst;
import ecp.bsp.system.commons.dto.ActionResult;
import ecp.bsp.system.commons.utils.ActionResultUtil;
import ecp.bsp.system.commons.utils.LoggerUtil;
import ecp.bsp.system.commons.utils.StringUtils;
import ecp.bsp.system.core.BaseDTO;
import ecp.bsp.system.core.BaseService;
import ecp.bsp.system.framework.file.data.dto.AttachmentDTO;
import ecp.bsp.system.framework.file.data.entity.AttachmentEntity;
import ecp.bsp.system.framework.file.data.entity.AttachmentTempEntity;
import ecp.bsp.system.framework.file.impl.AttachmentDAO;
import ecp.bsp.system.framework.file.impl.AttachmentService;

@Service
public class MeetingService extends BaseService {
	@Resource
	private MeetingDAO meetingDAO;
	
	@Resource
	private AttachmentDAO attachmentDAO;
	
	@Resource
	private AttachmentService attachmentService;
	
	@Resource
	private ShortMessageService shortMessageService;
	
	public ActionResult isMeetingExistByPlanDatetimeRang(MeetingDTO inMeetingDTO) {
		MeetingEntity tmpMeetingEntity = null;
		if (inMeetingDTO.getMeetingId() != null) {
			tmpMeetingEntity = this.meetingDAO.getMeetingByDatetimeRangWithoutMeetingId(inMeetingDTO.getMeetingRoomId(),
					inMeetingDTO.getMeetingId(), inMeetingDTO.getMeetingStartTime(), inMeetingDTO.getMeetingEndTime());
		} else {
			tmpMeetingEntity = this.meetingDAO.getMeetingByPlanDatetimeRang(inMeetingDTO.getMeetingRoomId(), 
				inMeetingDTO.getMeetingStartTime(), inMeetingDTO.getMeetingEndTime());
		}
		if (tmpMeetingEntity != null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "会议时间冲突" + "<br>" 
				    + "在当前发起会议的开始时间和结束时间段内已经有其它会议占用了会议室" + "<br>"
					+ "发生冲突的会议：" + tmpMeetingEntity.getMeetingSubject();
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		return ActionResultUtil.getActionResult(inMeetingDTO.getMeetingRoomId(), "不存在时间冲突的会议");
	}
	
	public ActionResult insertMeeting(MeetingDTO inMeetingDTO) throws Exception {
		// 会议时间重复时需要询问用户是否确认发起会议，这部分功能不在本方法中实现，所有本方法不需要检测会议时间是否重叠问题
        MeetingEntity tmpNewMeetingEntity = new MeetingEntity();
        MeetingDTO.dtoToEntity(inMeetingDTO, tmpNewMeetingEntity);
        tmpNewMeetingEntity.setMeetingStateId(String.valueOf(MeetingStateEnum.MEETING_OPEN.ordinal()));
		this.meetingDAO.insert(tmpNewMeetingEntity);
		
		// 设置会议ID信息
		inMeetingDTO.setMeetingId(tmpNewMeetingEntity.getMeetingId());
		
		// 插入与会人员信息
		MeetingMemberRfEntity tmpMeetingMemberRfEntity = null;
		List<MeetingMemberRfDTO> tmpMeetingMemberList = this.getMeetingMemberList(inMeetingDTO);
		for(MeetingMemberRfDTO tmpSubMeetingMember: tmpMeetingMemberList) {
			tmpMeetingMemberRfEntity = new MeetingMemberRfEntity();
			MeetingMemberRfDTO.dtoToEntity(tmpSubMeetingMember, tmpMeetingMemberRfEntity);
			this.meetingDAO.insert(tmpMeetingMemberRfEntity);
		}
		
		// 插入会议附件信息
		this.saveMeetingFile(inMeetingDTO);
		
		// 插入短信通知信息
		this.saveMessageSendInfo(inMeetingDTO);
		
		return ActionResultUtil.getActionResult(tmpNewMeetingEntity.getId(), "会议发起成功");
	}
	
	public ActionResult updateMeeting(MeetingDTO inMeetingDTO) throws Exception {
        MeetingEntity tmpCurrentMeetingEntity = this.meetingDAO.getEntity(MeetingEntity.class, "meetingId", inMeetingDTO.getMeetingId());
		if (tmpCurrentMeetingEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有会议可更新";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		// 清空与会人员信息
		this.meetingDAO.deleteMeetingMember(tmpCurrentMeetingEntity.getMeetingId());
		
		// 清空短信通知信息
//		MessageSendCenterEntity tmpMessageSendCenterEntity = this.meetingDAO.getEntity(MessageSendCenterEntity.class, "meetingId", inMeetingDTO.getMeetingId());
//		if (tmpMessageSendCenterEntity != null)
//			this.meetingDAO.delete(tmpMessageSendCenterEntity);
			
		// 更新会议信息
		MeetingDTO.dtoToEntity(inMeetingDTO, tmpCurrentMeetingEntity);
		this.meetingDAO.update(tmpCurrentMeetingEntity);
		
		// 插入与会人员信息
		MeetingMemberRfEntity tmpMeetingMemberRfEntity = null;
		List<MeetingMemberRfDTO> tmpMeetingMemberList = this.getMeetingMemberList(inMeetingDTO);
		for(MeetingMemberRfDTO tmpSubMeetingMember: tmpMeetingMemberList) {
			tmpMeetingMemberRfEntity = new MeetingMemberRfEntity();
			MeetingMemberRfDTO.dtoToEntity(tmpSubMeetingMember, tmpMeetingMemberRfEntity);
			this.meetingDAO.insert(tmpMeetingMemberRfEntity);
		}
			
		// 插入会议附件信息
		this.saveMeetingFile(inMeetingDTO);
				
		// 插入短信通知信息
		this.saveMessageSendInfo(inMeetingDTO);
		
		return ActionResultUtil.getActionResult(tmpCurrentMeetingEntity.getId(), "会议更新成功");
	}
	
	public ActionResult deleteMeeting(String inMeetingId) throws Exception {
		MeetingEntity tmpMeetingEntity = this.meetingDAO.getEntity(MeetingEntity.class, "meetingId", inMeetingId);
		if (tmpMeetingEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有会议可删除";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		// 清空与会人员信息
		this.meetingDAO.deleteMeetingMember(tmpMeetingEntity.getMeetingId());
		
		// 删除会议附件信息
		List<MeetingAttachmentEntity> tmpMeetingAttachmentEntity = this.meetingDAO.getEntityList(MeetingAttachmentEntity.class, 
				"meetingId", inMeetingId, null);
		for (MeetingAttachmentEntity subMeetingAttachmentEntity : tmpMeetingAttachmentEntity)
			this.deleteAttachmentByAttachmentId(subMeetingAttachmentEntity.getAttachmentId());
		
		// 更新短信通知信息
		MessageSendCenterEntity tmpMessageSendCenterEntity = this.meetingDAO.getEntity(MessageSendCenterEntity.class, "meetingId", inMeetingId);
		tmpMessageSendCenterEntity.setMeetingId(null);
		this.meetingDAO.update(tmpMessageSendCenterEntity);
		
		// 清空定时作业任务
		if(QuarzManager.checkJobExists(inMeetingId, "meetingJobGroup"))
			QuarzManager.deleteJob(inMeetingId, "meetingJobGroup");		
				
		// 删除会议信息
		this.meetingDAO.delete(tmpMeetingEntity);
		
		return ActionResultUtil.getActionResult(tmpMeetingEntity.getId(), "会议删除成功");
	}
	
	public ActionResult closeMeeting(String inMeetingId) throws Exception {
		MeetingEntity tmpMeetingEntity = this.meetingDAO.getEntity(MeetingEntity.class, "meetingId", inMeetingId);
		if (tmpMeetingEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有找到对应的会议，会议ID：" + inMeetingId;
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		tmpMeetingEntity.setMeetingStateId(String.valueOf(MeetingStateEnum.MEETING_CLOSE.ordinal()));
		tmpMeetingEntity.setMeetingUploadEndTime(new Date());
		this.meetingDAO.update(tmpMeetingEntity);
		
		// 清空定时作业任务
		if(QuarzManager.checkJobExists(inMeetingId, "meetingJobGroup"))
			QuarzManager.deleteJob(inMeetingId, "meetingJobGroup");	
				
		return ActionResultUtil.getActionResult(tmpMeetingEntity.getId(), "会议关闭成功");
	}
	
	private List<MeetingMemberRfDTO> getMeetingMemberList(MeetingDTO inMeetingDTO) {
		List<MeetingMemberRfDTO> tmpMeetingMemberRfDTOList = new ArrayList<MeetingMemberRfDTO>();
		MeetingMemberRfDTO tmpMeetingMemberRfDTO = null;
		if (StringUtils.isValidateString(inMeetingDTO.getMeetingCreator())) {
			tmpMeetingMemberRfDTO = new MeetingMemberRfDTO();
			tmpMeetingMemberRfDTO.setEmployeeId(inMeetingDTO.getMeetingCreator());
			tmpMeetingMemberRfDTO.setMeetingId(inMeetingDTO.getMeetingId());
			tmpMeetingMemberRfDTO.setMeetingMemberRoleId(String.valueOf(MeetingMemberRoleEnum.MEETING_CREATOR.ordinal()));
			tmpMeetingMemberRfDTOList.add(tmpMeetingMemberRfDTO);
		}
		if (StringUtils.isValidateString(inMeetingDTO.getMeetingPresenter())) {
			tmpMeetingMemberRfDTO = new MeetingMemberRfDTO();
			tmpMeetingMemberRfDTO.setEmployeeId(inMeetingDTO.getMeetingPresenter());
			tmpMeetingMemberRfDTO.setMeetingId(inMeetingDTO.getMeetingId());
			tmpMeetingMemberRfDTO.setMeetingMemberRoleId(String.valueOf(MeetingMemberRoleEnum.MEETING_PRESENTER.ordinal()));
			tmpMeetingMemberRfDTOList.add(tmpMeetingMemberRfDTO);
		}
		if (StringUtils.isValidateString(inMeetingDTO.getMeetingParticipants())) {
			String[] tmpMeetingParticipantArray = inMeetingDTO.getMeetingParticipants().split(",");
			for (String subMeetingParticipant : tmpMeetingParticipantArray) {
				tmpMeetingMemberRfDTO = new MeetingMemberRfDTO();
				tmpMeetingMemberRfDTO.setEmployeeId(subMeetingParticipant);
				tmpMeetingMemberRfDTO.setMeetingId(inMeetingDTO.getMeetingId());
				tmpMeetingMemberRfDTO.setMeetingMemberRoleId(String.valueOf(MeetingMemberRoleEnum.MEETING_PARTICIPANT.ordinal()));
				tmpMeetingMemberRfDTOList.add(tmpMeetingMemberRfDTO);
			}
		}
		
		return tmpMeetingMemberRfDTOList;
	}
	
	public void saveMeetingFile(MeetingDTO inMeetingDTO) throws Exception {
		List<AttachmentEntity> tmpSaveMeetingAttachmentEntityList = new ArrayList<AttachmentEntity>();
		// 保存会议记录附件信息		
		this.saveAttachment(inMeetingDTO.getMeetingRecordFileList(), inMeetingDTO, 
				AttachmentCategoryEnum.MEETING_RECORD_ATTACHMENT, tmpSaveMeetingAttachmentEntityList);
		// 保存普通附件信息		
		this.saveAttachment(inMeetingDTO.getMeetingFileList(), inMeetingDTO, 
				AttachmentCategoryEnum.NORMAL_ATTACHMENT, tmpSaveMeetingAttachmentEntityList);
		// 保存文件物理信息
		for(AttachmentEntity subAttachmentEntity : tmpSaveMeetingAttachmentEntityList){
			String tmpSourceFileName = subAttachmentEntity.getAttachmentTempPath() + File.separator + subAttachmentEntity.getAttachmentCreateId();
			String tmpTargeFileName = subAttachmentEntity.getAttachmentPath() + File.separator + subAttachmentEntity.getAttachmentName();
			FileUtil.copyFile(new File(tmpSourceFileName), new File(tmpTargeFileName));
		}
	}
	
	public ActionResult deleteAttachmentByAttachmentId(String inAttachmentId) throws Exception {
		MeetingAttachmentEntity tmpMeetingAttachmentEntity = this.meetingDAO.getEntity(MeetingAttachmentEntity.class, "attachmentId", inAttachmentId);
		if (tmpMeetingAttachmentEntity != null) {
			// 删除会议附件信息及物理信息
			this.meetingDAO.delete(tmpMeetingAttachmentEntity);
			this.attachmentService.deleteAttachmentByAttachmentId(inAttachmentId);
		}
		// 删除临时附件信息
		this.attachmentService.deleteTempAttachmentByAttachmentId(inAttachmentId);
		
		return ActionResultUtil.getActionResult(inAttachmentId, "会议删除成功");
	}
	
	public void saveAttachment(List<AttachmentDTO> inAttachmentList, MeetingDTO inMeetingDTO, AttachmentCategoryEnum inAttachmentCategoryEnum,
			List<AttachmentEntity> outSaveMeetingAttachmentEntityList) throws Exception {
		AttachmentEntity tmpAttachmentEntity = null;
		MeetingAttachmentEntity tmpMeetingAttachmentEntity = null;
		for (AttachmentDTO subAttachmentDTO : inAttachmentList) {
			AttachmentTempEntity tmpAttachmentTempEntity = this.attachmentDAO.getEntity(AttachmentTempEntity.class, subAttachmentDTO.getAttachmentId());
			tmpMeetingAttachmentEntity = this.meetingDAO.getEntity(MeetingAttachmentEntity.class, "attachmentId",subAttachmentDTO.getAttachmentId());
			// 如果会议附件已存在，则无需保存
			if (tmpMeetingAttachmentEntity == null) {
				// 保存附件信息
				tmpAttachmentEntity = new AttachmentEntity();    
				BaseDTO.copyObjectPropertys(tmpAttachmentTempEntity, tmpAttachmentEntity);
				tmpAttachmentEntity.setAttachmentRename(tmpAttachmentTempEntity.getAttachmentCreateId());
				tmpAttachmentEntity.setEmployeeId(inMeetingDTO.getEmployeeId());
				tmpAttachmentEntity.setAttachmentCreateTime(new Date());
				this.attachmentDAO.insert(tmpAttachmentEntity);
				outSaveMeetingAttachmentEntityList.add(tmpAttachmentEntity);
				
				// 保存会议附件信息
				tmpMeetingAttachmentEntity = new MeetingAttachmentEntity();
				tmpMeetingAttachmentEntity.setAttachmentId(tmpAttachmentEntity.getAttachmentId());
				tmpMeetingAttachmentEntity.setMeetingId(inMeetingDTO.getMeetingId());
				tmpMeetingAttachmentEntity.setAttachmentCategoryId(String.valueOf(inAttachmentCategoryEnum.ordinal()));
				this.attachmentDAO.insert(tmpMeetingAttachmentEntity);
			}
		}
	}
	
	public void saveMessageSendInfo(MeetingDTO inMeetingDTO) throws Exception {
		// 保存消息发送信息
		MessageSendCenterEntity tmpMessageSendCenterEntity = this.meetingDAO.getEntity(MessageSendCenterEntity.class, "meetingId", inMeetingDTO.getMeetingId());;
		if (inMeetingDTO.getMessageNoticeTime() != null) {
			if (tmpMessageSendCenterEntity == null) {
				tmpMessageSendCenterEntity = new MessageSendCenterEntity();
				tmpMessageSendCenterEntity.setMeetingId(inMeetingDTO.getMeetingId());
				tmpMessageSendCenterEntity.setSendMessage(inMeetingDTO.getMeetingSubject());
				tmpMessageSendCenterEntity.setSendDatetime(inMeetingDTO.getMessageNoticeTime());
				tmpMessageSendCenterEntity.setMessageSendStateId(String.valueOf(MessageSendStateEnum.UNSEND.ordinal()));
				tmpMessageSendCenterEntity.setIsActive(inMeetingDTO.getIsSendMessageNotice());
				this.meetingDAO.insert(tmpMessageSendCenterEntity);
			} else {
				tmpMessageSendCenterEntity.setSendMessage(inMeetingDTO.getMeetingSubject());
				tmpMessageSendCenterEntity.setSendDatetime(inMeetingDTO.getMessageNoticeTime());
				tmpMessageSendCenterEntity.setIsActive(inMeetingDTO.getIsSendMessageNotice());
				this.meetingDAO.update(tmpMessageSendCenterEntity);
			}
		}
		
		// 清空作业任务
		if(QuarzManager.checkJobExists(inMeetingDTO.getMeetingId(), "meetingJobGroup"))
			QuarzManager.deleteJob(inMeetingDTO.getMeetingId(), "meetingJobGroup");		
		
		if (inMeetingDTO.getIsSendMessageNotice() == 1) {
			// 添加定时任务
			ShortMessageSendDTO tmpShortMessageSendDTO = new ShortMessageSendDTO();
			BaseDTO.copyObjectPropertys(inMeetingDTO, tmpShortMessageSendDTO);
			tmpShortMessageSendDTO.setMessageSendCenterId(tmpMessageSendCenterEntity.getMessageSendCenterId());
			Map<String,Object> tmpJobMap = new HashMap<String,Object>();
			tmpJobMap.put("shortMessageSendDTO", tmpShortMessageSendDTO);
			Date tmpMessagePreNoticeTime = new Date(inMeetingDTO.getMeetingStartTime().getTime() - 600000);
			JobEntity tmpJobEntity = new JobEntity(inMeetingDTO.getMeetingId(), "meetingJobGroup", MeetingMessageSendJob.class);
			QuarzManager.createSimpleJob(tmpJobEntity, tmpJobMap, true);
			Trigger tmpNoticeTrigger = MyTriggerUtils.newSimpleCountTrigger(inMeetingDTO.getMeetingId(), "meetingJobGroup", 
					inMeetingDTO.getMeetingId() + "NoticeTrigger", "meetingTriggerGroup",
					IntervalUnit.MINUTE,Integer.valueOf("1"),Integer.valueOf(1), inMeetingDTO.getMessageNoticeTime());
			Trigger tmpPreNoticeTrigger = MyTriggerUtils.newSimpleCountTrigger(inMeetingDTO.getMeetingId(), "meetingJobGroup", 
					inMeetingDTO.getMeetingId() + "PreNoticeTrigger", "meetingTriggerGroup",
					IntervalUnit.MINUTE,Integer.valueOf("1"),Integer.valueOf(1),tmpMessagePreNoticeTime);
			QuarzManager.addTrigger2Job(tmpNoticeTrigger);
			QuarzManager.addTrigger2Job(tmpPreNoticeTrigger);
			// 如果通知时间大于当前操作时间，则发送短信信息
			if (inMeetingDTO.getMessageNoticeTime().getTime() > new Date().getTime())
				this.sendMeetingMessage(tmpShortMessageSendDTO);
		}
	}
	
	/**
	 * 根据会议ID获取会议信息
	 * 
	 * @param inMeetingId
	 *     会议ID
	 * @return
	 *     返回会议信息
	 * @throws Exception 
	 */
	public MeetingDTO getMeetingInfoById(String inMeetingId) throws Exception {
		MeetingDTO tmpMeetingDTO = this.meetingDAO.getMeetingInfoById(inMeetingId);
		
		// 获取参会人员或部门
		String meetingParticipants = StringUtils.EMPTY_STRING;
		String meetingParticipantNames = StringUtils.EMPTY_STRING;
		Map<String,String> tmpEmployeeDepartmentMap = new HashMap<String,String>();
		List<MeetingMemberRfDTO> tmpMeetingMemberRfDTOList = this.meetingDAO.getMeetingMemberRfInfoByMeetingId(inMeetingId);
		for (MeetingMemberRfDTO subMeetingMemberRfDTO : tmpMeetingMemberRfDTOList) {
			if (subMeetingMemberRfDTO.getMeetingMemberRoleId().equals(String.valueOf(MeetingMemberRoleEnum.MEETING_PRESENTER.ordinal()))) {
				tmpMeetingDTO.setMeetingPresenter(subMeetingMemberRfDTO.getEmployeeId());
				tmpMeetingDTO.setMeetingPresenterName(subMeetingMemberRfDTO.getEmployeeName());
			} else if (subMeetingMemberRfDTO.getMeetingMemberRoleId().equals(String.valueOf(MeetingMemberRoleEnum.MEETING_PARTICIPANT.ordinal()))) {
				if (StringUtils.isValidateString(meetingParticipants)) {
					meetingParticipants += "," + subMeetingMemberRfDTO.getEmployeeId();
				} else {
					meetingParticipants = subMeetingMemberRfDTO.getEmployeeId();
				}
				if (tmpEmployeeDepartmentMap.containsKey(subMeetingMemberRfDTO.getDepartmentName())) {
					String tmpNewEmployeeNames = tmpEmployeeDepartmentMap.get(subMeetingMemberRfDTO.getDepartmentName()) + "," + subMeetingMemberRfDTO.getEmployeeName();
					tmpEmployeeDepartmentMap.remove(subMeetingMemberRfDTO.getDepartmentName());
					tmpEmployeeDepartmentMap.put(subMeetingMemberRfDTO.getDepartmentName(), tmpNewEmployeeNames);
				}
				else {
					tmpEmployeeDepartmentMap.put(subMeetingMemberRfDTO.getDepartmentName(), subMeetingMemberRfDTO.getEmployeeName());
				}
			}
		}
		for (Map.Entry<String, String> subEntry : tmpEmployeeDepartmentMap.entrySet()) { 
			meetingParticipantNames += subEntry.getKey() + ": " + subEntry.getValue() + "\n"; 
		}
		tmpMeetingDTO.setMeetingParticipants(meetingParticipants);
		tmpMeetingDTO.setMeetingParticipantNames(meetingParticipantNames);
		
		// 获取短信提醒信息
		MessageSendCenterEntity tmpMessageSendCenterEntity = this.meetingDAO.getEntity(MessageSendCenterEntity.class, "meetingId", inMeetingId);
		if (tmpMessageSendCenterEntity != null)
			tmpMeetingDTO.setMessageNoticeTime(tmpMessageSendCenterEntity.getSendDatetime());
		
		// 获取附件信息
		List<AttachmentDTO> tmpMeetingRecordFileList = this.meetingDAO.getMeetingAttachmentInfoByMeetingId(inMeetingId,
				String.valueOf(AttachmentCategoryEnum.MEETING_RECORD_ATTACHMENT.ordinal()));
		List<AttachmentDTO> tmpMeetingFileList = this.meetingDAO.getMeetingAttachmentInfoByMeetingId(inMeetingId,
				String.valueOf(AttachmentCategoryEnum.NORMAL_ATTACHMENT.ordinal()));
		tmpMeetingDTO.setMeetingRecordFileList(tmpMeetingRecordFileList);
		tmpMeetingDTO.setMeetingFileList(tmpMeetingFileList);
		
		return tmpMeetingDTO;
	}
	
	/**
	 * 根据用户ID获取用户参与的会议列表
	 * 
	 * @param inEmployeeId
	 * 			用户ID
	 * @return
	 * 			返回用户参与的会议列表
	 */
	public List<MeetingDTO> getParticipantMeetingByEmployeeId(String inEmployeeId) {
		return this.meetingDAO.getParticipantMeetingByEmployeeId(inEmployeeId);
	}
	
	/**
	 * 根据用户ID获取用户发起的会议列表
	 * 
	 * @param inEmployeeId
	 * 			用户ID
	 * @return
	 * 			返回用户发起的会议列表
	 */
	public List<MeetingDTO> getCreatorMeetingByEmployeeId(String inEmployeeId) {
		return this.meetingDAO.getCreatorMeetingByEmployeeId(inEmployeeId);
	}
	
	public List<AttachmentDTO> getMeetingAttachmentInfoByMeetingId(String inMeetingId, String inAttachmentCategoryId) throws Exception {
		return this.meetingDAO.getMeetingAttachmentInfoByMeetingId(inMeetingId,inAttachmentCategoryId);
	}
	
	public ActionResult insertMeetingRoom(MeetingRoomDTO inMeetingRoomDTO) throws Exception {
		MeetingRoomEntity tmpExistMeetingRoomEntity = this.meetingDAO.getEntity(MeetingRoomEntity.class, "meetingRoomName", inMeetingRoomDTO.getMeetingRoomName());
        if (tmpExistMeetingRoomEntity != null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前新建的会议室地址与其它会议室地址相同，发生冲突";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
        
        MeetingRoomEntity tmpNewMeetingRoomEntity = new MeetingRoomEntity();
        MeetingRoomDTO.dtoToEntity(inMeetingRoomDTO, tmpNewMeetingRoomEntity);
		this.meetingDAO.insert(tmpNewMeetingRoomEntity);
		
		return ActionResultUtil.getActionResult(tmpNewMeetingRoomEntity.getId(), "会议室新建成功");
	}
	
	public ActionResult updateMeetingRoom(MeetingRoomDTO inMeetingRoomDTO) throws Exception {
		MeetingRoomEntity tmpExistMeetingRoomEntity = this.meetingDAO.getEntity(MeetingRoomEntity.class, "meetingRoomName", inMeetingRoomDTO.getMeetingRoomName());
        if (tmpExistMeetingRoomEntity != null) {
        	if (!tmpExistMeetingRoomEntity.getId().equals(inMeetingRoomDTO.getMeetingRoomId()) && tmpExistMeetingRoomEntity.getMeetingRoomName().equals(inMeetingRoomDTO.getMeetingRoomName())) {
    			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前修改的会议室名称与其它会议室名称相同，发生冲突";
    			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
    			throw new RuntimeException(exceptionMessage);
    		}
        }
		
        MeetingRoomEntity tmpCurrentMeetingRoomEntity = this.meetingDAO.getEntity(MeetingRoomEntity.class, "meetingRoomId", inMeetingRoomDTO.getMeetingRoomId());
		if (tmpCurrentMeetingRoomEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有会议室可修改";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}

		MeetingRoomDTO.dtoToEntity(inMeetingRoomDTO, tmpCurrentMeetingRoomEntity);
		this.meetingDAO.update(tmpCurrentMeetingRoomEntity);
		
		return ActionResultUtil.getActionResult(tmpCurrentMeetingRoomEntity.getId(), "会议室修改成功");
	}
	
	public ActionResult deleteMeetingRoom(String inMeetingRoomId) throws Exception {
		MeetingRoomEntity tmpMeetingRoomEntity = this.meetingDAO.getEntity(MeetingRoomEntity.class, "meetingRoomId", inMeetingRoomId);
		if (tmpMeetingRoomEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有会议室可删除";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		this.meetingDAO.delete(tmpMeetingRoomEntity);
		
		return ActionResultUtil.getActionResult(tmpMeetingRoomEntity.getId(), "会议室删除成功");
	}
	
	/**
	 * 获取会议室信息列表
	 * 
	 * @return
	 *     返回会议室信息列表
	 * @throws Exception 
	 */
	public Object getMeetingRoomList() throws Exception {
		return (List<MeetingRoomDTO>) this.meetingDAO.getMeetingRoomList();
	}
	
	public List<MeetingRoomBookingDTO> getMeetingRoomBookingListByRoomId(String inMeetingRoomId) {
		return (List<MeetingRoomBookingDTO>) this.meetingDAO.getMeetingRoomBookingListByRoomId(inMeetingRoomId);
	}

	public void sendMeetingMessage(ShortMessageSendDTO inShortMessageSendDTO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		
		MeetingRoomEntity tmpMeetingRoomEntity = this.attachmentDAO.getEntity(MeetingRoomEntity.class, inShortMessageSendDTO.getMeetingRoomId());
		String tmpMessageParam = "各位,"+sdf.format(inShortMessageSendDTO.getMeetingStartTime()) + "在" + tmpMeetingRoomEntity.getMeetingRoomName() + "-" + tmpMeetingRoomEntity.getMeetingRoomAddress();
		MeetingDTO tmpMeetingDTO = this.meetingDAO.getMeetingEmployeeInfo(inShortMessageSendDTO.getMeetingId());
		// 发送信息
		ShortMessageResultDTO tmpShortMessageResultDTO = this.shortMessageService.sendMessage(tmpMeetingDTO.getTelephone(), tmpMessageParam);
		// 保存消息发送日志
		ShortMessageSendLogEntity tmpShortMessageSendLogEntity = new ShortMessageSendLogEntity();
		tmpShortMessageSendLogEntity.setMessageSendCenterId(inShortMessageSendDTO.getMessageSendCenterId());
		tmpShortMessageSendLogEntity.setShortMessageCenterId(null);
		tmpShortMessageSendLogEntity.setMessageSendParam(tmpMessageParam);
		tmpShortMessageSendLogEntity.setMessageSendTelephone(tmpMeetingDTO.getTelephone());
		tmpShortMessageSendLogEntity.setMessageSendResult(tmpShortMessageResultDTO.getMessageSendResult());
		tmpShortMessageSendLogEntity.setMessageSendTime(new Date());
		tmpShortMessageSendLogEntity.setMessageSendCount(1);
		if (tmpShortMessageResultDTO.getCode().equals("00")) 
			tmpShortMessageSendLogEntity.setMessageSendStateId(String.valueOf(MessageSendStateEnum.SEND_SUCCESSED.ordinal()));
		else 
			tmpShortMessageSendLogEntity.setMessageSendStateId(String.valueOf(MessageSendStateEnum.SEND_FAILED.ordinal()));
		this.meetingDAO.insert(tmpShortMessageSendLogEntity);
	}
}
