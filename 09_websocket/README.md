# WebSocket FizzBuzz Example

This example demonstrates real-time WebSocket communication between a Spring Boot backend and a web frontend using the classic FizzBuzz algorithm.

## Features

- **Real-time WebSocket Communication**: Server pushes messages to connected clients every 5 seconds
- **FizzBuzz Algorithm**: Generates messages based on divisibility rules:
  - Numbers divisible by 3: "Fizz"
  - Numbers divisible by 5: "Buzz"
  - Numbers divisible by both 3 and 5: "FizzBuzz"
  - Other numbers: Regular number messages
- **Topic-based Messaging**: Messages are categorized into different topics (fizz, buzz, fizzbuzz, number)
- **Beautiful Modern UI**: Responsive design with Tailwind CSS and animations
- **Live Statistics**: Real-time counters for each message type
- **Interactive Features**: Connect/disconnect, send custom messages, clear history

## Architecture

### Backend Components

1. **FizzBuzzWebSocket** (`websocket/FizzBuzzWebSocket.java`)
   - WebSocket endpoint at `/websocket/fizzbuzz`
   - Manages client connections using `@ServerEndpoint`
   - Scheduled task generates FizzBuzz messages every 5 seconds
   - Sends JSON messages with topic, content, and timestamp

2. **WebSocketApplication** (`WebSocketApplication.java`)
   - Main Spring Boot application with `@EnableWebSocket` and `@EnableScheduling`
   - Configures WebSocket support

3. **Controllers**
   - **Web Controllers** (`web/` package):
     - `HomeController`: Handles root redirect (`/` → `/fizzbuzz`)
     - `FizzBuzzController`: Thymeleaf controller for serving the frontend
   - **API Controllers** (`api/` package):
     - `ApiController`: REST API endpoints for status, health, and info

4. **Models** (`model/` package):
   - `FizzBuzzMessage`: Data model for WebSocket messages
   - `MessageType`: Enum defining all FizzBuzz message types (eliminates magic strings)

### Frontend Components

1. **HTML Template** (`fizzbuzz.html`)
   - Modern, responsive UI with Tailwind CSS
   - Real-time message display with color-coded topics
   - Connection status indicators
   - Interactive controls and statistics

2. **JavaScript**
   - WebSocket client implementation
   - Message parsing and display
   - Connection management
   - Statistics tracking

## How It Works

1. **Server Startup**: Spring Boot application starts on port 8088
2. **Client Connection**: User opens the web page and clicks "Connect"
3. **WebSocket Handshake**: Client establishes WebSocket connection to `/websocket/fizzbuzz`
4. **Welcome Message**: Server sends initial welcome message
5. **Scheduled Messages**: Every 5 seconds, server:
   - Increments counter
   - Determines FizzBuzz category
   - Creates JSON message with topic and content
   - Broadcasts to all connected clients
6. **Client Display**: Frontend receives messages and displays them with appropriate styling
7. **Statistics**: Live counters update for each message type

## Message Format

```json
{
  "topic": "fizz|buzz|fizzbuzz|number|welcome",
  "message": "Fizz! Number 3 is divisible by 3",
  "timestamp": "14:30:25"
}
```

## Topics

- **fizz**: Numbers divisible by 3 (purple gradient)
- **buzz**: Numbers divisible by 5 (pink gradient)
- **fizzbuzz**: Numbers divisible by both 3 and 5 (cyan gradient)
- **number**: Regular numbers (green gradient)
- **welcome**: Initial connection message (yellow gradient)

## Running the Example

### Prerequisites

- Java 17+
- Maven 3.6+

### Build and Run

```bash
cd 08_websocket
mvn clean package
java -jar target/websocket-0.1.0-SNAPSHOT.jar
```

### Access the Application

- Open browser to: <http://localhost:8088>
- Click "Connect" to establish WebSocket connection
- Watch real-time FizzBuzz messages appear every 5 seconds

## API Endpoints

### Web Endpoints

- `GET /` - Redirects to `/fizzbuzz`
- `GET /fizzbuzz` - Main application page

### REST API Endpoints

- `GET /api/status` - Application status with version and timestamp
- `GET /api/health` - Health check endpoint
- `GET /api/info` - Application information and feature list

### WebSocket Endpoints

- `WS /websocket/fizzbuzz` - WebSocket endpoint for real-time messaging

## Key Technologies

- **Spring Boot 3.5.5**: Application framework
- **Spring WebSocket**: WebSocket support
- **Thymeleaf**: Server-side templating
- **Tailwind CSS**: Utility-first CSS framework
- **JavaScript WebSocket API**: Client-side WebSocket implementation
- **Jackson**: JSON serialization

## Learning Objectives

This example demonstrates:

- WebSocket server implementation in Spring Boot
- Real-time bidirectional communication
- Scheduled tasks with `@Scheduled`
- Client-side WebSocket handling
- Modern web UI development
- JSON message formatting
- Session management
- Error handling and connection states

## Extensions

Possible enhancements:

- Add authentication to WebSocket connections
- Implement message persistence
- Add more complex FizzBuzz variations
- Support for multiple rooms/channels
- Real-time user presence indicators
- Message filtering by topic
