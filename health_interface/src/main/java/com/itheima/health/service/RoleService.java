package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;

import java.util.List;

/**
 * @Author: huangganda
 * @Data: 2020-10-08 10:05
 */
public interface RoleService {
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<Role> findPage(QueryPageBean queryPageBean);

    /**
     * 查询所有权限项
     * @return
     */
    List<Permission> findAll();

    /**
     * 根据用户名获取菜单
     * @param username
     * @return
     */
    List<Menu> getMenu(String username);

    /**
     * 新增角色
     * @param role
     * @param permissionds
     * @param menuIds
     */
    void add(Role role, Integer[] permissionds, Integer[] menuIds);


    /**
     * 通过id删除
     * @param id
     */
    void deleteById(int id);


    /**
     * 根据id查询角色信息
     * @param id
     */
    Role findById(int id);

    /**
     * 根据id查询角色拥有的权限项
     * @param id
     * @return
     */
    List<Integer> findPermissionByRoleId(int id);

    /**
     * 根据id查询角色拥有的菜单项
     * @param id
     * @return
     */
    List<Integer> getMenuByRoleId(int id);

    /**
     * 修改角色信息
     * @param role
     * @param permissionds
     * @param menuIds
     */
    void update(Role role, Integer[] permissionds, Integer[] menuIds);

    List<Role> findAllRole();
}
