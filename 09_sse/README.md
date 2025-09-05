# Server-Sent Events (SSE) Example

This example demonstrates Server-Sent Events (SSE) in Spring Boot, providing one-way real-time communication from server to client over HTTP.

## 🎯 What is SSE?

Server-Sent Events (SSE) is a web standard that allows servers to push data to web clients over HTTP. Unlike WebSockets, SSE is:

- **One-way communication** (server to client only)
- **Built on HTTP** (no special protocol)
- **Automatic reconnection** (browsers handle reconnection)
- **Simpler to implement** than WebSockets

## 🚀 Features

- **Real-time event streaming** from server to client
- **Event type categorization** (System, Weather, Stock, News, Alert)
- **Connection management** with visual status indicators
- **Event statistics** showing counts by type
- **Automatic reconnection** when connection is lost
- **Professional logging** with Lombok
- **Modern UI** with Tailwind CSS and toast notifications

## 📁 Project Structure

```none
09_sse/
├── src/main/java/org/example/spring/sse/
│   ├── SseApplication.java              # Main Spring Boot application
│   ├── api/
│   │   └── ApiController.java           # REST API endpoints + SSE endpoint
│   ├── model/
│   │   ├── EventType.java              # Event type enum
│   │   └── SseEvent.java               # SSE event data class
│   ├── sse/
│   │   └── SseService.java             # SSE service for event management
│   ├── utils/
│   │   └── TimeUtils.java              # Utility for timestamps
│   └── web/
│       └── HomeController.java         # Web controller for frontend
└── src/main/resources/
    ├── application.yml                  # Application configuration
    └── templates/
        └── sse.html                    # Frontend UI
```

## 🔧 Key Components

### SseService

- Manages SSE connections using `SseEmitter`
- Sends events to all connected clients
- Handles connection lifecycle (connect, disconnect, error)
- Generates random events every 3 seconds

### ApiController

- `/api/events` - SSE endpoint for client subscriptions
- `/api/status` - Service status and client count
- `/api/health` - Health check endpoint
- `/api/info` - Service information

### Frontend (sse.html)

- Uses native `EventSource` API
- Real-time event display with type-based styling
- Connection status indicators
- Event statistics dashboard
- Toast notifications for user feedback

## 🎮 How to Run

1. **Build the project:**

   ```bash
   mvn clean compile
   ```

2. **Run the SSE example:**

   ```bash
   cd 09_sse && mvn spring-boot:run
   ```

3. **Access the application:**
   - Web UI: <http://localhost:8080/sse>
   - SSE endpoint: <http://localhost:8080/api/events>
   - API status: <http://localhost:8080/api/status>

## 📡 SSE vs WebSocket

| Feature | SSE | WebSocket |
|---------|-----|-----------|
| **Communication** | One-way (server→client) | Two-way |
| **Protocol** | HTTP | WebSocket |
| **Reconnection** | Automatic | Manual |
| **Complexity** | Simple | More complex |
| **Use Cases** | Notifications, feeds | Chat, gaming |

## 🔍 Event Types

- **System** ⚙️ - System notifications and status updates
- **Weather** 🌤️ - Weather data updates
- **Stock** 📈 - Stock market data
- **News** 📰 - News headlines
- **Alert** 🚨 - Important alerts and notifications

## 📊 API Endpoints

- `GET /` - Redirects to `/sse`
- `GET /sse` - SSE demo web interface
- `GET /api/events` - SSE event stream
- `GET /api/status` - Service status
- `GET /api/health` - Health check
- `GET /api/info` - Service information

## 🛠️ Technologies Used

- **Spring Boot 3.5.5** - Application framework
- **Spring Web** - HTTP and SSE support
- **Thymeleaf** - Server-side templating
- **Lombok** - Boilerplate reduction
- **Tailwind CSS** - Utility-first CSS framework
- **Toastify.js** - Toast notifications
- **EventSource API** - Native browser SSE support

## 🎯 Learning Outcomes

- Understanding Server-Sent Events vs WebSockets
- Implementing SSE with Spring Boot's `SseEmitter`
- Managing multiple client connections
- Handling connection lifecycle events
- Building real-time UIs with SSE
- Professional logging and error handling
