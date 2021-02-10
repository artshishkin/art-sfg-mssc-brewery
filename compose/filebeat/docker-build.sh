#!/bin/bash

docker image build --file Dockerfile --tag artarkatesoft/mssc-filebeat:7.10.1 --tag artarkatesoft/mssc-filebeat:latest  .

docker image push artarkatesoft/mssc-filebeat
docker image push artarkatesoft/mssc-filebeat:7.10.1

