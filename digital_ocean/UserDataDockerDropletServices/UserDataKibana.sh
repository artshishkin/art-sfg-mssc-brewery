#!/bin/bash

docker run -d --add-host elasticsearch:10.114.16.6 -p 5601:5601 --restart unless-stopped docker.elastic.co/kibana/kibana:7.10.1