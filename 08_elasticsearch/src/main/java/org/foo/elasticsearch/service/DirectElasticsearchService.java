package org.foo.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foo.elasticsearch.model.Product;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectElasticsearchService {

    private final ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "products";

    /**
     * Direct client: Get document by ID using the raw Elasticsearch client
     */
    public Product getProductByIdDirect(String id) {
        log.info("Getting product by ID using direct client: {}", id);

        try {
            GetResponse<Product> response = elasticsearchClient.get(
                g -> g.index(INDEX_NAME).id(id),
                Product.class
            );

            // Guard clause: if product not found, return null
            if (!response.found()) {
                log.warn("Product not found with ID: {}", id);
                return null;
            }

            return response.source();
        } catch (IOException e) {
            log.error("Error getting product by ID: {}", id, e);
            throw new RuntimeException("Failed to get product", e);
        }
    }

    /**
     * Direct client: Save product using the raw Elasticsearch client
     */
    public Product saveProductDirect(Product product) {
        log.info("Saving product using direct client: {}", product.getName());

        try {
            IndexResponse response = elasticsearchClient.index(
                i -> i.index(INDEX_NAME)
                    .id(product.getId())
                    .document(product)
            );

            log.info("Product saved successfully with ID: {}", response.id());
            return product;
        } catch (IOException e) {
            log.error("Error saving product: {}", product.getName(), e);
            throw new RuntimeException("Failed to save product", e);
        }
    }

    /**
     * Direct client: Find all products using the raw Elasticsearch client
     */
    public List<Product> findAllDirect() {
        log.info("Finding all products using direct client");

        try {
            SearchResponse<Product> response = elasticsearchClient.search(
                s -> s.index(INDEX_NAME)
                    .size(10000), // Increased size to handle more documents
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error finding all products", e);
            throw new RuntimeException("Failed to find all products", e);
        }
    }

    /**
     * Direct client: Index a document using the raw Elasticsearch client
     */
    public String indexProductDirect(Product product) {
        log.info("Indexing product using direct client: {}", product.getName());

        try {
            IndexResponse response = elasticsearchClient.index(
                i -> i.index(INDEX_NAME)
                    .id(product.getId())
                    .document(product)
            );

            log.info("Product indexed successfully with ID: {}", response.id());
            return response.id();
        } catch (IOException e) {
            log.error("Error indexing product: {}", product.getName(), e);
            throw new RuntimeException("Failed to index product", e);
        }
    }

    /**
     * Direct client: Delete document using the raw Elasticsearch client
     */
    public boolean deleteProductDirect(String id) {
        log.info("Deleting product using direct client: {}", id);

        try {
            DeleteResponse response = elasticsearchClient.delete(
                d -> d.index(INDEX_NAME).id(id)
            );

            boolean deleted = response.result().name().equals("DELETED");
            log.info("Product deletion result: {}", deleted);
            return deleted;
        } catch (IOException e) {
            log.error("Error deleting product: {}", id, e);
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    /**
     * Direct client: Full-text search using the raw Elasticsearch client
     */
    public List<Product> fullTextSearchDirect(String query) {
        log.info("Performing full-text search using direct client with query: {}", query);

        try {
            SearchResponse<Product> response = elasticsearchClient.search(
                s -> s.index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .multiMatch(m -> m
                            .query(query)
                            .fields("name^2.0", "description^1.0", "category^1.5", "brand^1.5")
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products using full-text search with direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error in full-text search: {}", query, e);
            throw new RuntimeException("Failed to perform full-text search", e);
        }
    }

    /**
     * Direct client: Find by category using the raw Elasticsearch client
     */
    public List<Product> findByCategoryDirect(String category) {
        log.info("Finding products by category using direct client: {}", category);

        try {
            SearchResponse<Product> response = elasticsearchClient.search(
                s -> s.index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .term(t -> t
                            .field("category")
                            .value(category)
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products in category using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error finding by category: {}", category, e);
            throw new RuntimeException("Failed to find by category", e);
        }
    }

    /**
     * Direct client: Find by brand using the raw Elasticsearch client
     */
    public List<Product> findByBrandDirect(String brand) {
        log.info("Finding products by brand using direct client: {}", brand);

        try {
            SearchResponse<Product> response = elasticsearchClient.search(
                s -> s.index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .term(t -> t
                            .field("brand")
                            .value(brand)
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products by brand using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error finding by brand: {}", brand, e);
            throw new RuntimeException("Failed to find by brand", e);
        }
    }

    /**
     * Direct client: Find by price range using the raw Elasticsearch client
     */
    public List<Product> findByPriceRangeDirect(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Finding products by price range using direct client: {} - {}", minPrice, maxPrice);

        try {
            SearchResponse<Product> response = elasticsearchClient
                .search(s -> s
                    .index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .range(rq -> rq
                            .number(nrq -> nrq
                                .field("price")
                                .gte(minPrice.doubleValue())
                                .lte(maxPrice.doubleValue())
                            )
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products in price range using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error finding by price range: {} - {}", minPrice, maxPrice, e);
            throw new RuntimeException("Failed to find by price range", e);
        }
    }

    /**
     * Direct client: Find in stock using the raw Elasticsearch client
     */
    public List<Product> findInStockDirect(Integer minQuantity) {
        log.info("Finding products in stock using direct client with minimum quantity: {}", minQuantity);

        try {
            SearchResponse<Product> response = elasticsearchClient
                .search(s -> s
                    .index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .range(rq -> rq
                            .number(nrq -> nrq
                                .field("stockQuantity")
                                .gte(minQuantity.doubleValue())
                            )
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products in stock using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error finding in stock: {}", minQuantity, e);
            throw new RuntimeException("Failed to find in stock", e);
        }
    }

    /**
     * Direct client: Find active products using the raw Elasticsearch client
     */
    public List<Product> findActiveProductsDirect() {
        log.info("Finding active products using direct client");

        try {
            SearchResponse<Product> response = elasticsearchClient
                .search(s -> s
                    .index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .term(t -> t
                            .field("active")
                            .value(true)
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} active products using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error finding active products", e);
            throw new RuntimeException("Failed to find active products", e);
        }
    }

    /**
     * Direct client: Search by name using the raw Elasticsearch client
     */
    public List<Product> searchByNameDirect(String name) {
        log.info("Searching products by name using direct client: {}", name);

        try {
            SearchResponse<Product> response = elasticsearchClient
                .search(s -> s
                    .index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .match(m -> m
                            .field("name")
                            .query(name)
                            .fuzziness("AUTO")
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products by name using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error searching by name: {}", name, e);
            throw new RuntimeException("Failed to search by name", e);
        }
    }

    /**
     * Direct client: Search by description using the raw Elasticsearch client
     */
    public List<Product> searchByDescriptionDirect(String description) {
        log.info("Searching products by description using direct client: {}", description);

        try {
            SearchResponse<Product> response = elasticsearchClient
                .search(s -> s
                    .index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .match(m -> m
                            .field("description")
                            .query(description)
                            .fuzziness("AUTO")
                        )
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products by description using direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error searching by description: {}", description, e);
            throw new RuntimeException("Failed to search by description", e);
        }
    }

    /**
     * Direct client: Advanced search using the raw Elasticsearch client
     */
    public List<Product> advancedSearchDirect(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Performing advanced search using direct client - name: {}, category: {}, price: {} - {}",
                name, category, minPrice, maxPrice);

        try {
            SearchResponse<Product> response = elasticsearchClient
                .search(s -> s
                    .index(INDEX_NAME)
                    .size(10000) // Increased size to handle more documents
                    .query(q -> q
                        .bool(b -> {
                            // Add name query_string query if provided
                            if (name != null && !name.trim().isEmpty()) {
                                b.must(m -> m
                                    .queryString(qs -> qs
                                        .query("*" + name + "*")
                                        .fields("name")
                                    )
                                );
                            }

                            // Add category term query if provided
                            if (category != null && !category.trim().isEmpty()) {
                                b.must(m -> m
                                    .term(t -> t
                                        .field("category")
                                        .value(category)
                                    )
                                );
                            }

                            // Add price range query if provided
                            if (minPrice != null || maxPrice != null) {
                                b.must(m -> m
                                    .range(rq -> rq
                                        .number(nrq -> {
                                            nrq.field("price");
                                            if (minPrice != null) {
                                                nrq.gte(minPrice.doubleValue());
                                            }
                                            if (maxPrice != null) {
                                                nrq.lte(maxPrice.doubleValue());
                                            }
                                            return nrq;
                                        })
                                    )
                                );
                            }
                            return b;
                        })
                    ),
                Product.class
            );

            List<Product> products = new ArrayList<>();
            for (Hit<Product> hit : response.hits().hits()) {
                products.add(hit.source());
            }

            log.info("Found {} products using advanced search with direct client", products.size());
            return products;
        } catch (IOException e) {
            log.error("Error in advanced search", e);
            throw new RuntimeException("Failed to perform advanced search", e);
        }
    }

    /**
     * Direct client: Aggregation query to get category counts
     */
    public Map<String, Long> getCategoryAggregationDirect() {
        log.info("Getting category aggregation using direct client");

        try {
            SearchResponse<Void> response = elasticsearchClient.search(
                s -> s.index(INDEX_NAME)
                    .size(0) // Don't return documents, only aggregations
                    .aggregations("categories", a -> a
                        .terms(t -> t.field("category"))
                    ),
                Void.class
            );

            Map<String, Long> categoryCounts = new java.util.HashMap<>();
            var termsAgg = response.aggregations().get("categories").sterms();

            for (var bucket : termsAgg.buckets().array()) {
                categoryCounts.put(bucket.key().stringValue(), bucket.docCount());
            }

            log.info("Category aggregation results: {}", categoryCounts);
            return categoryCounts;
        } catch (IOException e) {
            log.error("Error getting category aggregation", e);
            throw new RuntimeException("Failed to get category aggregation", e);
        }
    }

    /**
     * Direct client: Bulk operations
     */
    public void bulkIndexProductsDirect(List<Product> products) {
        log.info("Bulk indexing {} products using direct client", products.size());

        try {
            BulkRequest.Builder br = new BulkRequest.Builder();

            for (Product product : products) {
                br.operations(op -> op
                    .index(idx -> idx
                        .index(INDEX_NAME)
                        .id(product.getId())
                        .document(product)
                    )
                );
            }

            BulkResponse result = elasticsearchClient.bulk(br.build());

            // Guard clause: if bulk operation had errors, log them
            if (result.errors()) {
                log.error("Bulk indexing had errors");
                for (var item : result.items()) {
                    ErrorCause error = item.error();
                    if (error != null) {
                        log.error("Error indexing item {}: {}", item.id(), error.reason());
                    }
                }
                return;
            }

            // Bulk operation completed successfully
            log.info("Bulk indexing completed successfully");
        } catch (IOException e) {
            log.error("Error in bulk indexing", e);
            throw new RuntimeException("Failed to bulk index products", e);
        }
    }

    /**
     * Direct client: Update document using script
     */
    public boolean updateProductPriceDirect(String id, BigDecimal newPrice) {
        log.info("Updating product price using direct client: {} -> {}", id, newPrice);

        try {
            elasticsearchClient.update(
                u -> u.index(INDEX_NAME)
                    .id(id)
                    .script(s -> s
                        .source("ctx._source.price = params.newPrice")
                        .params("newPrice", JsonData.of(newPrice.doubleValue()))
                    ),
                Product.class
            );

            log.info("Product price updated successfully");
            return true;
        } catch (IOException e) {
            log.error("Error updating product price: {}", id, e);
            throw new RuntimeException("Failed to update product price", e);
        }
    }

    /**
     * Direct client: Check if index exists
     */
    public boolean indexExistsDirect() {
        log.info("Checking if index exists using direct client");

        try {
            var response = elasticsearchClient.indices().exists(
                e -> e.index(INDEX_NAME)
            );

            boolean exists = response.value();
            log.info("Index {} exists: {}", INDEX_NAME, exists);
            return exists;
        } catch (IOException e) {
            log.error("Error checking index existence", e);
            throw new RuntimeException("Failed to check index existence", e);
        }
    }

    /**
     * Direct client: Create index with mapping
     */
    public boolean createIndexDirect() {
        log.info("Creating index using direct client");

        try {
            var response = elasticsearchClient.indices().create(
                c -> c.index(INDEX_NAME)
                    .mappings(m -> m
                        .properties("name", p -> p.text(t -> t.analyzer("standard")))
                        .properties("description", p -> p.text(t -> t.analyzer("standard")))
                        .properties("category", p -> p.keyword(k -> k))
                        .properties("brand", p -> p.keyword(k -> k))
                        .properties("price", p -> p.double_(d -> d))
                        .properties("stockQuantity", p -> p.integer(i -> i))
                        .properties("active", p -> p.boolean_(b -> b))
                        .properties("createdAt", p -> p.date(d -> d))
                    )
            );

            log.info("Index created successfully: {}", response.index());
            return true;
        } catch (IOException e) {
            log.error("Error creating index", e);
            throw new RuntimeException("Failed to create index", e);
        }
    }
}
