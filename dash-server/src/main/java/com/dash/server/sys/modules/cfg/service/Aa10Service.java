package com.dash.server.sys.modules.cfg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dash.server.common.utils.PageUtils;
import com.dash.server.sys.modules.cfg.dto.Aa10DTO;
import com.dash.server.sys.modules.cfg.entity.Aa10Entity;
import com.dash.server.sys.modules.cfg.vo.Aa10VO;


import java.util.List;
import java.util.Map;

/**
 * 代码表
 *
 * @author wangpanke915
 * @email wangpanke915@pansky.com.cn
 * @date 2019-06-03 10:57:59
 */
public interface Aa10Service extends IService<Aa10Entity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils dictList(Aa10DTO aa10DTO);

    void saveUpdate(Aa10DTO aa10DTO);

    Aa10VO dictDataList(Aa10DTO aa10DTO);

    void removeByAaa100(Aa10DTO aa10DTO);

    Map<String, Object> validatorRepeat(Aa10DTO aa10DTO);

    List<Aa10Entity> lowerDictDataList(Aa10DTO aa10);

    List<Aa10Entity> displayItems(Aa10DTO aa10DTO);

    void itemSaveUpdate(Aa10DTO aa10DTO);

    void itemDelete(Aa10DTO aa10DTO);

    Map<String, Object> validatorItem(Aa10DTO aa10DTO);

    void itemStatusUpdate(Aa10DTO aa10DTO);

    List<Aa10Entity> options(String aaa100, Integer aaa104);

    List<Aa10Entity> options(Aa10DTO aa10DTO);

    List<Aa10Entity> wholeOptions(Aa10DTO aa10DTO);
}

