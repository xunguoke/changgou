package com.itheima.health.service;

import com.itheima.health.pojo.Role;

import java.util.List;

/**
 * @Author:Sun FuPing
 * @Date: 2020/10/10 22:19
 */
public interface RoleService {

    /**
     * 查询所有角色信息
     * @return
     */
    List<Role> findAllRoles();
}
