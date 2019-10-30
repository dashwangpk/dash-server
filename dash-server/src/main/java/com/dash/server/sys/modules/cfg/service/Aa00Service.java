package com.dash.server.sys.modules.cfg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dash.server.sys.modules.cfg.entity.Aa00Entity;


import java.util.List;

/**
 * 代码表
 *
 * @author wangpanke915
 * @email wangpanke915@pansky.com.cn
 * @date 2019-06-03 10:57:59
 */
public interface Aa00Service extends IService<Aa00Entity> {

    List<Aa00Entity> displayList();

}

