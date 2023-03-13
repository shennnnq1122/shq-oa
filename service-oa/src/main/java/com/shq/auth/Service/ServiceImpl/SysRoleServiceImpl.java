package com.shq.auth.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shq.auth.Mapper.SysRoleMapper;
import com.shq.auth.Service.SysRoleService;
import com.shq.model.system.SysRole;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
