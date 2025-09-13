# Makefile for Hipparchus Demo Project

.PHONY: clean compile package run test help

# Default target
all: compile

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	mvn clean

# Compile the project
compile:
	@echo "Compiling Hipparchus demo..."
	mvn compile

# Package the application
package: compile
	@echo "Packaging Hipparchus demo..."
	mvn package

# Run the application
run: compile
	@echo "Running Hipparchus demo..."
	mvn exec:java

# Run the packaged JAR
run-jar: package
	@echo "Running packaged JAR..."
	java -jar target/hipparchus-demo.jar

# Run tests
test:
	@echo "Running tests..."
	mvn test

# Install to local repository
install: package
	@echo "Installing to local repository..."
	mvn install

# Show help
help:
	@echo "Available targets:"
	@echo "  clean     - Clean build artifacts"
	@echo "  compile   - Compile the project"
	@echo "  package   - Package the application"
	@echo "  run       - Run the application using Maven exec plugin"
	@echo "  run-jar   - Run the packaged JAR file"
	@echo "  test      - Run tests"
	@echo "  install   - Install to local repository"
	@echo "  help      - Show this help message"
