package ecp.bsp.system.framework.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecp.bsp.system.commons.utils.FileUtil;
import ecp.bsp.system.commons.utils.PropertiesUtil;
import ecp.bsp.system.commons.utils.ZipUtil;
import ecp.bsp.system.framework.file.data.constant.AttachmentDAOConst;
import ecp.bsp.system.framework.file.data.dto.FileDownloadEntity;

public class FileDownload {
	/**
	 * 鍘嬬缉鏂囦欢涓存椂瀛樻斁浣嶇疆
	 */
	private String characterEncoding ;
	
	/**
	 * 鎶婁粠鍓嶅彴浼犺繃鏉ョ殑瀛楃涓蹭互鏂囦欢娴佷笅杞界殑鏂瑰紡r
	 * @param response
	 * @param text
	 */
	public void downloadDirective(HttpServletResponse response,String text){
		  response.reset();
		  // 鎺ユ敹涓枃鍙傛暟涔辩爜澶勭悊
		  response.setCharacterEncoding("utf-8");
		  // 璁剧疆涓轰笅杞絘pplication/x-download
		  response.setContentType("application/x-download");
		  // 涓嬭浇鏂囦欢鏃舵樉绀虹殑鏂囦欢淇濆瓨鍚嶇О
		  response.addHeader("Content-Disposition", "attachment;filename="+ "error.txt");
		  try {
		   OutputStream os = response.getOutputStream();
		   os.write(text.getBytes());
		   os.flush();
		   os.close();
		  } catch (Exception e) {
		  }
	}
	public void downloadFile(HttpServletRequest request,HttpServletResponse response,String displayName,String fileName,boolean isZip) throws IOException{
		response.reset();
		response.setContentType("application/x-download");
		if(isZip){
			response.addHeader("Content-Disposition", "attachment;filename="+ "allFiles.zip");
		}else{
			response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(displayName, "UTF-8"));
//			String filename111 = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
//			response.addHeader("Content-Disposition", "attachment;filename="+ filename111);
		}
        //鍒涘缓file瀵硅薄
        File file=new File(fileName);
        //鍐欐槑瑕佷笅杞界殑鏂囦欢鐨勫ぇ灏�
        response.setContentLength((int)file.length());
        //璇诲嚭鏂囦欢鍒癷/o娴�
        FileInputStream fis  = null;
        BufferedInputStream buff  = null;
        OutputStream myout  = null;
		fis = new FileInputStream(file);
		buff=new BufferedInputStream(fis);
		byte [] b=new byte[1024];//鐩稿綋浜庢垜浠殑缂撳瓨
		long k=0;//璇ュ�鐢ㄤ簬璁＄畻褰撳墠瀹為檯涓嬭浇浜嗗灏戝瓧鑺�
		//浠巖esponse瀵硅薄涓緱鍒拌緭鍑烘祦,鍑嗗涓嬭浇
		myout=response.getOutputStream();
		//寮�寰幆涓嬭浇
		while(k<file.length()){
			int j=buff.read(b,0,1024);
			k+=j;
			//灏哹涓殑鏁版嵁鍐欏埌瀹㈡埛绔殑鍐呭瓨
			myout.write(b,0,j);
			
		}
        //灏嗗啓鍏ュ埌瀹㈡埛绔殑鍐呭瓨鐨勬暟鎹�鍒锋柊鍒扮鐩�
		if(null!=myout){
	        myout.flush();
	        myout.close();
	        fis.close();
	        buff.close();
		}
        //濡傛灉鏈夊帇缂╁氨鎶婂帇缂╂枃浠跺垹闄�
        if(isZip)
        	file.delete();
	}
	/**
	 * 鍘嬬缉鍓嶅彴浼犺繃鏉ョ殑鎵�湁鏂囦欢鍚嶅搴旂殑鏂囦欢
	 * @param fileDownloadEntity
	 * @param dest 鍘嬬缉鍚庣殑鏂囦欢鍚�
	 * @throws IOException 
	 */
	public String zipFiles(String dest,FileDownloadEntity fileDownloadEntity) throws IOException{
		PropertiesUtil propertiesUtil = new PropertiesUtil(AttachmentDAOConst.PROPERTIES_NAME_FILE_DIR_PATH);
		String zipTempPath = propertiesUtil.readPropertiesByName(AttachmentDAOConst.PROPERTIES_KEY_TEMP_ZIP_PATH);
		FileUtil.generateDerectory(zipTempPath);
		ZipUtil zipUtil = new ZipUtil();
		String resultDistPosition = zipUtil.zipFiles(zipTempPath + File.separator + dest,fileDownloadEntity.getFiles(),
				fileDownloadEntity.getIsEncrypted(),fileDownloadEntity.getEncryptedString());
		return resultDistPosition;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}
	/**
	 * 鑾峰彇鍙互浼犺緭鍒板墠鍙扮殑涓枃鍚�
	 * @param fileNameSrc
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getChineseTitle(HttpServletRequest request,String fileNameSrc) throws UnsupportedEncodingException{
		
		//IE娴忚鍣�
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") >= 0)
			fileNameSrc = URLEncoder.encode(fileNameSrc, "UTF-8");
		//鍏跺畠娴忚鍣�
		else
			fileNameSrc = new String(fileNameSrc.getBytes("UTF-8"), "ISO8859-1");
		/*String fileName = null;
		if(fileNameSrc.length()>150)//瑙ｅ喅IE 6.0 bug
			fileName   =new String(fileNameSrc.getBytes("GB2312"),"ISO-8859-1");*/
		 return fileNameSrc;
	}
	
}
