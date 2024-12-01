#!/bin/bash

# Use the environment variable to create the keyspace
cqlsh cassandra -e "CREATE KEYSPACE IF NOT EXISTS ${CASSANDRA_KEYSPACE:-near_connect}
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"