package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.HealthException;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;



    /**
     * 获取用户的角色权限信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /*复制于检查组*/
    /**
     * 添加用户
     * @param user
     * @param roleIds
     */
    @Override
    @Transactional
    public void add(User user, Integer[] roleIds) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 对新增用户的密码加密
        user.setPassword(encoder.encode(user.getPassword()));
        // 添加用户
        userDao.add(user);
        // 获取用户id
        Integer userIds = user.getId();
        // 循环遍历选中角色id，
        if(null != roleIds){
            for (Integer roleId : roleIds) {
                //添加用户与角色的关系
                userDao.addUserRole(userIds,roleId);//TODO
            }
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<User> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 有条件，则要模糊查询
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 拼接 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询
        Page<User> page = userDao.findPage(queryPageBean.getQueryString());
        PageResult<User> pageResult = new PageResult<User>(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    /**
     * 通过用户id查询选中的角色id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdsByUserId(int id) {
        return userDao.findRoleIdsByUserId(id);
    }

    /**
     * 修改用户
     * @param user
     * @param roleIds
     */
    @Override
    @Transactional
    public void update(User user, Integer[] roleIds) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 对修改用户的密码加密
        user.setPassword(encoder.encode(user.getPassword()));
        // 先更新用户信息
        userDao.update(user);
        // 删除旧关系
        userDao.deleteUserRole(user.getId());
        // 添加新关系
        if(null != roleIds){
            for (Integer roleId : roleIds) {
                //添加检查组与检查项的关系
                userDao.addUserRole(user.getId(),roleId);
            }
        }
    }

    /**
     * 通过id删除
     * @param id
     * @throws HealthException
     */
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        // 判断是被套餐使用了，不需要判断有没有被其他东西用到
        /*int count = userDao.findSetmealCountByCheckGroupId(id);
        // count>0使用了
        if(count > 0){
            throw new HealthException("访检查组已经被套餐使用了，不能删除");
        }*/
        // 未被使用
        // 先删除用户与角色关系
        userDao.deleteUserRole(id);
        // 再删除用户
        userDao.deleteById(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
