package mis.security.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import mis.security.constant.SecurityConst;
import mis.security.dto.DepartmentDTO;
import mis.security.dto.EmployeeDTO;
import mis.security.dto.LoginDTO;
import mis.security.dto.PostDTO;
import mis.security.dto.RoleDTO;
import mis.security.dto.SecurityDTO;
import mis.security.dto.ZtreeNode;
import mis.security.service.SecurityService;

import org.springframework.stereotype.Controller;

import ecp.bsp.business.file.dto.QueryResultDTO;
import ecp.bsp.system.commons.dto.ActionResult;
import ecp.bsp.system.commons.dto.AjaxResult;
import ecp.bsp.system.framework.query.PageQueryHepler;
import ecp.bsp.system.framework.query.data.entity.DynamicGridQueryEntity;
import ecp.bsp.system.framework.query.data.entity.QueryPager;
import ecp.bsp.system.framework.query.impl.PageQueryService;

@Controller
public class SecurityController  {
	
	@Resource
	private PageQueryService pageQueryService;
	
	@Resource
	private SecurityService securityService;
	
	/**
	 * 用户登陆
	 * @param inLoginDTO
	 *        用户登陆信息
	 *        
	 * @return 返回用户登陆状态
	 */
	public AjaxResult login(LoginDTO inLoginDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.login(inLoginDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 插入用户信息
	 * 
	 * @param inEmployeeDTO
	 *     用户信息
	 * @return
	 *     返回用户信息插入情况
	 *     
	 */
	public AjaxResult insertEmployee(EmployeeDTO inEmployeeDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		String[] tmpRoleIdArray = inEmployeeDTO.getRoleId().split(",");
		inEmployeeDTO.setRoleIdList(Arrays.asList(tmpRoleIdArray));
		
		try {
			result = this.securityService.insertEmployee(inEmployeeDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult updateEmployee(EmployeeDTO inEmployeeDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		String[] tmpRoleIdArray = inEmployeeDTO.getRoleId().split(",");
		inEmployeeDTO.setRoleIdList(Arrays.asList(tmpRoleIdArray));
		
		try {
			result = this.securityService.updateEmployee(inEmployeeDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult deleteEmployee(String inEmployeeId) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.deleteEmployee(inEmployeeId);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 获取用户列表
	 * 
	 * @return
	 *     用户列表
	 */
	public Object getEmployeeList() {
		return (List<EmployeeDTO>) this.securityService.getEmployeeList();
	}
	
	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @return
	 *     用户列表
	 */
	public Object getEmployeeInfoById(String inEmployeeId) {
		return this.securityService.getEmployeeInfoById(inEmployeeId);
	}
	
	/**
	 * 获取用户信息列表（根据查询条件queryCondition）
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object getEmployeeListByCondition(List queryCondition,Integer start,Integer limit){
		String sql = SecurityConst.SQL_GET_EMPLOYEE_LIST_BY_CONDITION;
		List<DynamicGridQueryEntity> filter = PageQueryHepler.createConditions(queryCondition);
		QueryPager queryPager = this.pageQueryService.queryList(filter, sql, start, limit, EmployeeDTO.class);
		QueryResultDTO queryResultDTO = new QueryResultDTO();
		queryResultDTO.setRecordsTotal(queryPager.getTotalCount());
		queryResultDTO.setRecordsFiltered((queryPager.getTotalCount()));
		queryResultDTO.setData(queryPager.getResult());
		return queryResultDTO;
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
	 */
	public AjaxResult insertDepartment(DepartmentDTO inDepartmentDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.insertDepartment(inDepartmentDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult updateDepartment(DepartmentDTO inDepartmentDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.updateDepartment(inDepartmentDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult deleteDepartment(String inDepartmentId) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.deleteDepartment(inDepartmentId);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 获取部门信息列表
	 * 
	 * @return
	 *     返回部门信息列表
	 */
	public Object getDepartmentList() {
		return (List<DepartmentDTO>) this.securityService.getDepartmentList();
	}
	
	/**
	 * 获取部门信息列表
	 * 
	 * @return
	 *     返回部门信息列表
	 */
	public Object getDepartmentTreeList() {
		return (List<ZtreeNode>) this.securityService.getDepartmentTreeList();
	}
	
	/**
	 * 获取部门信息列表
	 * 
	 * @return
	 *     返回部门信息列表
	 */
	public Object getDepartmentGroupList() {
		return (List<DepartmentDTO>) this.securityService.getDepartmentGroupList();
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
	 */
	public AjaxResult insertRole(RoleDTO inRoleDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.insertRole(inRoleDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult updateRole(RoleDTO inRoleDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.updateRole(inRoleDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult deleteRole(String inRoleId) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.deleteRole(inRoleId);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 获取角色列表
	 * 
	 * @return
	 *     返回角色列表
	 */
	public Object getRoleList() {
		return (List<RoleDTO>) this.securityService.getRoleList();
	}
	
	/**
	 * 获取角色列表
	 * 
	 * @return
	 *     返回角色列表
	 */
	public Object getRoleTreeList() {
		return (List<ZtreeNode>) this.securityService.getRoleTreeList();
	}
	
	/**
	 * 获取权限列表
	 * 
	 * @return
	 *     返回权限列表
	 */
	public Object getSecurityList() {
		return (List<SecurityDTO>) this.securityService.getSecurityList();
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
	 */
	public AjaxResult insertPost(PostDTO inPostDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.insertPost(inPostDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult updatePost(PostDTO inPostDTO) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.updatePost(inPostDTO);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	 */
	public AjaxResult deletePost(String inPostId) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.deletePost(inPostId);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
	}
	
	/**
	 * 获取职务信息列表
	 * 
	 * @return
	 *     返回职务信息列表
	 */
	public Object getPostList() {
		return (List<PostDTO>) this.securityService.getPostList();
	}
	
	/**
	 * 保存角色权限信息
	 * @param inRoleId
	 *     角色ID
	 *     
	 * @param inSecurutyIdList
	 *     角色的权限列表
	 *     
	 * @return
	 *     返回保存角色权限信息情况
	 *     
	 */
	public AjaxResult SaveRoleSecurity(String inRoleId, List<String> inSecurutyIdList) {
		ActionResult result = null;	
		AjaxResult ajaxResult = new AjaxResult();
		
		try {
			result = this.securityService.SaveRoleSecurity(inRoleId, inSecurutyIdList);
			ajaxResult.setSuccess(result.getIsSuccessful());
			ajaxResult.setMsg(result.getActionResultMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}	
		
		return ajaxResult;
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
	public Object getRoleSecurity(String inRoleId) {		
		return (List<SecurityDTO>) this.securityService.getRoleSecurity(inRoleId);
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
	public Object getEmployeeSecurity(String inEmployeeId) {
		return (List<SecurityDTO>) this.securityService.getEmployeeSecurity(inEmployeeId);
	}
	
}
