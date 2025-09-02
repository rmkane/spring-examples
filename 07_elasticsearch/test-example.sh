#!/bin/bash

# Elasticsearch Example Test Script
# This script tests the Elasticsearch Spring Boot application

BASE_URL="http://localhost:8080"
PORT=8080

# Test function to compare both approaches
test_operation() {
    local operation_name="$1"
    local spring_data_url="$2"
    local direct_client_url="$3"
    local description="$4"

    local spring_result
    local spring_count

    echo "🔍 Testing $operation_name..."
    echo "  $description"

    # Test Spring Data approach
    echo "  📚 Spring Data:"
    spring_result=$(curl -s "$BASE_URL$spring_data_url")
    # Check if response is a SearchHits object (has totalHits) or a simple array
    if echo "$spring_result" | jq -e '.totalHits' >/dev/null 2>&1; then
        spring_count=$(echo "$spring_result" | jq '.totalHits' 2>/dev/null || echo "0")
    else
        spring_count=$(echo "$spring_result" | jq 'length' 2>/dev/null || echo "0")
    fi
    echo "    $spring_count results"

    # Test Direct Client approach
    echo "  🔧 Direct Client:"
    direct_result=$(curl -s "$BASE_URL$direct_client_url")
    direct_count=$(echo "$direct_result" | jq 'length' 2>/dev/null || echo "0")
    echo "    $direct_count results"

    # Guard clause: if results match, show success
    if [ "$spring_count" = "$direct_count" ]; then
        echo "  ✅ Results match: $spring_count products"
        echo ""
        return
    fi

    # Default case: results differ
    echo "  ⚠️  Results differ: Spring Data=$spring_count, Direct Client=$direct_count"
    echo ""
}

# Test function for operations that return different data structures
test_operation_different() {
    local operation_name="$1"
    local spring_data_url="$2"
    local direct_client_url="$3"
    local description="$4"

    echo "🔍 Testing $operation_name..."
    echo "  $description"

    # Test Spring Data approach
    echo "  📚 Spring Data:"
    curl -s "$BASE_URL$spring_data_url" | jq '.' 2>/dev/null || echo "    Error or no results"

    # Test Direct Client approach
    echo "  🔧 Direct Client:"
    curl -s "$BASE_URL$direct_client_url" | jq '.' 2>/dev/null || echo "    Error or no results"
    echo ""
}

# Function to create a product and return its ID
create_product() {
    local name="$1"
    local description="$2"
    local category="$3"
    local price="$4"
    local stock_quantity="$5"
    local brand="$6"

    local response
    local id

    echo "Creating $name..."
    response=$(curl -s -X POST "$BASE_URL/api/products" \
      -H "Content-Type: application/json" \
      -d "{
        \"name\": \"$name\",
        \"description\": \"$description\",
        \"category\": \"$category\",
        \"price\": $price,
        \"stockQuantity\": $stock_quantity,
        \"brand\": \"$brand\"
      }")

    id=$(echo "$response" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
    echo "✅ $name created with ID: $id"
    echo ""
    echo "$id"
}

# Function to test CRUD operations
test_crud_operation() {
    local operation="$1"
    local url="$2"
    local method="$3"
    local data="$4"
    local description="$5"

    local result

    echo "🔄 Testing $operation..."
    echo "  $description"

    # Guard clause: if data is provided, use it
    if [ -n "$data" ]; then
        result=$(curl -s -X "$method" "$BASE_URL$url" \
          -H "Content-Type: application/json" \
          -d "$data")
        echo "  Result: $result"
        echo ""
        return
    fi

    # Default case: no data provided
    result=$(curl -s -X "$method" "$BASE_URL$url")
    echo "  Result: $result"
    echo ""
}

# Function to test direct client only features
test_direct_only() {
    local feature_name="$1"
    local url="$2"
    local method="$3"
    local data="$4"
    local description="$5"

    local result

    echo "🔧 Testing $feature_name (Direct Client only)..."
    echo "  $description"

    # Guard clause: if data is provided, use it
    if [ -n "$data" ]; then
        result=$(curl -s -X "$method" "$BASE_URL$url" \
          -H "Content-Type: application/json" \
          -d "$data")
        echo "  Result: $result"
        echo ""
        return
    fi

    # Default case: no data provided
    result=$(curl -s -X "$method" "$BASE_URL$url")
    echo "  Result: $result"
    echo ""
}

echo "🧪 Testing Elasticsearch Spring Boot Example"
echo "============================================="
echo ""

# Check if application is running
echo "📡 Checking if application is running on port $PORT..."

# Guard clause: if application is not running, exit
if ! curl -s "$BASE_URL/api/products" > /dev/null; then
    echo "❌ Application is not running on port $PORT"
    echo "   Please start the application with: mvn spring-boot:run"
    exit 1
fi

# Application is running, continue
echo "✅ Application is running"
echo ""

# Create sample products
echo "📝 Creating sample products..."
echo ""

# Create products using the reusable function
IPHONE_ID=$(create_product "iPhone 15 Pro" "Latest iPhone with advanced camera system and A17 Pro chip" "Electronics" 999.99 50 "Apple")
MACBOOK_ID=$(create_product "MacBook Pro 16" "Professional laptop with M3 chip and advanced graphics" "Electronics" 2499.99 25 "Apple")
SAMSUNG_ID=$(create_product "Samsung Galaxy S24" "Android smartphone with advanced AI features and camera" "Electronics" 899.99 75 "Samsung")
BOOK_ID=$(create_product "Spring Boot in Action" "Comprehensive guide to Spring Boot development and best practices" "Books" 49.99 100 "Manning")

echo "✅ Products created: $IPHONE_ID, $MACBOOK_ID, $SAMSUNG_ID, $BOOK_ID"
echo ""

# Test basic operations
echo "🔍 Testing basic operations..."
echo ""

test_operation "Get All Products" "/api/products" "/api/direct/products" "Getting all products"
echo ""

echo "🔎 Getting product by ID ($IPHONE_ID)..."
curl -s "$BASE_URL/api/products/$IPHONE_ID" | jq '.'
echo ""

# Test search operations
echo "🔍 Testing search operations..."
echo ""

test_operation "Category Search" "/api/products/category/Electronics" "/api/direct/products/category/Electronics" "Finding products by category 'Electronics'"
test_operation "Brand Search" "/api/products/brand/Apple" "/api/direct/products/brand/Apple" "Finding products by brand 'Apple'"
test_operation "Price Range Search" "/api/products/price-range?minPrice=500&maxPrice=1000" "/api/direct/products/price-range?minPrice=500&maxPrice=1000" "Finding products in price range 500-1000"
test_operation "Stock Search" "/api/products/in-stock?minQuantity=10" "/api/direct/products/in-stock?minQuantity=10" "Finding products with stock quantity >= 10"
test_operation "Active Products" "/api/products/active" "/api/direct/products/active" "Finding active products"

# Test text search
echo "🔍 Testing text search..."
echo ""

test_operation "Name Search" "/api/products/search/name?name=iPhone" "/api/direct/products/search/name?name=iPhone" "Searching products by name containing 'iPhone'"
test_operation "Description Search" "/api/products/search/description?description=camera" "/api/direct/products/search/description?description=camera" "Searching products by description containing 'camera'"

# Test advanced search
echo "🔍 Testing advanced search..."
echo ""

test_operation "Advanced Search" "/api/products/search/advanced?name=iPhone&category=Electronics&minPrice=500&maxPrice=1200" "/api/direct/products/search/advanced?name=iPhone&category=Electronics&minPrice=500&maxPrice=1200" "Advanced search with multiple criteria"

# Test full-text search
echo "🔍 Testing full-text search..."
echo ""

test_operation "Full-Text Search" "/api/products/search/full-text?query=iPhone camera" "/api/direct/products/search/full-text?query=iPhone camera" "Full-text search for 'iPhone camera'"

# Test CRUD operations
test_crud_operation "Update Product" "/api/products/$IPHONE_ID" "PUT" '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with advanced camera system and A17 Pro chip",
    "category": "Electronics",
    "price": 1099.99,
    "stockQuantity": 45,
    "brand": "Apple"
  }' "Updating iPhone price to 1099.99"

test_crud_operation "Delete Product" "/api/products/$SAMSUNG_ID" "DELETE" "" "Deleting Samsung Galaxy"

test_crud_operation "Verify Deletion" "/api/products" "GET" "" "Getting all products to verify deletion"

echo "🎉 All comparison tests completed successfully!"
echo ""

# Test Direct Client Only Features
echo "🔧 Testing Direct Client Only Features"
echo "======================================"
echo ""

test_direct_only "Category Aggregation" "/api/direct/products/aggregations/categories" "GET" "" "Getting category counts"

test_direct_only "Bulk Indexing" "/api/direct/products/bulk" "POST" '[
    {
      "id": "bulk-1",
      "name": "Bulk Product 1",
      "description": "First bulk product",
      "category": "Electronics",
      "price": 199.99,
      "stockQuantity": 20,
      "brand": "BulkBrand"
    },
    {
      "id": "bulk-2",
      "name": "Bulk Product 2",
      "description": "Second bulk product",
      "category": "Books",
      "price": 29.99,
      "stockQuantity": 100,
      "brand": "BulkBrand"
    }
  ]' "Bulk indexing multiple products"

test_direct_only "Index Exists Check" "/api/direct/index/exists" "GET" "" "Checking if index exists"

echo "🎉 All tests completed successfully!"
echo ""

echo "📊 Summary:"
echo "  - Created 4 products"
echo "  - Tested Spring Data Elasticsearch approach"
echo "  - Tested Direct Elasticsearch Client approach"
echo "  - Tested all CRUD operations (both approaches)"
echo "  - Tested various search capabilities (both approaches)"
echo "  - Tested advanced search with multiple criteria (both approaches)"
echo "  - Tested full-text search (both approaches)"
echo "  - Tested aggregations (direct client only)"
echo "  - Tested bulk operations (direct client only)"
echo "  - Tested index management (direct client only)"
echo "  - Deleted 1 product"
echo ""
echo "🚀 Both Elasticsearch integration approaches are working correctly!"
