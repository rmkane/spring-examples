<!-- omit in toc -->
# Spring Examples - Multi-Module Project

A comprehensive collection of Spring Boot examples demonstrating various aspects of Spring development, organized as a Maven multi-module project.

<!-- omit in toc -->
## Table of contents

- [Project Structure](#project-structure)
- [Modules Overview](#modules-overview)
  - [01 pom](#01-pom)
  - [02 basic](#02-basic)
  - [03 web](#03-web)
  - [04 rest](#04-rest)
  - [05 logging](#05-logging)
  - [06 security](#06-security)
  - [07 activemq](#07-activemq)
  - [08 elasticsearch](#08-elasticsearch)
  - [09 websocket](#09-websocket)
  - [10 sse](#10-sse)
  - [11 scheduling](#11-scheduling)
  - [12 libs](#12-libs)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
  - [1. Build All Modules](#1-build-all-modules)
  - [2. Run Individual Examples](#2-run-individual-examples)
    - [Basic Console App](#basic-console-app)
    - [Web Application](#web-application)
    - [REST API](#rest-api)
    - [Logging Example](#logging-example)
    - [Security Example](#security-example)
    - [ActiveMQ Example](#activemq-example)
    - [Elasticsearch Example](#elasticsearch-example)
    - [WebSocket Example](#websocket-example)
    - [SSE Example](#sse-example)
    - [Scheduling Example](#scheduling-example)
  - [3. Stop Applications](#3-stop-applications)
- [Makefile Commands](#makefile-commands)
  - [Build Commands](#build-commands)
  - [Run Commands](#run-commands)
  - [Utility Commands](#utility-commands)
- [Maven Commands](#maven-commands)
  - [Individual Module Building](#individual-module-building)
  - [Root Level Operations](#root-level-operations)
- [Configuration](#configuration)
  - [Port Configuration](#port-configuration)
  - [JDK Configuration](#jdk-configuration)
  - [Spring Boot Version](#spring-boot-version)
- [Development Workflow](#development-workflow)
  - [Adding New Modules](#adding-new-modules)
  - [Module Dependencies](#module-dependencies)
  - [Testing](#testing)
- [Troubleshooting](#troubleshooting)
  - [Common Issues](#common-issues)
    - [Port Already in Use](#port-already-in-use)
    - [Maven Build Failures](#maven-build-failures)
    - [Module Not Found](#module-not-found)
  - [Log Files](#log-files)
- [Contributing](#contributing)
  - [Adding New Examples](#adding-new-examples)
  - [Code Style](#code-style)
- [License](#license)
- [Support](#support)

## Project Structure

```none
spring-examples/
├── 01_pom/               # Multi-module POM structure (foo-dependencies + foo-starter-parent)
├── 02_basic/             # Basic Spring Boot console application
├── 03_web/              # Spring Boot web application with HTML controller
├── 04_rest/             # Spring Boot REST API example
├── 05_logging/          # Advanced logging configuration example
├── 06_security/         # Spring Security with authentication and authorization
├── 07_activemq/         # ActiveMQ messaging example
├── 08_elasticsearch/    # Elasticsearch integration example
├── 09_websocket/        # WebSocket real-time communication example
├── 10_sse/              # Server-Sent Events (SSE) example
├── 11_scheduling/       # Scheduling with desync logic example
├── 12_libs/             # External libraries integration example (Hipparchus)
├── pom.xml              # Root aggregator POM
├── Makefile             # Build automation and project management
└── README.md            # This file
```

## Modules Overview

### 01 pom

- **Purpose**: Multi-module POM structure combining dependency management and build configuration
- **Structure**:
  - `foo-dependencies`: Custom dependencies BOM (Bill of Materials)
  - `foo-starter-parent`: Custom starter parent POM for all modules
- **Features**:
  - Imports Spring Boot dependencies BOM
  - Manages Maven plugin versions
  - Centralizes dependency version management
  - Provides internal project dependency management
  - Extends `foo-dependencies` BOM
  - Provides plugin management with Lombok support
  - Centralized Maven compiler settings
  - Spring Boot Maven plugin configuration
  - JDK 17 configuration
  - CI-friendly versioning with flatten-maven-plugin

### 02 basic

- **Purpose**: Simple Spring Boot console application
- **Features**:
  - Basic Spring Boot starter
  - Console output demonstration
  - JDK 17 features showcase

### 03 web

- **Purpose**: Spring Boot web application
- **Features**:
  - Spring Boot Web starter
  - HTML controller with GET endpoint
  - Simple web interface

### 04 rest

- **Purpose**: Spring Boot REST API
- **Features**:
  - RESTful endpoints
  - JSON response examples
  - API status endpoint

### 05 logging

- **Purpose**: Advanced logging configuration
- **Features**:
  - Custom logger categories (business, security, performance)
  - Separate log files for different concerns
  - Log rotation and file management
  - Custom Logback configuration
  - REST endpoints for testing logging

### 06 security

- **Purpose**: Comprehensive Spring Security implementation
- **Features**:
  - User authentication and authorization
  - Role-based access control (USER, MODERATOR, ADMIN)
  - Custom login forms and security configuration
  - JPA integration with user management
  - Thymeleaf templates with security integration
  - H2 database for demonstration
  - API endpoints with role-based access
  - Lombok integration for reduced boilerplate

### 07 activemq

- **Purpose**: ActiveMQ messaging integration
- **Features**:
  - Spring Boot ActiveMQ starter
  - Message producer and consumer examples
  - Queue and topic messaging
  - REST endpoints for sending messages
  - Embedded ActiveMQ broker

### 08 elasticsearch

- **Purpose**: Elasticsearch integration and search functionality
- **Features**:
  - Spring Boot Data Elasticsearch
  - Document indexing and searching
  - REST API for search operations
  - Custom search queries and filters
  - Elasticsearch client configuration

### 09 websocket

- **Purpose**: Real-time WebSocket communication with FizzBuzz algorithm
- **Features**:
  - Spring Boot WebSocket support
  - Real-time bidirectional communication
  - FizzBuzz message generation every 5 seconds
  - Topic-based messaging (fizz, buzz, fizzbuzz, number)
  - Modern responsive UI with Tailwind CSS
  - Live statistics and message counters
  - Interactive connection management

### 10 sse

- **Purpose**: Server-Sent Events (SSE) real-time communication
- **Features**:
  - Spring Boot SSE support with SseEmitter
  - One-way server-to-client real-time communication
  - Multiple event types (system, weather, stock, news, alert)
  - Scheduled event generation every 3 seconds
  - Modern responsive UI with Tailwind CSS
  - Live connection status and event counters
  - Interactive connection management
  - Graceful error handling and reconnection

### 11 scheduling

- **Purpose**: Scheduling with desynchronization patterns
- **Features**:
  - Fixed rate scheduling (every 5 seconds)
  - Fixed delay scheduling (3 seconds after completion)
  - Cron-based scheduling (every 10 seconds)
  - Asynchronous task execution with parallel processing
  - Conditional scheduling (business hours only)
  - Real-time task statistics and monitoring
  - Modern responsive UI with live task execution log
  - Interactive controls for managing scheduled tasks

### 12 libs

- **Purpose**: External libraries integration example
- **Features**:
  - Hipparchus mathematical library integration
  - Statistical calculations and mathematical operations
  - Demonstrates integration with third-party libraries
  - Maven Shade plugin for executable JAR
  - Mathematical computation examples

## Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6+
- **Make**: For build automation (optional but recommended)

## Quick Start

### 1. Build All Modules

```bash
make install
```

### 2. Run Individual Examples

#### Basic Console App

```bash
make run-basic
```

#### Web Application

```bash
make run-web
```

- Access at: <http://localhost:8080>

#### REST API

```bash
make run-rest
```

- Access at: <http://localhost:8080>
- API endpoints: `/`, `/api/status`

#### Logging Example

```bash
make run-logging
```

- Access at: <http://localhost:8080>
- Test logging: `/log-examples`, `/business-log`, `/security-log`, `/performance-log`

#### Security Example

```bash
make run-security
```

- Access at: <http://localhost:8080>
- Demo users: admin/admin123, moderator/mod123, user/user123
- Test role-based access control and security features

#### ActiveMQ Example

```bash
make run-activemq
```

- Access at: <http://localhost:8080>
- ActiveMQ console: <http://localhost:8161/admin> (admin/admin)
- Test messaging endpoints

#### Elasticsearch Example

```bash
make run-elasticsearch
```

- Access at: <http://localhost:8080>
- Elasticsearch: <http://localhost:9200>
- Test search functionality

#### WebSocket Example

```bash
make run-websocket
```

- Access at: <http://localhost:8080>
- WebSocket endpoint: `ws://localhost:8080/websocket/fizzbuzz`
- Connect and watch real-time FizzBuzz messages every 5 seconds

#### SSE Example

```bash
make run-sse
```

- Access at: <http://localhost:8080>
- SSE endpoint: <http://localhost:8080/api/events>
- Connect and watch real-time events every 3 seconds

#### Scheduling Example

```bash
make run-scheduling
```

- Access at: <http://localhost:8080>
- API endpoints: `/api/stats`, `/api/status`, `/api/reset`
- Watch various scheduling patterns execute in real-time
- Monitor task statistics and execution logs

### 3. Stop Applications

```bash
make kill
```

## Makefile Commands

### Build Commands

- `make install` - Install all modules to local Maven repository
- `make clean` - Clean all modules
- `make build-common` - Build common parent only
- `make build-module MODULE=name` - Build specific module
- `make build-module-with-deps MODULE=name` - Build module with dependencies

### Run Commands

- `make run-basic` - Run basic console example
- `make run-web` - Run web application
- `make run-rest` - Run REST API
- `make run-logging` - Run logging example
- `make run-security` - Run security example
- `make run-activemq` - Run ActiveMQ example
- `make run-elasticsearch` - Run Elasticsearch example
- `make run-websocket` - Run WebSocket example
- `make run-sse` - Run SSE example
- `make run-scheduling` - Run scheduling example
- `make run-module MODULE=name` - Run specific module

### Utility Commands

- `make list-modules` - List all available modules
- `make show-deps` - Show dependency tree
- `make test` - Run all tests
- `make kill` - Kill running Spring Boot applications
- `make help` - Show this help information

## Maven Commands

### Individual Module Building

```bash
# Build specific module
cd 01_basic && mvn clean install

# Run specific module
cd 02_web && mvn spring-boot:run
```

### Root Level Operations

```bash
# Build all modules from root
mvn clean install

# Build specific module from root
mvn clean install -pl 03_rest -am
```

## Configuration

### Port Configuration

Each module has its own `application.yml` file:

- **Default Port**: 8080 (configured in each module's `application.yml`)
- **Note**: Only one application can run at a time on the same port

### JDK Configuration

- **Target Version**: JDK 17
- **Source/Target**: 17
- **Compiler**: Maven Compiler Plugin 3.14.0

### Spring Boot Version

- **Version**: 3.5.6
- **Dependencies BOM**: `org.foo:foo-dependencies` (imports Spring Boot BOM)
- **Starter Parent**: `org.foo:foo-starter-parent` (extends foo-dependencies)
- **Package Structure**: `org.foo.*` (custom package namespace)

## Development Workflow

### Adding New Modules

1. Create module directory with standard Maven structure
2. Add module to root `pom.xml` `<modules>` section
3. Add module dependency to `01_pom/foo-dependencies/pom.xml` if needed
4. Configure module dependencies in `Makefile` if needed
5. Add run target to `Makefile`
6. Update help text
7. Use `org.foo` package namespace

### Module Dependencies

- All modules inherit from `org.foo:foo-starter-parent`
- Dependencies are managed in `01_pom/foo-dependencies` BOM
- Internal project dependencies are managed in `foo-dependencies`
- Use `MODULE_DEPS_<module_name>` variables in `Makefile` to define build order

### Testing

```bash
# Run tests for all modules
make test

# Run tests for specific module
cd 01_basic && mvn test
```

## Troubleshooting

### Common Issues

#### Port Already in Use

```bash
make kill
# Then run your desired module
```

#### Maven Build Failures

```bash
# Clean and rebuild
make clean
make install
```

#### Module Not Found

```bash
# Ensure module is in root pom.xml
# Check Makefile dependencies
make list-modules
```

### Log Files

- **Logging Module**: Check `04_logging/logs/` directory
- **Application Logs**: Each module generates its own logs
- **Maven Logs**: Check individual module `target/` directories

## Contributing

### Adding New Examples

1. Follow the existing module structure
2. Use `org.foo:foo-starter-parent` as parent
3. Add appropriate Spring Boot starters
4. Include `application.yml` configuration
5. Add Makefile targets
6. Update documentation
7. Use `org.foo` package namespace
8. Add Lombok annotations if needed (already configured in parent)

### Code Style

- Use JDK 17 features where appropriate
- Follow Spring Boot best practices
- Include comprehensive logging
- Add README for complex modules

## License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). For the full license text, see the [LICENSE](LICENSE) file in this repository.

This project is intended for educational and demonstration purposes, showcasing various Spring Boot features and best practices.

## Support

For issues or questions:

1. Check the individual module READMEs
2. Review the Makefile help (`make help`)
3. Check Maven build logs
4. Verify Java and Maven versions
