/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.dash.server.sys.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dash.server.common.exception.RRException;
import com.dash.server.common.utils.Constant;
import com.dash.server.common.utils.PageUtils;
import com.dash.server.common.utils.Query;
import com.dash.server.sys.modules.sys.dao.SysRoleDao;
import com.dash.server.sys.modules.sys.dto.SysRoleDTO;
import com.dash.server.sys.modules.sys.entity.SysRoleEntity;
import com.dash.server.sys.modules.sys.entity.SysRoleMenuEntity;
import com.dash.server.sys.modules.sys.entity.SysUserRoleEntity;
import com.dash.server.sys.modules.sys.service.SysRoleMenuService;
import com.dash.server.sys.modules.sys.service.SysRoleService;
import com.dash.server.sys.modules.sys.service.SysUserRoleService;
import com.dash.server.sys.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");
		Long createUserId = (Long)params.get("createUserId");

		IPage<SysRoleEntity> page = this.page(
			new Query<SysRoleEntity>().getPage(params),
			new QueryWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
				.eq(createUserId != null,"create_user_id", createUserId)
		);

		return new PageUtils(page);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleEntity role) {
        role.setCreateTime(new Date());
        this.save(role);

        //检查权限是否越权
        checkPrems(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        this.updateById(role);

        //检查权限是否越权
        checkPrems(role);

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }


    @Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return baseMapper.queryRoleIdList(createUserId);
	}

	@Override
	public PageUtils list(SysRoleDTO sysRoleDTO) {
		String roleName =sysRoleDTO.getRoleName();
		QueryWrapper<SysRoleEntity> query = new QueryWrapper<SysRoleEntity>();
		query.like(StringUtils.isNotBlank(roleName), "role_name", roleName);
		query.orderByDesc("create_time");

		IPage<SysRoleEntity> page = this.page(
				new Query<SysRoleEntity>().getPage(sysRoleDTO.getParams()),
				query);

		return new PageUtils(page);
	}

	@Override
	public void saveUpdate(SysRoleDTO sysRole) {
		Long roleId = sysRole.getRoleId();
		if(roleId == null){
			sysRole.setCreateTime(new Date());
			this.baseMapper.insert(sysRole);
		}else{
			this.baseMapper.updateById(sysRole);
		}
	}

	@Override
	public SysRoleEntity infos(SysRoleDTO sysRoleDTO) {
		SysRoleEntity roleEntity = this.baseMapper.selectById(sysRoleDTO.getRoleId());
		return roleEntity;
	}

	@Override
	public Map<String, Object> validatorRepeat(SysRoleDTO sysRole) {
		Long roleId = sysRole.getRoleId();
		sysRole.setRoleId(null);
		String roleName = sysRole.getRoleName();

		QueryWrapper queryWrapper = new QueryWrapper<SysRoleEntity>(sysRole);
		if(roleId != null){
			queryWrapper.ne(true, "role_id", roleId);
		}
		Integer count = this.baseMapper.selectCount(queryWrapper);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isRepeat", false);
		result.put("repeatMessage", "无重复数据");

		if(count > 0){
			result.put("isRepeat", true);
			result.put("count", count);
			if(roleName != null){
				result.put("repeatField", "username");
				result.put("repeatMessage", "角色名重复");
			}else{
				result.put("repeatField", "other");
				result.put("repeatMessage", "其它字段重复");
			}
		}
		return result;
	}

	@Transactional
	@Override
	public void delete(SysRoleDTO sysRoleDTO) {
		this.removeById(sysRoleDTO.getRoleId());
		sysRoleMenuService.deleteByRoleId(sysRoleDTO.getRoleId());
		sysUserRoleService.deleteByRoleId(sysRoleDTO.getRoleId());
	}

	@Override
	public List<Long> memuIds(SysRoleDTO sysRoleDTO) {
		List<Long> memuIds = this.baseMapper.memuIds(sysRoleDTO.getRoleId());
		return memuIds;
	}

	@Transactional
	@Override
	public void saveRoleMenuIds(SysRoleDTO sysRoleDTO) {
		Long roleId = sysRoleDTO.getRoleId();
		List<Long> menuIds = sysRoleDTO.getMenuIds();

		QueryWrapper<SysRoleMenuEntity> queryWrapper = new QueryWrapper<SysRoleMenuEntity>();
		queryWrapper.eq("role_id", roleId);
		sysRoleMenuService.remove(queryWrapper);

		for(Long menuId : menuIds){
			SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
			sysRoleMenuEntity.setMenuId(menuId);
			sysRoleMenuEntity.setRoleId(roleId);

			sysRoleMenuService.save(sysRoleMenuEntity);
		}

	}

	@Override
	public List<SysRoleEntity> enableList() {
		QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<SysRoleEntity>();
		queryWrapper.eq("status", "1");
		List<SysRoleEntity> roles = this.list(queryWrapper);
		return roles;
	}

	@Override
	public List<Long> userRoleIds(SysRoleDTO sysRoleDTO) {
		List<Long> roles = this.baseMapper.userRoleIds(sysRoleDTO.getUserId());
		return roles;
	}

	@Transactional
	@Override
	public void grantUserRoles(SysRoleDTO sysRoleDTO) {
		QueryWrapper<SysUserRoleEntity> queryWrapper = new QueryWrapper<SysUserRoleEntity>();
		queryWrapper.eq("user_id", sysRoleDTO.getUserId());
		sysUserRoleService.remove(queryWrapper);
		for(Long roleId :sysRoleDTO.getGrantRoleIds()){
			SysUserRoleEntity userRoleEntity = new SysUserRoleEntity(sysRoleDTO.getUserId(), roleId);
			sysUserRoleService.save(userRoleEntity);
		}
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRoleEntity role){
		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if(role.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户所拥有的菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());
		
		//判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new RRException("新增角色的权限，已超出你的权限范围");
		}
	}
}
