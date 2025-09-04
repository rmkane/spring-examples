# Spring Examples Makefile
# Handles hierarchical module dependencies and build order

.PHONY: clean build install test deploy help build-common build-all list-modules kill

# Default target
all: install

# Get all module directories (excluding common and root)
MODULES := $(shell find . -maxdepth 1 -name "[0-9][0-9]_*" -type d | grep -v "00_common" | sort | sed 's|./||')

# Define module dependencies (module: dependencies)
# Format: MODULE_DEPS_<module_name> = dependency1 dependency2
MODULE_DEPS_01_basic = 00_common
MODULE_DEPS_02_web = 00_common
MODULE_DEPS_03_rest = 00_common
MODULE_DEPS_04_logging = 00_common
MODULE_DEPS_05_security = 00_common
MODULE_DEPS_06_activemq = 00_common
MODULE_DEPS_07_elasticsearch = 00_common
MODULE_DEPS_08_websocket = 00_common

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
	cd 01_basic && mvn spring-boot:run

# Build and run the web example
run-web:
	@echo "Running web Spring Boot example..."
	cd 02_web && mvn spring-boot:run

# Build and run the REST example
run-rest:
	@echo "Running REST Spring Boot example..."
	cd 03_rest && mvn spring-boot:run

# Build and run the logging example
run-logging:
	@echo "Running logging Spring Boot example..."
	cd 04_logging && mvn spring-boot:run

# Build and run the security example
run-security:
	@echo "Running security Spring Boot example..."
	cd 05_security && mvn spring-boot:run

# Build and run the ActiveMQ example
run-activemq:
	@echo "Running ActiveMQ Spring Boot example..."
	cd 06_activemq && mvn spring-boot:run

# Build and run the Elasticsearch example
run-elasticsearch:
	@echo "Running Elasticsearch Spring Boot example..."
	cd 07_elasticsearch && mvn spring-boot:run

# Build and run the WebSocket example
run-websocket:
	@echo "Running WebSocket Spring Boot example..."
	cd 08_websocket && mvn spring-boot:run

# Build specific modules in correct order
build-common:
	@echo "Building common module..."
	cd 00_common && mvn clean install

# Build a module and its dependencies
build-module-with-deps:
	@if [ -z "$(MODULE)" ]; then \
		echo "Usage: make build-module-with-deps MODULE=module_name"; \
		echo "Available modules: $(MODULES)"; \
		exit 1; \
	fi
	@echo "Building module $(MODULE) with dependencies..."
	@# Build common first if needed
	@if [ "$(MODULE)" != "00_common" ]; then \
		$(MAKE) build-common; \
	fi
	@# Build dependencies
	@deps="$(call get-deps,$(MODULE))"; \
	for dep in $$deps; do \
		if [ "$$dep" != "00_common" ]; then \
			echo "Building dependency: $$dep"; \
			cd $$dep && mvn clean install && cd ..; \
		fi; \
	done
	@# Build the target module
	@echo "Building target module: $(MODULE)"
	@cd $(MODULE) && mvn clean install

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
	@cd $(MODULE) && mvn clean install

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
	@echo "  00_common (parent POM)"
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
	@echo "  00_common"
	@for module in $(MODULES); do \
		deps="$(call get-deps,$$module)"; \
		if [ -n "$$deps" ]; then \
			echo "  $$module -> $$deps"; \
		else \
			echo "  $$module -> 00_common"; \
		fi; \
	done

# Build modules in parallel (faster for large projects, but may fail with dependencies)
build-parallel: build-common
	@echo "Building modules in parallel (WARNING: may fail with dependencies)..."
	@for module in $(MODULES); do \
		echo "Building module: $$module"; \
		cd $$module && mvn clean install & \
	done
	@wait

# Development helpers
dev-setup: install
	@echo "Development environment setup complete"

# Show project structure
info:
	@echo "Spring Examples Project Structure:"
	@echo "├── 00_common/     (shared parent POM)"
	@for module in $(MODULES); do \
		echo "├── $$module/"; \
	done
	@echo "└── pom.xml        (root aggregator)"
	@echo ""
	@echo "Available targets:"
	@echo "  make install                    - Install all modules to local repo (in order)"
	@echo "  make run-basic                  - Build and run basic example"
	@echo "  make run-web                    - Build and run web example"
	@echo "  make run-rest                   - Build and run REST example"
	@echo "  make run-logging                - Build and run logging example"
	@echo "  make run-security               - Build and run security example"
	@echo "  make run-activemq               - Build and run ActiveMQ example"
	@echo "  make run-elasticsearch          - Build and run Elasticsearch example"
	@echo "  make run-websocket              - Build and run WebSocket example"
	@echo "  make clean                      - Clean all modules"
	@echo "  make kill                       - Kill running Spring Boot applications"
	@echo "  make test                       - Run all tests"
	@echo "  make deploy                     - Deploy to remote repository"
	@echo "  make build-common               - Build common parent only"
	@echo "  make build-module MODULE=name   - Build specific module"
	@echo "  make build-module-with-deps MODULE=name - Build module with dependencies"
	@echo "  make run-module MODULE=name     - Run specific module"
	@echo "  make list-modules               - List all available modules"
	@echo "  make show-deps                  - Show dependency tree"
	@echo "  make build-parallel             - Build modules in parallel (risky)"

# Help target
help: info
