package com.jsonExample.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for creating a new Product request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    private String name; // Name of the product
    private JsonNode attributes; // JSON attributes of the product
}