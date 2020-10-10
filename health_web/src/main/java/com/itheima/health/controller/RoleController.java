package com.itheima.health.controller;

/**
 * @Author:Sun FuPing
 * @Date: 2020/10/10 22:15
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import com.itheima.health.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 查询所有角色信息
     * @return
     */
    @GetMapping("/findAllRoles")
    public Result findAllRoles(){
        List<Role> roles = roleService.findAllRoles();
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, roles);
    }

}
