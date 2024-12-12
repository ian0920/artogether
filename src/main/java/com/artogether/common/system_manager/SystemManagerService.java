package com.artogether.common.system_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 平台管理員 新增 更新 刪除 查全部

@Service
public class SystemManagerService {

    @Autowired
    private SystemManagerRepository system_managerRepo;

    // 新增平台管理員，返回新增的管理員
    public SystemManager add(SystemManager system_manager) {
        if (system_manager.getPassword().length() < 8 || system_manager.getPassword().length() > 20) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters.");
        }

        // 驗證 phone 格式 (必須以 09 開頭，並且長度為 10 位)
        if (system_manager.getPhone() == null || !system_manager.getPhone().startsWith("09") || system_manager.getPhone().length() != 10) {
            throw new IllegalArgumentException("Phone number must start with '09' and be 10 digits long.");
        }

        // 設置創建日期 (此處假設有 getCreatedDate 方法) 已有自動設置@CreationTimestamp
//        system_manager.setRegister_date(new Date());

        // 設置狀態為 0 (預設)
        system_manager.setStatus((byte) 0);

        return system_managerRepo.save(system_manager);
    }

    // 修改平台管理員
    public SystemManager update(SystemManager system_manager) {
        // 確保該管理員存在，否則返回 null
        if (system_managerRepo.existsById(system_manager.getId())) {
            return system_managerRepo.save(system_manager);  // 更新管理員
        } else {
            return null; // 返回null，表示未找到該管理員
        }
    }

    // 更新管理員狀態
    public SystemManager updateStatus(int id) {
        SystemManager system_manager = findById(id);
        if (system_manager != null) {
            // 切換狀態
            system_manager.setStatus(system_manager.getStatus() == 0 ? (byte) 1 : (byte) 0);
            return system_managerRepo.save(system_manager); // 更新管理員
        }
        return null; // 如果找不到管理員，返回null
    }

    public void delete(int id) {
        SystemManager system_manager = findById(id);
    }

    // 查詢單一管理員
    public SystemManager findById(int id) {
        return system_managerRepo.findById(id).orElse(null);
    }

    // 查詢全部管理員
    public List<SystemManager> findAll() {
        return system_managerRepo.findAll();
    }

    //用登入帳號查詢管理員
    public SystemManager findByAccount(String account) {
        return system_managerRepo.findByAccount(account);
    }
}
