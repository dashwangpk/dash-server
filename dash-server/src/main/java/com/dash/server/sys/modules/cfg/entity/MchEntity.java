package com.dash.server.sys.modules.cfg.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 商户
 * 
 * @author wangpanke915
 * @email wangpanke915@pingan.com
 * @date 2019-11-01 10:14:45
 */
@Data
@TableName("mch")
public class MchEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商户ID
	 */
	@TableId
	private Integer mchId;
	/**
	 * 商户编号
	 */
	private String mchSn;
	/**
	 * 商户名称
	 */
	private String name;
	/**
	 * 是否启用
	 */
	private Integer enable;
	/**
	 * 备注
	 */
	private String remark;

	private String contacts;

	private String phone;

}
