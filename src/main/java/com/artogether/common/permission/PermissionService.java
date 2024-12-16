    package com.artogether.common.permission;

    import com.artogether.common.system_manager.SystemManager;
    import com.artogether.common.system_manager.SystemManagerRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.*;

    // 平台功能權限 新增 更新 刪除 查詢全部

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

        public SystemManager findManagerById(Integer managerId) {
            return systemManagerRepo.findById(managerId).orElse(null);
        }

        // 找全部
        public List<Permission> findAll() {
            return permissionRepo.findAll();
        }

        // FindALLDTO 創建集合取出所有
        public List<PermissionDTO> findAllDTO() {
            List<Permission> permissions = permissionRepo.findAll();
//            System.out.println(permissions);
            List<PermissionDTO> permissionDTOs = new ArrayList<>();
            for (Permission permission : permissions) {
                PermissionDTO permissionDTO = PermissionDTO.builder()
                        .managerId(permission.getPermissionId().getManagerId())
                        .descId(permission.getPermissionId().getDescId())
                        .descName(permission.getPermDesc().getDescription())
                        .build();
                permissionDTOs.add(permissionDTO);
            }
//            System.out.println(permissionDTOs);
            return permissionDTOs;
        }

        // 更新權限
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

        // 不能重複給權限
    }