package mis.meeting.constant;

public class MeetingConstant {
	public static final String SQL_GET_MEETING_ROOM_LIST = "SELECT *, MEETING_ROOM_ID AS ID,concat(MEETING_ROOM_NAME,'—',MEETING_ROOM_ADDRESS) AS TEXT FROM MEETING_ROOM";
	
	public static final String SQL_GET_MEETING_BY_PAN_DATETIME_RANG = "SELECT * FROM MEETING A WHERE A.MEETING_ROOM_ID = ? "
			+ "AND NOT ((A.MEETING_END_TIME < ?) OR (A.MEETING_START_TIME > ?))";
	
	public static final String SQL_GET_MEETING_BY_DATETIME_RANG_WITHOUT_MEETING_ID = "SELECT * FROM MEETING A WHERE A.MEETING_ROOM_ID = ? "
			+ "AND A.MEETING_ID != ? "
			+ "AND NOT ((A.MEETING_END_TIME < ?) OR (A.MEETING_START_TIME > ?))";
	
	public static final String SQL_UPDATE_MEETING_STATE = "UPDATE MEETING A SET A.MEET_STATE_ID = ? WHERE A.MEETING_ID = ?";

	public static final String SQL_DELETE_MEETING_MEMBER_RF = "DELETE FROM MEETING_MEMBER_RF WHERE MEETING_ID = ?";
	
	public static final String SQL_GET_EMPLOYEE_PHONE_INFO_BY_MEETING_ID = "SELECT GROUP_CONCAT(DISTINCT(B.TELEPHONE)) AS TELEPHONE FROM MEETING_MEMBER_RF A,EMPLOYEE B WHERE A.EMPLOYEE_ID = B.EMPLOYEE_ID AND A.MEETING_ID = ?";
	
	public static final String SQL_GET_MEETING_MEMBER_RF_LIST = new StringBuilder("")
	.append(" SELECT A.*,B.EMPLOYEE_NAME,C.DEPARTMENT_NAME ")
	.append(" FROM MEETING_MEMBER_RF A,EMPLOYEE B,DEPARTMENT C ") 
	.append(" WHERE A.EMPLOYEE_ID = B.EMPLOYEE_ID ")
	.append(" AND B.DEPARTMENT_ID = C.DEPARTMENT_ID ")
	.append(" AND A.MEETING_ID = ? ")
	.toString();

	public static final String SQL_GET_MEETING_ROOM_BOOKING_LIST_BY_ROOM_ID = new StringBuilder("")
	.append(" SELECT A.MEETING_ROOM_ID,A.MEETING_ROOM_ADDRESS, ")
	.append(" B.MEETING_ID,B.MEETING_SUBJECT,B.MEETING_START_TIME,B.MEETING_END_TIME, ")
	.append(" (SELECT E.EMPLOYEE_NAME FROM MEETING_MEMBER_RF D,EMPLOYEE E ")
	.append(" WHERE D.EMPLOYEE_ID = E.EMPLOYEE_ID ")   
	.append(" AND D.MEETING_ID = B.MEETING_ID ")     
	.append(" AND D.MEETING_MEMBER_ROLE_ID = '0') AS EMPLOYEE_NAME , ")
	.append(" (SELECT F.DEPARTMENT_NAME FROM MEETING_MEMBER_RF D,EMPLOYEE E,DEPARTMENT F ")      
	.append(" WHERE D.EMPLOYEE_ID = E.EMPLOYEE_ID ")     
	.append(" AND D.MEETING_ID = B.MEETING_ID ")     
	.append(" AND E.DEPARTMENT_ID = F.DEPARTMENT_ID ")     
	.append(" AND D.MEETING_MEMBER_ROLE_ID = '0') AS DEPARTMENT_NAME ")    
	.append(" FROM MEETING_ROOM A, MEETING B ")
	.append(" WHERE A.MEETING_ROOM_ID = B.MEETING_ROOM_ID ") 
	.append(" AND A.MEETING_ROOM_ID = ? ")
	.toString();
	
	public static final String SQL_GET_MEETING_ATTACHMENT_LIST_BY_CONDITION = new StringBuilder("")
	.append(" SELECT B.*,C.ATTACHMENT_ID,C.ATTACHMENT_NAME,C.ATTACHMENT_PATH,D.EMPLOYEE_ID,D.EMPLOYEE_NAME,E.DEPARTMENT_ID,E.DEPARTMENT_NAME,F.MEETING_STATE_NAME ") 
	.append(" FROM MEETING_ATTACHMENT A,MEETING B,ATTACHMENT C,EMPLOYEE D,DEPARTMENT E, MEETING_STATE F ")
	.append(" WHERE A.MEETING_ID = B.MEETING_ID ")
	.append(" AND A.ATTACHMENT_ID = C.ATTACHMENT_ID ")
	.append(" AND C.EMPLOYEE_ID = D.EMPLOYEE_ID ")
	.append(" AND D.DEPARTMENT_ID = E.DEPARTMENT_ID ")
	.append(" AND B.MEETING_STATE_ID = F.MEETING_STATE_ID ")
	.toString();
	
	public static final String SQL_GET_MEETING_ATTACHMENT_LIST_BY_MEETING_ID = new StringBuilder("")
	.append(" SELECT B.*,C.EMPLOYEE_NAME,D.DEPARTMENT_NAME ")
	.append(" FROM MEETING_ATTACHMENT A,ATTACHMENT B,EMPLOYEE C,DEPARTMENT D ")
	.append(" WHERE A.ATTACHMENT_ID = B.ATTACHMENT_ID ")
	.append(" AND B.EMPLOYEE_ID = C.EMPLOYEE_ID ")
	.append(" AND C.DEPARTMENT_ID = D.DEPARTMENT_ID ")
	.append(" AND A.MEETING_ID = ? ")
	.append(" AND A.ATTACHMENT_CATEGORY_ID = ? ")
	.toString();
	
	public static final String SQL_GET_MEETING_LIST_BY_CONDITION = new StringBuilder("")
	.append(" SELECT A.*,B.MEETING_ROOM_NAME,C.MEETING_STATE_NAME, ")
	.append(" (SELECT E.EMPLOYEE_ID FROM MEETING_MEMBER_RF D,EMPLOYEE E ")
	.append(" WHERE D.EMPLOYEE_ID = E.EMPLOYEE_ID ")
	.append(" AND D.MEETING_ID = A.MEETING_ID ") 
	.append(" AND D.MEETING_MEMBER_ROLE_ID = '0') AS MEETING_CREATOR, ")
	.append(" (SELECT E.EMPLOYEE_NAME FROM MEETING_MEMBER_RF D,EMPLOYEE E ") 
	.append(" WHERE D.EMPLOYEE_ID = E.EMPLOYEE_ID ")
	.append(" AND D.MEETING_ID = A.MEETING_ID ")
	.append(" AND D.MEETING_MEMBER_ROLE_ID = '0') AS MEETING_CREATOR_NAME, ")
	.append(" (SELECT F.DEPARTMENT_NAME FROM MEETING_MEMBER_RF D,EMPLOYEE E,DEPARTMENT F ") 
	.append(" WHERE D.EMPLOYEE_ID = E.EMPLOYEE_ID ")
	.append(" AND D.MEETING_ID = A.MEETING_ID ")
	.append(" AND E.DEPARTMENT_ID = F.DEPARTMENT_ID ")
	.append(" AND D.MEETING_MEMBER_ROLE_ID = '0') AS MEETING_CREATOR_DEPARTMENT_NAME, ")
	.append(" (SELECT F.DEPARTMENT_ID FROM MEETING_MEMBER_RF D,EMPLOYEE E,DEPARTMENT F ")
	.append(" WHERE D.EMPLOYEE_ID = E.EMPLOYEE_ID ")
	.append(" AND D.MEETING_ID = A.MEETING_ID ")
	.append(" AND E.DEPARTMENT_ID = F.DEPARTMENT_ID ")
	.append(" AND D.MEETING_MEMBER_ROLE_ID = '0') AS MEETING_CREATOR_DEPARTMENT_ID ")
	.append(" FROM MEETING A, MEETING_ROOM B, MEETING_STATE C ")
	.append(" WHERE A.MEETING_ROOM_ID = B.MEETING_ROOM_ID ")
	.append(" AND A.MEETING_STATE_ID = C.MEETING_STATE_ID ")
	.toString();
	
	public static final String SQL_GET_MEETING_INFO_BY_MEETING_ID = new StringBuilder("SELECT * FROM (")
	.append(SQL_GET_MEETING_LIST_BY_CONDITION)
	.append(") A WHERE A.MEETING_ID = ?")
	.toString();

	public static final String SQL_GET_CREATOR_MEETING_INFO_BY_EMPLOYEE_ID = new StringBuilder("SELECT * FROM (")
	.append(SQL_GET_MEETING_LIST_BY_CONDITION)
	.append(") A WHERE A.MEETING_CREATOR = ?")
	.toString();
	
	public static final String SQL_GET_PARTICIPANT_MEETING_INFO_BY_EMPLOYEE_ID = new StringBuilder("SELECT DISTINCT A.* FROM (")
	.append(SQL_GET_MEETING_LIST_BY_CONDITION)
	.append(") A, MEETING_MEMBER_RF B ")
	.append(" WHERE A.MEETING_ID = B.MEETING_ID ")
	.append(" AND B.EMPLOYEE_ID = ? ")
	.toString();
}
