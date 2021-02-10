#!/bin/bash

docker run -p 9200:9200 -p 9300:9300 -d -e "discovery.type=single-node" --restart unless-stopped  docker.elastic.co/elasticsearch/elasticsearch:7.10.1