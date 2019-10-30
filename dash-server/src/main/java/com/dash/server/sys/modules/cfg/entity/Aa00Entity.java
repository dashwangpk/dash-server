package com.dash.server.sys.modules.cfg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("aa00")
public class Aa00Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value="aaa100",type= IdType.INPUT)
	private String aaa100;
	private String aaa101;
	private String aaa108;
	private String aae013;
	private String aaa106;
	private String aaa107;
}
