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
//            updateManagerMenu();
        }else {
            /*
             * 二级菜单
             * @Param name linkUrl parentMenuId description level (已有) path priority (需插入)
             */
            // 1.插入二级菜单
            menuDao.addChildrenMenu(menu);
            // 2.刷新path和priority
            updatePathAndPriorityByParentMenuId((int)menu.get("parentMenuId"));
//            updateManagerMenu();
        }
    }

    @Override
    public Map<String, Object> getMenuById(int id) {
        return menuDao.getMenuPathById(id);
    }

    /**
     * 菜单编辑功能
     * @param menu 需要编辑的菜单
     * @param childrenIds
     */
    @Override
    @Transactional
    public void update(Map<String, Object> menu, Integer[] childrenIds) {
        // 1.判断一级菜单or二级菜单
        String level = (String) menu.get("level");
        // 获取未分配菜单id
        String str = "未分配菜单";
        Integer id = menuDao.getMenuByName(str);
        // 获取一级菜单id
        int parentMenuId = (int) menu.get("id");
        // 获取二级菜单列表
        List<Menu> menuList = menuDao.getChildrenMenuByParentMenuId(parentMenuId);
        if (level.equals("1")) {
            // 1.1 更新一级菜单信息
            menuDao.update(menu);
            // 1.2 变更二级菜单
            // 1.2.1 更新下属所有二级菜单的parentMenuId为未分配
            for (Menu childrenMenu : menuList) {
                 Integer childrenMenuId = childrenMenu.getId();
                menuDao.updateParentMenuId(id,childrenMenuId);
            }
            // 1.2.2 更新选中的二级菜单
            for (Integer childrenId : childrenIds) {
                menuDao.updateParentMenuId(parentMenuId,childrenId);
            }
        }else {
            // 2.1 更新二级菜单
            menuDao.update(menu);
        }
        // 3.1 刷新path和priority
        updatePathAndPriorityByParentMenuId(parentMenuId);
        updatePathAndPriorityByParentMenuId(id);
    }

    /**
     * 查询未分配的菜单
     * @return
     */
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
     * 删除菜单
     * 【注意事项】删除前需确认该项是否被角色使用
     * @param id 菜单id
     */
    @Override
    @Transactional
    public boolean deleteById(int id) {
        // 1.根据id查询菜单类型
        Map<String, Object> menuPathById = menuDao.getMenuPathById(id);
        int level = (int) menuPathById.get("level");
        // 1.1 查询该项是否被角色使用
        List<Integer> roleIdList = menuDao.getRoleByMenuId(id);
        if (roleIdList==null||roleIdList.size()==0) {
            // 1.1.1 获取未分配菜单的id
            String str = "未分配菜单";
            Integer noneId = menuDao.getMenuByName(str);
            if (level==1) {
                // 获取二级菜单
                List<Menu> menuList = menuDao.getChildrenMenuByParentMenuId(id);
                // 首先抹除二级菜单的parentMenuId
                // 遍历
                for (Menu childrenMenu : menuList) {
                    Integer childrenMenuId = childrenMenu.getId();
                    menuDao.updateParentMenuId(noneId,childrenMenuId);
                }
                // 删除该菜单
                menuDao.deleteById(id);
                // 刷新未分配菜单path和priority
                updatePathAndPriorityByParentMenuId(noneId);
            }else {
                // 获取其一级菜单的id
                int parentMenuId = (int) menuPathById.get("parentMenuId");
                // 1.1.2 为空则未被占用，执行删除
                menuDao.deleteById(id);
                // 刷新当前菜单
                updatePathAndPriorityByParentMenuId(parentMenuId);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Menu> getAllMenu() {
        return menuDao.getAllMenu();
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
        // 1.1.1 判断该一级菜单是新增还是编辑
        // 1.1.2 查询该菜单的优先级
        Map<String, Object> parentMenu = menuDao.getMenuPathById(id);
        Integer parentMenuPriority = (Integer) parentMenu.get("priority");
        Integer priority = null;
        if (parentMenuPriority!=null) {
            priority = parentMenuPriority;
        }else {
            priority = menuDao.getParentMenuTotal();
            menu.put("priority",priority);
        }
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

//    @Transactional
//    public void updateManagerMenu() {
//        // 1.删除t_role_menu表中所有role_id=1的数据
//        menuDao.deleteManagerMenu();
//        // 2.查询所有菜单的id
//        List<Integer> idList = menuDao.getAllMenuId();
//        // 3.遍历idList
//        for (Integer menuId : idList) {
//            menuDao.addManagerMenu(menuId);
//        }
//
//    }
}
