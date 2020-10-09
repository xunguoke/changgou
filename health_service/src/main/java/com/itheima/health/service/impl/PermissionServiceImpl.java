package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/7 23:17
 **/
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public PageResult<Permission> findPage(QueryPageBean queryPageBean) {
        // 使用PageHelper分页插件
        // 检查配置了插件没有sqlMapConfig.xml  sqlSessionFactoryBean
        // 放入threadlocal 页码及大小 Page
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 判断是否有查询条件，有则要拼接%
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 紧接着的查询语句会被分页, 从线程中获取threadlocal 页码与大小, total放入threadlocal Page
        Page<Permission> page = permissionDao.findByCondition(queryPageBean.getQueryString());
        // 防止数据丢失Page属性用的是基础数据类型，没有实现序列化
        // web与service代码解耦
        return new PageResult<Permission>(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void update(Permission permission) {
        permissionDao.update(permission);
    }

    @Override
    public boolean deleteById(Integer id) {
        // 查询权限是否被使用
        List<Integer> roleIds = permissionDao.getRoleById(id);
        if (roleIds == null||roleIds.size()==0) {
            permissionDao.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
