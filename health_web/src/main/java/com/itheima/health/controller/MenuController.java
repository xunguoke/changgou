package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/1 12:33
 **/
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    /**
     * 根据用户名获取菜单
     * @param username 用户名
     * @return menuList
     */
    @PostMapping("/getMenu")
    public Result getMenu(String username) {
        List<Menu> menuList = menuService.getMenu(username);
        return new Result(true, MessageConstant.GET_MENU_SUCCESS,menuList);
    }

    /**
     * 获取一级菜单列表
     * @return parentMenuIdList
     */
    @GetMapping("/getParentMenuID")
    public Result getMenuTree() {
        List<Map<String,Object>> parentMenuIdList = menuService.getParentMenuID();
        return new Result(true,"获取父菜单ID列表成功",parentMenuIdList);
    }

    /**
     * 新增菜单功能
     * @param menu Map集合
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody Map<String, Object> menu,Integer[] childrenIds) {
        menuService.add(menu,childrenIds);
        return new Result(true, MessageConstant.GET_MENU_SUCCESS);
    }

    /**
     * 根据id查找菜单
     * @param id 菜单id
     * @return menu
     */
    @PostMapping("/getMenuById")
    public Result getMenuById(int id) {
        Map<String, Object> menu = menuService.getMenuById(id);
        return new Result(true,MessageConstant.GET_MENU_SUCCESS,menu);
    }

    @PostMapping("/getAllMenu")
    public Result getAllMenu() {
        List<Menu> menuList = menuService.getAllMenu();
        return new Result(true,"查询菜单成功",menuList);
    }

    /**
     * 获取子菜单【新增一级菜单时用】
     * @return menuList
     */
    @GetMapping("/getChildrenMenus")
    public Result getChildrenMenus() {
        List<Menu> menuList = menuService.getChildrenMenus();
        return new Result(true,MessageConstant.GET_MENU_SUCCESS,menuList);
    }

    /**
     * 编辑菜单
     * @param menu 编辑时提交的数据
     * @param childrenIds 二级菜单列表
     * @return 空
     */
    @PostMapping("/update")
    public Result update(@RequestBody Map<String, Object> menu,Integer[] childrenIds) {
        menuService.update(menu,childrenIds);
        return new Result(true,MessageConstant.GET_MENU_SUCCESS);
    }

    /**
     * 根据id删除
     * @return 空
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id) {
        boolean flag = menuService.deleteById(id);
        return new Result(flag,flag?"删除成功":"删除失败");
    }

    /**
     * 编辑时二级菜单的回显
     * @param id 一级菜单id
     * @return childrenIds
     */
    @PostMapping("/getChildrenIds")
    public Result getChildrenIds(Integer id) {
        Map<String, Object> childrenIds = menuService.getChildrenIds(id);
        return new Result(true,"查询子菜单成功",childrenIds);
    }
}
