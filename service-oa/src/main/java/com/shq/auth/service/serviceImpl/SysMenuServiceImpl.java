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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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


}
