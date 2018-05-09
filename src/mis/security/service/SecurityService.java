package mis.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import mis.security.dao.SecurityDAO;
import mis.security.dto.DepartmentDTO;
import mis.security.dto.EmployeeDTO;
import mis.security.dto.LoginDTO;
import mis.security.dto.PostDTO;
import mis.security.dto.RoleDTO;
import mis.security.dto.SecurityDTO;
import mis.security.entity.DepartmentEntity;
import mis.security.entity.EmployeeEntity;
import mis.security.entity.EmployeeRoleRfEntity;
import mis.security.entity.PostEntity;
import mis.security.entity.RoleEntity;
import mis.security.entity.RoleSecurityRfEntity;
import ecp.bsp.system.commons.constant.ExceptionCodeConst;
import ecp.bsp.system.commons.dto.ActionResult;
import ecp.bsp.system.commons.utils.LoggerUtil;
import ecp.bsp.system.commons.utils.StringUtils;
import ecp.bsp.system.core.BaseService;

/**
 * 权限管理服务
 * 
 * @author Huanglinhui(Rombo)
 *
 */
@Service
public class SecurityService extends BaseService {
	@Resource
	private SecurityDAO securityDAO;
	
	/**
	 * 用户登陆
	 * @param inLoginDTO
	 *        用户登陆信息
	 *        
	 * @return 返回用户登陆状态
	 */
	public ActionResult login(LoginDTO inLoginDTO) {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		// 将密码转化为md5码
		String tmpPasswordMd5 = "";
		// 用户是否存在
		EmployeeEntity tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "login", inLoginDTO.getLogin());
		if (tmpEmployeeEntity == null) {
			tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("'" + inLoginDTO.getLogin() + "'用户账号不存在");
			return tmpActionResult;
		}
		
		// 密码是否正确
		tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, new String[] {"login", "password"}, new Object[] {inLoginDTO.getLogin(), tmpPasswordMd5});
		if (tmpEmployeeEntity == null) {
			tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("'" + inLoginDTO.getLogin() + "'用户的密码错误");
			return tmpActionResult;
		}

		return tmpActionResult;
	}
	
	/**
	 * 插入用户信息
	 * @param inEmployeeDTO
	 *     用户信息
	 * @return
	 *     返回用户信息插入情况
	 *     
	 * @throws Exception
	 */
	public ActionResult insertEmployee(EmployeeDTO inEmployeeDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		// 账号不能为空也不能重复
		EmployeeEntity tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "login", inEmployeeDTO.getLogin());
		if (tmpEmployeeEntity == null) {
			tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("'" + inEmployeeDTO.getLogin() + "'用户账号已经存在");
			return tmpActionResult;
		}
		
		// 身份证可以为空，如果身份证不为空则不能重复
		String tmpIdentifyCardNumber = inEmployeeDTO.getIdentifyCardNumber();
		if (StringUtils.isValidateString(tmpIdentifyCardNumber)) {
			tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "identifyCardNumber", inEmployeeDTO.getIdentifyCardNumber());
			if (tmpEmployeeEntity != null) {
				tmpActionResult.setIsSuccessful(false);
				tmpActionResult.setActionResultMessage("'新增的用户身份证号'" + inEmployeeDTO.getIdentifyCardNumber() + "'和已有用户'" 
				    + tmpEmployeeEntity.getEmployeeName() + "(" + tmpEmployeeEntity.getLogin() + ")" + "'发生冲突.");
				return tmpActionResult;
			}
		}
		
		EmployeeEntity tmpNewEmployeeEntity = new EmployeeEntity();
		EmployeeDTO.dtoToEntity(inEmployeeDTO, tmpNewEmployeeEntity);
		this.securityDAO.insert(tmpNewEmployeeEntity);
		
		EmployeeRoleRfEntity tmpEmployeeRoleRfEntity = null;
		List<EmployeeRoleRfEntity> tmpEmployeeRoleRfEntityList = new ArrayList<EmployeeRoleRfEntity>();
		List<String> tmpRoleIdList = inEmployeeDTO.getRoleIdList();
		for (String tmpSubRoleId: tmpRoleIdList) {
			if (StringUtils.isValidateString(tmpSubRoleId)) {
				String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "未能获取用户权限数据，联系技术支持人员处理.";
				LoggerUtil.instance(this.getClass()).error(exceptionMessage);
				throw new RuntimeException(exceptionMessage);
			}
			
			tmpEmployeeRoleRfEntity = new EmployeeRoleRfEntity();
			tmpEmployeeRoleRfEntity.setEmployeeId(tmpNewEmployeeEntity.getEmployeeId());
			tmpEmployeeRoleRfEntity.setRoleId(tmpSubRoleId);
		}
		
		this.securityDAO.deleteEmployeeRoleByEmployeeId(tmpNewEmployeeEntity.getEmployeeId());
		this.securityDAO.insert(tmpEmployeeRoleRfEntityList);
		
		return tmpActionResult;
	}
	
	/**
	 * 更新用户信息
	 * 
	 * @param inEmployeeDTO
	 *     用户信息
	 *     
	 * @return
	 *     返回用户信息更新情况
	 *     
	 * @throws Exception
	 */
	public ActionResult updateEmployee(EmployeeDTO inEmployeeDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		EmployeeEntity tmpOtherEmployeeEntityOne = this.securityDAO.getEntity(EmployeeEntity.class, "identifyCardNumber", inEmployeeDTO.getIdentifyCardNumber());
		EmployeeEntity tmpOtherEmployeeEntityTwo = this.securityDAO.getEntity(EmployeeEntity.class, "login", inEmployeeDTO.getLogin());
		
		// 身份证可以为空，如果身份证不为空则不能重复
		String tmpIdentifyCardNumber = inEmployeeDTO.getIdentifyCardNumber();
		if (StringUtils.isValidateString(tmpIdentifyCardNumber)) {
			if (inEmployeeDTO.getEmployeeId() != tmpOtherEmployeeEntityOne.getId() && tmpIdentifyCardNumber == tmpOtherEmployeeEntityOne.getIdentifyCardNumber()) {
				tmpActionResult.setIsSuccessful(false);
				tmpActionResult.setActionResultMessage("当前修改的用户身份证号码与其他用户身份证号码发生冲突");
			}
		}

		if (inEmployeeDTO.getEmployeeId() != tmpOtherEmployeeEntityTwo.getId() && inEmployeeDTO.getLogin() == tmpOtherEmployeeEntityTwo.getLogin()) {
			tmpActionResult.setIsSuccessful(false);			
			if (StringUtils.isValidateString(tmpActionResult.getActionResultMessage())) {
				tmpActionResult.setActionResultMessage(tmpActionResult.getActionResultMessage() + "\n\r");
			}
			
			tmpActionResult.setActionResultMessage(tmpActionResult.getActionResultMessage() + "当前修改的用户账号与其他用户账号发生冲突");
		}
		
		EmployeeEntity tmpCuttentEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "employeeId", inEmployeeDTO.getEmployeeId());
		if (tmpCuttentEmployeeEntity != null) {
			EmployeeDTO.dtoToEntity(inEmployeeDTO, tmpCuttentEmployeeEntity);
			this.securityDAO.update(tmpCuttentEmployeeEntity);
			
			EmployeeRoleRfEntity tmpEmployeeRoleRfEntity = null;
			List<EmployeeRoleRfEntity> tmpEmployeeRoleRfEntityList = new ArrayList<EmployeeRoleRfEntity>();
			List<String> tmpRoleIdList = inEmployeeDTO.getRoleIdList();
			for (String tmpSubRoleId: tmpRoleIdList) {
				if (StringUtils.isValidateString(tmpSubRoleId)) {
					String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "未能获取用户权限数据，联系技术支持人员处理.";
					LoggerUtil.instance(this.getClass()).error(exceptionMessage);
					throw new RuntimeException(exceptionMessage);
				}
				
				tmpEmployeeRoleRfEntity = new EmployeeRoleRfEntity();
				tmpEmployeeRoleRfEntity.setEmployeeId(tmpCuttentEmployeeEntity.getEmployeeId());
				tmpEmployeeRoleRfEntity.setRoleId(tmpSubRoleId);
			}
			
			this.securityDAO.deleteEmployeeRoleByEmployeeId(tmpCuttentEmployeeEntity.getEmployeeId());
			this.securityDAO.insert(tmpEmployeeRoleRfEntityList);
		}
		
		return tmpActionResult;
	}
	
	/**
	 * 删除用户信息
	 * 
	 * @param inEmployeeId
	 *     用户ID
	 *     
	 * @return
	 *     返回用户信息删除情况
	 *     
	 * @throws Exception
	 */
	public ActionResult deleteEmployee(String inEmployeeId) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		EmployeeEntity tmpCuttentEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "employeeId", inEmployeeId);
		if (tmpCuttentEmployeeEntity == null) {
			tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("没有用户可删除");
			return tmpActionResult;
		}
		
		this.securityDAO.deleteEmployeeRoleByEmployeeId(tmpCuttentEmployeeEntity.getEmployeeId());
		this.securityDAO.delete(tmpCuttentEmployeeEntity);
		
		return tmpActionResult;
	}
	
	/**
	 * 获取用户列表
	 * 
	 * @return
	 *     用户列表
	 */
	public List<EmployeeDTO> getEmployeeList() {
		return this.securityDAO.getEmployeeList();
	}
	
	/**
	 * 插入部门信息
	 * 
	 * @param inDepartmentDTO
	 *     部门信息
	 *     
	 * @return
	 *     返回部门信息插入情况
	 *     
	 * @throws Exception
	 */
	public ActionResult insertDepartment(DepartmentDTO inDepartmentDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		DepartmentEntity tmpExistDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentName", inDepartmentDTO.getDepartmentName());

        if (inDepartmentDTO.getDepartmentId() != tmpExistDepartmentEntity.getId() && inDepartmentDTO.getDepartmentName() == tmpExistDepartmentEntity.getDepartmentName()) {
        	tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("当前新建的部门名称与其它部门名称相同，发生冲突");
		}
        
        DepartmentEntity tmpNewDepartmentEntity = new DepartmentEntity();
        DepartmentDTO.dtoToEntity(inDepartmentDTO, tmpNewDepartmentEntity);
		this.securityDAO.insert(tmpNewDepartmentEntity);
		
		return tmpActionResult;
	}
	
	/**
	 * 更新部门信息
	 * 
	 * @param inDepartmentDTO
	 *     部门信息
	 *     
	 * @return
	 *     返回部门信息更新情况
	 *     
	 * @throws Exception
	 */
	public ActionResult updateDepartment(DepartmentDTO inDepartmentDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		DepartmentEntity tmpExistDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentName", inDepartmentDTO.getDepartmentName());

        if (inDepartmentDTO.getDepartmentId() != tmpExistDepartmentEntity.getId() && inDepartmentDTO.getDepartmentName() == tmpExistDepartmentEntity.getDepartmentName()) {
        	tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("当前修改的部门名称与其它部门名称相同，发生冲突");
		}
		
        DepartmentEntity tmpCuttentDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentId", inDepartmentDTO.getDepartmentId());
		if (tmpCuttentDepartmentEntity != null) {
			DepartmentDTO.dtoToEntity(inDepartmentDTO, tmpCuttentDepartmentEntity);
			this.securityDAO.update(tmpCuttentDepartmentEntity);
		}
		
		return tmpActionResult;
	}
	
	/**
	 * 删除部门信息
	 * 
	 * @param inDepartmentId
	 *     部门ID
	 *     
	 * @return
	 *     返回部门信息删除情况
	 *     
	 * @throws Exception
	 */
	public ActionResult deleteDepartment(String inDepartmentId) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		DepartmentEntity tmpDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentId", inDepartmentId);
		if (tmpDepartmentEntity == null) {
			tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("没有部门可删除");
			return tmpActionResult;
		}
		
		this.securityDAO.delete(tmpDepartmentEntity);
		
		return tmpActionResult;
	} 
	
	/**
	 * 获取部门信息列表
	 * 
	 * @return
	 *     返回部门信息列表
	 */
	public List<DepartmentDTO> getDepartmentList() {
		return this.securityDAO.getDepartmentList();
	}
	
	/**
	 * 插入角色信息
	 * 
	 * @param inRoleDTO
	 *     角色信息
	 *     
	 * @return
	 *     返回角色信息插入情况
	 *     
	 * @throws Exception
	 */
	public ActionResult insertRole(RoleDTO inRoleDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		RoleEntity tmpExistRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleName", inRoleDTO.getRoleName());

        if (inRoleDTO.getRoleId() != tmpExistRoleEntity.getId() && inRoleDTO.getRoleName() == tmpExistRoleEntity.getRoleName()) {
        	tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("当前新建的角色名称与其它角色名称相同，发生冲突");
		}
        
        RoleEntity tmpNewRoleEntity = new RoleEntity();
        RoleDTO.dtoToEntity(inRoleDTO, tmpNewRoleEntity);
		this.securityDAO.insert(tmpNewRoleEntity);
		
		return tmpActionResult;
	}
	
	/**
	 * 更新角色信息
	 * 
	 * @param inRoleDTO
	 *     角色信息
	 *     
	 * @return
	 *     返回角色信息
	 *     
	 * @throws Exception
	 */
	public ActionResult updateRole(RoleDTO inRoleDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		RoleEntity tmpExistRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleName", inRoleDTO.getRoleName());

        if (inRoleDTO.getRoleId() != tmpExistRoleEntity.getId() && inRoleDTO.getRoleName() == tmpExistRoleEntity.getRoleName()) {
        	tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("当前修改的角色名称与其它角色名称相同，发生冲突");
		}
		
        RoleEntity tmpCuttentRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleId", inRoleDTO.getRoleId());
		if (tmpCuttentRoleEntity != null) {
			RoleDTO.dtoToEntity(inRoleDTO, tmpCuttentRoleEntity);
			this.securityDAO.update(tmpCuttentRoleEntity);
		}
		
		return tmpActionResult;
	}
	
	/**
	 * 删除角色信息
	 * 
	 * @param inRoleId
	 *     角色ID
	 *     
	 * @return
	 *     返回删除角色信息情况
	 *     
	 * @throws Exception
	 */
	public ActionResult deleteRole(String inRoleId) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		RoleEntity tmpRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleId", inRoleId);
		if (tmpRoleEntity == null) {
			tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("没有角色可删除");
			return tmpActionResult;
		}
		
		this.securityDAO.delete(tmpRoleEntity);
		
		return tmpActionResult;
	} 
	
	/**
	 * 获取角色列表
	 * 
	 * @return
	 *     返回角色列表
	 */
	public List<RoleDTO> getRoleList() {
		return this.securityDAO.getRoleList();
	}
	
	/**
	 * 插入职务信息
	 * 
	 * @param inPostDTO
	 *     职务信息
	 *     
	 * @return
	 *     返回插入职务信息情况
	 *     
	 * @throws Exception
	 */
	public ActionResult insertPost(PostDTO inPostDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		PostEntity tmpExistPostEntity = this.securityDAO.getEntity(PostEntity.class, "postName", inPostDTO.getPostName());

        if (inPostDTO.getPostId() != tmpExistPostEntity.getId() && inPostDTO.getPostName() == tmpExistPostEntity.getPostName()) {
        	tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("当前新建的岗位名称与其它岗位名称相同，发生冲突");
		}
        
        PostEntity tmpNewPostEntity = new PostEntity();
        PostDTO.dtoToEntity(inPostDTO, tmpNewPostEntity);
		this.securityDAO.insert(tmpNewPostEntity);
		
		return tmpActionResult;
	}
	
	/**
	 * 更新职务信息
	 * 
	 * @param inPostDTO
	 *     职务信息
	 *     
	 * @return
	 *     返回更新职务信息情况
	 *     
	 * @throws Exception
	 */
	public ActionResult updatePost(PostDTO inPostDTO) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		
		PostEntity tmpExistPostEntity = this.securityDAO.getEntity(PostEntity.class, "postName", inPostDTO.getPostName());

        if (inPostDTO.getPostId() != tmpExistPostEntity.getId() && inPostDTO.getPostName() == tmpExistPostEntity.getPostName()) {
        	tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("当前修改的岗位名称与其它岗位名称相同，发生冲突");
		}
		
        PostEntity tmpCuttentPostEntity = this.securityDAO.getEntity(PostEntity.class, "postId", inPostDTO.getPostId());
		if (tmpCuttentPostEntity != null) {
			PostDTO.dtoToEntity(inPostDTO, tmpCuttentPostEntity);
			this.securityDAO.update(tmpCuttentPostEntity);
		}
		
		return tmpActionResult;
	}
	
	/**
	 * 删除职务信息
	 * 
	 * @param inPostId
	 *     职务ID
	 *     
	 * @return
	 *     返回删除职务信息情况
	 *     
	 * @throws Exception
	 */
	public ActionResult deletePost(String inPostId) throws Exception {
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		PostEntity tmpPostEntity = this.securityDAO.getEntity(PostEntity.class, "postId", inPostId);
		if (tmpPostEntity == null) {
			tmpActionResult.setIsSuccessful(false);
			tmpActionResult.setActionResultMessage("没有岗位可删除");
			return tmpActionResult;
		}
		
		this.securityDAO.delete(tmpPostEntity);
		
		return tmpActionResult;
	} 
	
	/**
	 * 获取职务信息列表
	 * 
	 * @return
	 *     返回职务信息列表
	 */
	public List<PostDTO> getPostList() {
		return this.securityDAO.getPostList();
	}
	
	/**
	 * 保存角色权限信息
	 * @param inRoleId
	 *     角色ID
	 *     
	 * @param inSecurutyList
	 *     角色的权限列表
	 *     
	 * @return
	 *     返回保存角色权限信息情况
	 *     
	 * @throws Exception
	 */
	public ActionResult SaveRoleSecurity(String inRoleId, List<SecurityDTO> inSecurutyList) throws Exception {
		// 删除角色原来权限
		this.securityDAO.deleteRoleAllSecurity(inRoleId);
		
		ActionResult tmpActionResult = new ActionResult();
		tmpActionResult.setIsSuccessful(true);
		if (inSecurutyList == null || inSecurutyList.isEmpty()) {
			return tmpActionResult;
		}
		
		// 添加当前要求保存的权限
		List<RoleSecurityRfEntity> tmpRoleSecurityRfEntityList = new ArrayList<RoleSecurityRfEntity>();
		RoleSecurityRfEntity tmpNewRoleSecurityRfEntity = null;
		for (SecurityDTO tmpSubSecurityDTO: inSecurutyList) {
			tmpNewRoleSecurityRfEntity = new RoleSecurityRfEntity();
			tmpNewRoleSecurityRfEntity.setRoleId(inRoleId);
			tmpNewRoleSecurityRfEntity.setSecurityId(tmpSubSecurityDTO.getSecurityId());
		}
		
		this.securityDAO.insert(tmpRoleSecurityRfEntityList);
		
		return tmpActionResult;
	}
	
	/**
	 * 获取指定角色ID的权限信息列表
	 * 
	 * @param inRoleId
	 *     角色ID
	 *     
	 * @return
	 *     返回指定角色ID的权限信息列表
	 */
	public List<SecurityDTO> getRoleSecurity(String inRoleId) {
		return this.securityDAO.getRoleSecurity(inRoleId);
	}
	
	/**
	 * 获取指定用户ID的权限列表
	 * 
	 * @param inEmployeeId
	 *     用户ID
	 *     
	 * @return
	 *     指定用户ID的权限列表
	 */
	public List<SecurityDTO> getEmployeeSecurity(String inEmployeeId) {
		return this.securityDAO.getRoleSecurityByEmployeeId(inEmployeeId);
	}
}
