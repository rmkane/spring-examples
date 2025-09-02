package org.example.spring.elasticsearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.elasticsearch.model.Product;
import org.example.spring.elasticsearch.service.DirectElasticsearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/direct")
@RequiredArgsConstructor
public class DirectElasticsearchController {

    private final DirectElasticsearchService directElasticsearchService;

    /**
     * Get all products using direct Elasticsearch client
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("GET /api/direct/products - Using direct client");
        List<Product> products = directElasticsearchService.findAllDirect();
        return ResponseEntity.ok(products);
    }

    /**
     * Get product by ID using direct Elasticsearch client
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        log.info("GET /api/direct/products/{} - Using direct client", id);
        Product product = directElasticsearchService.getProductByIdDirect(id);

        // Guard clause: if product not found, return 404
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    /**
     * Index a product using direct Elasticsearch client
     */
    @PostMapping("/products")
    public ResponseEntity<String> indexProduct(@RequestBody Product product) {
        log.info("POST /api/direct/products - Using direct client");
        String id = directElasticsearchService.indexProductDirect(product);
        return ResponseEntity.ok(id);
    }

    /**
     * Delete a product using direct Elasticsearch client
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("DELETE /api/direct/products/{} - Using direct client", id);
        boolean deleted = directElasticsearchService.deleteProductDirect(id);

        // Guard clause: if deletion failed, return 404
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Full-text search using direct Elasticsearch client
     */
    @GetMapping("/products/search/full-text")
    public ResponseEntity<List<Product>> fullTextSearch(@RequestParam String query) {
        log.info("GET /api/direct/products/search/full-text?query={} - Using direct client", query);
        List<Product> products = directElasticsearchService.fullTextSearchDirect(query);
        return ResponseEntity.ok(products);
    }

    /**
     * Find by category using direct Elasticsearch client
     */
    @GetMapping("/products/category/{category}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable String category) {
        log.info("GET /api/direct/products/category/{} - Using direct client", category);
        List<Product> products = directElasticsearchService.findByCategoryDirect(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Find by brand using direct Elasticsearch client
     */
    @GetMapping("/products/brand/{brand}")
    public ResponseEntity<List<Product>> findByBrand(@PathVariable String brand) {
        log.info("GET /api/direct/products/brand/{} - Using direct client", brand);
        List<Product> products = directElasticsearchService.findByBrandDirect(brand);
        return ResponseEntity.ok(products);
    }

    /**
     * Find by price range using direct Elasticsearch client
     */
    @GetMapping("/products/price-range")
    public ResponseEntity<List<Product>> findByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("GET /api/direct/products/price-range?minPrice={}&maxPrice={} - Using direct client", minPrice, maxPrice);
        List<Product> products = directElasticsearchService.findByPriceRangeDirect(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Find in stock using direct Elasticsearch client
     */
    @GetMapping("/products/in-stock")
    public ResponseEntity<List<Product>> findInStock(@RequestParam Integer minQuantity) {
        log.info("GET /api/direct/products/in-stock?minQuantity={} - Using direct client", minQuantity);
        List<Product> products = directElasticsearchService.findInStockDirect(minQuantity);
        return ResponseEntity.ok(products);
    }

    /**
     * Find active products using direct Elasticsearch client
     */
    @GetMapping("/products/active")
    public ResponseEntity<List<Product>> findActiveProducts() {
        log.info("GET /api/direct/products/active - Using direct client");
        List<Product> products = directElasticsearchService.findActiveProductsDirect();
        return ResponseEntity.ok(products);
    }

    /**
     * Search by name using direct Elasticsearch client
     */
    @GetMapping("/products/search/name")
    public ResponseEntity<List<Product>> searchByName(@RequestParam String name) {
        log.info("GET /api/direct/products/search/name?name={} - Using direct client", name);
        List<Product> products = directElasticsearchService.searchByNameDirect(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Search by description using direct Elasticsearch client
     */
    @GetMapping("/products/search/description")
    public ResponseEntity<List<Product>> searchByDescription(@RequestParam String description) {
        log.info("GET /api/direct/products/search/description?description={} - Using direct client", description);
        List<Product> products = directElasticsearchService.searchByDescriptionDirect(description);
        return ResponseEntity.ok(products);
    }

    /**
     * Advanced search using direct Elasticsearch client
     */
    @GetMapping("/products/search/advanced")
    public ResponseEntity<List<Product>> advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        log.info("GET /api/direct/products/search/advanced - Using direct client");
        List<Product> products = directElasticsearchService.advancedSearchDirect(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Get category aggregation using direct Elasticsearch client
     */
    @GetMapping("/products/aggregations/categories")
    public ResponseEntity<Map<String, Long>> getCategoryAggregation() {
        log.info("GET /api/direct/products/aggregations/categories - Using direct client");
        Map<String, Long> categoryCounts = directElasticsearchService.getCategoryAggregationDirect();
        return ResponseEntity.ok(categoryCounts);
    }

    /**
     * Bulk index products using direct Elasticsearch client
     */
    @PostMapping("/products/bulk")
    public ResponseEntity<Void> bulkIndexProducts(@RequestBody List<Product> products) {
        log.info("POST /api/direct/products/bulk - Using direct client for {} products", products.size());
        directElasticsearchService.bulkIndexProductsDirect(products);
        return ResponseEntity.ok().build();
    }

    /**
     * Update product price using direct Elasticsearch client
     */
    @PutMapping("/products/{id}/price")
    public ResponseEntity<Void> updateProductPrice(
            @PathVariable String id,
            @RequestParam BigDecimal newPrice) {
        log.info("PUT /api/direct/products/{}/price?newPrice={} - Using direct client", id, newPrice);
        boolean updated = directElasticsearchService.updateProductPriceDirect(id, newPrice);

        // Guard clause: if update failed, return 404
        if (!updated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Check if index exists using direct Elasticsearch client
     */
    @GetMapping("/index/exists")
    public ResponseEntity<Boolean> indexExists() {
        log.info("GET /api/direct/index/exists - Using direct client");
        boolean exists = directElasticsearchService.indexExistsDirect();
        return ResponseEntity.ok(exists);
    }

    /**
     * Create index using direct Elasticsearch client
     */
    @PostMapping("/index")
    public ResponseEntity<Void> createIndex() {
        log.info("POST /api/direct/index - Using direct client");
        boolean created = directElasticsearchService.createIndexDirect();

        // Guard clause: if creation failed, return 500
        if (!created) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
