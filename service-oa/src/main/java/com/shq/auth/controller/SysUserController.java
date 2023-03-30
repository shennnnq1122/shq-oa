package com.shq.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.auth.service.SysUserService;
import com.shq.common.Result;
import com.shq.model.system.SysUser;
import com.shq.vo.system.SysUserQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author shq
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/admin/system/sys-user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户条件分页查询
     *
     * @param page 页数
     * @param pageSize 每页记录数
     * @param sysUserQueryVo 查询条件
     * @return 返回查询的分页数据
     */
    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{pageSize}")
    public Result page(@PathVariable int page, @PathVariable int pageSize, SysUserQueryVo sysUserQueryVo) {
        Page<SysUser> sysUserPage  = new Page<>(page,pageSize);

        // 获取条件
        String userName = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(userName),SysUser::getUsername,userName);
        queryWrapper.ge(!StringUtils.isEmpty(createTimeBegin),SysUser::getCreateTime,createTimeBegin);
        queryWrapper.le(!StringUtils.isEmpty(createTimeEnd),SysUser::getCreateTime,createTimeEnd);

        sysUserService.page(sysUserPage,queryWrapper);

        return Result.ok(sysUserPage);

    }

    /**
     * 获取用户
     * @param id
     * @return
     */
    @ApiOperation("获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable long id){
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @ApiOperation("删除用户")
    @GetMapping("/del/{id}")
    public Result del(@PathVariable long id){
        sysUserService.removeById(id);
        return Result.ok();
    }

    /**
     * 添加用户
     * @param sysUser
     * @return
     */
    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser){
        sysUserService.save(sysUser);
        return Result.ok();
    }

    /**
     * 更新用户
     * @param sysUser
     * @return
     */
    @ApiOperation("更新用户")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser){
        sysUserService.updateById(sysUser);
        return Result.ok();
    }
}

