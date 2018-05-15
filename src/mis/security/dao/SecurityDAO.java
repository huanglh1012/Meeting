package mis.security.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import mis.security.constant.SecurityConst;
import mis.security.dto.DepartmentDTO;
import mis.security.dto.EmployeeDTO;
import mis.security.dto.PostDTO;
import mis.security.dto.RoleDTO;
import mis.security.dto.SecurityDTO;
import mis.security.dto.ZtreeNode;

import ecp.bsp.system.core.BaseDAO;

/**
 * 权限管理持久化
 * 
 * @author Huanglinhui(Rombo)
 *
 */
@Repository
public class SecurityDAO extends BaseDAO {
	
	@SuppressWarnings("unchecked")
	public List<PostDTO> getPostList() {
		return (List<PostDTO>) this.query(SecurityConst.SQL_GET_POST_LIST, PostDTO.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<DepartmentDTO> getDepartmentList() {
		return (List<DepartmentDTO>) this.query(SecurityConst.SQL_GET_DEPARTMENT_LIST, DepartmentDTO.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ZtreeNode> getDepartmentEmployeeTreeList() {
		return (List<ZtreeNode>) this.query(SecurityConst.SQL_GET_DEPARTMENT_EMPLOYEE_LIST, ZtreeNode.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<DepartmentDTO> getDepartmentChildrenList(String inParentDepartmentId) {
		return (List<DepartmentDTO>) this.query(SecurityConst.SQL_GET_DEPARTMENT_CHILDREN_LIST_BY_ID, new Object[] { inParentDepartmentId }, DepartmentDTO.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleDTO> getRoleList() {
		return (List<RoleDTO>) this.query(SecurityConst.SQL_GET_ROLE_LIST, RoleDTO.class);
	}

	@SuppressWarnings("unchecked")
	public List<SecurityDTO> getSecurityList() {
		return (List<SecurityDTO>) this.query(SecurityConst.SQL_GET_SECURITY_LIST, SecurityDTO.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<EmployeeDTO> getEmployeeList() {
		return (List<EmployeeDTO>) this.query(SecurityConst.SQL_GET_EMPLOYEE_LIST, EmployeeDTO.class);
	}

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @param inEmployeeId
	 * @return
	 * 		用户信息
	 */
	@SuppressWarnings("unchecked")
	public EmployeeDTO getEmployeeInfoById(String inEmployeeId) {
		List<EmployeeDTO> tmpEmployeeDTOList = (List<EmployeeDTO>) this.query(SecurityConst.SQL_GET_EMPLOYEE_INFO_BY_EMPLOYEE_ID,
				new Object[] { inEmployeeId }, EmployeeDTO.class);
		return tmpEmployeeDTOList.size() > 0 ? tmpEmployeeDTOList.get(0) : null;
	}
	
	public void deleteRoleAllSecurity(String inRoleId) throws Exception {		
		this.excuteSQL(SecurityConst.SQL_DELETE_ROLE_ALL_SECURITY, new Object[] {inRoleId});
	}

	@SuppressWarnings("unchecked")
	public List<SecurityDTO> getRoleSecurity(String inRoleId) {
		return (List<SecurityDTO>) this.query(SecurityConst.SQL_GET_ROLE_SECURITY_LIST, new Object[] { inRoleId },SecurityDTO.class);
	}

	public void deleteEmployeeRoleByEmployeeId(String employeeId) throws Exception {
		this.excuteSQL(SecurityConst.SQL_DELETE_EMPLOYEE_ROLE_BY_EMPLOYEE_ID, new Object[] { employeeId });
	}

	@SuppressWarnings("unchecked")
	public List<SecurityDTO> getRoleSecurityByEmployeeId(String inEmployeeId) {
		return (List<SecurityDTO>) this.query(SecurityConst.SQL_GET_ROLE_SECURITY_LIST_BY_EMPLOEE_ID, new Object[] { inEmployeeId }, SecurityDTO.class);
	}
}
