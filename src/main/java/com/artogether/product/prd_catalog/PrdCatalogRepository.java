package com.artogether.product.prd_catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdCatalogRepository extends JpaRepository<PrdCatalog, Integer> {
//    List<PrdCatalog> findAll
}
