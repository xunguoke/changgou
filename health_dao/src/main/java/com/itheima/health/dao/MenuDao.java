package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;

import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/1 12:49
 **/
public interface MenuDao {
    List<Menu> getMenuByUsername(String username);

    List<Map<String, Object>> getParentMenuID();

    int getParentMenuTotal();

    void addParentMenu(Map<String, Object> menu);

    int getChildrenMenuTotal(int parentMenuId);

    String getParentMenuPathById(int parentMenuId);

    void addChildrenMenu(Map<String, Object> menu);

    Menu getMenuPathById(int id);

    void update(Map<String, Object> menu);

    List<Menu> getChildrenMenus();
}
