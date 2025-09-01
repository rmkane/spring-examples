<!-- omit in toc -->
# Spring Examples - Multi-Module Project

A comprehensive collection of Spring Boot examples demonstrating various aspects of Spring development, organized as a Maven multi-module project.

<!-- omit in toc -->
## Table of contents

- [Project Structure](#project-structure)
- [Modules Overview](#modules-overview)
  - [00\_common](#00_common)
  - [01\_basic](#01_basic)
  - [02\_web](#02_web)
  - [03\_rest](#03_rest)
  - [04\_logging](#04_logging)
  - [05\_security](#05_security)
  - [06\_activemq](#06_activemq)
  - [07\_elasticsearch](#07_elasticsearch)
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
├── 00_common/           # Shared parent POM with Spring Boot configuration
├── 01_basic/            # Basic Spring Boot console application
├── 02_web/              # Spring Boot web application with HTML controller
├── 03_rest/             # Spring Boot REST API example
├── 04_logging/          # Advanced logging configuration example
├── 05_security/         # Spring Security with authentication and authorization
├── 06_activemq/          # ActiveMQ messaging example
├── 07_elasticsearch/    # Elasticsearch integration example
├── pom.xml              # Root aggregator POM
├── Makefile             # Build automation and project management
└── README.md            # This file
```

## Modules Overview

### 00_common

- **Purpose**: Shared parent POM for all modules
- **Features**:
  - Spring Boot 3.5.5 parent
  - JDK 17 configuration
  - Common dependencies and properties
  - Centralized Maven compiler settings

### 01_basic

- **Purpose**: Simple Spring Boot console application
- **Features**:
  - Basic Spring Boot starter
  - Console output demonstration
  - JDK 17 features showcase

### 02_web

- **Purpose**: Spring Boot web application
- **Features**:
  - Spring Boot Web starter
  - HTML controller with GET endpoint
  - Simple web interface

### 03_rest

- **Purpose**: Spring Boot REST API
- **Features**:
  - RESTful endpoints
  - JSON response examples
  - API status endpoint

### 04_logging

- **Purpose**: Advanced logging configuration
- **Features**:
  - Custom logger categories (business, security, performance)
  - Separate log files for different concerns
  - Log rotation and file management
  - Custom Logback configuration
  - REST endpoints for testing logging

### 05_security

- **Purpose**: Comprehensive Spring Security implementation
- **Features**:
  - User authentication and authorization
  - Role-based access control (USER, MODERATOR, ADMIN)
  - Custom login forms and security configuration
  - JPA integration with user management
  - Thymeleaf templates with security integration
  - H2 database for demonstration
  - API endpoints with role-based access

### 06_activemq

- **Purpose**: ActiveMQ messaging integration
- **Features**:
  - Spring Boot ActiveMQ starter
  - Message producer and consumer examples
  - Queue and topic messaging
  - REST endpoints for sending messages
  - Embedded ActiveMQ broker

### 07_elasticsearch

- **Purpose**: Elasticsearch integration and search functionality
- **Features**:
  - Spring Boot Data Elasticsearch
  - Document indexing and searching
  - REST API for search operations
  - Custom search queries and filters
  - Elasticsearch client configuration

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

- **Version**: 3.5.5
- **Parent**: `spring-boot-starter-parent`

## Development Workflow

### Adding New Modules

1. Create module directory with standard Maven structure
2. Add module to root `pom.xml` `<modules>` section
3. Configure module dependencies in `Makefile` if needed
4. Add run target to `Makefile`
5. Update help text

### Module Dependencies

- All modules inherit from `00_common`
- Dependencies are managed in the `Makefile` for build order
- Use `MODULE_DEPS_<module_name>` variables to define dependencies

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
2. Use `00_common` as parent
3. Add appropriate Spring Boot starters
4. Include `application.yml` configuration
5. Add Makefile targets
6. Update documentation

### Code Style

- Use JDK 17 features where appropriate
- Follow Spring Boot best practices
- Include comprehensive logging
- Add README for complex modules

## License

This project is for educational and demonstration purposes.

## Support

For issues or questions:

1. Check the individual module READMEs
2. Review the Makefile help (`make help`)
3. Check Maven build logs
4. Verify Java and Maven versions
