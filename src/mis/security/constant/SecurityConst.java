package mis.security.constant;

public class SecurityConst {
	public static final String SQL_GET_POST_LIST = "SELECT * FROM POST";
	public static final String SQL_GET_DEPARTMENT_LIST = "SELECT * FROM DEPARTMENT";
	public static final String SQL_GET_ROLE_LIST = "SELECT * FROM ROLE";
	public static final String SQL_GET_EMPLOYEE_LIST = "SELECT * FROM EMPLOYEE";
	public static final String SQL_DELETE_ROLE_ALL_SECURITY = "DELETE FROM ROLE_SECURITY_RF WHERE ROLE_ID = ?";
	public static final String SQL_GET_ROLE_SECURITY_LIST = "SELECT * FROM SECURITY A, ROLE_SECURITY_RF B WHERE A.SECURITY_ID = B.SECURITY_ID AND B.ROLE_ID = ?";
}
