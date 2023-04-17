package com.shq.auth.util;

import java.util.ArrayList;
import java.util.List;
import com.shq.model.system.SysMenu;

public class MenuHelper {

    public static List<SysMenu> buildTree(List<SysMenu> sysMenusList) {
        // 存放最终数据
        List<SysMenu> trees = new ArrayList<>();

        // 把所有的菜单数据进行遍历
        for(SysMenu sysMenu : sysMenusList){
            if(sysMenu.getParentId() == 0 ){
                trees.add(getChildren(sysMenu,sysMenusList));
            }
        }

        return trees;
    }

    private static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenusList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());

        for(SysMenu item : sysMenusList){
            if(item.getParentId() == sysMenu.getId()){
                getChildren(item,sysMenusList);
                sysMenu.getChildren().add(item);
            }
        }

        return sysMenu;
    }
}
