package com.artogether.common.permission;

import com.artogether.common.system_mamager.SystemManager;
import com.artogether.common.system_mamager.SystemManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepo;

    @Autowired
    private SystemManagerRepository systemManagerRepo;

    // 新增權限編號
    public void add(Permission permission) {
        permissionRepo.save(permission);
    }

    // 刪除權限
    public void delete(Permission permission) {
        permissionRepo.delete(permission);
    }

    // 單一查詢
    public Permission findById(int id) {
        return permissionRepo.findById(id).orElse(null);
    }

    public SystemManager findManagerById(Integer managerId) {
        return systemManagerRepo.findById(managerId).orElse(null);
    }

    //找全部
    public List<Permission> findAll() {
        return permissionRepo.findAll();
    }

    // 更新權限
//    public Permission update(Permission permission) {
//        // 確保權限存在於資料庫，根據 managerId 查詢
//        if (permissionRepo.existsById(permission.getManager().getId())) {
//            return permissionRepo.save(permission);  // save方法會自動進行更新操作
//        }
//        return null; // 如果該權限不存在，返回null
//    }

    public Permission update(Permission permission) {
        // 使用自定義查詢方法根據 managerId 和 descId 查詢是否存在權限
        Optional<Permission> existingPermission = permissionRepo.findByManagerIdAndDescId(
                permission.getManager().getId(),
                permission.getPermDesc().getId()
        );
        if (existingPermission.isPresent()) {
            // 如果權限存在，進行更新
            return permissionRepo.save(permission);
        }
        return null; // 如果該權限不存在，返回 null
    }



    // 管理員擁有權限
    public List<Integer> findByDescId(SystemManager manager) {
        List<Integer> memberHasPermission = new ArrayList<>();
        List<Permission> permissions = permissionRepo.getByUserPerm(manager);
        for (Permission permission : permissions) {
            memberHasPermission.add(permission.getPermDesc().getId());
        }
        return memberHasPermission;
    }

}
