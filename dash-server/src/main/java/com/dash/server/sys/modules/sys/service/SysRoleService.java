/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.dash.server.sys.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.dash.server.common.utils.PageUtils;
import com.dash.server.sys.modules.sys.dto.SysRoleDTO;
import com.dash.server.sys.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);

	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);

    PageUtils list(SysRoleDTO sysRoleDTO);

	void saveUpdate(SysRoleDTO sysRole);

	SysRoleEntity infos(SysRoleDTO sysRoleDTO);

	Map<String, Object> validatorRepeat(SysRoleDTO sysRoleDTO);

	void delete(SysRoleDTO sysRoleDTO);

	List<Long> memuIds(SysRoleDTO sysRoleDTO);

	void saveRoleMenuIds(SysRoleDTO sysRoleDTO);

	List<SysRoleEntity> enableList();

	List<Long> userRoleIds(SysRoleDTO sysRoleDTO);

	void grantUserRoles(SysRoleDTO sysRoleDTO);
}
