#!/bin/bash

sudo chown -R appuser:appuser /
whoami

echo "Waiting for Kafka to be ready..."
cub kafka-ready -b localhost:9092 1 20

echo "Creating topics..."
sleep 2

kafka-topics --create --topic input-topic --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1 || true
kafka-topics --create --topic output-topic --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1 || true

for i in {1..255}; do ip=127.0.0.$i; $(echo "ie.w12.33.$ip" | kafka-console-producer --topic input-topic --bootstrap-server kafka:9092) ; done >> /dev/null 2>&1 &

echo "Done."
