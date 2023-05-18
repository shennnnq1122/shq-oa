package com.shq.auth.service.serviceImpl;

import com.shq.auth.service.SysMenuService;
import com.shq.auth.service.SysUserService;
import com.shq.model.system.SysUser;
import com.shq.security.custom.CustomUser;
import com.shq.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getByUserName(username);

        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus() == 0) {
            throw new RuntimeException("账号已停用");
        }

        //根据用户id查询用户可操作的按钮
        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String userPerms : userPermsList) {
            authorities.add(new SimpleGrantedAuthority(userPerms.trim()));
        }

        return new CustomUser(sysUser, authorities);

    }
}
