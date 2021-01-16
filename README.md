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

####  Section 8: Spring MVC REST Docs

#####  95. Documenting Validation Constraints

-  view
-  `target/generated-snippets/v1/beer/request-fiels.snippet`

#####  96. URI Customization

-  view
-  `target/generated-snippets/v1/beer/curl-request.snippet`

#####  98. Serving Docs with Spring Boot

-  generate jar
-  run it
-  go to `http://localhost:8080/docs/index.html` to view docs

####  Section 11: Enterprise Dependency Management

#####  142. Maven BOM Setting Common Properties

-  `pom.xml` -> right mouse click -> Maven -> Show Effective POM

#####  147. Beer Service Parent POM Configuration

-  SFG uses separate project as BOM (I am using multi-module Maven project)
-  to use it as BOM we need to install it into local m2 repo
    -  `mvn install`
    
#####  Extracting Dependencies into separate BOM module

-  `mvn clean install` module `art-sfg-mssc-brewery-bom` **first**
-  then we can `mvn clean verify all the project`

#####  150. Using Released BOMs (deploy to PackageCloud)

We want to upload our RELEASE into some repository to be online. This may be:
-  Maven Central
-  Nexus (in Maven Course we installed it locally)
-  Package Cloud

I choose `PackageCloud` - [Packagecloud Maven Wagon](https://github.com/computology/maven-packagecloud-wagon). Steps to deploy:
1.  Follow tutorial [Password Encryption](https://maven.apache.org/guides/mini/guide-encryption.html)
    -  create master password
    -  copy PackageCloud `API Token` and encrypt it
2.  Add `maven-packagecloud-wagon` extension into `pom.xml`
3.  Add `distributionManagement` section into `pom.xml`
4.  Modify `settings.xml` to have encrypted credentials
5.  From project module `art-sfg-mssc-brewery-bom` run
    -  `mvn clean deploy`
6.  View `https://packagecloud.io/art_shishkin/snapshot`
    -  `https://packagecloud.io/art_shishkin/snapshot/packages/java/net.shyshkin.study/art-sfg-mssc-brewery-bom-0.0.1-SNAPSHOT.pom`
    -  was deployed            

```xml
<settings>
  <servers>
    <server>
      <id>packagecloud.my_repo_releases</id>
      <password>{yqWsgm3YOgEH/Ia+04m7ne6an0djxyGmDN1yxZmeD83du7bF2qJj6CUrtG1lTE4zEfbr9jgrXs4AFpVoC92ijL2rMhtUrhfRZvQmeqmA/S0=}</password>
    </server>
    <server>
      <id>packagecloud.my_repo_snapshots</id>
      <password>{yqWsgm3YOgEH/Ia+04m7ne6an0djxyGmDN1yxZmeD83du7bF2qJj6CUrtG1lTE4zEfbr9jgrXs4AFpVoC92ijL2rMhtUrhfRZvQmeqmA/S0=}</password>
    </server>
  </servers>
</settings>
```

#####  153. IntelliJ Workspace Tips and Tricks

SFG has every microservice project separate (I have multi-module maven project).
For SFG goal is to use one IDEA workspace for all three projects.
Steps:
1.  Create directory `mssc-brewery-ws`
2.  Create new **Empty** project  with the same name
3.  Clone every project into that directory
4.  Idea -> New -> Module from existing sources
    -  repeat for all 3 projects
    
####  Section 12: Local MySQL Configuration

#####  159. MySQL Beer Service Configuration

-  in MySQL Workbench run script [mysql-init.sql](https://github.com/artshishkin/art-sfg-mssc-brewery/blob/master/beer-service/src/main/scripts/mysql-init.sql)    

#####  161. Correcting Hibernate Error with MySQL

-  run with `localmysql` profile through setting it in Intellij Run Config (active profiles)

####  Section 13: JMS Messaging

#####  180. Running Active MQ in Docker

-  Use docker image [activemq-artemis-docker](https://github.com/vromero/activemq-artemis-docker)
-  `docker run -it --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis`
-  port 8161
-  username/password: artemis / simetraehcapa

#####  181. Using Local ActiveMQ Broker with Spring Boot

-  after start login to Artemis Management Console and view Queues
-  to view messages in console exclude listeners (comment out @Component)

####  Section 17: Integration Testing of Sagas

#####  235. Testing with WireMock

[WireMock Examples](https://github.com/JensPiegsa/wiremock-extension/blob/master/src/test/java/com/github/jenspiegsa/wiremockextension/ExampleTest.java)

####  Section 23: Spring Cloud Config

#####  273. Spring Cloud Config Server Configuration

-  GET `localhost:8888/foo/default`

```json
{
    "name": "foo",
    "profiles": [
        "default"
    ],
    "label": null,
    "version": "00e296ff9309e4e66353b5f43003393232d62c6a",
    "state": null,
    "propertySources": [
        {
            "name": "https://github.com/artshishkin/art-sfg-mssc-example-config-repo/file:C:\\Users\\Admin\\AppData\\Local\\Temp\\config-repo-16298660152083958882\\application.yml",
            "source": {
                "foo": "bar"
            }
        }
    ]
}
```
-  **OR**
-  GET `host.docker.internal:8888/some_app_name/dev_profile`
```json
{
    "name": "some_app_name",
    "profiles": [
        "dev_profile"
    ],
    "label": null,
    "version": "00e296ff9309e4e66353b5f43003393232d62c6a",
    "state": null,
    "propertySources": [
        {
            "name": "https://github.com/artshishkin/art-sfg-mssc-example-config-repo/file:C:\\Users\\Admin\\AppData\\Local\\Temp\\config-repo-16298660152083958882\\application.yml",
            "source": {
                "foo": "bar"
            }
        }
    ]
}
```

#####  274. Server Side Application Configuration

-  copy `application-localmysql.properties` from beer-service module into `art-sfg-mssc-brewery-config-repo/beer-service`
-  rename it to `application-local.properties`
-  push `art-sfg-mssc-brewery-config-repo`
-  start `ArtSfgMsscConfigServerApplication`
-  GET `localhost:8888/all/default` -> receive global config
```json
{
    "name": "all",
    "profiles": [
        "default"
    ],
    "label": null,
    "version": "6243d2bab0061cbf22a0606fbc61df0217565629",
    "state": null,
    "propertySources": [
        {
            "name": "https://github.com/artshishkin/art-sfg-mssc-brewery-config-repo/file:C:\\Users\\Admin\\AppData\\Local\\Temp\\config-repo-8342947173172847625\\application.yml",
            "source": {
                "some.global.property": "globalPropertyValue"
            }
        }
    ]
}
```
-  GET `localhost:8888/beer-service/default` -> receive global config (for beer-service we have no default profile in GIT)
-  GET `localhost:8888/beer-service` -> error  
-  GET `localhost:8888/beer-service/local` -> receive config for it (same as in `application-local.properties`) PLUS global
```json
{
    "name": "beer-service",
    "profiles": [
        "local"
    ],
    "label": null,
    "version": "6243d2bab0061cbf22a0606fbc61df0217565629",
    "state": null,
    "propertySources": [
        {
            "name": "https://github.com/artshishkin/art-sfg-mssc-brewery-config-repo/file:C:\\Users\\Admin\\AppData\\Local\\Temp\\config-repo-8342947173172847625\\beer-service\\application-local.properties",
            "source": {
                "spring.datasource.url": "jdbc:mysql://localhost:3306/beerservice?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC",
                "spring.datasource.username": "beer_service",
                "spring.datasource.password": "password",
                "spring.jpa.database": "mysql",
                "spring.datasource.platform": "mysql",
                "spring.jpa.hibernate.ddl-auto": "update",
                "spring.jpa.properties.hibernate.dialect": "org.hibernate.dialect.MySQL8Dialect",
                "spring.datasource.driver-class-name": "com.mysql.cj.jdbc.Driver",
                "spring.datasource.hikari.maximum-pool-size": "5",
                "spring.datasource.hikari.data-source-properties.cachePrepStmts": "true",
                "spring.datasource.hikari.data-source-properties.prepStmtCacheSize": "250",
                "spring.datasource.hikari.data-source-properties.prepStmtCacheSqlLimit": "2048",
                "spring.datasource.hikari.data-source-properties.useServerPrepStmts": "true",
                "spring.datasource.hikari.data-source-properties.useLocalSessionState": "true",
                "spring.datasource.hikari.data-source-properties.rewriteBatchedStatements": "true",
                "spring.datasource.hikari.data-source-properties.cacheResultSetMetadata": "true",
                "spring.datasource.hikari.data-source-properties.cacheServerConfiguration": "true",
                "spring.datasource.hikari.data-source-properties.elideSetAutoCommits": "true",
                "spring.datasource.hikari.data-source-properties.maintainTimeStats": "false"
            }
        },
        {
            "name": "https://github.com/artshishkin/art-sfg-mssc-brewery-config-repo/file:C:\\Users\\Admin\\AppData\\Local\\Temp\\config-repo-8342947173172847625\\application.yml",
            "source": {
                "some.global.property": "globalPropertyValue"
            }
        }
    ]
}
```

#####  275. Spring Cloud Client Configuration

-  change active profiles from `localmysql,local-discovery` to `local,local-discovery`

####  Section 24: Distributed Tracing

#####  278. Zipkin Server

-  [Zipkin Quick Start](https://zipkin.io/pages/quickstart.html)
-  `docker run -d -p 9411:9411 openzipkin/zipkin`