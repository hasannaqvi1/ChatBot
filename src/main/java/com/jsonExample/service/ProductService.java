package com.jsonExample.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonExample.dto.ProductRequest;
import com.jsonExample.entity.Product;
import com.jsonExample.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for Product-related operations.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    // Method to save a product
    public Product saveProduct(ProductRequest productRequest) throws IOException {
        Product product = new Product();
        product.setName(productRequest.getName());
        JsonNode jsonNode = objectMapper.readTree(productRequest.getAttributes().traverse()); // Parse JSON attributes
        product.setAttributes(jsonNode);
        return productRepository.save(product); // Save product to the database
    }

    // Method to get a product by ID
    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id); // Retrieve product from the database
    }


    public Optional<ProductRequest> getProductDTO(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    ProductRequest dto = new ProductRequest();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setAttributes(product.getAttributes()); // Directly set JsonNode
                    return dto;
                });
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<ProductRequest> getAllProductsDto(){
        List<Product> list = productRepository.findAll();
        List<ProductRequest> productRequestList = list.stream()
                .map(product -> {
                    ProductRequest dto = new ProductRequest();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setAttributes(product.getAttributes());
                    return dto;
                })
                .collect(Collectors.toList());

        return productRequestList;
    }

    public List<ProductRequest> getProductByName(String name){
        List<Product> list = productRepository.findByName(name);
        List<ProductRequest> productList = list.stream()
                .map(product ->{
                    ProductRequest dto = new ProductRequest();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setAttributes(product.getAttributes());
                    return dto;
                })
                .collect(Collectors.toList());
        return productList;
    }

    public Page<Product> getAllProductsPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public Page<ProductRequest> getProductByNamePagination(String name, int page, int size) {
        // Create Pageable with sorting by id in descending order
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));

        // Fetch the products using the repository with pagination and sorting
        Page<Product> productPage = productRepository.findByName(name, pageable);

        // Convert the products to ProductRequest DTOs
        Page<ProductRequest> productRequestPage = productPage.map(product -> {
            ProductRequest dto = new ProductRequest();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setAttributes(product.getAttributes());
            return dto;
        });

        return productRequestPage;
    }
    @Transactional
    public Page<Product> getProductByNamePaginationEntity(String name, int page, int size) {
        // Create Pageable with sorting by id in descending order
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));

        // Fetch the products using the repository with pagination and sorting
        Page<Product> productPage = productRepository.findByName(name, pageable);
        return productPage;}





    @Transactional
    public void saveBulkData() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 1_300_000; i++) {
            Product product = new Product();
            product.setName("User" + (i % 10));  // 10 different users
            product.setAttributes(createRandomAttributes());
            productList.add(product);

            // Insert in batches of 1000 to avoid memory issues
            if (i % 1000 == 0 && !productList.isEmpty()) {
                productRepository.saveAll(productList);
                productList.clear();  // Clear the list to prevent memory overload
            }
        }

        // Insert any remaining products after the loop ends
        if (!productList.isEmpty()) {
            productRepository.saveAll(productList);
        }
    }

    private JsonNode createRandomAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("question", "Question " + Math.random());
        attributes.put("answer", "Answer " + Math.random());
        return objectMapper.convertValue(attributes, JsonNode.class);  // Reuse ObjectMapper
    }


}