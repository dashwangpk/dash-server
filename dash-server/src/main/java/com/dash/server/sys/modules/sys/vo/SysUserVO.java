package com.dash.server.sys.modules.sys.vo;

import com.dash.server.sys.modules.sys.entity.SysUserEntity;
import lombok.Data;

@Data
public class SysUserVO extends SysUserEntity {

    private String roleNames;

}
