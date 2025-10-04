package org.foo.elasticsearch.repository;

import org.foo.elasticsearch.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByStockQuantityGreaterThan(Integer quantity);

    List<Product> findByActiveTrue();

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByDescriptionContainingIgnoreCase(String description);
}
