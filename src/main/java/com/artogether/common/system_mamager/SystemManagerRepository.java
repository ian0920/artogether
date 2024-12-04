package com.artogether.common.system_mamager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemManagerRepository extends JpaRepository<SystemManager, Integer> {
    SystemManager findByAccount(String account);
}
