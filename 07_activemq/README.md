# ActiveMQ Example

This example demonstrates how to use Apache ActiveMQ with Spring Boot for message queuing and pub/sub messaging.

## Features

- **Message Producer**: Service to send messages to queues and topics
- **Message Consumer**: Service to receive and process messages
- **REST API**: Endpoints to test message sending and receiving
- **Multiple Message Types**: Support for INFO, WARNING, ERROR, and SUCCESS message types
- **Queue and Topic Support**: Both point-to-point (queue) and pub/sub (topic) messaging

## Prerequisites

1. **ActiveMQ Server**: You need to have ActiveMQ running locally or remotely
   - Default configuration expects ActiveMQ at `tcp://localhost:61616`
   - Default credentials: `admin/admin`

2. **Java 17+**: Required for Spring Boot 3.x

## Running ActiveMQ

### Option 1: Docker (Recommended)

```bash
docker run -d --name activemq -p 61616:61616 -p 8161:8161 apache/activemq:latest
```

### Option 2: Download and Run

1. Download ActiveMQ from <https://activemq.apache.org/>
2. Extract and run: `./bin/activemq start`
3. Web console available at: <http://localhost:8161/admin>

## Running the Application

```bash
# From the project root
mvn clean install

# Run the ActiveMQ example
cd 06_activemq
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

### Send Messages

#### Send to Queue

```bash
curl -X POST http://localhost:8080/api/messages/send/queue \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hello from queue!",
    "sender": "test-user",
    "type": "INFO"
  }'
```

#### Send to Topic

```bash
curl -X POST http://localhost:8080/api/messages/send/topic \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hello from topic!",
    "sender": "test-user",
    "type": "INFO"
  }'
```

#### Send Error Message

```bash
curl -X POST http://localhost:8080/api/messages/send/error \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Something went wrong!",
    "sender": "error-handler",
    "type": "ERROR"
  }'
```

#### Send Simple Text Message

```bash
curl -X POST "http://localhost:8080/api/messages/send/text?content=Simple message&sender=test-user&destination=message.queue"
```

### Retrieve Messages

#### Get All Received Messages

```bash
curl http://localhost:8080/api/messages/received
```

#### Get Message Count

```bash
curl http://localhost:8080/api/messages/count
```

#### Clear Received Messages

```bash
curl -X DELETE http://localhost:8080/api/messages/clear
```

#### Health Check

```bash
curl http://localhost:8080/api/messages/health
```

## Message Types

The application supports different message types:

- **INFO**: General information messages
- **WARNING**: Warning messages
- **ERROR**: Error messages (special handling)
- **SUCCESS**: Success messages

## Destinations

The example uses these destinations:

- `message.queue`: Point-to-point messaging
- `message.topic`: Pub/sub messaging
- `error.queue`: Dedicated error message queue

## Configuration

Key configuration in `application.yml`:

```yaml
spring:
  activemq:
    broker-url: tcp://localhost:61616
    user: admin
    password: admin
    pool:
      enabled: true
      max-connections: 10
```

## Testing the Example

1. Start ActiveMQ server
2. Run the application
3. Send messages using the API endpoints
4. Check the application logs to see message processing
5. Use the `/api/messages/received` endpoint to see consumed messages

## Key Components

- **ActiveMqConfig**: Configuration for JMS connection factory and message converters
- **MessageProducer**: Service for sending messages to various destinations
- **MessageConsumer**: Service for receiving and processing messages
- **MessageController**: REST API for testing message operations
- **Message**: Model class representing message structure

## Monitoring

- Application logs show message sending and receiving
- ActiveMQ web console (<http://localhost:8161/admin>) shows queue/topic statistics
- Health endpoint provides application status
