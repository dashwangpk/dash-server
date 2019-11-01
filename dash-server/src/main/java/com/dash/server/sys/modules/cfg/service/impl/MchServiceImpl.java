package com.dash.server.sys.modules.cfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dash.server.common.utils.PageUtils;
import com.dash.server.common.utils.Query;
import com.dash.server.sys.modules.cfg.dao.MchDao;
import com.dash.server.sys.modules.cfg.dto.MchDTO;
import com.dash.server.sys.modules.cfg.entity.MchEntity;
import com.dash.server.sys.modules.cfg.service.MchService;
import com.dash.server.sys.modules.sys.entity.SysUserEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service("mchService")
public class MchServiceImpl extends ServiceImpl<MchDao, MchEntity> implements MchService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MchEntity> page = this.page(
                new Query<MchEntity>().getPage(params),
                new QueryWrapper<MchEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveUpdate(MchDTO mchDTO) {
        Integer mchId = mchDTO.getMchId();
        if(mchId == null){
            this.baseMapper.insert(mchDTO);
        }else{
            this.baseMapper.updateById(mchDTO);
        }
    }

    @Override
    public Map<String, Object> validatorRepeat(MchDTO mchDTO) {

        Integer mchId = mchDTO.getMchId();
        mchDTO.setMchId(null);
        String mchSn = mchDTO.getMchSn();
        String name = mchDTO.getName();

        QueryWrapper queryWrapper = new QueryWrapper<MchEntity>(mchDTO);
        if(mchId != null){
            queryWrapper.ne(true, "mch_id", mchId);
        }
        Integer count = this.baseMapper.selectCount(queryWrapper);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("isRepeat", false);
        result.put("repeatMessage", "无重复数据");

        if(count > 0){
            result.put("isRepeat", true);
            result.put("count", count);
            if(name != null){
                result.put("repeatField", "name");
                result.put("repeatMessage", "商户名重复");
            }else if(mchSn != null){
                result.put("repeatField", "mchSn");
                result.put("repeatMessage", "商户编码重复");
            }else{
                result.put("repeatField", "other");
                result.put("repeatMessage", "其它字段重复");
            }
        }

        return result;
    }

}