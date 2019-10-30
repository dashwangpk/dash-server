/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.dash.server.sys.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.dash.server.common.validator.group.AddGroup;
import com.dash.server.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_menu")
public class SysMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	@TableId
	private Long menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@NotBlank(message="父菜单不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Long parentId;
	
	/**
	 * 父菜单名称
	 */
	@TableField(exist=false)
	private String parentName;

	/**
	 * 菜单名称
	 */
	@NotBlank(message="菜单名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	@NotBlank(message="菜单类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;

	private String componentName;

	private Integer noCache;

	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;

	@TableField(exist=false)
	private List<?> list;



}
