package com.artogether.common.perm_desc;

import java.util.List;

public interface Perm_descDao {
	
    // 新增一個系統管理員
    int add(Perm_desc permDesc);

    // 更新系統管理員的資料
    int update(Perm_desc permDesc);

    // 刪除系統管理員，根據 id（主鍵）
    int delete(Integer id);

    // 根據 id 查找單個系統管理員
    Perm_desc findByPK(Integer id);

    // 查找所有的系統管理員
    List<Perm_desc> getAll();

}
