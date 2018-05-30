package mis.shortmessage.constant;

public class ShortMessageConst {
	public static final String PROPERTIES_NAME_FILE_DIR_PATH = "shortMessage.properties";
	public static final String PROPERTIES_KEY_SEND_URL = "sendUrl";
	public static final String PROPERTIES_KEY_CALLER_ID = "callerId";
	public static final String PROPERTIES_KEY_PASSOWRD = "password";
	public static final String PROPERTIES_KEY_TEMPLATE_ID = "templateId";
	
	public static final String SQL_SET_MESSAGE_SEND_CENTER_NULL_BY_MEETING_ID = 
			"UPDATE MESSAGE_SEND_CENTER SET MEETING_ID = NULL WHERE MEETING_ID = ?";
	
	public static final String SQL_SET_MESSAGE_SEND_CENTER_DISACTIVE_BY_MEETING_ID = 
			"UPDATE MESSAGE_SEND_CENTER SET IS_ACTIVE = 0 WHERE MEETING_ID = ?";

	public static final String SQL_DELETE_MESSAGE_SEND_CENTER_WAITING_MESSAGE_BY_MEETING_ID = 
			"DELETE FROM MESSAGE_SEND_CENTER WHERE MESSAGE_SEND_CENTER_ID NOT IN (" +
			"  SELECT B.MESSAGE_SEND_CENTER_ID FROM MESSAGE_SEND_CENTER A, SHORT_MESSAGE_SEND_LOG B " +
			"  WHERE A.MESSAGE_SEND_CENTER_ID = B.MESSAGE_SEND_CENTER_ID AND A.MEETING_ID = ?" +
			"  ) " +
			"AND MEETING_ID = ?";

	public static final String SQL_GET_SHORT_MESSAGE_CENTER_INFO = "SELECT * FROM SHORT_MESSAGE_CENTER";

}
