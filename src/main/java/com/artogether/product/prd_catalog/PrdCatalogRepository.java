package com.artogether.product.prd_catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdCatalogRepository extends JpaRepository<PrdCatalog, Integer> {
}
