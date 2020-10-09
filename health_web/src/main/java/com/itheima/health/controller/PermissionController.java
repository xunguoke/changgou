package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/7 23:07
 **/
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<Permission> pageResult = permissionService.findPage(queryPageBean);
        return new Result(true, "成功获取权限列表",pageResult);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Permission permission) {
        permissionService.add(permission);
        return new Result(true,"成功添加权限");
    }

    @GetMapping("/findById")
    public Result findById(Integer id) {
        Permission permission = permissionService.findById(id);
        return new Result(true,"成功获取权限",permission);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Permission permission) {
        permissionService.update(permission);
        return new Result(true,"成功编辑权限");
    }

    @PostMapping("/deleteById")
    public Result deleteById(Integer id) {
        boolean flag = permissionService.deleteById(id);
        return new Result(flag,flag?"成功删除权限":"不能删除权限，已被用户使用");
    }

}
