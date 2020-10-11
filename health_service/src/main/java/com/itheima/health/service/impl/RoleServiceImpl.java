package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.HealthException;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: huangganda
 * @Data: 2020-10-08 10:07
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
        //页码和每页记录数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有条件查询
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            //有查询条件
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString()+ "%");
        }
        //查询后分页
        Page<Role> page = roleDao.findByCondition(queryPageBean.getQueryString());
        //防止数据丢失，page属性用的是基础数据类型，没有实现序列化接口
        //可以对web和Service代码解耦(总记录数，页数)
        PageResult<Role> rolePageResult = new PageResult<>(page.getTotal(), page.getResult());
        return rolePageResult;
    }

    /**
     * 查询所有权限项
     * @return
     */
    @Override
    public List<Permission> findAll() {
        return roleDao.findAll();
    }

    /**
     * 根据用户名获取菜单
     * @param username
     * @return
     */
    @Override
    public List<Menu> getMenu(String username) {
        return roleDao.getMenuByUsername(username);
    }

    /**
     * 新增角色
     * @param role
     * @param permissionds
     * @param menuIds
     */
    @Override
    public void add(Role role, Integer[] permissionds, Integer[] menuIds) {
        //判断助记码是否是ROLE_开头
        if(role.getKeyword().startsWith("ROLE_")&&role.getKeyword().endsWith("ROLE_")){
            //如果只有ROLE_，则抛出异常
            throw new HealthException("该用户关键字错误!");
        }else if(!role.getKeyword().startsWith("ROLE_")){
            //如果不以ROLE_开头，则拼接上ROLE_
            role.setKeyword("ROLE_" + role.getKeyword());
        }
        //添加角色到角色表
        roleDao.add(role);
        //获取新增角色id
        Integer id = role.getId();
        //判断获取的角色id是否为空
        if(id!=null){
            //不为空，循环遍历权限项，根据角色id和权限id添加到数据库角色权限关系表
            for (Integer checkitemId : permissionds) {
                //根据角色id和权限id添加到数据库角色权限关系表
                roleDao.addrolepermission(id,checkitemId);
            }
            //不为空，循环遍历菜单项，根据角色id和菜单id添加到数据库角色菜单关系表
            for (Integer integer : menuIds) {
                //根据角色id和菜单id添加到数据库角色菜单关系表
                roleDao.addrolemenu(id,integer);
            }

        }

    }

    /**
     * 通过id删除
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //通过角色id查询角色用户关系表中是否有关系数据
        int count = roleDao.findUserRole(id);
        //对返回结果进行判断
        if(count>0){
            //如果有用户在使用该角色，则角色不可以被删除
            throw new HealthException("该角色已经被用户使用了，不能删除!");
        }
        //未被使用，先删除角色权限表
        roleDao.deleteRolePermission(id);
        //根据id删除角色菜单关系表
        roleDao.deleteRoleMenu(id);
        //最后根据id删除角色
        roleDao.deleteRole(id);
    }

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    @Override
    public Role findById(int id) {
        return roleDao.findById(id);
    }

    /**
     * 根据id查询角色拥有的权限项
     * @param id
     * @return
     */
    @Override
    public List<Integer> findPermissionByRoleId(int id) {
        //打印123456加密后的，待会删除 TODO
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("123456加密后的密码为：" + encoder.encode("123456"));
        return roleDao.findPermissionByRoleId(id);
    }

    /**
     * 根据id查询角色拥有的菜单项
     * @param id
     * @return
     */
    @Override
    public List<Integer> getMenuByRoleId(int id) {
        return roleDao.getMenuByRoleId(id);
    }

    /**
     * 修改角色信息
     * @param role
     * @param permissionds
     * @param menuIds
     */
    @Override
    public void update(Role role, Integer[] permissionds, Integer[] menuIds) {
        //判断助记码是否是ROLE_开头
        if(role.getKeyword().startsWith("ROLE_")&&role.getKeyword().endsWith("ROLE_")){
            //如果只有ROLE_，则抛出异常
            throw new HealthException("该用户关键字错误!");
        }else if(!role.getKeyword().startsWith("ROLE_")){
            //如果不以ROLE_开头，则拼接上ROLE_
            role.setKeyword("ROLE_" + role.getKeyword());
        }
        //先更新角色信息
        roleDao.update(role);
        Integer id = role.getId();
        //根据id删除角色权限关系表
        roleDao.deleteRolePermission(role.getId());
        //根据id删除角色菜单关系表
        roleDao.deleteRoleMenu(role.getId());
        //判断获取的角色id是否为空
        if(id!=null&&permissionds!=null&&menuIds!=null){
            //不为空，循环遍历权限项，根据角色id和权限id添加到数据库角色权限关系表
            for (Integer checkitemId : permissionds) {
                //根据角色id和权限id添加到数据库角色权限关系表
                roleDao.addrolepermission(id,checkitemId);
            }
            //不为空，循环遍历菜单项，根据角色id和菜单id添加到数据库角色菜单关系表
            for (Integer integer : menuIds) {
                //根据角色id和菜单id添加到数据库角色菜单关系表
                roleDao.addrolemenu(id,integer);
            }

        }
    }

    @Override
    public List<Role> findAllRole() {
        return roleDao.findAllRole();
    }


}
