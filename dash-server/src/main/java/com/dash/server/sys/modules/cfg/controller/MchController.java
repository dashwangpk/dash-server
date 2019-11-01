package com.dash.server.sys.modules.cfg.controller;

import java.util.Arrays;
import java.util.Map;

import com.dash.server.common.annotation.SysLog;
import com.dash.server.common.utils.PageUtils;
import com.dash.server.common.utils.R;
import com.dash.server.sys.modules.cfg.dto.MchDTO;
import com.dash.server.sys.modules.cfg.entity.MchEntity;
import com.dash.server.sys.modules.cfg.service.MchService;
import com.dash.server.sys.modules.sys.dto.SysUserDTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商户
 *
 * @author wangpanke915
 * @email wangpanke915@pingan.com
 * @date 2019-11-01 10:14:45
 */
@RestController
@RequestMapping("cfg/mch")
public class MchController {
    @Autowired
    private MchService mchService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = mchService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info")
    public R info(@RequestBody MchDTO mchDTO){
		MchEntity mch = mchService.getById(mchDTO.getMchId());
        return R.ok().put("mch", mch);
    }

    /**
     *
     */
    @RequestMapping("/validatorRepeat")
    public R validatorRepeat(@RequestBody MchDTO mchDTO){
        Map<String, Object> result = mchService.validatorRepeat(mchDTO);
        return R.ok(result);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MchEntity mch){
		mchService.save(mch);

        return R.ok();
    }

    @RequestMapping("/saveUpdate")
    public R saveUpdate(@RequestBody MchDTO mchDTO){
        mchService.saveUpdate(mchDTO);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MchEntity mch){
		mchService.updateById(mch);

        return R.ok();
    }

    @SysLog("删除商户")
    @RequestMapping("/delete")
    @RequiresPermissions("cfg:mch:delete")
    public R delete(@RequestBody MchDTO mch){
        mchService.removeById(mch.getMchId());
        return R.ok();
    }


}
