# Spring Boot Logging Example

This module demonstrates advanced logging configuration in Spring Boot using both `application.yml` and custom Logback configuration.

## Features

### Custom Logger Categories

- **Business Logger**: For business operations, orders, inventory
- **Security Logger**: For authentication, access attempts, security events
- **Performance Logger**: For response times, database queries, cache hits

### Logging Configuration

- **Multiple Log Levels**: TRACE, DEBUG, INFO, WARN, ERROR
- **Separate Log Files**: Different files for different concerns
- **Log Rotation**: Automatic rotation by size (10MB) and time (daily)
- **Custom Patterns**: Formatted timestamps and structured output

## Configuration Files

### `application.yml`

- Log levels for different packages and custom loggers
- File logging configuration
- Log groups for easier management

### `logback-spring.xml`

- Custom appenders for different log categories
- Rolling file policies with size and time-based rotation
- Filters and encoders for specific requirements

## Usage

### Running the Application

```bash
make run-logging
```

### Testing Logging Endpoints

```bash
# Basic info
curl http://localhost:8080/

# Generate examples of all log levels
curl http://localhost:8080/log-examples

# Generate business logging examples
curl http://localhost:8080/business-log

# Generate security logging examples
curl http://localhost:8080/security-log

# Generate performance logging examples
curl http://localhost:8080/performance-log
```

### Log Files Generated

- `logs/logging-app.log` - Main application log
- `logs/business.log` - Business operations (INFO level and above)
- `logs/security.log` - Security events (all levels)
- `logs/performance.log` - Performance metrics (all levels)

## Log Levels

- **TRACE**: Very detailed debugging (not shown by default)
- **DEBUG**: Debugging information (shown for application and performance)
- **INFO**: General information (default for most loggers)
- **WARN**: Warning information
- **ERROR**: Error information

## Customization

### Adding New Logger Categories

1. Add logger configuration in `application.yml`
2. Create custom appender in `logback-spring.xml`
3. Use `LoggerFactory.getLogger("category_name")` in code

### Changing Log Levels

Modify the `logging.level` section in `application.yml`:

```yaml
logging:
  level:
    business: DEBUG  # Change from INFO to DEBUG
    security: INFO   # Change from WARN to INFO
```

### Custom Log Patterns

Modify the `pattern` section in `logback-spring.xml`:

```xml
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
```

## Best Practices Demonstrated

1. **Separation of Concerns**: Different log files for different types of events
2. **Log Rotation**: Prevents log files from growing too large
3. **Structured Logging**: Consistent format across all loggers
4. **Performance**: Efficient logging with appropriate levels
5. **Maintainability**: Centralized configuration in YAML and XML
