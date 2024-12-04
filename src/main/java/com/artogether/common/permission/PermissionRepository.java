package com.artogether.common.permission;

import com.artogether.common.system_mamager.SystemManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.manager = :manager")
    List<Permission> getByUserPerm(@Param("manager") SystemManager manager);

}
