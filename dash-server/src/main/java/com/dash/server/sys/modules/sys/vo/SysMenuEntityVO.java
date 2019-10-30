package com.dash.server.sys.modules.sys.vo;

import com.dash.server.sys.modules.sys.entity.SysMenuEntity;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class SysMenuEntityVO extends SysMenuEntity {

    private List<SysMenuEntityVO> children = Lists.newArrayList();

    public void addChildren(SysMenuEntityVO menuEntity){
        children.add(menuEntity);
    }

}
