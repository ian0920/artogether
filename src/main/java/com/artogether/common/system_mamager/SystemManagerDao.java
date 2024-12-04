package com.artogether.common.system_mamager;

import java.util.List;


public interface SystemManagerDao {
    // 新增一個系統管理員
    int add(SystemManager systemManager);

    // 更新系統管理員的資料
    int update(SystemManager systemManager);

    // 刪除系統管理員，根據 id（主鍵）
    int delete(Integer id);

    // 根據 id 查找單個系統管理員
    SystemManager findByPK(Integer id);

    // 查找所有的系統管理員
    List<SystemManager> getAll();


}
