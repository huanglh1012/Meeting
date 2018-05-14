package mis.meeting.controller;

import java.util.List;

import javax.annotation.Resource;

import mis.meeting.constant.MeetingConstant;
import mis.meeting.dto.MeetingDTO;
import mis.meeting.dto.MeetingRoomDTO;
import mis.meeting.service.MeetingService;

import org.springframework.stereotype.Controller;

import ecp.bsp.business.file.dto.QueryResultDTO;
import ecp.bsp.system.commons.dto.ActionResult;
import ecp.bsp.system.commons.dto.AjaxResult;
import ecp.bsp.system.framework.query.PageQueryHepler;
import ecp.bsp.system.framework.query.data.entity.DynamicGridQueryEntity;
import ecp.bsp.system.framework.query.data.entity.QueryPager;
import ecp.bsp.system.framework.query.impl.PageQueryService;

@Controller
public class MeetingController  {
	
	@Resource
	private PageQueryService pageQueryService;
	
	@Resource
	private MeetingService meetingService;
	
	
	/**
	 * 插入会议信息
	 * 
	 * @param inMeetingDTO
	 * 			会议信息
	 * @param inMeetingSummaryFiles
	 * 			会议纪要文件信息
	 * @param inMeetingFiles
	 * 			会议附件信息
	 * @return
	 * 			返回会议插入情况
	 */
	@SuppressWarnings("rawtypes")
	public Object insertMeeting(MeetingDTO inMeetingDTO, List inMeetingSummaryFiles, List inMeetingFiles) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		try {
			result = this.meetingService.insertMeeting(inMeetingDTO);
			ajaxResult.setSuccess(true);
			ajaxResult.setMsg(result);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	

	/**
	 * 更改会议信息
	 * 
	 * @param inMeetingDTO
	 * 			会议信息
	 * @param inMeetingSummaryFiles
	 * 			会议纪要文件信息
	 * @param inMeetingFiles
	 * 			会议附件信息
	 * @return
	 * 			返回会议更改情况
	 */
	@SuppressWarnings("rawtypes")
	public Object updateMeeting(MeetingDTO inMeetingDTO, List inMeetingSummaryFiles, List inMeetingFiles) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		try {
			result = this.meetingService.updateMeeting(inMeetingDTO);
			ajaxResult.setSuccess(true);
			ajaxResult.setMsg(result);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 删除会议信息
	 * 
	 * @param inMeetingId
	 *     会议ID
	 * @return
	 *     返回会议信息删除情况
	 */
	public AjaxResult deleteMeeting(String inMeetingId) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.meetingService.deleteMeeting(inMeetingId);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 关闭会议信息
	 * 
	 * @param inMeetingId
	 *     会议ID
	 * @return
	 *     返回会议信息关闭情况
	 */
	public AjaxResult closeMeeting(String inMeetingId) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.meetingService.closeMeeting(inMeetingId);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	public Object getMeetingInfoById(String inMeetingId) throws Exception {
		return this.meetingService.getMeetingInfoById(inMeetingId);
	}
	
	/**
	 * 获取会议信息列表（根据查询条件queryCondition）
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object getMeetingListByCondition(List queryCondition,Integer start,Integer limit){
		String sql = MeetingConstant.SQL_GET_MEETING_LIST_BY_CONDITION;
		List<DynamicGridQueryEntity> filter = PageQueryHepler.createConditions(queryCondition);
		QueryPager queryPager = this.pageQueryService.queryList(filter, sql, start, limit, MeetingDTO.class);
		QueryResultDTO queryResultDTO = new QueryResultDTO();
		queryResultDTO.setRecordsTotal(queryPager.getTotalCount());
		queryResultDTO.setRecordsFiltered((queryPager.getTotalCount()));
		queryResultDTO.setData(queryPager.getResult());
		return queryResultDTO;
	}
	
	/**
	 * 获取会议材料信息列表（根据查询条件queryCondition）
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object getMeetingAttachmentListByCondition(List queryCondition,Integer start,Integer limit){
		String sql = MeetingConstant.SQL_GET_MEETING_ATTACHMENT_LIST_BY_CONDITION;
		List<DynamicGridQueryEntity> filter = PageQueryHepler.createConditions(queryCondition);
		QueryPager queryPager = this.pageQueryService.queryList(filter, sql, start, limit, MeetingDTO.class);
		QueryResultDTO queryResultDTO = new QueryResultDTO();
		queryResultDTO.setRecordsTotal(queryPager.getTotalCount());
		queryResultDTO.setRecordsFiltered((queryPager.getTotalCount()));
		queryResultDTO.setData(queryPager.getResult());
		return queryResultDTO;
	}
	
	/**
	 * 插入会议室信息
	 * 
	 * @param inMeetingRoomDTO
	 * 			会议室信息
	 * @return
	 * 			返回会议室插入情况
	 */
	public Object insertMeetingRoom(MeetingRoomDTO inMeetingRoomDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		try {
			result = this.meetingService.insertMeetingRoom(inMeetingRoomDTO);
			ajaxResult.setSuccess(true);
			ajaxResult.setMsg(result);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	

	/**
	 * 更改会议室信息
	 * 
	 * @param inMeetingRoomDTO
	 * 			会议室信息
	 * @return
	 * 			返回会议室更改情况
	 */
	public Object updateMeetingRoom(MeetingRoomDTO inMeetingRoomDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		try {
			result = this.meetingService.updateMeetingRoom(inMeetingRoomDTO);
			ajaxResult.setSuccess(true);
			ajaxResult.setMsg(result);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 删除会议室信息
	 * 
	 * @param inMeetingRoomId
	 *     会议室ID
	 * @return
	 *     返回会议信息室删除情况
	 */
	public AjaxResult deleteMeetingRoom(String inMeetingRoomId) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.meetingService.deleteMeetingRoom(inMeetingRoomId);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 获取会议室信息列表
	 * 
	 * @return
	 *     返回会议室信息列表
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Object getMeetingRoomList() throws Exception {
		return (List<MeetingRoomDTO>) this.meetingService.getMeetingRoomList();
	}
	
	/**
	 * 根据会议室ID获取会议室预定信息
	 * 
	 * @param inMeetingRoomId
	 *     会议室ID
	 * @return
	 *     返回会议信息室预定信息
	 */
	public Object getMeetingRoomBookingListByRoomId(String inMeetingRoomId) {	
		return this.meetingService.getMeetingRoomBookingListByRoomId(inMeetingRoomId);
	}
}
