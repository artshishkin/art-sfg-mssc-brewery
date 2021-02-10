#!/bin/bash

docker run -d -p 9411:9411 --restart unless-stopped  openzipkin/zipkin