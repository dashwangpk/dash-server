package com.dash.server.sys.modules.sys.dto;

import com.dash.server.sys.modules.sys.entity.SysMenuEntity;
import lombok.Data;

import java.util.Map;

@Data
public class SysMenuDTO extends SysMenuEntity {
    private Map<String, Object> params ;

}
