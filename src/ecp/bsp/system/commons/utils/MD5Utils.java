package ecp.bsp.system.commons.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* MD5���ܴ��?����
* @author Administrator
*
*/
public class MD5Utils {
	/**
	* Ĭ�ϵ������ַ���ϣ��������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�,apacheУ�����ص��ļ�����ȷ���õľ���Ĭ�ϵ�������
	*/
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	protected static MessageDigest messagedigest = null;
	
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MD5Utils.class.getName() + "��ʼ��ʧ�ܣ�MessageDigest��֧��MD5Util��");
			nsaex.printStackTrace();
		}
	}
	
	/**
	* ����ַ��md5У��ֵ
	*
	* @param s
	* @return
	*/
	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}
	
	/**
	* �ж��ַ��md5У�����Ƿ���һ����֪��md5����ƥ��
	*
	* @param password
	* ҪУ����ַ�
	* @param md5PwdStr
	* ��֪��md5У����
	* @return
	*/
	public static boolean isEqualsToMd5(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}
	
	/**
	* ����ļ���md5У��ֵ
	*
	* @param file
	* @return
	* @throws IOException
	*/
	public static String getFileMD5String(File file) throws IOException {
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			messagedigest.update(buffer, 0, numRead);
		}
		fis.close();
		
		return bufferToHex(messagedigest.digest());
	}
	
	/**
	* ����ֽ������md5У��ֵ
	*
	* @param s
	* @return
	*/
	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}
	
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);	
	}
	
	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		
		return stringbuffer.toString();
	}
	
	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// ȡ�ֽ��и� 4 λ������ת��, >>>
		// Ϊ�߼����ƣ������λһ������,�˴�δ�������ַ���кβ�ͬ
		char c1 = hexDigits[bt & 0xf];// ȡ�ֽ��е� 4 λ������ת��
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
	
	/**
	* ��Դ�ַ�ʹ��MD5����Ϊ�ֽ�����
	* @param source
	* @return
	*/
	public static byte[] encode2bytes(String source) {
		byte[] result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(source.getBytes("UTF-8"));
			result = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	* ��Դ�ַ�ʹ��MD5����Ϊ32λ16������
	* @param source
	* @return
	*/
	public static String encode2hex(String source) {
		byte[] data = encode2bytes(source);
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0xff & data[i]);
			if (hex.length() == 1) 
				hexString.append('0');
			hexString.append(hex);
		}
		
		return hexString.toString();
	}
	
	/**
	* ��֤�ַ��Ƿ�ƥ��
	* @param unknown ����֤���ַ�
	* @param okHex ʹ��MD5���ܹ��16�����ַ�
	* @return ƥ�䷵��true����ƥ�䷵��false
	*/
	public static boolean validate(String unknown , String okHex) {
		return okHex.equals(encode2hex(unknown));
	}
}