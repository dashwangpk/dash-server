package com.dash.server.sys.modules.cfg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dash.server.sys.modules.cfg.dto.Aa10DTO;
import com.dash.server.sys.modules.cfg.entity.Aa10Entity;
import com.dash.server.sys.modules.cfg.vo.Aa10VO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代码表
 * 
 * @author wangpanke915
 * @email wangpanke915@pansky.com.cn
 * @date 2019-06-03 10:57:59
 */
@Mapper
public interface Aa10Dao extends BaseMapper<Aa10Entity> {

    long dictCount(@Param("aaa100") String aaa100, @Param("aaa101") String aaa101);

    List<Aa10VO> dictList(@Param("aaa100") String aaa100, @Param("aaa101") String aaa101, @Param("size") long size, @Param("offset") long offset);

    List<Aa10Entity> lowerDictDataList(@Param("aa10") Aa10DTO aa10);


}
