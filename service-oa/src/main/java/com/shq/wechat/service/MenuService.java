package com.shq.wechat.service;

import com.shq.model.wechat.Menu;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shq.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author shq
 * @since 2023-06-08
 */
public interface MenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
