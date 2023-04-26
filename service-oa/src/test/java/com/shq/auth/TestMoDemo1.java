package com.shq.auth;

import com.shq.auth.mapper.SysMenuMapper;
import com.shq.auth.service.SysMenuService;
import com.shq.model.system.SysMenu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

public class TestMoDemo1 {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Test
    public void getAll(){


        List<SysMenu> menuListByUserId = sysMenuMapper.findMenuListByUserId(1l);

        for (SysMenu sysMenu : menuListByUserId) {
            System.out.println(sysMenu.getName());
        }

    }


}
