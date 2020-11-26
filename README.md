[![CircleCI](https://circleci.com/gh/artshishkin/art-sfg-mssc-brewery.svg?style=svg)](https://circleci.com/gh/artshishkin/art-sfg-mssc-brewery)
![Spring Boot version][springver]
![Project licence][licence]
 
# Spring Boot Microservices with Spring Cloud: Beginner to Guru

## Tutorial on Microservices from SFG (Udemy)

[springver]: https://img.shields.io/badge/dynamic/xml?label=Spring%20Boot&query=%2F%2A%5Blocal-name%28%29%3D%27project%27%5D%2F%2A%5Blocal-name%28%29%3D%27parent%27%5D%2F%2A%5Blocal-name%28%29%3D%27version%27%5D&url=https%3A%2F%2Fraw.githubusercontent.com%2Fartshishkin%2Fart-sfg-mssc-brewery%2Fmaster%2Fpom.xml&logo=Spring&labelColor=white&color=grey
[licence]: https://img.shields.io/github/license/artshishkin/art-sfg-mssc-brewery.svg

####  Section 5: Spring Boot RestTemplate

#####  54. Apache Client Request Logging

-  enable debug for Apache Http
    -  `logging.level.org.apache.http=debug`
-  run integration test
    -  start `ArtSfgMsscBreweryApplication`
    -  run test `BreweryClientIT`
        -  for example just `getBeerById`
    -  view logs
        -  requests
        -  responses    
