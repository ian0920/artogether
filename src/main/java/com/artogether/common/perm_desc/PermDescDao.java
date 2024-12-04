package com.artogether.common.perm_desc;

import java.util.List;

public interface PermDescDao {
	
    // 新增一個系統管理員
    int add(PermDesc permDesc);

    // 更新系統管理員的資料
    int update(PermDesc permDesc);

    // 刪除系統管理員，根據 id（主鍵）
    int delete(Integer id);

    // 根據 id 查找單個系統管理員
    PermDesc findByPK(Integer id);

    // 查找所有的系統管理員
    List<PermDesc> getAll();

}
