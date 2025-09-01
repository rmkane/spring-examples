# Spring Boot Elasticsearch Example

This example demonstrates how to integrate Spring Boot with Elasticsearch for document indexing, searching, and management.

## Features

- **Document Management**: CRUD operations for products
- **Search Capabilities**:
  - Full-text search across multiple fields
  - Category and brand filtering
  - Price range queries
  - Stock quantity filtering
  - Advanced search with multiple criteria
- **Elasticsearch Integration**: Using Spring Data Elasticsearch
- **REST API**: Complete RESTful endpoints for all operations

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker (for running Elasticsearch)

## Setup

### 1. Start Elasticsearch

Run Elasticsearch using Docker:

```bash
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  docker.elastic.co/elasticsearch/elasticsearch:8.11.0
```

### 2. Verify Elasticsearch is Running

```bash
curl -X GET "localhost:9200/_cluster/health?pretty"
```

You should see a response indicating the cluster is healthy.

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

### Product Management

#### Create Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with advanced camera system",
    "category": "Electronics",
    "price": 999.99,
    "stockQuantity": 50,
    "brand": "Apple"
  }'
```

#### Get All Products

```bash
curl -X GET http://localhost:8080/api/products
```

#### Get Product by ID

```bash
curl -X GET http://localhost:8080/api/products/{id}
```

#### Update Product

```bash
curl -X PUT http://localhost:8080/api/products/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro Max",
    "description": "Updated description",
    "category": "Electronics",
    "price": 1099.99,
    "stockQuantity": 45,
    "brand": "Apple"
  }'
```

#### Delete Product

```bash
curl -X DELETE http://localhost:8080/api/products/{id}
```

### Search Operations

#### Search by Category

```bash
curl -X GET "http://localhost:8080/api/products/category/Electronics"
```

#### Search by Brand

```bash
curl -X GET "http://localhost:8080/api/products/brand/Apple"
```

#### Search by Price Range

```bash
curl -X GET "http://localhost:8080/api/products/price-range?minPrice=500&maxPrice=1000"
```

#### Search Products in Stock

```bash
curl -X GET "http://localhost:8080/api/products/in-stock?minQuantity=10"
```

#### Get Active Products

```bash
curl -X GET "http://localhost:8080/api/products/active"
```

#### Search by Name

```bash
curl -X GET "http://localhost:8080/api/products/search/name?name=iPhone"
```

#### Search by Description

```bash
curl -X GET "http://localhost:8080/api/products/search/description?description=camera"
```

#### Full-Text Search

```bash
curl -X GET "http://localhost:8080/api/products/search/full-text?query=iPhone camera"
```

#### Advanced Search

```bash
curl -X GET "http://localhost:8080/api/products/search/advanced?name=iPhone&category=Electronics&minPrice=500&maxPrice=1200"
```

## Sample Data

Here are some sample products you can create for testing:

### Electronics

```json
{
  "name": "MacBook Pro 16",
  "description": "Professional laptop with M3 chip",
  "category": "Electronics",
  "price": 2499.99,
  "stockQuantity": 25,
  "brand": "Apple"
}
```

```json
{
  "name": "Samsung Galaxy S24",
  "description": "Android smartphone with advanced AI features",
  "category": "Electronics",
  "price": 899.99,
  "stockQuantity": 75,
  "brand": "Samsung"
}
```

### Books

```json
{
  "name": "Spring Boot in Action",
  "description": "Comprehensive guide to Spring Boot development",
  "category": "Books",
  "price": 49.99,
  "stockQuantity": 100,
  "brand": "Manning"
}
```

```json
{
  "name": "Elasticsearch: The Definitive Guide",
  "description": "Complete guide to Elasticsearch and search technology",
  "category": "Books",
  "price": 39.99,
  "stockQuantity": 50,
  "brand": "O'Reilly"
}
```

## Architecture

- **Model Layer**: `Product` entity with Elasticsearch annotations
- **Repository Layer**: `ProductRepository` extending `ElasticsearchRepository`
- **Service Layer**: `ProductService` with business logic and search operations
- **Controller Layer**: `ProductController` exposing REST endpoints
- **Configuration**: `ElasticsearchConfig` for client setup and Jackson configuration

## Key Features Explained

### Document Mapping

The `Product` class uses Elasticsearch annotations to define field types:

- `@Document(indexName = "products")`: Specifies the index name
- `@Field(type = FieldType.Text, analyzer = "standard")`: Text fields for full-text search
- `@Field(type = FieldType.Keyword)`: Keyword fields for exact matching
- `@Field(type = FieldType.Double/Integer/Date/Boolean)`: Numeric and date fields

### Search Types

1. **Repository Methods**: Simple queries using method names
2. **Full-Text Search**: Multi-field search with field boosting
3. **Advanced Search**: Complex boolean queries with multiple criteria

### Field Boosting

In full-text search, the name field is boosted (2.0f) to give it higher relevance than description (1.0f).

## Troubleshooting

### Elasticsearch Connection Issues

- Ensure Elasticsearch is running on port 9200
- Check Docker container status: `docker ps`
- Verify cluster health: `curl localhost:9200/_cluster/health`

### Index Issues

- If the index doesn't exist, it will be created automatically when the first document is saved
- Check index status: `curl localhost:9200/products/_stats`

### Search Issues

- Ensure documents are properly indexed before searching
- Check Elasticsearch logs for query errors
- Use the debug logging level to see detailed Elasticsearch operations

## Cleanup

To stop and remove the Elasticsearch container:

```bash
docker stop elasticsearch
docker rm elasticsearch
```
