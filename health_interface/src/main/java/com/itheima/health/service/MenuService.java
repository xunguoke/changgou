package com.itheima.health.service;

import com.itheima.health.pojo.Menu;

import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/1 12:36
 **/
public interface MenuService {
    List<Menu> getMenu(String username);

    List<Map<String, Object>> getParentMenuID();

    void add(Map<String, Object> menu,Integer[] childrenIds);

    Map<String, Object> getMenuById(int id);

    void update(Map<String, Object> menu, Integer[] childrenIds);

    List<Menu> getChildrenMenus();

    Map<String, Object> getChildrenIds(Integer id);

    boolean deleteById(int id);

    List<Menu> getAllMenu();
}
