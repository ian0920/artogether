package com.artogether.common.perm_desc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermDescRepository extends JpaRepository<PermDesc, Integer> {

}
