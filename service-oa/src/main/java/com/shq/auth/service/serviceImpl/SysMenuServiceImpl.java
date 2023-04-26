package com.shq.auth.service.serviceImpl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shq.auth.mapper.SysMenuMapper;
import com.shq.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shq.auth.service.SysRoleMenuService;
import com.shq.auth.util.MenuHelper;
import com.shq.common.config.exception.MyException;
import com.shq.model.system.SysMenu;
import com.shq.model.system.SysRoleMenu;
import com.shq.vo.system.AssginMenuVo;
import com.shq.vo.system.MetaVo;
import com.shq.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author shq
 * @since 2023-04-11
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> findNodes() {

        List<SysMenu> sysMenusList = baseMapper.selectList(null);

        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenusList);

        return sysMenus;
    }

    @Override
    public void removeMenuById(Long id) {
        //判断当前菜单是否有子菜单
        LambdaQueryWrapper<SysMenu> queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId,id);
        Integer count = baseMapper.selectCount(queryWrapper);


        if(count>0){//如果有子菜单，则不能删除
            throw new MyException(201,"菜单不能删除");
        }
        baseMapper.deleteById(id);

    }


    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        //1.查询所有菜单
        LambdaQueryWrapper<SysMenu> allQueryWrapper = new LambdaQueryWrapper<>();
        allQueryWrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> allList = baseMapper.selectList(allQueryWrapper);

        //2.根据角色id查询所有相关菜单
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> list = sysRoleMenuService.list(queryWrapper);

        //3.获取角色id相关的菜单id列表
        List<Long> collect = list.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        //4.根据菜单id列表对所有菜单列表进行标记
        allList.stream().forEach(item->{
            if(collect.contains(item.getId())) {
                item.setSelect(true);
            }
            else{
                item.setSelect(false);
            }
        });

        List<SysMenu> rList = MenuHelper.buildTree(allList);
        return rList;
    }

    @Override
    public void doAssgin(AssginMenuVo assginMenuVo) {
        Long roleId = assginMenuVo.getRoleId();

        //删除原有的角色相关菜单
        LambdaUpdateWrapper<SysRoleMenu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysRoleMenu::getRoleId,roleId);
        sysRoleMenuService.remove(updateWrapper);

        //添加新的角色相关菜单
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for(Long item : menuIdList){
            if(StringUtils.isEmpty(item)) continue;
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(item);
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenuService.save(sysRoleMenu);
        }


    }

    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenusList = null;

        if(userId == 1){//如果是管理员，则查询所有菜单列表
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus, 1);
            queryWrapper.orderByAsc(SysMenu::getSortValue);
            sysMenusList = baseMapper.selectList(queryWrapper);
        }
        else{//如果不是管理员，根据 userId 查询可以操作菜单列表
            sysMenusList = baseMapper.findMenuListByUserId(userId);

        }

        //构造路由结构
        //1.先构造树型结构
        List<SysMenu> list = MenuHelper.buildTree(sysMenusList);
        //2.根据树型结构完善路由结构
        List<RouterVo> routerList = this.buildRouter(list);

        return routerList;
    }

    private List<RouterVo> buildRouter(List<SysMenu> sysMenus){

        List<RouterVo> routers = new ArrayList<>();

        for (SysMenu menu : sysMenus) {
            //设置当前路由属性
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));

            List<SysMenu> children = menu.getChildren();
            if(menu.getType() == 1){//如果菜单类型一,则下一级为隐藏路由
                List<SysMenu> hiddenMenuList  = children.stream().
                        filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            }
            else{
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    // 递归
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    private String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    @Override
    public List<String> findUserPermsByUserId(Long userId) {

        List<SysMenu> sysMenusList = null;

        if(userId == 1){//如果是管理员，则查询所有菜单列表
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus, 1);
            queryWrapper.orderByAsc(SysMenu::getSortValue);
            sysMenusList = baseMapper.selectList(queryWrapper);
        }
        else{//如果不是管理员，根据 userId 查询可以操作菜单列表
            sysMenusList = baseMapper.findMenuListByUserId(userId);

        }
        //从菜单列表中获取可操作的按钮
        List<String> permsList  = sysMenusList.stream().filter(item -> item.getType() == 2)
                .map(SysMenu::getPerms).collect(Collectors.toList());

        return permsList ;
    }
//
//
//    @Override
//    public List<RouterVo> findUserMenuListByUserId(Long userId) {
//        //超级管理员admin账号id为：1
//        List<SysMenu> sysMenuList = null;
//        if (userId.longValue() == 1) {
//            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
//        } else {
//            sysMenuList = baseMapper.findMenuListByUserId(userId);
//        }
//        //构建树形数据
//        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
//
//        List<RouterVo> routerVoList = this.buildMenus(sysMenuTreeList);
//        return routerVoList;
//    }
//
//    /**
//     * 根据菜单构建路由
//     * @param menus
//     * @return
//     */
//    private List<RouterVo> buildMenus(List<SysMenu> menus) {
//        List<RouterVo> routers = new LinkedList<RouterVo>();
//        for (SysMenu menu : menus) {
//            RouterVo router = new RouterVo();
//            router.setHidden(false);
//            router.setAlwaysShow(false);
//            router.setPath(getRouterPath(menu));
//            router.setComponent(menu.getComponent());
//            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
//            List<SysMenu> children = menu.getChildren();
//            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
//            if(menu.getType() == 1) {
//                List<SysMenu> hiddenMenuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
//                for (SysMenu hiddenMenu : hiddenMenuList) {
//                    RouterVo hiddenRouter = new RouterVo();
//                    hiddenRouter.setHidden(true);
//                    hiddenRouter.setAlwaysShow(false);
//                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
//                    hiddenRouter.setComponent(hiddenMenu.getComponent());
//                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
//                    routers.add(hiddenRouter);
//                }
//            } else {
//                if (!CollectionUtils.isEmpty(children)) {
//                    if(children.size() > 0) {
//                        router.setAlwaysShow(true);
//                    }
//                    router.setChildren(buildMenus(children));
//                }
//            }
//            routers.add(router);
//        }
//        return routers;
//    }
//
//    /**
//     * 获取路由地址
//     *
//     * @param menu 菜单信息
//     * @return 路由地址
//     */
//    public String getRouterPath(SysMenu menu) {
//        String routerPath = "/" + menu.getPath();
//        if(menu.getParentId().intValue() != 0) {
//            routerPath = menu.getPath();
//        }
//        return routerPath;
//    }


}
