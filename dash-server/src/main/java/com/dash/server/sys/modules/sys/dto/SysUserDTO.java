package com.dash.server.sys.modules.sys.dto;


import com.dash.server.common.validator.group.UpdatePasswordGroup;
import com.dash.server.sys.modules.sys.entity.SysUserEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class SysUserDTO extends SysUserEntity {

    private Map<String, Object> params ;

    @NotBlank(message="新密码不能为空", groups = UpdatePasswordGroup.class)
    private String newPassword;

}
