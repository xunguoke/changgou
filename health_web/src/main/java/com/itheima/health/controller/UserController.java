package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @GetMapping("/getUsername")
    public Result getUsername(){
        // SecurityContextHolder security容器的持有者
        // getContext 获取它的容器
        System.out.println("====Authentication==========" + SecurityContextHolder.getContext().getAuthentication().getName());
        UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("=======UserDetails=====" + user.getUsername());
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
    }

    @RequestMapping("/loginSuccess")
    public Result loginSuccess(){
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    @RequestMapping("/loginFail")
    public Result loginFail(){
        return new Result(false, "用户名或密码不正确");
    }

    /**
     * 添加用户名
     * checkitemIds 与 前端提交的参数名要一致
     * TODO 下面的常量类的属性新增了，增加用户相关的message提示
     * TODO 新增了工具类 ListUtils 判断链表是否为空，还没有新增，还没找到这个工具类
     * TODO 返回数据时作判断，不加了
     * TODO 要不要增加事务控制呢，增删改加了事务控制
     * 常量类的属性新增了，增加用户相关的message提示，增删改加了事务控制
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user, Integer[] roleIds){
        // 调用服务添加检查组
        userService.add(user,roleIds);
        // 响应结果
        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }

    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务分页查询
        PageResult<User> pageResult = userService.findPage(queryPageBean);
        if (pageResult == null) {
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS,pageResult);
    }

    /**
     * 通过id查询
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 查询检查组信息
        User user = userService.findById(id);
        if (user == null) {
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS,user);
    }

    /**
     * 通过用户id查询选中的角色id集合
     */
    @GetMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(int id){
        // 选中的检查项id
        List<Integer> roleIds = userService.findRoleIdsByUserId(id);
        //TODO 还没有新增ListUtils工具类
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, roleIds);
    }

    /**
     * 修改
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody User user, Integer[] roleIds){
        // 调用服务修改检查组
        userService.update(user,roleIds);
        // 响应结果
        return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
    }

    /**
     * 通过id删除
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除
        userService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }

    /**
     * 查询所有
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<User> list = userService.findAll();
        //TODO 还没有新增ListUtils工具类
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS,list);
    }

}
