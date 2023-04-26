package com.shq.auth.controller;


import com.shq.auth.service.SysMenuService;
import com.shq.common.result.Result;
import com.shq.model.system.SysMenu;
import com.shq.vo.system.AssginMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author shq
 * @since 2023-04-11
 */
@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("查询菜单列表")
    @GetMapping("/findNodes")
    private Result findNodes(){

        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }


    @ApiOperation("添加菜单")
    @PostMapping("/save")
    private Result save(@RequestBody SysMenu SysMenu){
        sysMenuService.save(SysMenu);
        return Result.ok();
    }


    @ApiOperation("修改菜单")
    @PutMapping("/update")
    private Result update(@RequestBody SysMenu SysMenu){
        sysMenuService.updateById(SysMenu);
        return Result.ok();
    }



    @ApiOperation("根据id删除菜单")
    @DeleteMapping("/delete/{id}")
    private Result del(@PathVariable Long id){
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }

    @ApiOperation("查询所有菜单和角色分配的菜单")
    @GetMapping("toAssign/{roleId}")
    public  Result toAssign(@PathVariable Long roleId){
        List<SysMenu> list = sysMenuService.findMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation("为角色分配菜单")
    @PostMapping("doAssign")
    public  Result doAssgin(@RequestBody AssginMenuVo assginMenuVo){
        sysMenuService.doAssgin(assginMenuVo);
        return Result.ok();
    }


    //TODO 菜单模块
}

