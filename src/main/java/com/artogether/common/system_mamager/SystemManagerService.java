package com.artogether.common.system_mamager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemManagerService {

    @Autowired
    private SystemManagerRepository system_managerRepo;


    //新增平台管理員
    public void add(SystemManager system_manager) {
        system_managerRepo.save(system_manager);
    }

    //刪除
//    public void delete(SystemManager system_manager) {
//        system_managerRepo.delete(system_manager);
//    }

    //修改
    public void update(SystemManager system_manager) {
        system_managerRepo.save(system_manager);
    }

    //狀態修改 預設為0 如果是0(可使用)改成1(停權)如果不是改成0(可使用)
    public SystemManager update(int id) {
        SystemManager system_manager = findById(id);

        if(system_manager.getStatus() == 0){
            system_manager.setStatus((byte) 1);
        } else {
            system_manager.setStatus((byte) 0);
        }
        return system_managerRepo.save(system_manager);
    }

    //單一查詢，查詢單個管理員
    public SystemManager findById(int id) {
        return system_managerRepo.findById(id).orElse(null);
    }

    //查詢全部管理員
    public List<SystemManager> findAll() {
        return system_managerRepo.findAll();
    }

}
