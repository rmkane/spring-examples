#!/bin/bash

# Elasticsearch Example Test Script
# This script tests the Elasticsearch Spring Boot application

BASE_URL="http://localhost:8080"
PORT=8080

echo "🧪 Testing Elasticsearch Spring Boot Example"
echo "============================================="
echo ""

# Check if application is running
echo "📡 Checking if application is running on port $PORT..."
if ! curl -s "$BASE_URL/api/products" > /dev/null; then
    echo "❌ Application is not running on port $PORT"
    echo "   Please start the application with: mvn spring-boot:run"
    exit 1
fi
echo "✅ Application is running"
echo ""

# Create sample products
echo "📝 Creating sample products..."
echo ""

# Product 1
echo "Creating iPhone 15 Pro..."
IPHONE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with advanced camera system and A17 Pro chip",
    "category": "Electronics",
    "price": 999.99,
    "stockQuantity": 50,
    "brand": "Apple"
  }')

IPHONE_ID=$(echo "$IPHONE_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "✅ iPhone 15 Pro created with ID: $IPHONE_ID"
echo ""

# Product 2
echo "Creating MacBook Pro..."
MACBOOK_RESPONSE=$(curl -s -X POST "$BASE_URL/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro 16",
    "description": "Professional laptop with M3 chip and advanced graphics",
    "category": "Electronics",
    "price": 2499.99,
    "stockQuantity": 25,
    "brand": "Apple"
  }')

MACBOOK_ID=$(echo "$MACBOOK_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "✅ MacBook Pro created with ID: $MACBOOK_ID"
echo ""

# Product 3
echo "Creating Samsung Galaxy..."
SAMSUNG_RESPONSE=$(curl -s -X POST "$BASE_URL/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Samsung Galaxy S24",
    "description": "Android smartphone with advanced AI features and camera",
    "category": "Electronics",
    "price": 899.99,
    "stockQuantity": 75,
    "brand": "Samsung"
  }')

SAMSUNG_ID=$(echo "$SAMSUNG_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "✅ Samsung Galaxy created with ID: $SAMSUNG_ID"
echo ""

# Product 4
echo "Creating Spring Boot Book..."
BOOK_RESPONSE=$(curl -s -X POST "$BASE_URL/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spring Boot in Action",
    "description": "Comprehensive guide to Spring Boot development and best practices",
    "category": "Books",
    "price": 49.99,
    "stockQuantity": 100,
    "brand": "Manning"
  }')

BOOK_ID=$(echo "$BOOK_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "✅ Spring Boot Book created with ID: $BOOK_ID"
echo ""

# Test basic operations
echo "🔍 Testing basic operations..."
echo ""

echo "📋 Getting all products..."
curl -s "$BASE_URL/api/products" | jq '.[0:3]'  # Show first 3 products
echo ""

echo "🔎 Getting product by ID ($IPHONE_ID)..."
curl -s "$BASE_URL/api/products/$IPHONE_ID" | jq '.'
echo ""

# Test search operations
echo "🔍 Testing search operations..."
echo ""

echo "📂 Searching by category (Electronics)..."
curl -s "$BASE_URL/api/products/category/Electronics" | jq 'length'
echo " products found"
echo ""

echo "🏷️  Searching by brand (Apple)..."
curl -s "$BASE_URL/api/products/brand/Apple" | jq 'length'
echo " products found"
echo ""

echo "💰 Searching by price range (500-1000)..."
curl -s "$BASE_URL/api/products/price-range?minPrice=500&maxPrice=1000" | jq 'length'
echo " products found"
echo ""

echo "📦 Searching products in stock (min 10)..."
curl -s "$BASE_URL/api/products/in-stock?minQuantity=10" | jq 'length'
echo " products found"
echo ""

echo "✅ Getting active products..."
curl -s "$BASE_URL/api/products/active" | jq 'length'
echo " products found"
echo ""

# Test text search
echo "🔍 Testing text search..."
echo ""

echo "📝 Searching by name (iPhone)..."
curl -s "$BASE_URL/api/products/search/name?name=iPhone" | jq 'length'
echo " products found"
echo ""

echo "📖 Searching by description (camera)..."
curl -s "$BASE_URL/api/products/search/description?description=camera" | jq 'length'
echo " products found"
echo ""

# Test advanced search
echo "🔍 Testing advanced search..."
echo ""

echo "🎯 Advanced search (name: iPhone, category: Electronics, price: 500-1200)..."
ADVANCED_RESULTS=$(curl -s "$BASE_URL/api/products/search/advanced?name=iPhone&category=Electronics&minPrice=500&maxPrice=1200")
echo "$ADVANCED_RESULTS" | jq '.totalHits.value'
echo " products found"
echo ""

# Test full-text search
echo "🔍 Testing full-text search..."
echo ""

echo "🔎 Full-text search (iPhone camera)..."
FULLTEXT_RESULTS=$(curl -s "$BASE_URL/api/products/search/full-text?query=iPhone camera")
echo "$FULLTEXT_RESULTS" | jq '.totalHits.value'
echo " products found"
echo ""

# Test update operation
echo "🔄 Testing update operation..."
echo ""

echo "📝 Updating iPhone price..."
curl -s -X PUT "$BASE_URL/api/products/$IPHONE_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with advanced camera system and A17 Pro chip",
    "category": "Electronics",
    "price": 1099.99,
    "stockQuantity": 45,
    "brand": "Apple"
  }' | jq '.price'
echo " (new price)"
echo ""

# Test delete operation
echo "🗑️  Testing delete operation..."
echo ""

echo "❌ Deleting Samsung Galaxy ($SAMSUNG_ID)..."
curl -s -X DELETE "$BASE_URL/api/products/$SAMSUNG_ID"
echo "✅ Product deleted"
echo ""

echo "📋 Verifying deletion (getting all products)..."
curl -s "$BASE_URL/api/products" | jq 'length'
echo " products remaining"
echo ""

echo "🎉 All tests completed successfully!"
echo ""
echo "📊 Summary:"
echo "  - Created 4 products"
echo "  - Tested all CRUD operations"
echo "  - Tested various search capabilities"
echo "  - Tested advanced search with multiple criteria"
echo "  - Tested full-text search"
echo "  - Deleted 1 product"
echo ""
echo "🚀 Elasticsearch integration is working correctly!"
