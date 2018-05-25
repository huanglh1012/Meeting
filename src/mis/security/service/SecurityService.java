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
import mis.security.dto.ZtreeNode;
import mis.security.entity.DepartmentEntity;
import mis.security.entity.EmployeeEntity;
import mis.security.entity.EmployeeRoleRfEntity;
import mis.security.entity.PostEntity;
import mis.security.entity.RoleEntity;
import mis.security.entity.RoleSecurityRfEntity;
import ecp.bsp.system.commons.constant.ExceptionCodeConst;
import ecp.bsp.system.commons.dto.ActionResult;
import ecp.bsp.system.commons.utils.ActionResultUtil;
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
		// 将密码转化为md5码
		String tmpPasswordMd5 = "";
		// 用户是否存在
		EmployeeEntity tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "login", inLoginDTO.getLogin());
		if (tmpEmployeeEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "'" + inLoginDTO.getLogin() + "'用户账号不存在";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		// 密码是否正确
		tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, new String[] {"login", "password"}, new Object[] {inLoginDTO.getLogin(), inLoginDTO.getPassword()});
		if (tmpEmployeeEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "'" + inLoginDTO.getLogin() + "'用户的密码错误";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		return ActionResultUtil.getActionResult(tmpEmployeeEntity, "用户登陆成功");
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
		String exceptionMessage = "";
		// 账号不能为空也不能重复
		EmployeeEntity tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "login", inEmployeeDTO.getLogin());
		if (tmpEmployeeEntity != null) {
			exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "'" + inEmployeeDTO.getLogin() + "'用户账号已经存在";
		}
		
		// 身份证可以为空，如果身份证不为空则不能重复
		String tmpIdentifyCardNumber = inEmployeeDTO.getIdentifyCardNumber();
		if (StringUtils.isValidateString(tmpIdentifyCardNumber)) {
			tmpEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "identifyCardNumber", inEmployeeDTO.getIdentifyCardNumber());
			if (tmpEmployeeEntity != null) {
				if (StringUtils.isValidateString(exceptionMessage)) {
					exceptionMessage += "\n\r";
				}
				
				exceptionMessage += "'新增的用户身份证号'" + inEmployeeDTO.getIdentifyCardNumber() + "'和已有用户'" 
					    + tmpEmployeeEntity.getEmployeeName() + "(" + tmpEmployeeEntity.getLogin() + ")" + "'发生冲突.";
			}
		}
		
		if (StringUtils.isValidateString(exceptionMessage)) {
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		EmployeeEntity tmpNewEmployeeEntity = new EmployeeEntity();
		EmployeeDTO.dtoToEntity(inEmployeeDTO, tmpNewEmployeeEntity);
		this.securityDAO.insert(tmpNewEmployeeEntity);
		
		EmployeeRoleRfEntity tmpEmployeeRoleRfEntity = null;
		List<String> tmpRoleIdList = inEmployeeDTO.getRoleIdList();
		for (String tmpSubRoleId: tmpRoleIdList) {
			if (!StringUtils.isValidateString(tmpSubRoleId)) {
				exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "未能获取用户权限数据，联系技术支持人员处理.";
				LoggerUtil.instance(this.getClass()).error(exceptionMessage);
				throw new RuntimeException(exceptionMessage);
			}
			
			tmpEmployeeRoleRfEntity = new EmployeeRoleRfEntity();
			tmpEmployeeRoleRfEntity.setEmployeeId(tmpNewEmployeeEntity.getId());
			tmpEmployeeRoleRfEntity.setRoleId(tmpSubRoleId);
			this.securityDAO.insert(tmpEmployeeRoleRfEntity);
		}
		
		return ActionResultUtil.getActionResult(tmpNewEmployeeEntity.getId(), "用户新建成功");
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
		EmployeeEntity tmpOtherEmployeeEntityOne = this.securityDAO.getEntity(EmployeeEntity.class, "identifyCardNumber", inEmployeeDTO.getIdentifyCardNumber());
		EmployeeEntity tmpOtherEmployeeEntityTwo = this.securityDAO.getEntity(EmployeeEntity.class, "login", inEmployeeDTO.getLogin());
		
		String exceptionMessage = "";
		if (tmpOtherEmployeeEntityTwo != null) {
			if (!tmpOtherEmployeeEntityTwo.getId().equals(inEmployeeDTO.getEmployeeId()) && tmpOtherEmployeeEntityTwo.getLogin().equals(inEmployeeDTO.getLogin())) {		
				exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前修改的用户账号与其他用户账号发生冲突";
			}
		}
		
		// 身份证可以为空，如果身份证不为空则不能重复
		String tmpIdentifyCardNumber = inEmployeeDTO.getIdentifyCardNumber();
		if (tmpOtherEmployeeEntityOne != null) {
			if (!tmpOtherEmployeeEntityOne.getId().equals(inEmployeeDTO.getEmployeeId()) && tmpOtherEmployeeEntityOne.getIdentifyCardNumber().equals(tmpIdentifyCardNumber)) {
				if (StringUtils.isValidateString(exceptionMessage)) {
					exceptionMessage += "\n\r";
				}
				
				exceptionMessage += "当前修改的用户身份证号码与其他用户身份证号码发生冲突";
			}
		}

		if (StringUtils.isValidateString(exceptionMessage)) {
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		EmployeeEntity tmpCuttentEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "employeeId", inEmployeeDTO.getEmployeeId());
		if (tmpCuttentEmployeeEntity == null) {
			exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有用户可修改";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		EmployeeDTO.dtoToEntity(inEmployeeDTO, tmpCuttentEmployeeEntity);
		this.securityDAO.update(tmpCuttentEmployeeEntity);
		
		this.securityDAO.deleteEmployeeRoleByEmployeeId(tmpCuttentEmployeeEntity.getId());
		
		EmployeeRoleRfEntity tmpEmployeeRoleRfEntity = null;
		List<String> tmpRoleIdList = inEmployeeDTO.getRoleIdList();
		for (String tmpSubRoleId: tmpRoleIdList) {
			if (!StringUtils.isValidateString(tmpSubRoleId)) {
				exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "未能获取用户权限数据，联系技术支持人员处理.";
				LoggerUtil.instance(this.getClass()).error(exceptionMessage);
				throw new RuntimeException(exceptionMessage);
			}
			
			tmpEmployeeRoleRfEntity = new EmployeeRoleRfEntity();
			tmpEmployeeRoleRfEntity.setEmployeeId(tmpCuttentEmployeeEntity.getId());
			tmpEmployeeRoleRfEntity.setRoleId(tmpSubRoleId);
			this.securityDAO.insert(tmpEmployeeRoleRfEntity);
		}
		
		return ActionResultUtil.getActionResult(tmpCuttentEmployeeEntity.getId(), "用户修改成功");
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
		EmployeeEntity tmpCuttentEmployeeEntity = this.securityDAO.getEntity(EmployeeEntity.class, "employeeId", inEmployeeId);
		if (tmpCuttentEmployeeEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有用户可删除";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		this.securityDAO.deleteEmployeeRoleByEmployeeId(tmpCuttentEmployeeEntity.getEmployeeId());
		this.securityDAO.delete(tmpCuttentEmployeeEntity);
		
		return ActionResultUtil.getActionResult(tmpCuttentEmployeeEntity.getId(), "用户删除成功");
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
	 * 根据用户ID获取用户信息
	 * 
	 * @param inEmployeeId
	 * @return
	 * 		用户信息
	 */
	public EmployeeDTO getEmployeeInfoById(String inEmployeeId) {
		return this.securityDAO.getEmployeeInfoById(inEmployeeId);
	}
	
	public EmployeeDTO getEmployeeDetailInfoByEmployeeId(String inEmployeeId) {
		// 获取用户权限列表
		List<SecurityDTO> tmpSecurityDTOList = this.getEmployeeSecurity(inEmployeeId);
		List<String> tmpSecurityCodeList = new ArrayList<String>();
		for (SecurityDTO subSecurityDTO : tmpSecurityDTOList) 
			tmpSecurityCodeList.add(subSecurityDTO.getSecurityCode());
		// 获取用户角色列表
		List<RoleDTO> tmpRoleDTOList = this.securityDAO.getRoleListByEmployeeId(inEmployeeId);
		List<String> tmpRoleIdList = new ArrayList<String>();
		for (RoleDTO subRoleDTO : tmpRoleDTOList) 
			tmpRoleIdList.add(subRoleDTO.getRoleId());
		// 获取用户信息
		EmployeeDTO tmpEmployeeDTO = this.getEmployeeInfoById(inEmployeeId);
		tmpEmployeeDTO.setSecurityCodeList(tmpSecurityCodeList);
		tmpEmployeeDTO.setRoleIdList(tmpRoleIdList);
		
		return tmpEmployeeDTO;
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
		DepartmentEntity tmpExistDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentName", inDepartmentDTO.getDepartmentName());
        if (tmpExistDepartmentEntity != null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前新建的部门名称与其它部门名称相同，发生冲突";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
        
        DepartmentEntity tmpNewDepartmentEntity = new DepartmentEntity();
        DepartmentDTO.dtoToEntity(inDepartmentDTO, tmpNewDepartmentEntity);
		this.securityDAO.insert(tmpNewDepartmentEntity);
		
		return ActionResultUtil.getActionResult(tmpNewDepartmentEntity.getId(), "部门新建成功");
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
		DepartmentEntity tmpExistDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentName", inDepartmentDTO.getDepartmentName());

		if (tmpExistDepartmentEntity != null) {
			if (tmpExistDepartmentEntity.getDepartmentName().equals(inDepartmentDTO.getDepartmentName())) {
				String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前修改的部门名称与其它部门名称相同，发生冲突";
				LoggerUtil.instance(this.getClass()).error(exceptionMessage);
				throw new RuntimeException(exceptionMessage);
			}
		}
		
        DepartmentEntity tmpCuttentDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentId", inDepartmentDTO.getDepartmentId());
		if (tmpCuttentDepartmentEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有部门可修改";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		DepartmentDTO.dtoToEntity(inDepartmentDTO, tmpCuttentDepartmentEntity);
		this.securityDAO.update(tmpCuttentDepartmentEntity);
		
		return ActionResultUtil.getActionResult(tmpCuttentDepartmentEntity.getId(), "部门修改成功");
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
		DepartmentEntity tmpDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "departmentId", inDepartmentId);
		if (tmpDepartmentEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有部门可删除";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		this.securityDAO.delete(tmpDepartmentEntity);
		
		return ActionResultUtil.getActionResult(tmpDepartmentEntity.getId(), "部门删除成功");
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
	 * 获取部门信息列表
	 * 
	 * @return
	 *     返回部门信息列表
	 */
	public List<DepartmentDTO> getDepartmentGroupList() {
		DepartmentEntity tmpDepartmentEntity = this.securityDAO.getEntity(DepartmentEntity.class, "-1");
		DepartmentDTO tmpParentDepartmentDTO = new DepartmentDTO();
		tmpParentDepartmentDTO.setId(tmpDepartmentEntity.getDepartmentId());
		tmpParentDepartmentDTO.setText(tmpDepartmentEntity.getDepartmentName());
		
		List<DepartmentDTO> tmpDepartmentChildrenList = new ArrayList<DepartmentDTO>();
		tmpDepartmentChildrenList.add(this.getDepartmentChildrenInfo(tmpParentDepartmentDTO));
		
		return tmpDepartmentChildrenList;
	}
	
	public DepartmentDTO getDepartmentChildrenInfo(DepartmentDTO outDepartmentDTO) {
		List<DepartmentDTO> tmpDepartmentChildrenList = this.securityDAO.getDepartmentChildrenList(outDepartmentDTO.getId());
		for (DepartmentDTO subDepartmentDTO : tmpDepartmentChildrenList) {
			this.getDepartmentChildrenInfo(subDepartmentDTO);
		}
		outDepartmentDTO.setChildren(tmpDepartmentChildrenList);
		
		return outDepartmentDTO;
	}
	
	
	/**
	 * 获取部门信息列表
	 * 
	 * @return
	 *     返回部门信息列表
	 */
	public List<ZtreeNode> getDepartmentTreeList() {
		List<DepartmentDTO> tmpDepartmentDTOList = this.securityDAO.getDepartmentList();
		List<ZtreeNode> tmpDepartmentTreeList = new ArrayList<ZtreeNode>();
		ZtreeNode tmpTreeNodeDTO = null;
		for (DepartmentDTO subDepartmentDTO : tmpDepartmentDTOList) {
			tmpTreeNodeDTO = new ZtreeNode();
			tmpTreeNodeDTO.setId(subDepartmentDTO.getDepartmentId()); 
			tmpTreeNodeDTO.setpId(subDepartmentDTO.getParentDepartmentId());
			tmpTreeNodeDTO.setName(subDepartmentDTO.getDepartmentName());
			tmpTreeNodeDTO.setOpen(true);
			tmpDepartmentTreeList.add(tmpTreeNodeDTO);
		}
		
		return tmpDepartmentTreeList;
	}
	
	/**
	 * 获取部门用户信息列表
	 * 
	 * @return
	 *     返回部门用户信息列表
	 */
	public List<ZtreeNode> getDepartmentEmployeeTreeList() {
		List<ZtreeNode> tmpDepartmentDTOList = this.securityDAO.getDepartmentEmployeeTreeList();
//		List<ZtreeNode> tmpDepartmentTreeList = new ArrayList<ZtreeNode>();
//		ZtreeNode tmpTreeNodeDTO = null;
//		for (DepartmentDTO subDepartmentDTO : tmpDepartmentDTOList) {
//			tmpTreeNodeDTO = new ZtreeNode();
//			tmpTreeNodeDTO.setId(subDepartmentDTO.getDepartmentId()); 
//			tmpTreeNodeDTO.setPId(subDepartmentDTO.getParentDepartmentId());
//			tmpTreeNodeDTO.setName(subDepartmentDTO.getDepartmentName());
//			tmpTreeNodeDTO.setOpen(true);
//			tmpDepartmentTreeList.add(tmpTreeNodeDTO);
//		}
		
		return tmpDepartmentDTOList;
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
		RoleEntity tmpExistRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleName", inRoleDTO.getRoleName());
        if (tmpExistRoleEntity != null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前新建的角色名称与其它角色名称相同，发生冲突";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
        
        RoleEntity tmpNewRoleEntity = new RoleEntity();
        RoleDTO.dtoToEntity(inRoleDTO, tmpNewRoleEntity);
		this.securityDAO.insert(tmpNewRoleEntity);
		
		return ActionResultUtil.getActionResult(tmpNewRoleEntity.getId(), "角色新建成功");
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
		RoleEntity tmpExistRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleName", inRoleDTO.getRoleName());
		if (tmpExistRoleEntity != null) {
			if (!tmpExistRoleEntity.getId().equals(inRoleDTO.getRoleId()) && tmpExistRoleEntity.getRoleName().equals(inRoleDTO.getRoleName())) {
				String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前修改的角色名称与其它角色名称相同，发生冲突";
				LoggerUtil.instance(this.getClass()).error(exceptionMessage);
				throw new RuntimeException(exceptionMessage);
			}
		}
        
		
        RoleEntity tmpCuttentRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleId", inRoleDTO.getRoleId());
		if (tmpCuttentRoleEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有角色可修改";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		RoleDTO.dtoToEntity(inRoleDTO, tmpCuttentRoleEntity);
		this.securityDAO.update(tmpCuttentRoleEntity);
		
		return ActionResultUtil.getActionResult(tmpCuttentRoleEntity.getId(), "角色修改成功");
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
		RoleEntity tmpRoleEntity = this.securityDAO.getEntity(RoleEntity.class, "roleId", inRoleId);
		if (tmpRoleEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有角色可删除";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		this.securityDAO.delete(tmpRoleEntity);
		
		return ActionResultUtil.getActionResult(tmpRoleEntity.getId(), "角色删除成功");
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
	 * 获取角色树形列表
	 * 
	 * @return
	 *     返回角色树形列表
	 */
	public List<ZtreeNode> getRoleTreeList() {
		List<RoleDTO> tmpRoleDTOList = this.securityDAO.getRoleList();
		List<ZtreeNode> tmpRoleTreeList = new ArrayList<ZtreeNode>();
		ZtreeNode tmpTreeNodeDTO = null;
		for (RoleDTO subRoleDTO : tmpRoleDTOList) {
			tmpTreeNodeDTO = new ZtreeNode();
			tmpTreeNodeDTO.setId(subRoleDTO.getRoleId()); 
			tmpTreeNodeDTO.setName(subRoleDTO.getRoleName());
			tmpRoleTreeList.add(tmpTreeNodeDTO);
		}
		
		return tmpRoleTreeList;
	}
	
	/**
	 * 获取权限列表
	 * 
	 * @return
	 *     返回权限列表
	 */
	public List<SecurityDTO> getSecurityList() {
		return this.securityDAO.getSecurityList();
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
		PostEntity tmpExistPostEntity = this.securityDAO.getEntity(PostEntity.class, "postName", inPostDTO.getPostName());
        if (tmpExistPostEntity != null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前新建的岗位名称与其它岗位名称相同，发生冲突";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
        
        PostEntity tmpNewPostEntity = new PostEntity();
        PostDTO.dtoToEntity(inPostDTO, tmpNewPostEntity);
		this.securityDAO.insert(tmpNewPostEntity);
		
		return ActionResultUtil.getActionResult(tmpNewPostEntity.getId(), "职务新建成功");
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
		PostEntity tmpExistPostEntity = this.securityDAO.getEntity(PostEntity.class, "postName", inPostDTO.getPostName());
		if (tmpExistPostEntity != null) {
	        if (!tmpExistPostEntity.getId().equals(inPostDTO.getPostId()) && tmpExistPostEntity.getPostName().equals(inPostDTO.getPostName())) {
				String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "当前修改的岗位名称与其它岗位名称相同，发生冲突";
				LoggerUtil.instance(this.getClass()).error(exceptionMessage);
				throw new RuntimeException(exceptionMessage);
			}
		}
		
        PostEntity tmpCuttentPostEntity = this.securityDAO.getEntity(PostEntity.class, "postId", inPostDTO.getPostId());
		if (tmpCuttentPostEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有职务可修改";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}

		PostDTO.dtoToEntity(inPostDTO, tmpCuttentPostEntity);
		this.securityDAO.update(tmpCuttentPostEntity);
		
		return ActionResultUtil.getActionResult(tmpCuttentPostEntity.getId(), "职务修改成功");
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
		PostEntity tmpPostEntity = this.securityDAO.getEntity(PostEntity.class, "postId", inPostId);
		if (tmpPostEntity == null) {
			String exceptionMessage = ExceptionCodeConst.SYSTEM_EXCEPTION_CODE + "没有岗位可删除";
			LoggerUtil.instance(this.getClass()).error(exceptionMessage);
			throw new RuntimeException(exceptionMessage);
		}
		
		this.securityDAO.delete(tmpPostEntity);
		
		return ActionResultUtil.getActionResult(tmpPostEntity.getId(), "岗位删除成功");
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
	public ActionResult SaveRoleSecurity(String inRoleId, List<String> inSecurutyList) throws Exception {
		// 删除角色原来权限
		this.securityDAO.deleteRoleAllSecurity(inRoleId);
		if (inSecurutyList != null && !inSecurutyList.isEmpty()) {
			// 添加当前要求保存的权限
			RoleSecurityRfEntity tmpNewRoleSecurityRfEntity = null;
			for (String subSecurityId: inSecurutyList) {
				tmpNewRoleSecurityRfEntity = new RoleSecurityRfEntity();
				tmpNewRoleSecurityRfEntity.setRoleId(inRoleId);
				tmpNewRoleSecurityRfEntity.setSecurityId(subSecurityId);
				this.securityDAO.insert(tmpNewRoleSecurityRfEntity);
			}
		}
		
		return ActionResultUtil.getActionResult(inRoleId, "角色权限保存成功");
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
