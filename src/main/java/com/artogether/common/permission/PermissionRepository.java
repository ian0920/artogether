package com.artogether.common.permission;

import com.artogether.common.system_manager.SystemManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.manager = :manager")
    List<Permission> getByUserPerm(@Param("manager") SystemManager manager);

//  findByManagerIdAndDescId 方法返回一個 Optional<Permission> 物件。
//  Optional 是 Java 8 引入的一個容器類型，用來避免 null 值的問題。它用來包裝可能為 null 的對象，讓你能夠安全地處理查詢結果，避免直接訪問 null。
//  如果查詢結果存在（即有符合條件的 Permission 記錄），則 Optional 會包含該 Permission 物件；如果查詢結果不存在，則 Optional 會是空的。
    @Query("SELECT p FROM Permission p WHERE p.manager.id = :managerId AND p.permDesc.id = :descId")
    Optional<Permission> findByManagerIdAndDescId(Integer managerId, Integer descId);

    @Query("SELECT p FROM Permission p ORDER BY p.manager.id ASC")
    List<Permission> findAll();
}


