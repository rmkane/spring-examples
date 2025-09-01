package org.example.spring.elasticsearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.elasticsearch.model.Product;
import org.example.spring.elasticsearch.service.ProductService;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("Creating new product: {}", product.getName());
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        log.info("Getting product with id: {}", id);
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Getting all products");
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        log.info("Updating product with id: {}", id);
        Optional<Product> existingProduct = productService.findById(id);
        if (existingProduct.isPresent()) {
            product.setId(id);
            Product updatedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("Deleting product with id: {}", id);
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        log.info("Getting products by category: {}", category);
        List<Product> products = productService.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        log.info("Getting products by brand: {}", brand);
        List<Product> products = productService.findByBrand(brand);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("Getting products by price range: {} - {}", minPrice, maxPrice);
        List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getProductsInStock(@RequestParam(defaultValue = "0") Integer minQuantity) {
        log.info("Getting products in stock with minimum quantity: {}", minQuantity);
        List<Product> products = productService.findInStock(minQuantity);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        log.info("Getting active products");
        List<Product> products = productService.findActiveProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Product>> searchByName(@RequestParam String name) {
        log.info("Searching products by name: {}", name);
        List<Product> products = productService.searchByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<Product>> searchByDescription(@RequestParam String description) {
        log.info("Searching products by description: {}", description);
        List<Product> products = productService.searchByDescription(description);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/full-text")
    public ResponseEntity<SearchHits<Product>> fullTextSearch(@RequestParam String query) {
        log.info("Performing full-text search with query: {}", query);
        SearchHits<Product> searchHits = productService.fullTextSearch(query);
        return ResponseEntity.ok(searchHits);
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<SearchHits<Product>> advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        log.info("Performing advanced search - name: {}, category: {}, price: {} - {}",
                name, category, minPrice, maxPrice);
        SearchHits<Product> searchHits = productService.advancedSearch(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(searchHits);
    }
}
