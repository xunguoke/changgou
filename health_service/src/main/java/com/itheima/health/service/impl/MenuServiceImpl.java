package com.itheima.health.service.impl;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/1 12:37
 **/

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> getMenu(String username) {
        // 获取一级菜单
        return menuDao.getMenuByUsername(username);
    }

    @Override
    public List<Map<String, Object>> getParentMenuID() {
        return menuDao.getParentMenuID();
    }

    @Override
    public void add(Map<String, Object> menu,Integer[] childrenIds) {
        String level = (String) menu.get("level");
        // 判断插入的是一级还是二级菜单
        if (level.equals("1")) {
            /*
             * 一级菜单
             * @Param name description level (已有) path priority (需插入)
             */
            //插入path值
            int parentTotal = menuDao.getParentMenuTotal();
            String path = Integer.toString(parentTotal +1);
            // 插入priority
            int priority = parentTotal+1;
            menu.put("path",path);
            menu.put("priority",priority);
            menuDao.addParentMenu(menu);
        }else {
            /*
             * 二级菜单
             * @Param name linkUrl parentMenuId description level (已有) path priority (需插入)
             */
            int childrenMenuTotal = menuDao.getChildrenMenuTotal((int)menu.get("parentMenuId"));
            String path = "/" + menuDao.getParentMenuPathById((int)menu.get("parentMenuId")) + "-" + (childrenMenuTotal+1);
            // 二级菜单的优先级为查询到的子菜单总数+1
            int priority = childrenMenuTotal+1;
            menu.put("path",path);
            menu.put("priority",priority);
            menuDao.addChildrenMenu(menu);
        }
    }

    @Override
    public Menu getMenuById(int id) {
        return menuDao.getMenuPathById(id);
    }

    @Override
    public void update(Map<String, Object> menu) {
        menuDao.update(menu);
    }

    @Override
    public List<Menu> getChildrenMenus() {
        return menuDao.getChildrenMenus();
    }
}
