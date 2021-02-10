#!/bin/bash

docker run -d -p 8761:8761 --restart unless-stopped  artarkatesoft/art-sfg-mssc-brewery-eureka
