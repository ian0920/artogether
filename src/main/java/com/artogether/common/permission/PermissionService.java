package com.artogether.common.permission;

import com.artogether.common.system_mamager.SystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepo;

    //新增權限編號
    public void add(Permission permission) {
        permissionRepo.save(permission);
    }

    //單一查詢
    public Permission findById(int id) {
        return permissionRepo.findById(id).orElse(null);
    }

    //找全部
    public List<Permission> findAll() {
        return permissionRepo.findAll();
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
