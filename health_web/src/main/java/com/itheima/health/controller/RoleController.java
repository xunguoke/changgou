package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.CheckGroupService;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: huangganda
 * @Data: 2020-10-08 10:00
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        // 调用服务分页查询
        PageResult<Role> pageResult = roleService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, pageResult);
    }

    /**
     * 查询所有权限项
     *
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        // 调用服务查询所有
        List<Permission> list = roleService.findAll();
        // 把结果包装到Reuslt再返回，统一返回的格式
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, list);
    }

    /**
     * 根据用户名获取菜单
     *
     * @param username 用户名
     * @return menuList
     */
    @PostMapping("/getMenu")
    public Result getMenu(String username) {
        List<Menu> menuList = roleService.getMenu(username);
        return new Result(true, MessageConstant.GET_MENU_SUCCESS, menuList);
    }

    /**
     * 新增角色
     * @param role
     * @param permissionds
     * @param menuIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionds,Integer[] menuIds) {
        //调用服务新增
        roleService.add(role,permissionds,menuIds);
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除套餐
        roleService.deleteById(id);
        return new Result(true, "删除套餐成功!");
    }

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务删除套餐
        Role role=roleService.findById(id);
        return new Result(true, "查询角色信息成功!",role);
    }

    /**
     * 根据id查询角色拥有的权限项
     * @param id
     * @return
     */
    @GetMapping("/findPermissionByRoleId")
    public Result findPermissionByRoleId(int id){
        // 调用服务删除套餐
        List<Integer> permission=roleService.findPermissionByRoleId(id);
        return new Result(true, "查询角色权限信息成功!",permission);
    }

    /**
     * 根据id查询角色拥有的菜单项
     * @param id
     * @return
     */
    @GetMapping("/getMenuByRoleId")
    public Result getMenuByRoleId(int id){
        // 调用服务删除套餐
        List<Integer> menuIds=roleService.getMenuByRoleId(id);
        return new Result(true, "查询角色菜单信息成功!",menuIds);
    }

    /**
     * 修改角色信息
     * @param role
     * @param permissionds
     * @param menuIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Role role, Integer[] permissionds,Integer[] menuIds) {
        //调用服务新增
        roleService.update(role,permissionds,menuIds);
        return new Result(true, "修改角色信息成功");
    }

    @GetMapping("/findAllRole")
    public Result findAllRole(){
        List<Role> roleList = roleService.findAllRole();
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,roleList);
    }
}
