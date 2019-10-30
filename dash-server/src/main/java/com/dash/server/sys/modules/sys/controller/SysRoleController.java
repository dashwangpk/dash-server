/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.dash.server.sys.modules.sys.controller;


import com.dash.server.common.annotation.SysLog;
import com.dash.server.common.utils.PageUtils;
import com.dash.server.common.utils.R;
import com.dash.server.common.validator.ValidatorUtils;
import com.dash.server.sys.modules.sys.dto.SysRoleDTO;
import com.dash.server.sys.modules.sys.entity.SysRoleEntity;
import com.dash.server.sys.modules.sys.service.SysRoleMenuService;
import com.dash.server.sys.modules.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@RequestMapping("/grantUserRoles")
	public R grantUserRoles(@RequestBody SysRoleDTO sysRoleDTO){
		sysRoleService.grantUserRoles(sysRoleDTO);
		return R.ok();
	}
	@RequestMapping("/enableList")
	public R enableList(){
		List<SysRoleEntity> roles = sysRoleService.enableList();
		return R.ok().put("roles",roles);
	}

	@RequestMapping("/userRoleIds")
	public R userRoleIds(@RequestBody SysRoleDTO sysRoleDTO){
		List<Long> userRoleIds = sysRoleService.userRoleIds(sysRoleDTO);
		return R.ok().put("userRoleIds",userRoleIds);
	}

	@RequestMapping("/saveRoleMenuIds")
	public R saveRoleMenuIds(@RequestBody SysRoleDTO sysRoleDTO){
		sysRoleService.saveRoleMenuIds(sysRoleDTO);
		return R.ok();
	}
	/**
	 * 角色列表
	 */
	@RequestMapping("/memuIds")
	public R memuIds(@RequestBody SysRoleDTO sysRoleDTO){
		List<Long> memuIds = sysRoleService.memuIds(sysRoleDTO);
		return R.ok().put("memuIds", memuIds);
	}
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	public R list(@RequestBody SysRoleDTO sysRoleDTO){
		PageUtils page = sysRoleService.list(sysRoleDTO);
		return R.ok().put("page", page);
	}
	@PostMapping("/saveUpdate")
	public R saveUpdate(@RequestBody SysRoleDTO sysRole){
		ValidatorUtils.validateEntity(sysRole);
		Long createUserId = getUserId();
		sysRole.setCreateUserId(createUserId);
		sysRoleService.saveUpdate(sysRole);
		return R.ok();
	}
	/**
	 * 获取角色信息
	 */
	@RequestMapping("/infos")
	public R infos(@RequestBody SysRoleDTO sysRoleDTO){
		SysRoleEntity sysRole = sysRoleService.infos(sysRoleDTO);
		return R.ok().put("role", sysRole);
	}

	/**
	 * 验证角色信息重复性
	 */
	@RequestMapping("/validatorRepeat")
	public R validatorRepeat(@RequestBody SysRoleDTO sysRoleDTO){
		Map<String, Object> result = sysRoleService.validatorRepeat(sysRoleDTO);
		return R.ok(result);
	}

	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	public R delete(@RequestBody SysRoleDTO sysRoleDTO){
		sysRoleService.delete(sysRoleDTO);
		return R.ok();
	}
}
