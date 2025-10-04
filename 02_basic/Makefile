# Basic Example Makefile

.DEFAULT_GOAL := run

.PHONY: build run clean test

build:
	mvn clean compile -q

run: build
	mvn spring-boot:run

clean:
	mvn clean -q

test:
	mvn test -q
