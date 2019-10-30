/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.dash.server.sys.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.dash.server.sys.modules.sys.dto.SysMenuDTO;
import com.dash.server.sys.modules.sys.entity.SysMenuEntity;
import com.dash.server.sys.modules.sys.vo.SysMenuEntityVO;

import java.util.List;


/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysMenuService extends IService<SysMenuEntity> {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntityVO> getUserRoleMenuList(Long userId);

	/**
	 * 删除
	 */
	void delete(Long menuId);

	List<SysMenuEntityVO> menuTree(Long type);

	List<SysMenuEntityVO> dirTree();

	void saveUpdate(SysMenuDTO sysMenuDTO);

	SysMenuEntityVO getMenuById(Long menuId);
}
