package ecp.bsp.system.framework.file.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ecp.bsp.system.commons.utils.FileUtil;
import ecp.bsp.system.commons.utils.PropertiesUtil;
import ecp.bsp.system.commons.utils.StringUtils;
import ecp.bsp.system.commons.utils.ZipUtil;
import ecp.bsp.system.core.BaseDTO;
import ecp.bsp.system.core.BaseService;
import ecp.bsp.system.framework.file.FileCopyTask;
import ecp.bsp.system.framework.file.data.constant.AttachmentDAOConst;
import ecp.bsp.system.framework.file.data.dto.AttachmentDTO;
import ecp.bsp.system.framework.file.data.dto.FileDownloadEntity;
import ecp.bsp.system.framework.file.data.dto.FileEntity;
import ecp.bsp.system.framework.file.data.entity.AttachmentEntity;
import ecp.bsp.system.framework.file.data.entity.AttachmentTempEntity;
import ecp.bsp.system.framework.thread.Task;
import ecp.bsp.system.framework.thread.ThreadPool;
/**
 * 闄勪欢淇℃伅鏈嶅姟
 * 
 * @author zengqingyue
 *
 * @create_date 2015-04-22
 */
@Service
public class AttachmentService  extends BaseService {

	@Resource
	private AttachmentDAO attachmentDAO;

	/**
	 * 淇濆瓨涓存椂鏂囦欢
	 * @param fileEntity
	 * @return
	 * @throws Exception
	 */
	public Object saveTempFileInfo(FileEntity fileEntity) throws Exception {
		//淇濆瓨涓存椂鏂囦欢淇℃伅
		AttachmentTempEntity attachmentTempEntity = new AttachmentTempEntity();
		attachmentTempEntity.setAttachmentName(fileEntity.getName());
		attachmentTempEntity.setAttachmentCreateId(fileEntity.getCreateId());
		attachmentTempEntity.setAttachmentPath(this.generateFilePath(attachmentTempEntity.getAttachmentId()));
		attachmentTempEntity.setAttachmentTempPath(fileEntity.getPath());
		attachmentTempEntity.setAttachmentCreateTime(new Date());
		attachmentTempEntity.setAttachmentSize(fileEntity.getSize());
		attachmentTempEntity.setAttachmentType(fileEntity.getType());
		attachmentTempEntity.setEmployeeId(fileEntity.getPaaEmployeeId());
		this.attachmentDAO.insert(attachmentTempEntity);
		//杩斿洖澶勭悊缁撴灉(Jquery File Upload鎺т欢杩斿洖鏁版嵁鏍煎紡)
		FileEntity result = new FileEntity();
        List<FileEntity> fileList = new ArrayList<FileEntity>();
        fileEntity.setId(attachmentTempEntity.getAttachmentId());
        fileList.add(fileEntity);
        result.setFiles(fileList);
		return result;
	}

	/**
	 * 鏍规嵁闄勪欢ID闆嗗悎鍒犻櫎涓存椂鏂囦欢淇℃伅
	 * @param attachmentIds
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteTempAttachment(List attachmentIds) throws Exception {
		for (Object attachmentId : attachmentIds) {
			this.deleteTempAttachmentByAttachmentId((String) attachmentId);
		}
	}
	
	/**
	 * 鍒犻櫎涓存椂闄勪欢鏁版嵁搴撲俊鎭拰鐗╃悊淇℃伅.
	 * @param attachmentId
	 *             闄勪欢ID.
	 * @throws Exception 
	 */
	public boolean deleteTempAttachmentByAttachmentId(String attachmentId) throws Exception {
		AttachmentTempEntity attachmentTempEntity = this.attachmentDAO.getEntity(AttachmentTempEntity.class, attachmentId);
		if(null!=attachmentTempEntity){
			this.attachmentDAO.delete(attachmentTempEntity);
			String fileName = attachmentTempEntity.getAttachmentTempPath()+File.separator+attachmentTempEntity.getAttachmentCreateId();
			FileUtil.deleteFile(fileName);
			return true;
		}
		return false;
	}

	/**
	 * 涓婁紶鍜屼繚瀛樻案涔呴檮浠�
	 * @param fileCreateIds
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<AttachmentEntity> completeFileUpload(List fileCreateIds) throws Exception {
		if(null!=fileCreateIds){
			List<AttachmentDTO> attachmentList = new ArrayList<AttachmentDTO>();
			//鑾峰彇涓存椂闄勪欢淇℃伅鍒楄〃
			for (Object fileCreateId : fileCreateIds) {
				AttachmentTempEntity attachmentTempEntity = this.attachmentDAO.getAttachmentTempEntityByCreateId((String)fileCreateId);
				AttachmentDTO attachmentDTO = new AttachmentDTO();
				BaseDTO.entityToDTO(attachmentTempEntity, attachmentDTO);
				attachmentList.add(attachmentDTO);
			}
			//淇濆瓨姘镐箙闄勪欢淇℃伅		
			List<AttachmentEntity> list = this.saveAttachment(attachmentList);
			//杞Щ涓存椂闄勪欢
			this.generateCopyFileTask(attachmentList);
			return list;
		}
		return null;
	}
	
	/**
	 * 淇濆瓨涓婁紶鎴愬姛鐨勯檮浠�
	 * @param attachmentList	
	 *            闄勪欢鍒楄〃.
	 * @return 缁撴灉淇℃伅.
	 * @throws Exception
	 */
	public List<AttachmentEntity> saveAttachment(List<AttachmentDTO> attachmentList) throws Exception {
		List<AttachmentEntity> list = new ArrayList<AttachmentEntity>();
		for (AttachmentDTO attachmentDTO : attachmentList) {
			AttachmentEntity attachmentEntity = new AttachmentEntity();    
			BaseDTO.dtoToEntity(attachmentDTO, attachmentEntity);
			attachmentEntity.setAttachmentRename(attachmentDTO.getAttachmentCreateId());
			this.attachmentDAO.insert(attachmentEntity);
			list.add(attachmentEntity);
		}
		return list;
	}
	
	/**
	 * 鐢熸垚鏂囦欢澶嶅埗浠诲姟
	 * @param attachmentList
	 */
	private void generateCopyFileTask(List<AttachmentDTO> attachmentList) {
		List<Task> fileGreatorTasks = new ArrayList<Task> ();
		for(AttachmentDTO attachmentDTO : attachmentList){
			FileCopyTask fileCreatorTask = new FileCopyTask(attachmentDTO.getAttachmentTempPath()+File.separator+attachmentDTO.getAttachmentCreateId()
					,attachmentDTO.getAttachmentPath()+File.separator+attachmentDTO.getAttachmentName());
			fileGreatorTasks.add(fileCreatorTask);
		}
		//娣诲姞浠诲姟骞朵笖鎵ц浠诲姟
		ThreadPool.getInstance().batchAddTask(fileGreatorTasks);
	}

	/**
	 * 鏍规嵁鍒涘缓id鐢熸垚淇濆瓨鏂囦欢璺緞,骞朵笖淇濆瓨涓�潯璁板綍
	 * @param createId
	 * @return
	 * @throws Exception
	 */
	private String generateFilePath(String createId) throws Exception{
		PropertiesUtil propertiesUtil = new PropertiesUtil(AttachmentDAOConst.PROPERTIES_NAME_FILE_DIR_PATH);
		String uploadPath = propertiesUtil.readPropertiesByName(AttachmentDAOConst.PROPERTIES_KEY_UOLOAD_PATH);
		//浜х敓瀵瑰簲缂洪櫡id鐨勯檮浠朵笂浼犺矾寰�
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateStr = format.format(new Date());
		String filePath = uploadPath.replaceAll("[\\\\+||/]+", File.separator+File.separator)
							+ File.separator + dateStr
							+ File.separator + createId;
		return filePath;
	}

	/**
	 * 鍒犻櫎姘镐箙闄勪欢鐨勬暟鎹簱鍜岀墿鐞嗕俊鎭�
	 * @param attachmentId
	 *             闄勪欢ID.
	 * @throws Exception 
	 */
	public String deleteAttachmentByAttachmentId(String attachmentId) throws Exception {
		AttachmentEntity attachmentEntity = this.attachmentDAO.getEntity(AttachmentEntity.class, attachmentId);
		if(null!=attachmentEntity){
			this.attachmentDAO.delete(attachmentEntity);
			String fileName = attachmentEntity.getAttachmentPath()+File.separator+attachmentEntity.getAttachmentName();
			FileUtil.deleteFile(fileName);
			return attachmentId;
		}
		return null;
	}
	
	
	/**
	 * 鏍规嵁鏂囦欢鍒楄〃涓嬭浇鏂囦欢
	 * @param attachmentRecord
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	public Object downloadFile(List downloadFileList) throws IOException{
		List<FileEntity> fileList = new ArrayList<FileEntity>();
		
		if (downloadFileList != null) {
			for (Object attachmentId : downloadFileList) {
				AttachmentEntity attachmentEntity = this.attachmentDAO.getAttachmentEntityById((String)attachmentId);
				FileEntity fileEntity = new FileEntity();
				fileEntity.setFileName(attachmentEntity.getAttachmentName());
				fileEntity.setPath(attachmentEntity.getAttachmentPath());
				fileList.add(fileEntity);
			}
		}
		
		if (fileList.size() > 1) {
			return this.getFileToZipPath(fileList, true);
		} else{
			return this.getFileToZipPath(fileList, false);
		}
	}
	
	/**
	 * 鑾峰彇涓嬭浇鏂囦欢璺緞
	 * @param files
	 * @param isZipable
	 * @return
	 * @throws IOException 
	 */
	private String getFileToZipPath(List<FileEntity> files, Boolean isZipable) throws IOException{
		FileDownloadEntity fileDownloadEntity = new FileDownloadEntity();
		fileDownloadEntity.setZipable(isZipable);
		fileDownloadEntity.setFiles(files);
		Long zipTime = System.currentTimeMillis();
		String filePath = "";
		
		if(fileDownloadEntity.getIsZipable()){
			String zipName = java.lang.Long.toString(zipTime)+".zip";
			//鍘嬬缉鏂囦欢
			filePath = this.zipFiles(zipName,fileDownloadEntity);
		}else{        
			//涓嶉渶瑕佸帇缂╂椂鎵ц
			filePath = fileDownloadEntity.getFiles().get(0).getPath()+File.separator+fileDownloadEntity.getFiles().get(0).getFileName();
		}
		
		if (StringUtils.isValidateString(filePath)) {
			if (filePath.contains("D:\\")){
				filePath = filePath.replace("D:\\", "").replace("\\", "/");
			} else if (filePath.contains("d:\\")) {
				filePath = filePath.replace("d:\\", "").replace("\\", "/");
			}
		}
		
		return filePath;
	}
	
	/**
	 * 鍘嬬缉鏂囦欢
	 * @param zipName
	 * @param fileDownloadEntity
	 * @return
	 * @throws IOException 
	 */
	public String zipFiles(String zipName,FileDownloadEntity fileDownloadEntity) throws IOException{
		PropertiesUtil propertiesUtil = new PropertiesUtil(AttachmentDAOConst.PROPERTIES_NAME_FILE_DIR_PATH);
		String zipTempPath = propertiesUtil.readPropertiesByName(AttachmentDAOConst.PROPERTIES_KEY_TEMP_ZIP_PATH);
		
		//鐢熸垚涓存椂鏂囦欢鐩綍
		FileUtil.generateDerectory(zipTempPath);
		List<FileEntity> files = fileDownloadEntity.getFiles();
		String resultDistPosition = "";
		for(FileEntity file : files){
			if(fileDownloadEntity.getIsEncrypted())
				resultDistPosition = ZipUtil.zip(file.getPath()+File.separator+file.getFileName(), 
						zipTempPath + File.separator + zipName, fileDownloadEntity.getEncryptedString());
			else
				resultDistPosition = ZipUtil.zip(file.getPath()+File.separator+file.getFileName(), 
						zipTempPath + File.separator + zipName, null);
		}
		
		return resultDistPosition;
	}

}
