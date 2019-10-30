package com.dash.server.sys.modules.sys.dto;

import com.dash.server.sys.modules.sys.entity.SysRoleEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SysRoleDTO extends SysRoleEntity {

    private Map<String, Object> params ;
    private List<Long> menuIds;
    private Long userId;
    private List<Long> grantRoleIds;

}
