package com.shq.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.auth.service.SysRoleService;
import com.shq.common.Result;
import com.shq.model.system.SysRole;
import com.sun.xml.internal.bind.v2.TODO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("查询所有角色")
    @GetMapping("/getAll")
    private Result getAll(){
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }


    @ApiOperation("添加角色")
    @PostMapping("/save")
    private Result save(@RequestBody SysRole sysRole){
        sysRoleService.save(sysRole);
        return Result.ok();
    }

    @ApiOperation("根据id查询角色")
    @GetMapping("/get/{id}")
    private Result<SysRole> get(@PathVariable Long id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @ApiOperation("修改角色")
    @PutMapping("/update")
    private Result update(@RequestBody SysRole sysRole){
        sysRoleService.updateById(sysRole);
        return Result.ok();
    }



    @ApiOperation("根据id删除角色")
    @DeleteMapping("/delete/{id}")
    private Result del(@PathVariable Long id){
        sysRoleService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("分页查询")
    @GetMapping("{page}/{limt}")
    public Result page(@PathVariable Long page,@PathVariable Long limt,SysRole searchObj){
        Page pageInfo = new Page(page,limt);

        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(searchObj.getRoleName()!=null,SysRole::getRoleName,searchObj.getRoleName());

        Page page1 = sysRoleService.page(pageInfo, queryWrapper);

        return Result.ok(page1);

    }

    @ApiOperation("批量删除")
    @DeleteMapping("deleteList")
    public Result deleteList(@RequestBody List<Long> idList){
        sysRoleService.removeByIds(idList);

        return Result.ok();
    }



}
