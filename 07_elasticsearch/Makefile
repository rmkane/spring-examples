# Elasticsearch Example Makefile

.DEFAULT_GOAL := run

.PHONY: build run clean test elasticsearch

build:
	mvn clean package -q

run: build
	mvn spring-boot:run

test:
	@bash test-example.sh

elasticsearch:
	docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "xpack.security.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.11.0

clean:
	mvn clean -q
