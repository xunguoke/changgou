package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;

import java.util.List;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/7 23:21
 **/
public interface PermissionDao {
    Page<Permission> findByCondition(String queryString);

    void add(Permission permission);

    Permission findById(Integer id);

    void update(Permission permission);

    void deleteById(Integer id);

    List<Integer> getRoleById(Integer id);
}
