#!/bin/bash

private_ip=`curl -w "\n" http://169.254.169.254/metadata/v1/interfaces/private/0/ipv4/address`

docker run -d -p 8888:8888 \
 -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@10.114.16.5:8761/eureka \
 -e eureka.instance.prefer-ip-address=true \
 -e eureka.instance.ip-address=$private_ip \
 --restart unless-stopped \
  artarkatesoft/art-sfg-mssc-config-server

