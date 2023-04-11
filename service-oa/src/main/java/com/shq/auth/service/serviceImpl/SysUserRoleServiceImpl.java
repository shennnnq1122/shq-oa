package com.shq.auth.service.serviceImpl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shq.auth.mapper.SysUserRoleMapper;
import com.shq.auth.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shq.model.system.SysUser;
import com.shq.model.system.SysUserRole;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author shq
 * @since 2023-04-09
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {


}
