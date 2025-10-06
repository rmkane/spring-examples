# Spring Examples Makefile
# Handles hierarchical module dependencies and build order

.PHONY: clean build install test deploy help build-common build-all list-modules kill

# Default target
all: install

# Get all module directories (excluding pom and root)
MODULES := $(shell find . -maxdepth 1 -name "[0-9][0-9]_*" -type d | grep -v "01_pom" | sort | sed 's|./||')

# Define module dependencies (module: dependencies)
# Format: MODULE_DEPS_<module_name> = dependency1 dependency2

MODULE_DEPS_02_basic = 01_pom
MODULE_DEPS_03_web = 01_pom
MODULE_DEPS_04_rest = 01_pom
MODULE_DEPS_05_logging = 01_pom
MODULE_DEPS_06_security = 01_pom
MODULE_DEPS_07_activemq = 01_pom
MODULE_DEPS_08_elasticsearch = 01_pom
MODULE_DEPS_09_websocket = 01_pom
MODULE_DEPS_10_sse = 01_pom
MODULE_DEPS_11_scheduling = 01_pom
MODULE_DEPS_12_libs = 01_pom

# Function to get dependencies for a module
get-deps = $(MODULE_DEPS_$(1))

# Function to check if a module has dependencies
has-deps = $(if $(MODULE_DEPS_$(1)),true,false)

# Clean all modules
clean:
	@echo "Cleaning all modules..."
	mvn clean

# Kill any running Spring Boot applications
kill:
	@echo "Killing any running Spring Boot applications..."
	@pkill -f "spring-boot:run" || echo "No Spring Boot applications running"
	@echo "Killed all Spring Boot applications"

# Build all modules in correct order
build:
	@echo "Building all modules..."
	mvn compile

# Install all modules to local repository with proper order
install: build-all
	@echo "Installing root module..."
	mvn install -pl . -am

# Run tests for all modules
test:
	@echo "Running tests for all modules..."
	mvn test

# Package all modules (creates JARs/WARs)
package:
	@echo "Packaging all modules..."
	mvn package

# Deploy to remote repository (requires proper configuration)
deploy:
	@echo "Deploying to remote repository..."
	mvn deploy

# Build and run the basic example
run-basic:
	@echo "Running basic Spring Boot example..."
	cd 02_basic && make run

# Build and run the web example
run-web:
	@echo "Running web Spring Boot example..."
	cd 03_web && make run

# Build and run the REST example
run-rest:
	@echo "Running REST Spring Boot example..."
	cd 04_rest && make run

# Build and run the logging example
run-logging:
	@echo "Running logging Spring Boot example..."
	cd 05_logging && make run

# Build and run the security example
run-security:
	@echo "Running security Spring Boot example..."
	cd 06_security && make run

# Build and run the ActiveMQ example
run-activemq:
	@echo "Running ActiveMQ Spring Boot example..."
	cd 07_activemq && make run

# Build and run the Elasticsearch example
run-elasticsearch:
	@echo "Running Elasticsearch Spring Boot example..."
	cd 08_elasticsearch && make run

# Build and run the WebSocket example
run-websocket:
	@echo "Running WebSocket Spring Boot example..."
	cd 09_websocket && make run

# Build and run the SSE example
run-sse:
	@echo "Running SSE Spring Boot example..."
	cd 10_sse && make run

# Build and run the scheduling example
run-scheduling:
	@echo "Running scheduling Spring Boot example..."
	cd 11_scheduling && make run

# Build and run the Hipparchus libs example
run-libs:
	@echo "Running Hipparchus mathematical library demo..."
	cd 12_libs && make run
	@echo "Analyzing POM files..."
	python3 12_libs/scripts/cli.py analyze --pattern "**/pom.xml" --output-dir 12_libs/output

# Start ActiveMQ broker
broker:
	@echo "Starting ActiveMQ broker..."
	cd 07_activemq && make broker

# Start Elasticsearch
elasticsearch:
	@echo "Starting Elasticsearch..."
	cd 08_elasticsearch && make elasticsearch

# Build specific modules in correct order
build-common:
	@echo "Building POM module..."
	cd 01_pom && mvn clean install

# Build a module and its dependencies
build-module-with-deps:
	@if [ -z "$(MODULE)" ]; then \
		echo "Usage: make build-module-with-deps MODULE=module_name"; \
		echo "Available modules: $(MODULES)"; \
		exit 1; \
	fi
	@echo "Building module $(MODULE) with dependencies..."
	@# Build common first if needed
	@if [ "$(MODULE)" != "01_pom" ]; then \
		$(MAKE) build-common; \
	fi
	@# Build dependencies
	@deps="$(call get-deps,$(MODULE))"; \
	for dep in $$deps; do \
		if [ "$$dep" != "01_pom" ]; then \
			echo "Building dependency: $$dep"; \
			cd $$dep && mvn clean compile && cd ..; \
		fi; \
	done
	@# Build the target module
	@echo "Building target module: $(MODULE)"
	@cd $(MODULE) && mvn clean compile

# Scalable build target - builds all modules in dependency order
build-all: build-common
	@echo "Building all modules in dependency order..."
	@echo "Modules to build: $(MODULES)"
	@# Build modules in dependency order
	@for module in $(MODULES); do \
		echo "Building module: $$module"; \
		$(MAKE) build-module-with-deps MODULE=$$module; \
	done

# Build specific module (useful for large projects)
build-module:
	@if [ -z "$(MODULE)" ]; then \
		echo "Usage: make build-module MODULE=module_name"; \
		echo "Available modules: $(MODULES)"; \
		exit 1; \
	fi
	@echo "Building module: $(MODULE)"
	@cd $(MODULE) && mvn clean compile

# Run specific module (useful for large projects)
run-module:
	@if [ -z "$(MODULE)" ]; then \
		echo "Usage: make run-module MODULE=module_name"; \
		echo "Available modules: $(MODULES)"; \
		exit 1; \
	fi
	@echo "Running module: $(MODULE)"
	@cd $(MODULE) && mvn spring-boot:run

# List all available modules with dependencies
list-modules:
	@echo "Available modules:"
	@echo "  01_pom (multi-module POM structure)"
	@for module in $(MODULES); do \
		deps="$(call get-deps,$$module)"; \
		if [ -n "$$deps" ]; then \
			echo "  $$module (depends on: $$deps)"; \
		else \
			echo "  $$module"; \
		fi; \
	done

# Show dependency tree
show-deps:
	@echo "Module Dependency Tree:"
	@echo "  01_pom"
	@for module in $(MODULES); do \
		deps="$(call get-deps,$$module)"; \
		if [ -n "$$deps" ]; then \
			echo "  $$module -> $$deps"; \
		else \
			echo "  $$module -> 01_pom"; \
		fi; \
	done

# Build modules in parallel (faster for large projects, but may fail with dependencies)
build-parallel: build-common
	@echo "Building modules in parallel (WARNING: may fail with dependencies)..."
	@for module in $(MODULES); do \
		echo "Building module: $$module"; \
		cd $$module && mvn clean compile & \
	done
	@wait

# Development helpers
dev-setup: install
	@echo "Development environment setup complete"

# Show project structure
info:
	@echo "Spring Examples Project Structure:"
	@echo "├── 01_pom/  (multi-module POM structure)"
	@for module in $(MODULES); do \
		echo "├── $$module/"; \
	done
	@echo "└── pom.xml           (root aggregator)"
	@echo ""
	@echo "Available targets:"
	@echo "  make install                    - Install all modules to local repo (in order)"
	@echo "  make run-basic                  - Run basic example (delegates to 02_basic/Makefile)"
	@echo "  make run-web                    - Run web example (delegates to 03_web/Makefile)"
	@echo "  make run-rest                   - Run REST example (delegates to 04_rest/Makefile)"
	@echo "  make run-logging                - Run logging example (delegates to 05_logging/Makefile)"
	@echo "  make run-security               - Run security example (delegates to 06_security/Makefile)"
	@echo "  make run-activemq               - Run ActiveMQ example (delegates to 07_activemq/Makefile)"
	@echo "  make run-elasticsearch          - Run Elasticsearch example (delegates to 08_elasticsearch/Makefile)"
	@echo "  make run-websocket              - Run WebSocket example (delegates to 09_websocket/Makefile)"
	@echo "  make run-sse                    - Run SSE example (delegates to 10_sse/Makefile)"
	@echo "  make run-scheduling             - Run scheduling example (delegates to 11_scheduling/Makefile)"
	@echo "  make run-libs                   - Run Hipparchus libs example (delegates to 12_libs/Makefile)"
	@echo "  make broker                     - Start ActiveMQ broker (delegates to 07_activemq/Makefile)"
	@echo "  make elasticsearch              - Start Elasticsearch (delegates to 08_elasticsearch/Makefile)"
	@echo "  make clean                      - Clean all modules"
	@echo "  make kill                       - Kill running Spring Boot applications"
	@echo "  make test                       - Run all tests"
	@echo "  make deploy                     - Deploy to remote repository"
	@echo "  make build-common               - Build POM module only"
	@echo "  make build-module MODULE=name   - Build specific module"
	@echo "  make build-module-with-deps MODULE=name - Build module with dependencies"
	@echo "  make run-module MODULE=name     - Run specific module"
	@echo "  make list-modules               - List all available modules"
	@echo "  make show-deps                  - Show dependency tree"
	@echo "  make build-parallel             - Build modules in parallel (risky)"

# Help target
help: info
