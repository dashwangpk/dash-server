package com.dash.server.sys.modules.cfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dash.server.common.utils.PageUtils;
import com.dash.server.common.utils.Query;
import com.dash.server.sys.modules.cfg.dao.Aa10Dao;
import com.dash.server.sys.modules.cfg.dto.Aa10DTO;
import com.dash.server.sys.modules.cfg.entity.Aa00Entity;
import com.dash.server.sys.modules.cfg.entity.Aa10Entity;
import com.dash.server.sys.modules.cfg.service.Aa00Service;
import com.dash.server.sys.modules.cfg.service.Aa10Service;
import com.dash.server.sys.modules.cfg.vo.Aa10VO;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class Aa10ServiceImpl extends ServiceImpl<Aa10Dao, Aa10Entity> implements Aa10Service {

    @Autowired
    private Aa00Service aa00Service;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Aa10Entity> page = this.page(
                new Query<Aa10Entity>().getPage(params),
                new QueryWrapper<Aa10Entity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils dictList(Aa10DTO aa10DTO) {
        IPage<Aa10VO> queryPage = new Query<Aa10VO>().getPage(aa10DTO.getParams());
        long size = queryPage.getSize();
        long offset = queryPage.offset();

        //字典编码
        String aaa100 = aa10DTO.getAaa100();
        String aaa101 = aa10DTO.getAaa101();

        long count = aa00Service.count();
        queryPage.setTotal(count);
        List<Aa10VO> dictList = this.baseMapper.dictList(aaa100,aaa101, size, offset);
        queryPage.setRecords(dictList);

        return new PageUtils(queryPage);
    }

    @Transactional
    @Override
    public void saveUpdate(Aa10DTO aa10DTO) {
        Aa00Entity aa00 = aa10DTO.getAa00();
        List<Aa10Entity> aa10s = aa10DTO.getDictList();
        aa00Service.saveOrUpdate(aa00);

        QueryWrapper<Aa10Entity> wrapper = new QueryWrapper<Aa10Entity>();
        wrapper.eq("aaa100", aa00.getAaa100());
        this.remove(wrapper);
        this.saveBatch(aa10s);
    }

    @Override
    public Aa10VO dictDataList(Aa10DTO aa10DTO) {

        Aa10VO aa10VO = new Aa10VO();
        Aa00Entity aa00 = aa00Service.getById(aa10DTO.getAaa100());
        QueryWrapper<Aa10Entity> wrapper = new QueryWrapper<Aa10Entity>();
        wrapper.eq("aaa100", aa10DTO.getAaa100());
        List<Aa10Entity> aa10s = this.list(wrapper);
        aa10VO.setAa00(aa00);
        aa10VO.setAa10s(aa10s);

        return aa10VO;
    }

    @Override
    public void removeByAaa100(Aa10DTO aa10DTO) {
        List<String> aaa100List = aa10DTO.getAaa100s();
        for(String aaa100 : aaa100List){
            QueryWrapper<Aa10Entity> wrapper = new QueryWrapper<Aa10Entity>();
            wrapper.eq("aaa100", aaa100);
            this.remove(wrapper);
        }
    }

    @Override
    public Map<String, Object> validatorRepeat(Aa10DTO aa10DTO) {
        String aaa100 = aa10DTO.getAaa100();
        QueryWrapper queryWrapper = new QueryWrapper<Aa10Entity>();
        queryWrapper.eq(true, "aaa100", aaa100);

        Integer count = this.baseMapper.selectCount(queryWrapper);
        Map<String, Object> result = Maps.newHashMap();
        result.put("isRepeat", false);
        result.put("repeatMessage", "无重复数据");

        if(count > 0){
            result.put("isRepeat", true);
            result.put("count", count);
            if(aaa100 != null){
                result.put("repeatField", "aaa100");
                result.put("repeatMessage", "字典编码重发");
            }else{
                result.put("repeatField", "other");
                result.put("repeatMessage", "其它字段重复");
            }
        }
        return result;
    }

    @Override
    public List<Aa10Entity> lowerDictDataList(Aa10DTO aa10) {
        List<Aa10Entity> aa10List = this.baseMapper.lowerDictDataList(aa10);
        return aa10List;
    }

    @Override
    public List<Aa10Entity> displayItems(Aa10DTO aa10DTO) {
        QueryWrapper queryWrapper = new QueryWrapper<Aa10Entity>();
        queryWrapper.eq("aaa100", aa10DTO.getAaa100());
        queryWrapper.eq("aaa107", "1");
        queryWrapper.orderByAsc("aaa106");
        List list = this.list(queryWrapper);
        return list;
    }

    @Transactional
    @Override
    public void itemSaveUpdate(Aa10DTO aa10DTO) {
        String aaa100 = aa10DTO.getAaa100();
        String aaa102 = aa10DTO.getAaa102();
        String originalAaa102 = aa10DTO.getOriginalAaa102();

        if(StringUtils.isNotEmpty(originalAaa102)){
            QueryWrapper queryWrapper = new QueryWrapper<Aa10Entity>();
            queryWrapper.eq("aaa100", aaa100);
            queryWrapper.eq("aaa102", originalAaa102);
            this.remove(queryWrapper);
        }
        this.save(aa10DTO);
    }

    @Override
    public void itemDelete(Aa10DTO aa10DTO) {
        String aaa100 = aa10DTO.getAaa100();
        String aaa102 = aa10DTO.getAaa102();
        QueryWrapper queryWrapper = new QueryWrapper<Aa10Entity>();
        queryWrapper.eq("aaa100", aaa100);
        queryWrapper.eq("aaa102", aaa102);
        this.remove(queryWrapper);
    }

    @Override
    public Map<String, Object> validatorItem(Aa10DTO aa10DTO) {
        String aaa100 = aa10DTO.getAaa100();
        String aaa102 = aa10DTO.getAaa102();
        String originalAaa102 = aa10DTO.getOriginalAaa102();

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("count",0 );

        if(!aaa102.equals(originalAaa102)){
            QueryWrapper queryWrapper = new QueryWrapper<Aa10Entity>();
            queryWrapper.eq("aaa100", aaa100);
            queryWrapper.eq("aaa102", aaa102);
            int count = this.count(queryWrapper);

            resultMap.put("count",count );
            resultMap.put("message","\"" + aaa102 + "\"代码值已存在！" );
        }

        return resultMap;
    }

    @Override
    public void itemStatusUpdate(Aa10DTO aa10DTO) {
        String aaa100 = aa10DTO.getAaa100();
        String aaa102 = aa10DTO.getAaa102();
        QueryWrapper queryWrapper = new QueryWrapper<Aa10Entity>();
        queryWrapper.eq("aaa100", aaa100);
        queryWrapper.eq("aaa102", aaa102);
        this.update(aa10DTO, queryWrapper);
    }

    @Override
    public List<Aa10Entity> options(String aaa100,Integer aaa104 ) {
        QueryWrapper queryWrapper = new QueryWrapper<Aa10Entity>();
        queryWrapper.eq("aaa100", aaa100);
        queryWrapper.eq(aaa104!=null, "aaa104", aaa104);
        List list = this.list(queryWrapper);
        return list;
    }

    @Override
    public List<Aa10Entity> options(Aa10DTO aa10DTO) {
        String aaa100 = aa10DTO.getAaa100();
        Integer aaa104 = 1;
        List<Aa10Entity> list = this.options(aaa100, aaa104);
        return list;
    }

    @Override
    public List<Aa10Entity> wholeOptions(Aa10DTO aa10DTO) {
        String aaa100 = aa10DTO.getAaa100();
        List<Aa10Entity> list = this.options(aaa100, null);
        return list;
    }


}