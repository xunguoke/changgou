package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;

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

    Map<String, Object> getMenuPathById(int id);

    void update(Map<String, Object> menu);

    List<Menu> getChildrenMenus();

    List<Menu> getChildrenMenuByParentMenuId(int id);

    void updateParentMenuId(@Param("parentMenuId") int id,@Param("childrenId") Integer childrenId);

    void updatePathAndPriority(Map<String, Object> childrenMenuMap);

    void deleteManagerMenu();

    List<Integer> getAllMenuId();

    void addManagerMenu(Integer menuId);

    List<Integer> getChildrenIds(Integer id);

    List<Menu> getEditChildrenMenus(Integer id);

    Integer getMenuByName(String str);

    List<Integer> getRoleByMenuId(int id);

    void deleteById(int id);

    List<Menu> getAllMenu();
}
