package mis.security.constant;

public class SecurityConst {
	public static final String SQL_GET_POST_LIST = "SELECT *,POST_ID AS ID,POST_NAME AS TEXT FROM POST";
	public static final String SQL_GET_DEPARTMENT_LIST = "SELECT *,DEPARTMENT_ID AS ID,DEPARTMENT_NAME AS TEXT FROM DEPARTMENT";
	public static final String SQL_GET_DEPARTMENT_CHILDREN_LIST_BY_ID = "SELECT *,DEPARTMENT_ID AS ID,DEPARTMENT_NAME AS TEXT FROM DEPARTMENT WHERE PARENT_DEPARTMENT_ID = ?";
	public static final String SQL_GET_ROLE_LIST = "SELECT *,ROLE_ID AS ID,ROLE_NAME AS TEXT FROM ROLE";
	public static final String SQL_GET_SECURITY_LIST = "SELECT * FROM SECURITY";
	public static final String SQL_GET_EMPLOYEE_LIST = "SELECT * FROM EMPLOYEE";
	public static final String SQL_DELETE_ROLE_ALL_SECURITY = "DELETE FROM ROLE_SECURITY_RF WHERE ROLE_ID = ?";
	public static final String SQL_GET_ROLE_SECURITY_LIST = "SELECT A.*,B.ROLE_ID FROM SECURITY A, ROLE_SECURITY_RF B WHERE A.SECURITY_ID = B.SECURITY_ID AND B.ROLE_ID = ?";
	public static final String SQL_DELETE_EMPLOYEE_ROLE_BY_EMPLOYEE_ID = "DELETE FROM EMPLOYEE_ROLE_RF WHERE EMPLOYEE_ID = ?";
	public static final String SQL_GET_ROLE_SECURITY_LIST_BY_EMPLOEE_ID = "SELECT * FROM SECURITY A WHERE A.SECURITY_ID IN (SELECT B.SECURITY_ID FROM ROLE_SECURITY_RF B, EMPLOYEE_ROLE_RF C WHERE B.ROLE_ID = C.ROLE_ID AND C.EMPLOYEE_ID = ?)";
	public static final String SQL_GET_EMPLOYEE_LIST_BY_CONDITION = new StringBuilder("SELECT A.*,B.DEPARTMENT_NAME,C.POST_NAME,D.SEX_NAME,")
			.append(" (SELECT GROUP_CONCAT(F.ROLE_ID) FROM EMPLOYEE_ROLE_RF E, ROLE F WHERE E.ROLE_ID = F.ROLE_ID AND E.EMPLOYEE_ID = A.EMPLOYEE_ID) AS ROLE_ID, ")
			.append(" (SELECT GROUP_CONCAT(F.ROLE_NAME) FROM EMPLOYEE_ROLE_RF E, ROLE F WHERE E.ROLE_ID = F.ROLE_ID AND E.EMPLOYEE_ID = A.EMPLOYEE_ID) AS ROLE_NAME ")
			.append(" FROM EMPLOYEE A,DEPARTMENT B,POST C,SEX D WHERE A.DEPARTMENT_ID = B.DEPARTMENT_ID AND A.POST_ID = C.POST_ID AND A.SEX_ID = D.SEX_ID ")
			.toString();
}