package ecp.bsp.system.framework.file.impl;


import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import ecp.bsp.system.framework.file.FileUpload;
import ecp.bsp.system.framework.file.data.dto.FileEntity;

/**
 * 闄勪欢鎺у埗灞�
 * 
 * @since 2015-07-02
 * 
 * @author zengqingyue.
 * 
 */
@Controller
public class AttachmentController {
	
	@Resource
	private AttachmentService attachmentService;

	/**
	 * 鍒╃敤commons-uploadfile妗嗘灦鏉ヤ笂浼犳枃浠�
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public Object upload(FileEntity fileEntity,HttpServletRequest request) throws Exception{
		//涓婁紶涓存椂鏂囦欢
		FileUpload.upload(fileEntity,request);
		//淇濆瓨涓存椂鏂囦欢鏁版嵁搴撲俊鎭�
		return this.attachmentService.saveTempFileInfo(fileEntity);
	}
	
	/**
	 * 鏍规嵁闄勪欢ID鍒犻櫎涓存椂闄勪欢
	 * @return
	 * @throws Exception 
	 */
	public Object deleteTempAttachmentFileById(String attachmentId) throws Exception{
		return this.attachmentService.deleteTempAttachmentByAttachmentId(attachmentId);
	}
	
	/**
	 * 鏍规嵁涓嬭浇鍒楄〃涓嬭浇鏂囦欢
	 * @param request
	 * @param response
	 * @param downloadFileList
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	public Object downloadFile(List downloadFileList) throws IOException {
		return this.attachmentService.downloadFile(downloadFileList);
	}

	@SuppressWarnings("rawtypes")
	public void downloadFile(List downloadFileList,HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.attachmentService.downloadFile(downloadFileList,request,response);
	}
	
	/**
	 * 鎸佷箙鍖栭檮浠朵俊鎭�
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public Object completeFileUpload(List fileCreateIds) throws Exception{
		return this.attachmentService.completeFileUpload(fileCreateIds);
	}

}
