package com.shq.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shq.model.wechat.Menu;
import com.shq.vo.wechat.MenuVo;
import com.shq.wechat.mapper.MenuMapper;
import com.shq.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author shq
 * @since 2023-06-08
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private WxMpService wxMpService;

    @Override
    public List<MenuVo> findMenuInfo() {

        List<MenuVo> menuVoList = new ArrayList<>();
        List<Menu> menuList = baseMapper.selectList(null);

        Map<Long, List<Menu>> map = menuList.stream().collect(Collectors.groupingBy(Menu::getParentId));

        List<Menu> topMenus = map.get(0L);

        if (topMenus != null) {
            for (Menu topMenu : topMenus) {
                MenuVo topMenuVo = new MenuVo();
                BeanUtils.copyProperties(topMenu, topMenuVo);
                List<MenuVo> children = new ArrayList<>();
                List<Menu> subMenus = map.get(topMenu.getId());
                if (subMenus != null) {
                    for (Menu subMenu : subMenus) {
                        MenuVo menuVo = new MenuVo();
                        BeanUtils.copyProperties(subMenu, menuVo);
                        children.add(menuVo);
                    }
                }
                topMenuVo.setChildren(children);
                menuVoList.add(topMenuVo);
            }
        }

        return menuVoList;
    }

    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://shennnn.top:9090/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://shennnn.top:9090/#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
