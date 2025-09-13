# =============================================================================
# Scripts Development Makefile
# =============================================================================

# Variables
PYTHON := python3
VENV_DIR := venv
VENV_PYTHON := $(VENV_DIR)/bin/python
VENV_PIP := $(VENV_DIR)/bin/pip
REQUIREMENTS := requirements.txt
CLI_SCRIPT := cli.py
VISUALIZER := visualizer.html
PORT := 8080

# Colors for output
GREEN := \033[0;32m
YELLOW := \033[0;33m
RED := \033[0;31m
NC := \033[0m # No Color

# Default target
.PHONY: help
help:
	@echo "$(GREEN)Scripts Development Makefile$(NC)"
	@echo ""
	@echo "$(YELLOW)Available targets:$(NC)"
	@echo "  setup-venv     - Create virtual environment and install dependencies"
	@echo "  install        - Install/update dependencies in virtual environment"
	@echo "  lint           - Run linting checks on Python scripts"
	@echo "  format         - Format Python code with black and isort"
	@echo "  check          - Run all checks (lint + format check)"
	@echo "  clean          - Remove virtual environment and cache files"
	@echo "  serve          - Start HTTP server to host visualizer"
	@echo "  serve-bg       - Start HTTP server in background"
	@echo "  stop-server    - Stop background HTTP server"
	@echo "  test           - Run basic functionality tests"
	@echo "  generate       - Generate sample matrix data"
	@echo "  analyze        - Analyze POM files and create matrix"
	@echo "  all            - Setup, check, and test everything"
	@echo ""
	@echo "$(YELLOW)Examples:$(NC)"
	@echo "  make setup-venv    # First time setup"
	@echo "  make serve         # Start visualizer server"
	@echo "  make lint          # Check code quality"
	@echo "  make format        # Auto-format code"

# =============================================================================
# Virtual Environment Setup
# =============================================================================

.PHONY: setup-venv
setup-venv: $(VENV_DIR)/.created

$(VENV_DIR)/.created:
	@echo "$(GREEN)Creating virtual environment...$(NC)"
	$(PYTHON) -m venv $(VENV_DIR)
	@echo "$(GREEN)Installing dependencies...$(NC)"
	$(VENV_PIP) install --upgrade pip
	$(VENV_PIP) install -r $(REQUIREMENTS)
	@touch $(VENV_DIR)/.created
	@echo "$(GREEN)✅ Virtual environment setup complete!$(NC)"

.PHONY: install
install: setup-venv
	@echo "$(GREEN)Installing/updating dependencies...$(NC)"
	$(VENV_PIP) install -r $(REQUIREMENTS)
	@echo "$(GREEN)✅ Dependencies installed!$(NC)"

# =============================================================================
# Code Quality
# =============================================================================

.PHONY: lint
lint: setup-venv
	@echo "$(GREEN)Running linting checks...$(NC)"
	$(VENV_PYTHON) -m flake8 $(CLI_SCRIPT) --max-line-length=88 --ignore=E203,W503
	$(VENV_PYTHON) -m mypy $(CLI_SCRIPT) --ignore-missing-imports
	@echo "$(GREEN)✅ Linting passed!$(NC)"

.PHONY: format
format: setup-venv
	@echo "$(GREEN)Formatting code...$(NC)"
	$(VENV_PYTHON) -m isort $(CLI_SCRIPT)
	$(VENV_PYTHON) -m black $(CLI_SCRIPT) --line-length=88
	@echo "$(GREEN)✅ Code formatted!$(NC)"

.PHONY: check
check: setup-venv
	@echo "$(GREEN)Running format check...$(NC)"
	$(VENV_PYTHON) -m isort --check-only $(CLI_SCRIPT)
	$(VENV_PYTHON) -m black --check $(CLI_SCRIPT) --line-length=88
	@echo "$(GREEN)Running linting checks...$(NC)"
	$(VENV_PYTHON) -m flake8 $(CLI_SCRIPT) --max-line-length=88 --ignore=E203,W503
	@echo "$(GREEN)✅ All checks passed!$(NC)"

# =============================================================================
# Web Server
# =============================================================================

.PHONY: serve
serve:
	@echo "$(GREEN)Starting HTTP server on port $(PORT)...$(NC)"
	@echo "$(YELLOW)Open http://localhost:$(PORT)/$(VISUALIZER) in your browser$(NC)"
	@echo "$(YELLOW)Press Ctrl+C to stop the server$(NC)"
	$(PYTHON) -m http.server $(PORT)

.PHONY: serve-bg
serve-bg:
	@echo "$(GREEN)Starting HTTP server in background on port $(PORT)...$(NC)"
	@echo "$(YELLOW)Open http://localhost:$(PORT)/$(VISUALIZER) in your browser$(NC)"
	@echo "$(YELLOW)Use 'make stop-server' to stop the server$(NC)"
	$(PYTHON) -m http.server $(PORT) &
	@echo $$! > server.pid
	@echo "$(GREEN)✅ Server started with PID: $$(cat server.pid)$(NC)"

.PHONY: stop-server
stop-server:
	@if [ -f server.pid ]; then \
		echo "$(GREEN)Stopping HTTP server...$(NC)"; \
		kill $$(cat server.pid) 2>/dev/null || true; \
		rm -f server.pid; \
		echo "$(GREEN)✅ Server stopped$(NC)"; \
	else \
		echo "$(YELLOW)No server PID file found$(NC)"; \
	fi

# =============================================================================
# Testing and Development
# =============================================================================

.PHONY: test
test: setup-venv
	@echo "$(GREEN)Running basic functionality tests...$(NC)"
	@echo "$(YELLOW)Testing generate command...$(NC)"
	$(VENV_PYTHON) $(CLI_SCRIPT) generate
	@echo "$(YELLOW)Testing analyze command with sample pattern...$(NC)"
	$(VENV_PYTHON) $(CLI_SCRIPT) analyze --pattern "../../00_common/pom.xml" --output-dir test-output
	@echo "$(GREEN)✅ Basic tests passed!$(NC)"
	@rm -rf test-output

.PHONY: generate
generate: setup-venv
	@echo "$(GREEN)Generating sample matrix data...$(NC)"
	$(VENV_PYTHON) $(CLI_SCRIPT) generate

.PHONY: analyze
analyze: setup-venv
	@echo "$(GREEN)Analyzing POM files...$(NC)"
	$(VENV_PYTHON) $(CLI_SCRIPT) analyze --pattern "../../**/pom.xml" --output-dir ../output --verbose

# =============================================================================
# Cleanup
# =============================================================================

.PHONY: clean
clean: stop-server
	@echo "$(GREEN)Cleaning up...$(NC)"
	rm -rf $(VENV_DIR)
	rm -rf __pycache__
	rm -rf .pytest_cache
	rm -rf test-output
	rm -rf output
	rm -f server.pid
	rm -f *.pyc
	@echo "$(GREEN)✅ Cleanup complete!$(NC)"

# =============================================================================
# Complete Setup
# =============================================================================

.PHONY: all
all: setup-venv check test
	@echo "$(GREEN)🎉 Everything is set up and working!$(NC)"
	@echo "$(YELLOW)Next steps:$(NC)"
	@echo "  1. Run 'make serve' to start the visualizer"
	@echo "  2. Open http://localhost:$(PORT)/$(VISUALIZER)"
	@echo "  3. Upload a matrix.json file to visualize dependencies"
