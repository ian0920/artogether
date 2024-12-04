package com.artogether.common.platform_msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformMsgService {

    @Autowired
    private PlatformMsgRepository platform_msgRepo;

    //新增平台訊息
    public void add(PlatformMsg platform_msg){
        platform_msgRepo.save(platform_msg);
    }

    public void update(PlatformMsg platform_msg){
        platform_msgRepo.save(platform_msg);
    }

    public void delete(PlatformMsg platform_msg){
        platform_msgRepo.delete(platform_msg);
    }

    public List<PlatformMsg> getAll(){
        return platform_msgRepo.findAll();
    }

    public PlatformMsg getById(int id){
        return platform_msgRepo.findById(id).get();
    }

}
