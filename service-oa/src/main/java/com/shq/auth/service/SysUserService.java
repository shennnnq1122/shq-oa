package com.shq.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shq.model.system.SysRole;
import com.shq.model.system.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author shq
 * @since 2023-03-30
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUserName(String username);

    Map<String, Object> getCurrentUser();
}
