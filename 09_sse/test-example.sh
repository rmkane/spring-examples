#!/bin/bash

# SSE Example Test Script
echo "🚀 Building and running SSE example..."

# Build the project
echo "📦 Building project..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"

    # Run the SSE example
    echo "🎮 Starting SSE application..."
    echo "📍 Access the application at: http://localhost:8080/sse"
    echo "📡 SSE endpoint: http://localhost:8080/api/events"
    echo "🔍 API status: http://localhost:8080/api/status"
    echo ""
    echo "Press Ctrl+C to stop the application"
    echo ""

    cd 09_sse && mvn spring-boot:run
else
    echo "❌ Build failed!"
    exit 1
fi
