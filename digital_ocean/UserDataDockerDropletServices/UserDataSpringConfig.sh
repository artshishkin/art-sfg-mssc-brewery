#!/bin/bash

docker run -d -p 8888:8888 \
 -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@10.114.16.9:8761/eureka \
 -e eureka.instance.prefer-ip-address=true \
 --restart unless-stopped \
  artarkatesoft/art-sfg-mssc-config-server

#docker run -d -p 8888:8888 \
# -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@10.114.16.9:8761/eureka \
# -e eureka.instance.prefer-ip-address=true \
# -e eureka.instance.ip-address=104.248.253.6  \
# --restart unless-stopped \
#  artarkatesoft/art-sfg-mssc-config-server
