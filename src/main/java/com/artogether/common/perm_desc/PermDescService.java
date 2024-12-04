package com.artogether.common.perm_desc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 1.平台後台管理員(平台員工) 2.前台管理員 3.商城管理員 4.場租管理員 5.活動管理員 6.帳號管理員

@Service
public class PermDescService {

    @Autowired
    private PermDescRepository perm_descRepo;

    //新增平台功能說明
    public void add(PermDesc perm_desc) {
        perm_descRepo.save(perm_desc);
    }
    //判斷：如果新增的功能說明已經有了不進行新增

    //刪除
    public void delete(PermDesc perm_desc) {
        perm_descRepo.deleteById(perm_desc.getId());
    }

    //更新
    public void update(PermDesc perm_desc) {
        perm_descRepo.save(perm_desc);
    }

    //查詢全部
    public List<PermDesc> findAll() {
        return perm_descRepo.findAll();
    };

    //單一查詢
    public PermDesc findById(int id) {
        return perm_descRepo.findById(id).get();
    }

}