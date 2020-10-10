package com.itheima.health.dao;

import com.itheima.health.pojo.Role;

import java.util.List;

/**
 * @Author:Sun FuPing
 * @Date: 2020/10/10 22:23
 */
public interface RoleDao {

    /**
     * 查询所有角色信息
     * @return
     */
    List<Role> findAllRoles();
}
