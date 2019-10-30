package com.dash.server.sys.modules.cfg.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 代码表
 * 
 * @author wangpanke915
 * @email wangpanke915@pansky.com.cn
 * @date 2019-06-03 10:57:59
 */
@Data
@TableName("aa10")
public class Aa10Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 代码类别
	 */
	private String aaa100;
	/**
	 * 类别名称
	 */
	private String aaa101;
	/**
	 * 代码
	 */
	private String aaa102;
	/**
	 * 代码名称
	 */
	private String aaa103;
	/**
	 * 备注
	 */
	private String aae013;
	/**
	 * 是否启用
	 */
	private String aaa104;
	/**
	 * 是否编辑
	 */
	private String aaa105;
	private String aaa106;
	private String aaa107;

}
