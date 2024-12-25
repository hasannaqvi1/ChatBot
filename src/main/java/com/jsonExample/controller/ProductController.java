package com.jsonExample.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonExample.dto.ProductRequest;
import com.jsonExample.entity.Product;
import com.jsonExample.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Controller class for managing Product endpoints.
 */
@RestController
@RequestMapping("/products") // Base URL for product endpoints
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // POST request to create a new product
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            productService.saveProduct(productRequest); // Save product
            return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully"); // Return response
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create product"); // Handle errors
        }
    }

    // GET request to retrieve a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.getProduct(id)
                .map(ResponseEntity::ok) // Return product if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if not found
    }

    @GetMapping("/DTO/{id}")
    public ResponseEntity<ProductRequest> getProductDTO(@PathVariable Long id) {
        return productService.getProductDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/allProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/allProductsDTO")
    public ResponseEntity<List<ProductRequest>> getAllProductsDTOController(){
        List<ProductRequest> productRequestList = productService.getAllProductsDto();
        return ResponseEntity.ok(productRequestList);
    }

    @GetMapping("/productsByName/{name}")
    public ResponseEntity<List<ProductRequest>> getProductsByName(@PathVariable String name){
        List<ProductRequest> list = productService.getProductByName(name);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/productsByNamePagination/{name}")
    public ResponseEntity<Page<ProductRequest>> getProductsByName(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,  // Default to page 0 if not provided
            @RequestParam(defaultValue = "10") int size) {  // Default to size 10 if not provided

        // Fetch paginated and sorted products
        Page<ProductRequest> productRequests = productService.getProductByNamePagination(name, page, size);
        return ResponseEntity.ok(productRequests);
    }



    @GetMapping("/productsByNamePaginationEntity/{name}")
    public ResponseEntity<Page<Product>> getProductsByNameEntity(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,  // Default to page 0 if not provided
            @RequestParam(defaultValue = "10") int size) {  // Default to size 10 if not provided

        // Fetch paginated and sorted products
        Page<Product> productRequests = productService.getProductByNamePaginationEntity(name, page, size);
        return ResponseEntity.ok(productRequests);
    }

    @GetMapping("/allProductsPagination")
    public ResponseEntity<Page<Product>> getAllProductsPage(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = productService.getAllProductsPagination(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/saveBulkData")
    public String saveRecords(){
        productService.saveBulkData();
        return "Operation completed";
    }




    @GetMapping("/hello")
    public String test(){
        return "hello world";
    }
}