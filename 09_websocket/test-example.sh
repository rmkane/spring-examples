#!/bin/bash

echo "🚀 Starting WebSocket FizzBuzz Example"
echo "======================================"

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed or not in PATH"
    exit 1
fi

echo "✅ Java and Maven found"

# Build the project
echo "🔨 Building the project..."
mvn clean package -q

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo "✅ Build successful"

# Start the application
echo "🚀 Starting WebSocket application on port 8088..."
echo "📱 Open your browser to: http://localhost:8088"
echo "🔌 WebSocket endpoint: ws://localhost:8088/websocket/fizzbuzz"
echo ""
echo "💡 Instructions:"
echo "   1. Open http://localhost:8088 in your browser"
echo "   2. Click 'Connect' to establish WebSocket connection"
echo "   3. Watch FizzBuzz messages appear every 5 seconds"
echo "   4. Try sending custom messages from the input field"
echo "   5. Observe the live statistics counters"
echo ""
echo "⏹️  Press Ctrl+C to stop the application"
echo ""

# Run the application
java -jar target/websocket-0.1.0-SNAPSHOT.jar
