package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/7 23:17
 **/
public interface PermissionService {
    PageResult<Permission> findPage(QueryPageBean queryPageBean);

    void add(Permission permission);

    Permission findById(Integer id);

    void update(Permission permission);

    void deleteById(Integer id);
}
