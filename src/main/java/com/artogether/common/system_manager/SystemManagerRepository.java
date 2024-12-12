package com.artogether.common.system_manager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemManagerRepository extends JpaRepository<SystemManager, Integer> {
    SystemManager findByAccount(String account);
}
