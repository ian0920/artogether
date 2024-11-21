package com.artogether.common.permission;

import java.util.List;

public interface PermissionDao {
	
	    // 新增一個系統管理員
	    int add(Permission permission);

	    // 更新系統管理員的資料
	    int update(Permission permission);

	    // 刪除系統管理員，根據 id（主鍵）
	    int delete(Integer id);

	    // 根據 id 查找單個系統管理員
	    Permission findByPK(Integer id);

	    // 查找所有的系統管理員
	    List<Permission> getAll();
	
}
