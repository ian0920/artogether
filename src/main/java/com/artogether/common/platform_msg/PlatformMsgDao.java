package com.artogether.common.platform_msg;

import java.util.List;

public interface PlatformMsgDao {

	   // 新增一個系統管理員
    int add(PlatformMsg platformMsg);

    // 更新系統管理員的資料
    int update(PlatformMsg platformMsg);

    // 刪除系統管理員，根據 id（主鍵）
    int delete(Integer id);

    // 根據 id 查找單個系統管理員
    PlatformMsg findByPK(Integer id);

    // 查找所有的系統管理員
    List<PlatformMsg> getAll();
}
