package com.shq.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shq.auth.service.SysMenuService;
import com.shq.auth.service.SysRoleService;
import com.shq.auth.service.SysUserRoleService;
import com.shq.auth.service.SysUserService;
import com.shq.common.config.exception.MyException;
import com.shq.common.jwt.JwtHelper;
import com.shq.common.md5.MD5;
import com.shq.common.result.Result;
import com.shq.model.system.SysRole;
import com.shq.model.system.SysUser;
import com.shq.vo.system.LoginVo;
import com.shq.vo.system.RouterVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){

        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysUser::getUsername,username);

        //1.根据用户名获取用户信息
        SysUser sysUser = sysUserService.getOne(queryWrapper);


        //2.判断用户是否存在
        if(sysUser == null){
            throw new MyException(201, "用户不存在...");
        }

        //3.判断用户密码是否正确
        String password = MD5.encrypt(loginVo.getPassword());
        if(!password.equals(sysUser.getPassword())){
            throw new MyException(201, "密码错误...");
        }

        //4.判断用户是否被禁用  1  可用    0   禁用
        if(sysUser.getStatus() == 0){
            throw new MyException(201, "该用户已被禁用...");
        }

        //5.使用jwt根据用户id和用户名称生成token的字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        //6.返回token
        Map<String,Object> map = new HashMap<>();

        map.put("token",token);
        return Result.ok(map);
    }

    @GetMapping("/info")
    public Result info(HttpServletRequest request){

        //1.从请求头中获取token
//        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNrcwMDex1FEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQBFgGCbQgAAAA.qK5fTNiJgkAo05cOkMSr_ljYWtLH3M5Q8ciukwdVPGk7LVNw7KYxOP1CyxOLI3eIPwCRpIc4eE41LB9ZhYns4Q";
        final String token = request.getHeader("token");

        //2.从token中获取用户名
        final Long userId = JwtHelper.getUserId(token);

        //3.根据用户id获取用户信息
        SysUser sysUser = sysUserService.getById(userId);

        //4.根据用户id获取可操作的菜单
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

        //5.根据用户id获取可操作的按钮
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        // 6、返回相应的数据

        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

        // 返回用户可以操作的菜单
        map.put("routers", routerList);

        // 返回用户可以操作的按钮
        map.put("buttons", permsList);

        return Result.ok(map);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }


}
