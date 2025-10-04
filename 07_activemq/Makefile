# ActiveMQ Example Makefile

.DEFAULT_GOAL := run

.PHONY: build run clean test broker

build:
	mvn clean package -q

run: build
	mvn spring-boot:run

test:
	@bash test-example.sh

broker:
	docker run -d --name activemq -p 61616:61616 -p 8161:8161 rmohr/activemq

clean:
	mvn clean -q
