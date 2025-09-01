package org.example.spring.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.elasticsearch.model.Product;
import org.example.spring.elasticsearch.repository.ProductRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public Product saveProduct(Product product) {
        log.info("Saving product: {}", product.getName());

        // Set default values if not already set
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now().toString());
        }
        if (product.getActive() == null) {
            product.setActive(true);
        }

        return productRepository.save(product);
    }

    public Optional<Product> findById(String id) {
        log.info("Finding product by id: {}", id);
        return productRepository.findById(id);
    }

    public List<Product> findAll() {
        log.info("Finding all products");
        Iterable<Product> products = productRepository.findAll();
        List<Product> productList = new ArrayList<>();
        products.forEach(productList::add);
        return productList;
    }

    public void deleteProduct(String id) {
        log.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }

    public List<Product> findByCategory(String category) {
        log.info("Finding products by category: {}", category);
        return productRepository.findByCategory(category);
    }

    public List<Product> findByBrand(String brand) {
        log.info("Finding products by brand: {}", brand);
        return productRepository.findByBrand(brand);
    }

    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Finding products by price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> findInStock(Integer minQuantity) {
        log.info("Finding products in stock with minimum quantity: {}", minQuantity);
        return productRepository.findByStockQuantityGreaterThan(minQuantity);
    }

    public List<Product> findActiveProducts() {
        log.info("Finding active products");
        return productRepository.findByActiveTrue();
    }

    public List<Product> searchByName(String name) {
        log.info("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> searchByDescription(String description) {
        log.info("Searching products by description: {}", description);
        return productRepository.findByDescriptionContainingIgnoreCase(description);
    }

        public SearchHits<Product> fullTextSearch(String query) {
        log.info("Performing full-text search with query: {}", query);

        String searchQuery = String.format("""
            {
              "multi_match": {
                "query": "%s",
                "fields": ["name^2.0", "description^1.0", "category^1.5", "brand^1.5"]
              }
            }
            """, query);

        Query queryObj = new StringQuery(searchQuery);
        return elasticsearchOperations.search(queryObj, Product.class);
    }

        public SearchHits<Product> advancedSearch(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Performing advanced search - name: {}, category: {}, price: {} - {}",
                name, category, minPrice, maxPrice);

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("{ \"bool\": { \"must\": [");

        boolean hasConditions = false;

        if (name != null && !name.trim().isEmpty()) {
            if (hasConditions) queryBuilder.append(", ");
            queryBuilder.append(String.format("""
                {
                  "wildcard": {
                    "name": {
                      "value": "*%s*"
                    }
                  }
                }
                """, name));
            hasConditions = true;
        }

        if (category != null && !category.trim().isEmpty()) {
            if (hasConditions) queryBuilder.append(", ");
            queryBuilder.append(String.format("""
                {
                  "term": {
                    "category": "%s"
                  }
                }
                """, category));
            hasConditions = true;
        }

        if (minPrice != null && maxPrice != null) {
            if (hasConditions) queryBuilder.append(", ");
            queryBuilder.append(String.format("""
                {
                  "range": {
                    "price": {
                      "gte": %s,
                      "lte": %s
                    }
                  }
                }
                """, minPrice, maxPrice));
            hasConditions = true;
        } else if (minPrice != null) {
            if (hasConditions) queryBuilder.append(", ");
            queryBuilder.append(String.format("""
                {
                  "range": {
                    "price": {
                      "gte": %s
                    }
                  }
                }
                """, minPrice));
            hasConditions = true;
        } else if (maxPrice != null) {
            if (hasConditions) queryBuilder.append(", ");
            queryBuilder.append(String.format("""
                {
                  "range": {
                    "price": {
                      "lte": %s
                    }
                  }
                }
                """, maxPrice));
            hasConditions = true;
        }

        queryBuilder.append("] } }");

        Query queryObj = new StringQuery(queryBuilder.toString());
        return elasticsearchOperations.search(queryObj, Product.class);
    }
}
