package com.itheima.health.service;

import com.itheima.health.HealthException;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.User;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface UserService {
    /**
     * 获取用户的角色权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /*以下复制检查组*/
    /**
     * 添加检查组
     * @param user
     * @param roleIds
     */
    void add(User user, Integer[] roleIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<User> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 通过检查组id查询选中的检查项id集合
     * @param id
     * @return
     */
    List<Integer> findRoleIdsByUserId(int id);

    /**
     * 修改检查组
     * @param user
     * @param roleIds
     */
    void update(User user, Integer[] roleIds);

    /**
     * 通过id删除
     * @param id
     */
    void deleteById(int id) throws HealthException;

    /**
     * 查询所有
     * @return
     */
    List<User> findAll();
}
