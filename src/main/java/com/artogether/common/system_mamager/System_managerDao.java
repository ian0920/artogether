package com.artogether.common.system_mamager;

import java.util.List;


public interface System_managerDao {
    // 新增一個系統管理員
    int add(System_manager systemManager);

    // 更新系統管理員的資料
    int update(System_manager systemManager);

    // 刪除系統管理員，根據 id（主鍵）
    int delete(Integer id);

    // 根據 id 查找單個系統管理員
    System_manager findByPK(Integer id);

    // 查找所有的系統管理員
    List<System_manager> getAll();
}
