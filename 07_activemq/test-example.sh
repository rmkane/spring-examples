#!/bin/bash

# ActiveMQ Example Test Script
# Make sure ActiveMQ is running and the application is started on port 8080

PORT=8080

echo "=== ActiveMQ Example Test Script ==="
echo "Make sure ActiveMQ is running and the application is started on port $PORT"
echo ""

BASE_URL="http://localhost:$PORT/api/messages"

echo "1. Testing health endpoint..."
curl -s "$BASE_URL/health" | jq .
echo ""

echo "2. Sending a message to queue..."
curl -s -X POST "$BASE_URL/send/queue" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hello from queue!",
    "sender": "test-user",
    "type": "INFO"
  }' | jq .
echo ""

echo "3. Sending a message to topic..."
curl -s -X POST "$BASE_URL/send/topic" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hello from topic!",
    "sender": "test-user",
    "type": "INFO"
  }' | jq .
echo ""

echo "4. Sending an error message..."
curl -s -X POST "$BASE_URL/send/error" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Something went wrong!",
    "sender": "error-handler",
    "type": "ERROR"
  }' | jq .
echo ""

echo "5. Sending a simple text message..."
curl -s -X POST "$BASE_URL/send/text?content=Simple message&sender=test-user&destination=message.queue" | jq .
echo ""

echo "6. Getting message count..."
curl -s "$BASE_URL/count" | jq .
echo ""

echo "7. Getting all received messages..."
curl -s "$BASE_URL/received" | jq .
echo ""

echo "8. Clearing received messages..."
curl -s -X DELETE "$BASE_URL/clear" | jq .
echo ""

echo "=== Test completed ==="
