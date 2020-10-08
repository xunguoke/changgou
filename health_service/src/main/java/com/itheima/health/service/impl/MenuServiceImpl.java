package com.itheima.health.service.impl;

/**
 * @Author: ZhangYongLiang
 * @Date: 2020/10/1 12:37
 **/

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Transactional
    public void add(Map<String, Object> menu,Integer[] childrenIds) {
        String level = (String) menu.get("level");
        // 判断插入的是一级还是二级菜单
        if (level.equals("1")) {
            /*
             * 一级菜单
             * @Param name description level (已有) path priority (需插入)
             */
            // 1.插入一级菜单
            menuDao.addParentMenu(menu);
            // 2.获得新增的菜单id
            int id = (int) menu.get("id");
            // 3.将一级菜单id添加至二级菜单中
            if (childrenIds!=null) {
                for (Integer childrenId : childrenIds) {
                    menuDao.updateParentMenuId(id,childrenId);
                }
            }
            // 4.刷新该菜单及其子菜单的path和priority
            updatePathAndPriorityByParentMenuId(id);
            updateManagerMenu();
        }else {
            /*
             * 二级菜单
             * @Param name linkUrl parentMenuId description level (已有) path priority (需插入)
             */
            // 1.插入二级菜单
            menuDao.addChildrenMenu(menu);
            // 2.刷新path和priority
            updatePathAndPriorityByParentMenuId((int)menu.get("parentMenuId"));
            updateManagerMenu();
        }
    }

    @Override
    public Map<String, Object> getMenuById(int id) {
        return menuDao.getMenuPathById(id);
    }

    /**
     * 菜单编辑功能
     * @param menu 需要编辑的菜单
     */
    @Override
    @Transactional
    public void update(Map<String, Object> menu) {
        // 1.
        menuDao.update(menu);
    }

    @Override
    public List<Menu> getChildrenMenus() {
        return menuDao.getChildrenMenus();
    }

    /**
     * 编辑时查询的子菜单列表
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getChildrenIds(Integer id) {
        // 1.查询拥有的二级菜单id
        List<Integer> childrenIds = menuDao.getChildrenIds(id);
        // 2.查询菜单列表(包含自有和未分配)
        List<Menu> children = menuDao.getEditChildrenMenus(id);
        Map<String, Object> map = new HashMap<>();
        map.put("childrenIds",childrenIds);
        map.put("children",children);
        return map;
    }

    /**
     * 刷新所有菜单的path和priority
     */
    public void updatePathAndPriority() {
        // 1.查询所有一级菜单
        // 2.遍历所有一级菜单，根据查询结果的排序依次插入path、priority
        // 2.1 根据一级菜单的id查询下属子菜单
    }

    /**
     * 根据一级菜单id刷新菜单及其子菜单的path和priority
     * @param id 一级菜单id
     */
    @Transactional
    public void updatePathAndPriorityByParentMenuId(int id) {
        // 1.根据id查询一级菜单，刷新path
        Map<String, Object> menu = menuDao.getMenuPathById(id);
        // 1.1 计算priority (由一级菜单个数去生成优先级,看不懂就去看下数据库就明白了)
        int priority = menuDao.getParentMenuTotal();
        menu.put("priority",priority);
        // 1.2 计算path
        int path = priority + 1;
        menu.put("path",Integer.toString(path));
        // 1.3 插入level
        menu.put("level",1);
        // 1.3 更新
        menuDao.updatePathAndPriority(menu);
        // 2.遍历下属二级菜单，根据查询结果的排序依次插入path、priority
        // 2.1 根据一级菜单的id查询下属子菜单
        List<Menu> childrenMenuList = menuDao.getChildrenMenuByParentMenuId(id);
        // 2.2 生成path前缀
        String childrenPathPrefix = "/" + menu.get("path") + "-";
        Map<String, Object> childrenMenuMap = new HashMap<>();
        int childrenPriority = 1;
        for (Menu childMenu : childrenMenuList) {
            childMenu.setPriority(childrenPriority);
            childMenu.setPath(childrenPathPrefix + childrenPriority);
            childrenMenuMap = JSON.parseObject(JSON.toJSONString(childMenu), Map.class);
            childrenMenuMap.put("level",2);
            menuDao.updatePathAndPriority(childrenMenuMap);
            childrenPriority++;
        }
    }

    @Transactional
    public void updateManagerMenu() {
        // 1.删除t_role_menu表中所有role_id=1的数据
        menuDao.deleteManagerMenu();
        // 2.查询所有菜单的id
        List<Integer> idList = menuDao.getAllMenuId();
        // 3.遍历idList
        for (Integer menuId : idList) {
            menuDao.addManagerMenu(menuId);
        }

    }
}
