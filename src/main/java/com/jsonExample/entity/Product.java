    package com.jsonExample.entity;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.databind.JsonNode;
    import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.Type;

    /**
     * Entity class representing a Product.
     */
    @Data
    @Entity
    @Table(name = "products")
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown JSON properties
    @AllArgsConstructor
    @NoArgsConstructor
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
        private Long id;

        private String name; // Name of the product
        @Type(JsonBinaryType.class)
        @Column(columnDefinition = "jsonb")
        private JsonNode attributes;

    }