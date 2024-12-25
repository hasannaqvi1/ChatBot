package com.jsonExample.repository;

import com.jsonExample.dto.ProductRequest;
import com.jsonExample.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);

    Page<Product> findByName(String name, Pageable pageable);

    // Inherits CRUD operations from JpaRepository
}