#!/bin/bash

docker run -d -p 8161:8161 -p 61616:61616 --restart unless-stopped vromero/activemq-artemis