/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.dash.server.sys.modules.sys.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dash.server.common.utils.Constant;
import com.dash.server.common.utils.MapUtils;
import com.dash.server.sys.modules.sys.dao.SysMenuDao;
import com.dash.server.sys.modules.sys.dto.SysMenuDTO;
import com.dash.server.sys.modules.sys.entity.SysMenuEntity;
import com.dash.server.sys.modules.sys.service.SysMenuService;
import com.dash.server.sys.modules.sys.service.SysRoleMenuService;
import com.dash.server.sys.modules.sys.service.SysUserService;
import com.dash.server.sys.modules.sys.vo.SysMenuEntityVO;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public List<SysMenuEntityVO> getUserRoleMenuList(Long userId) {

		List<SysMenuEntityVO> menuList = this.baseMapper.userRoleMenuList(userId);
		List<SysMenuEntityVO> resultList = Lists.newArrayList();

		for(SysMenuEntityVO mVO : menuList){
			if(mVO.getParentId() == 0){
				resultList.add(mVO);
				getUserMenuTreeList(menuList, mVO);
			}
		}

		return resultList;
	}

	private void getUserMenuTreeList(List<SysMenuEntityVO> menuList, SysMenuEntityVO mVO) {

		for(SysMenuEntityVO cmVO : menuList){
			if(mVO.getMenuId() == cmVO.getParentId()){
				mVO.addChildren(cmVO);
				cmVO.setParentName(mVO.getParentName());
				getUserMenuTreeList( menuList, cmVO);
			}
		}
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.removeById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
	}

	@Override
	public List<SysMenuEntityVO> menuTree(Long type) {
		List<SysMenuEntityVO> menuList = this.baseMapper.list(type);
		List<SysMenuEntityVO> resultList = Lists.newArrayList();

		for(SysMenuEntityVO mVO : menuList){
			if(mVO.getParentId() == 0){
				resultList.add(mVO);
				getUserMenuTreeList(menuList, mVO);
			}
		}

		return resultList;
	}

	@Override
	public List<SysMenuEntityVO> dirTree() {
		List<SysMenuEntityVO> resultList = Lists.newArrayList();
		SysMenuEntityVO oneLevel = new SysMenuEntityVO();
		oneLevel.setParentId(-1l);
		oneLevel.setMenuId(0l);
		oneLevel.setName("一级目录");
		oneLevel.setType(0);
		resultList.add(oneLevel);
		oneLevel.setChildren(this.menuTree(0l));
		return resultList;
	}

	@Override
	public void saveUpdate(SysMenuDTO sysMenuDTO) {
		Long menuId = sysMenuDTO.getMenuId();
		if(menuId == null){
			this.save(sysMenuDTO);
		}else{
			this.updateById(sysMenuDTO);
		}
	}

	@Override
	public SysMenuEntityVO getMenuById(Long menuId) {
		SysMenuEntityVO sysMenuEntity = this.baseMapper.menuInfos(menuId);
		return sysMenuEntity;
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		
		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}
}
