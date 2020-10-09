package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.User;
import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface UserDao {

    /**
     * 获取用户的角色权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /*复制于检查组*/
    /**
     * 添加用户
     * @param user
     */
    void add(User user);

    /**
     * 添加用户与角色的关系
     * 参数的类型相同时，要取别名，或放入map
     * @param userId
     * @param roleId
     */
    void addUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 条件查询
     * @param queryString
     * @return
     */
    Page<User> findPage(String queryString);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 通过用户id查询选中的角色id集合
     * @param id
     * @return
     */
    List<Integer> findRoleIdsByUserId(int id);

    /**
     * 更新用户信息
     * @param user
     */
    void update(User user);

    /**
     * 删除用户与角色的关系
     * @param id
     */
    void deleteUserRole(Integer id);

    /**
     * 通过用户id统计个数
     * 不需要判断有没有被其他东西用到
     * @param id
     * @return
     */
//    int findSetmealCountByCheckGroupId(int id);

    /**
     * 删除用户
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有
     * @return
     */
    List<User> findAll();


}
