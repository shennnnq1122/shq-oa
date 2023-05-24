package com.shq.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shq.model.system.SysMenu;
import com.shq.vo.system.AssginMenuVo;
import com.shq.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author shq
 * @since 2023-04-11
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    //根据角色id获取菜单
    List<SysMenu> findMenuByRoleId(Long roleId);



    void doAssgin(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);

    List<String> findUserPermsByUserId(Long userId);

}
