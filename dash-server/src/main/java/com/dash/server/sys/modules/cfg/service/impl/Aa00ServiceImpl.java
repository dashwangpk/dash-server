package com.dash.server.sys.modules.cfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dash.server.sys.modules.cfg.dao.Aa00Dao;
import com.dash.server.sys.modules.cfg.entity.Aa00Entity;
import com.dash.server.sys.modules.cfg.service.Aa00Service;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Aa00ServiceImpl extends ServiceImpl<Aa00Dao, Aa00Entity> implements Aa00Service {


    @Override
    public List<Aa00Entity> displayList() {
        QueryWrapper queryWrapper = new QueryWrapper<Aa00Entity>();
        queryWrapper.eq("aaa108", 1);
        queryWrapper.orderByAsc("aaa106");
        List<Aa00Entity> list = this.list(queryWrapper);
        return list;
    }

}