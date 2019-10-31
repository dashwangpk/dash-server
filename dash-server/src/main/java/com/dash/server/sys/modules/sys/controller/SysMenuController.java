/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.dash.server.sys.modules.sys.controller;

import com.dash.server.common.annotation.SysLog;
import com.dash.server.common.utils.R;
import com.dash.server.common.validator.ValidatorUtils;
import com.dash.server.sys.modules.sys.dto.SysMenuDTO;
import com.dash.server.sys.modules.sys.entity.SysMenuEntity;
import com.dash.server.sys.modules.sys.service.ShiroService;
import com.dash.server.sys.modules.sys.service.SysMenuService;
import com.dash.server.sys.modules.sys.service.SysRoleMenuService;
import com.dash.server.sys.modules.sys.vo.SysMenuEntityVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@RequestMapping("/delete")
	public R delete(@RequestBody SysMenuDTO sysMenuDTO){

		//判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(sysMenuDTO.getMenuId());
		if(menuList.size() > 0){
			return R.error("请先删除子菜单或按钮");
		}
		sysRoleMenuService.deleteByMenuId(sysMenuDTO.getMenuId());
		sysMenuService.delete(sysMenuDTO.getMenuId());

		return R.ok();
	}

	@SysLog("菜单信息")
	@RequestMapping("/infos")
	public R infos(@RequestBody SysMenuDTO sysMenuDTO){
		SysMenuEntityVO sysMenu = sysMenuService.getMenuById(sysMenuDTO.getMenuId());
		return R.ok().put("sysMenu", sysMenu);
	}

	@SysLog("保存修改菜单")
	@RequestMapping("/saveUpdate")
	public R saveUpdate(@RequestBody SysMenuDTO sysMenuDTO){
		ValidatorUtils.validateEntity(sysMenuDTO);
		sysMenuService.saveUpdate(sysMenuDTO);
		return R.ok();
	}
	/**
	 * 导航菜单
	 */
	@GetMapping("/nav")
	public R nav(){
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}

	/**
	 * 获取用户菜单
	 * @return
	 */
	@GetMapping("/userRoleMenuList")
	public R getUserRoleMenuList(){
		List<SysMenuEntityVO> userRoleMenuList = sysMenuService.getUserRoleMenuList(getUserId());
		List<String> permissions = sysMenuService.userPermissions(getUserId());
		return R.ok().put("userRoleMenuList", userRoleMenuList).put("permissions", permissions);
	}

	/**
	 * 获取父子结构菜单
	 * @return
	 */
	@GetMapping("/tree")
	public R menuTree(){
		List<SysMenuEntityVO> menuTree = sysMenuService.menuTree(3);
		return R.ok().put("menuTree", menuTree);
	}

	/**
	 * 获取父子结构目录菜单
	 * @return
	 */
	@RequestMapping("/dirTree")
	public R dirTree(@RequestBody SysMenuDTO sysMenuDTO){
		List<SysMenuEntityVO> dirTree = sysMenuService.dirTree(sysMenuDTO);
		return R.ok().put("dirTree", dirTree);
	}

	/**
	 * 所有菜单列表
	 */
	@GetMapping("/list")
	public List<SysMenuEntity> list(){
		List<SysMenuEntity> menuList = sysMenuService.list();
		return menuList;
	}
	
	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();
		
		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		
		return R.ok().put("menuList", menuList);
	}
	
}
