# Example 10: Scheduling with Desync Logic
# Minimal Makefile for build automation

.DEFAULT_GOAL := run

# Maven commands
build:
	mvn clean compile

run:
	mvn spring-boot:run

clean:
	mvn clean

test:
	mvn test

install:
	mvn clean install

# Help
help:
	@echo "Available targets:"
	@echo "  build    - Compile the project"
	@echo "  run      - Run the Spring Boot application"
	@echo "  clean    - Clean build artifacts"
	@echo "  test     - Run tests"
	@echo "  install  - Install to local Maven repository"
	@echo "  help     - Show this help"

.PHONY: build run clean test install help
