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

    @PostMapping("/getMenu")
    public Result getMenu(String username) {
        List<Menu> menuList = menuService.getMenu(username);
        return new Result(true, MessageConstant.GET_MENU_SUCCESS,menuList);
    }

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

    @PostMapping("/getMenuById")
    public Result getMenuById(int id) {
        Menu menu = menuService.getMenuById(id);
        return new Result(true,MessageConstant.GET_MENU_SUCCESS,menu);
    }

    @GetMapping("/getChildrenMenus")
    public Result getChildrenMenus() {
        List<Menu> menuList = menuService.getChildrenMenus();
        return new Result(true,MessageConstant.GET_MENU_SUCCESS,menuList);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Map<String, Object> menu) {
        menuService.update(menu);
        return new Result(true,MessageConstant.GET_MENU_SUCCESS);
    }

    @PostMapping("/deleteById")
    public Result deleteById() {
        return null;
    }

}
