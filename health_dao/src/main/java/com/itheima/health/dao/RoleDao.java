package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: huangganda
 * @Data: 2020-10-08 10:13
 */
public interface RoleDao {

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Role> findByCondition(String queryString);

    /**
     * 查询所有权限项
     * @return
     */
    List<Permission> findAll();

    /**
     * 根据用户名获取菜单
     *
     * @return
     */
    List<Menu> getMenuByUsername(String username);

    /**
     * 新增角色
     * @param role
     */
    void add(Role role);

    /**
     * 根据角色id和权限id添加到数据库角色权限关系表
     * @param permissionds
     */
    void addrolepermission(@Param("id") Integer id,@Param("permissionds")  Integer permissionds);


    /**
     * 根据角色id和菜单id添加到数据库角色菜单关系表
     * @param id
     * @param integer
     */
    void addrolemenu(@Param("id") Integer id,@Param("integer") Integer integer);

    /**
     *通过角色id查询角色用户关系表中是否有关系数据
     * @param id
     * @return
     */
    int findUserRole(int id);

    /**
     * 根据id删除角色权限表
     * @param id
     */
    void deleteRolePermission(int id);

    /**
     * 根据id删除角色菜单关系表
     * @param id
     */
    void deleteRoleMenu(int id);

    /**
     * 根据id删除角色
     * @param id
     */
    void deleteRole(int id);

    /**
     * 根据id查询角色信息
     * @param id
     * @return
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
     * 更新角色信息
     * @param role
     */
    void update(Role role);

    List<Role> findAllRole();
}
