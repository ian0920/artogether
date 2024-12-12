package com.artogether.common.perm_desc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 平台功能說明 新增 更改 刪除 查詢全部
// 1.平台後台管理員(平台員工) 2.前台管理員 3.商城管理員 4.場租管理員 5.活動管理員 6.帳號管理員

@Service
public class PermDescService {

    @Autowired
    private PermDescRepository permDescRepo;

    //新增平台功能說明
    public void add(PermDesc perm_desc) {
        permDescRepo.save(perm_desc);
    }

    public boolean savePermDesc(PermDesc permDesc) {
        try {
            permDescRepo.save(permDesc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //刪除
    public void delete(PermDesc perm_desc) {
        permDescRepo.deleteById(perm_desc.getId());
    }

    //更新
    public PermDesc update(PermDesc perm_desc) {
        return permDescRepo.save(perm_desc); // 保存並返回更新後的實體
    }

    //查詢全部
    public List<PermDesc> findAll() {
        return permDescRepo.findAll();
    };

    // 判斷 如果新增的功能說明重複不進行新增

}