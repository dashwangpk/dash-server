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
import com.dash.server.common.validator.group.AddGroup;
import com.dash.server.common.validator.group.UpdateGroup;
import com.dash.server.common.validator.group.UpdatePasswordGroup;
import com.dash.server.sys.modules.sys.dto.SysUserDTO;
import com.dash.server.sys.modules.sys.entity.SysUserEntity;
import com.dash.server.sys.modules.sys.service.SysUserRoleService;
import com.dash.server.sys.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody SysUserDTO sysUser){
		sysUserService.delete(sysUser.getUserId());
		return R.ok();
	}
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestBody SysUserDTO sysUser){
		PageUtils page = sysUserService.list(sysUser);
		return R.ok().put("page", page);
	}


	@RequestMapping("/wholeOptions")
	public R wholeOptions(){
		List<SysUserEntity> wholeOptions = sysUserService.list();
		return R.ok().put("wholeOptions", wholeOptions);
	}

	@SysLog("保存修改用户")
	@RequestMapping("/saveUpdate")
	@RequiresPermissions(value={"sys:user:save", "sys:user:update"}, logical= Logical.OR)
	public R saveUpdate(@RequestBody SysUserDTO user){
		if(user.getUserId() ==  null){
			ValidatorUtils.validateEntity(user, AddGroup.class);
		}else{
			ValidatorUtils.validateEntity(user, UpdateGroup.class);
		}
		Long createUserId = getUserId();
		user.setCreateUserId(createUserId);
		sysUserService.saveUpdate(user);
		return R.ok();
	}

	@SysLog("修改用户密码")
	@RequestMapping("/passwordUpdate")
	public R passwordUpdate(@RequestBody SysUserDTO user){
		ValidatorUtils.validateEntity(user, UpdatePasswordGroup.class);
		Long userId = getUserId();
		user.setUserId(userId);
		sysUserService.passwordUpdate(user);
		return R.ok();
	}
	/**
	 * 获取用户信息
	 */
	@RequestMapping("/infos")
	public R infos(@RequestBody SysUserDTO user){
		SysUserEntity sysUse = this.sysUserService.infos(user);
		return R.ok().put("user", sysUse);
	}

	/**
	 * 获取用户信息
	 */
	@RequestMapping("/validatorRepeat")
	public R validatorRepeat(@RequestBody SysUserDTO user){
		Map<String, Object> result = this.sysUserService.validatorRepeat(user);
		return R.ok(result);
	}
	/**
	 * 重置密码
	 */
	@RequestMapping("/resetPassword")
	@RequiresPermissions("sys:user:resetPassword")
	public R resetPassword(@RequestBody SysUserDTO user){
		this.sysUserService.resetPassword(user);
		return R.ok();
	}
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}

	
}
