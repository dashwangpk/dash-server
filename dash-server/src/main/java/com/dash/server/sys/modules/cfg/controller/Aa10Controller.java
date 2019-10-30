package com.dash.server.sys.modules.cfg.controller;


import com.dash.server.common.utils.PageUtils;
import com.dash.server.common.utils.R;
import com.dash.server.sys.modules.cfg.dto.Aa10DTO;
import com.dash.server.sys.modules.cfg.entity.Aa00Entity;
import com.dash.server.sys.modules.cfg.entity.Aa10Entity;
import com.dash.server.sys.modules.cfg.service.Aa00Service;
import com.dash.server.sys.modules.cfg.service.Aa10Service;
import com.dash.server.sys.modules.cfg.vo.Aa10VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 代码表
 *
 * @author wangpanke915
 * @email wangpanke915@pansky.com.cn
 * @date 2019-06-03 10:57:59
 */
@RestController
@RequestMapping("cfg/aa10")
public class Aa10Controller {
    @Autowired
    private Aa10Service aa10Service;
    @Autowired
    private Aa00Service aa00Service;

    /**
     * 查询选项列表
     */
    @RequestMapping("/options")
    public R options(@RequestBody Aa10DTO aa10DTO){
        List<Aa10Entity> options = aa10Service.options(aa10DTO);
        return R.ok().put("options", options);
    }
    /**
     * 查询选项列表
     */
    @RequestMapping("/wholeoptions")
    public R wholeOptions(@RequestBody Aa10DTO aa10DTO){
        List<Aa10Entity> wholeOptions = aa10Service.wholeOptions(aa10DTO);
        return R.ok().put("wholeoptions", wholeOptions);
    }
    /**
     * 所有列表
     */
    @RequestMapping("/list")
    public R allList(){
        List<Aa10Entity> aa10List = aa10Service.list();
        return R.ok().put("list", aa10List);
    }

    @RequestMapping("/displayList")
    public R displayList(){
        List<Aa00Entity> displayList = aa00Service.displayList();
        return R.ok().put("displayList", displayList);
    }

    @RequestMapping("/displayItems")
    public R displayItems(@RequestBody Aa10DTO aa10DTO){
        List<Aa10Entity> displayItems = aa10Service.displayItems(aa10DTO);
        return R.ok().put("displayItems", displayItems);
    }

    @RequestMapping("/itemSaveUpdate")
    public R itemSaveUpdate(@RequestBody Aa10DTO aa10DTO) {
        Map<String, Object> resultMap = aa10Service.validatorItem(aa10DTO);
        if((int)resultMap.get("count") > 0){
            return R.error((String) resultMap.get("message"));
        }else{
            aa10Service.itemSaveUpdate(aa10DTO);
            return R.ok();
        }
    }

    @RequestMapping("/itemStatusUpdate")
    public R itemStatusUpdate(@RequestBody Aa10DTO aa10DTO) {
        aa10Service.itemStatusUpdate(aa10DTO);
        return R.ok();
    }

    @RequestMapping("/itemDelete")
    public R itemDelete(@RequestBody Aa10DTO aa10DTO) {
        aa10Service.itemDelete(aa10DTO);
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/dictList")
    public R dictList(@RequestBody Aa10DTO aa10DTO){
        PageUtils page = aa10Service.dictList(aa10DTO);
        return R.ok().put("dictList", page);
    }

    @RequestMapping("/aa10Info")
    public R aa10Info(@RequestBody Aa10DTO aa10DTO){
        Aa10VO aa10VO = aa10Service.dictDataList(aa10DTO);
        return R.ok().put("aa10Info", aa10VO);
    }

    @RequestMapping("/saveUpdate")
    public R saveUpdate(@RequestBody Aa10DTO aa10DTO) {
        aa10Service.saveUpdate(aa10DTO);
        return R.ok();
    }
    /**
     * 校验
     */
    @RequestMapping("/validatorRepeat")
    public R validatorRepeat(@RequestBody Aa10DTO aa10DTO){
        Map<String, Object> result = aa10Service.validatorRepeat(aa10DTO);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody Aa10Entity aa10){
		aa10Service.updateById(aa10);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Aa10DTO aa10DTO){
		aa10Service.removeByAaa100(aa10DTO);
        return R.ok();
    }

}
