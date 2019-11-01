package com.dash.server.sys.modules.cfg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dash.server.common.utils.PageUtils;
import com.dash.server.sys.modules.cfg.dto.MchDTO;
import com.dash.server.sys.modules.cfg.entity.MchEntity;


import java.util.Map;

/**
 * 商户
 *
 * @author wangpanke915
 * @email wangpanke915@pingan.com
 * @date 2019-11-01 10:14:45
 */
public interface MchService extends IService<MchEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveUpdate(MchDTO mchDTO);

    Map<String, Object> validatorRepeat(MchDTO mchDTO);
}

