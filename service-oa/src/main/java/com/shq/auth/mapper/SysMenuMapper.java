package com.shq.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shq.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author shq
 * @since 2023-04-11
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    public List<SysMenu> findMenuListByUserId(@Param("id") Long id);

}
