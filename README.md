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

####  Section 25: Securing Spring Cloud

#####  284. Property Encryption / Decryption

-  Set Environment Variable
-  `ENCRYPT_KEY=MySuperSecretKey`
-  POST `localhost:8888/encrypt` with body `MyPassword` -> 
    -  will receive encrypted password `ab29f52397a4bdbc89a9873d9cd46e492c20112c0d1204774e68240f9d74d652`
-  POST `localhost:8888/decrypt` with body `ab29f52397a4bdbc89a9873d9cd46e492c20112c0d1204774e68240f9d74d652` -> 
    -  will receive decrypted password `MyPassword`

####  Section 26: Building Docker Images with Maven

#####  299. Push Images to Docker Hub

-  `mvn clean package docker:build docker:push` - was errors
```
[ERROR] No plugin found for prefix 'docker' in the current project and in the plugin groups [org.apache.maven.plugins, org.codehaus.mojo] available from the repositories [local (C:\Use
rs\Admin\.m2\repository), central (https://repo.maven.apache.org/maven2)] -> [Help 1]
```
-  made with Intellij IDEA 

####  Section 27: Docker Compose

##### 305. Docker Compose for JMS Broker

-  `docker-compose up -d`
-  `docker-compose down`

#### Section 28: Consolidated Logging with ELK Stack

#####  317. View Logs in Kibana

1. Start docker compose
    -  `docker-compose -f docker-compose-logging.yml up -d`  
2.  View `filebeat` logs
    -  Exiting: error loading config file: config file ("filebeat.yml") can only be writable by the owner but the permissions are "-rwxrwxrwx" (to fix the permissions use: 'chmod go-w /usr/share/filebeat/filebeat.yml')
    -  in Windows we can disable the permission checking by adding:
        -  `command: filebeat -e -strict.perms=false`
3.  View logs
    -  localhost: 5601
    -  Kibana -> Discover ->
    -  Create index pattern
        -  Your index pattern matches 2 sources.
        -  Index pattern name: `filebeat*`
        -  Next step
    -  Select a primary time field for use with the global time filter.
        -  `@timestamp`
        -  Create index pattern
    -  Kibana -> Discover
        -  by trace id -> not working
        -  no  data from pattern
```json
{
    "trace": {
    "trace_id": "%mdc{X-B3-TraceId}",
    "span_id": "%mdc{X-B3-SpanId}",
    "parent_span_id": "%mdc{X-B3-ParentSpanId}",
    "exportable": "%mdc{X-Span-Export}"
    }
}
```         

####  Section 29: Deploying with Docker Swarm

#####  321. Provision Database Servers

1.  Create new Project in Digital Ocean
    -  Name: `Brewery Microservices from SFG`
    -  Description: `Microservices with Spring Boot Tutorial from SpringframeworkGuru`
2.  Create Database Cluster for Inventory Service
    -  DigitalOcean Console
    -  Manage -> Databases -> 
    -  Create a Database Cluster
        -  MySQL 8
        -  Frankfurt
        -  VPC Network: `fra1-vpc-01` (early created)
        -  DB name: `db-mysql-fra1-96429-beer-service`
        -  Project: `Brewery Microservices from SFG`
        -  `Important: We will automatically create a default database (defaultdb) and default admin user (doadmin).`
        -  Create Cluster
    -  Secure this database cluster
        -  Restrict inbound connections
            -  Add trusted sources
        -  Skip for now
3.  Create another Database Cluster for Beer Service
    -  `db-mysql-fra1-40585-inventory-service`
        -  Create Cluster
    -  Connection details
        - Connect to this database cluster
            -  Public network:
                -  username = doadmin
                -  password = **************** show
                -  host = db-mysql-fra1-40585-inventory-service-do-user-8611302-0.b.db.ondigitalocean.com
                -  port = 25060
                -  database = defaultdb
                -  sslmode = REQUIRED
                -  Download CA certificate -> rename it to `ca-certificate-inventory.crt`
                -  Connection String
                    -  `mysql://doadmin:show-password@db-mysql-fra1-40585-inventory-service-do-user-8611302-0.b.db.ondigitalocean.com:25060/defaultdb?ssl-mode=REQUIRED`                
            -  Private network:
                -  only host changes
                    -  `host = private-db-mysql-fra1-40585-inventory-service-do-user-8611302-0.b.db.ondigitalocean.com`
                -  Connection String
                    -  `mysql://doadmin:o2u...password...jeh@private-db-mysql-fra1-40585-inventory-service-do-user-8611302-0.b.db.ondigitalocean.com:25060/defaultdb?ssl-mode=REQUIRED`
    -  Next Steps
        -  A few things to help set up this database cluster
            -  Migrate an existing database
            -  Choose a time for automatic updates
            -  Restore from a backup
    -  Great, I'm done  
4.  Create another Database Cluster for Beer Order Service        

#####  322. Configure Database

1.  Create MySQL Connection
    -  MySQL Workbench
        -  Connection name: `inventory-service-db-digital-ocean`
        -  Hostname: host of DigitalOcean `db-mysql-fra1-67417-inventory-do-user-8611302-0.b.db.ondigitalocean.com` 
        -  Port: 25060
        -  Username: doadmin
        -  Password: from DO
        -  Default schema: defaultdb
        -  SSL CA File: path to downloaded file from Digital Ocean
        -  Create Connection (Save)
2.  Initialize Database  
    -  Connect to DB
    -  use inventory script [mysql-init.sql](beer-inventory-service/src/main/scripts/mysql-init.sql)
    -  evaluate
3.  Make the same for beer-service and beer-order-service    
            
#####  323. Configure Java Truststore

1.  Theory
    -  [Difference Between a Java Keystore and a Truststore](https://www.baeldung.com/java-keystore-truststore-difference)
    -  [Введение во взаимную аутентификацию сервисов на Java c TLS/SSL](https://habr.com/ru/company/dbtc/blog/487318/)
    -  [Keystore в Java](https://javadev.ru/https/ssl-keystore-java/)
3.  Create truststore 
    -  Copy certificate into docker folder
    -  `keytool -importcert -alias DoMySQLCert -file .\ca-certificate-inventory.crt -keystore truststore -storepass superSecretPassword`
        -  was generated truststore
4.  Add Program arguments
    -  javax.net.ssl.trustStore
        -  `-Djavax.net.ssl.trustStore=C:\Users\Admin\IdeaProjects\Study\SpringFrameworkGuru\MicroservicesCourse\art-sfg-mssc-brewery\beer-inventory-service\src\main\docker\truststore`
    -  javax.net.ssl.trustStorePassword             
        -  `-Djavax.net.ssl.trustStorePassword=superSecretPassword`
    -  Edit Configuration - add these arguments             
5.  Create new profile for DigitalOcean SQL
    -  create file `application-dosql.properties` from `localmysql`
    -  update `datasource.url` 
        -  modify hostname
        -  modify port
        -  sslMode -> `ssl-mode=REQUIRED`
6.  Activate profile
    -  Edit Configuration
    -  Active profiles: dosql        

##### 324. Add Truststore file to Docker Image

#####  325. Docker Image Release Process

1.  Release prepare dry run
    -  `mvn release:prepare -DdryRun`
    -  check everything is correct
    -  `mvn release:clean`
2.  Release prepare
    -  `mvn release:prepare`
    -  got an error
        -  [ERROR] Failed to execute goal org.apache.maven.plugins:maven-release-plugin:3.0.0-M1:prepare (default-cli) on project art-sfg-mssc-brewery: Unable to commit files
        -  [ERROR] Provider message:
        -  [ERROR] The git-push command failed.
        -  [ERROR] Command output:
        -  [ERROR] Host key verification failed.
        -  [ERROR] fatal: Could not read from remote repository.
        -  [ERROR]
        -  [ERROR] Please make sure you have the correct access rights
        -  [ERROR] and the repository exists.
    -  ~~mvn release:rollback~~  (failed - `[ERROR] The git-push command failed.`) and also wrong command
    -  `mvn release:clean`
3.  Set up credentials for github
    -  in settings.xml add server    
    -  `<server>`
    -  `  <id>github</id>`
    -  `  <username>USERNAME</username>`
    -  `  <password>PASSWORD</password>`
    -  `</server>`
    -  use PASSWORD in plain text
    -  **OR**
    -  `mvn --encrypt-password` + Enter -> insert PASSWORD (deprecated)
        -  `{7a8MxCHIq3gHK5Kk5c3nMKe1ZArCovlI5EuVunlbexM=}`
    -  **OR** 
    -  use TOKEN instead of PASSWORD (better solution)
4.  Release
    -  `mvn release:prepare`
    -  `mvn release:perform`
5.  Exceeded packagecloud Free limit
    -  250MB
    -  Need to migrate somewhere else -> for example Local Artifactory (for development purposes)    
6.  Deploy artifacts to Artifactory
    -  `docker volume create artifactory-data`
    -  `docker run -d --name artifactory -p 8182:8082 -p 8181:8081 -v artifactory-data:/var/opt/jfrog/artifactory docker.bintray.io/jfrog/artifactory-oss:6.21.0`
    -  `docker logs -f artifactory` -> to view credentials
    -  admin password
    -  localhost:8181 -> change password `qwerty123QWERTY`
    -  create Repositories for Maven
    -  view [art-spring-core-devops-aws](https://github.com/artshishkin/art-spring-core-devops-aws) - Section 6, Step 60
    -  in `settings.xml` we have       
        -  `<server>`
        -  `    <username>admin</username>`
        -  `    <password>AP7rgxNdpRkby5Y74TasgtVEXp7</password>`
        -  `    <id>central-local</id>`
        -  `</server>`
        -  `<server>`
        -  `    <username>admin</username>`
        -  `    <password>AP7rgxNdpRkby5Y74TasgtVEXp7</password>`
        -  `    <id>snapshots-local</id>`
        -  `</server>`
    -  deploy to artifactory
        -  `art-sfg-mssc-beerworks-bom>mvn clean deploy -P artifactory_local`
        -  `art-sfg-mssc-brewery-bom>mvn clean deploy -P artifactory_local`
7.  Deploy all artifacts to Artifactory
    -  `mvn clean deploy -P artifactory_local`
8.  Prepare Release
    -  `mvn release:prepare -P artifactory_local`
9.  Perform Release
    -  `mvn release:perform -P artifactory_local`          

#####  326. Provision Service VMs

-  Create Droplets -> Marketplace
-  Docker on Ubuntu
-  Frankfurt
-  SSH Key `digital_ocean`
-  6 droplets:
    -  jms
    -  zipkin
    -  kibana
    -  elastic
    -  eureka
    -  spring-config

#####  327. Configure JMS Server

-  SSH to it
    -  `ssh -i ~\.ssh\digital_ocean root@165.227.167.220`
-  start JMS
    -  `docker run -d --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis`  
```
Note: The default firewall for the Docker One-Click is UFW, which is a front end to iptables. 
However, Docker modifies iptables directly to set up communication to and from containers. 
This means that UFW won’t give you a full picture of the firewall settings. 
You can override this behavior in Docker by adding --iptables=false to the Docker daemon.
```
-  Just for now we enable public entrypoints for Docker Containers

#####  328. Configure Elasticsearch Server

-  SSH to it
-  `docker run -p 9200:9200 -p 9300:9300 -d -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.10.1`
-  can not start on Basic 1vCPU and 1Gb RAM
-  Resize to Basic Droplet with 1 vCPU and 2 Gb RAM
    -  Turn Off
    -  Resize
    -  Turn On 

#####  329. Configure Kibana Server

-  SSH to it
-  copy private IP of Elasticsearch server `10.114.16.6` (SFG uses public IP, I use private and private net)
-  `docker run --add-host elasticsearch:10.114.16.6 -p 5601:5601 docker.elastic.co/kibana/kibana:7.10.1`
-  test it is running -> OK
-  curl http://64.227.124.128:5601/
-  stop running container -> rerun it in detached mode
-  `docker run -d --add-host elasticsearch:10.114.16.6 -p 5601:5601 docker.elastic.co/kibana/kibana:7.10.1`

#####  330. Configure Zipkin Server

-  SSH to it
-  `docker run -d -p 9411:9411 openzipkin/zipkin`

#####  331. Configure Eureka Server

-  SSH to it
-  `docker run -d -p 8761:8761 artarkatesoft/art-sfg-mssc-brewery-eureka`

#####  332. Configure Spring Cloud Config Server

-  SSH to it
-  copy private IP of Eureka server `10.114.16.9` (SFG uses public IP, I use private and private VPC network)
-  `docker run -d -p 8888:8888 -e EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://EurekaUser:EurekaSuperSecretPass@10.114.16.9:8761/eureka -e EUREKA_INSTANCE_PREFER_IP_ADDRESS=true artarkatesoft/art-sfg-mssc-config-server`
-  **OR**
-  `docker run -d -p 8888:8888 -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@10.114.16.9:8761/eureka -e eureka.instance.prefer-ip-address=true artarkatesoft/art-sfg-mssc-config-server`
-  **OR** public IP
-  `docker run -d -p 8888:8888 -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@157.230.119.137:8761/eureka -e eureka.instance.prefer-ip-address=true artarkatesoft/art-sfg-mssc-config-server`
-  All points to 172.17.0.2:8888 but actual IPs:
    -  public: `104.248.253.6`
    -  private: `10.114.16.10`
-  If I go http://104.248.253.6:8888/actuator -> Login
```json
{
  "_links": {
    "self": {
      "href": "http://104.248.253.6:8888/actuator",
      "templated": false
    },
    "health": {
      "href": "http://104.248.253.6:8888/actuator/health",
      "templated": false
    },
    "health-path": {
      "href": "http://104.248.253.6:8888/actuator/health/{*path}",
      "templated": true
    },
    "info": {
      "href": "http://104.248.253.6:8888/actuator/info",
      "templated": false
    }
  }
}
```

#####  333. Spring Cloud Config Server IP Address Update

-  172.17.0.2 is internal IP of container inside the Docker context
-  to modify we can set IP manually by  
    -  `eureka.instance.ip-address=PUBLIC_IP_ADDRESS`
```shell script
docker run -d -p 8888:8888 \
 -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@10.114.16.9:8761/eureka \
 -e eureka.instance.prefer-ip-address=true \
 -e eureka.instance.ip-address=104.248.253.6  \
  artarkatesoft/art-sfg-mssc-config-server
```
#####  Deploying Services by using UserData

1.  Elasticsearch
    -  1vCPU, 2GB
    -  [UserDataElastic](digital_ocean/UserDataDockerDropletServices/UserDataElastic.sh)
2.  JMS 
    -  1vCPU, 1GB RAM
    -  [UserDataJMS](digital_ocean/UserDataDockerDropletServices/UserDataJMS.sh)
3.  Kibana
    -  [UserDataKibana](digital_ocean/UserDataDockerDropletServices/UserDataKibana.sh)
    -  copy private URL of Elasticsearch -> modify URL in UserData
4.  [Eureka](digital_ocean/UserDataDockerDropletServices/UserDataEureka.sh)
5.  [Zipkin](digital_ocean/UserDataDockerDropletServices/UserDataZipkin.sh)
6.  Spring Cloud Config Server 
    -  copy private URL of Eureka Server -> modify URL in UserData
    -  [UserDataSpringConfig](digital_ocean/UserDataDockerDropletServices/UserDataSpringConfig.sh)              

#####  335. [Linux Troubleshooting Commands](digital_ocean/LinuxTroubleshootingCommands.md)

#####  336. Initialize Docker Swarm Cluster

1.  Provision Docker Droplet Node 1 with UserData from [UserDataNode1.sh](digital_ocean/UserDataDockerDroplet/UserDataNode1.sh)
2.  Get join-token for managers
    -  SSH to node1
        -  `ssh -i ~\.ssh\digital_ocean root@167.71.62.227`
    -  get join-token
        -  `docker swarm join-token worker` -> save it
        -  **OR** manager
        -  `docker swarm join-token manager` -> save it
3.  Provision 2 another nodes Docker Droplet with UserData from [UserDataNode23.sh](digital_ocean/UserDataDockerDroplet/UserDataNode23.sh)
    -  replace line with join token by token from step 2

##### 337. Filebeat Swarm Configuration

1. Create image from filebeat to include filebeat config file
    -  `docker image build --file Dockerfile --tag artarkatesoft/mssc-filebeat:7.10.1 --tag artarkatesoft/mssc-filebeat:latest  .`
    -  `docker image push artarkatesoft/mssc-filebeat`
    -  `docker image push artarkatesoft/mssc-filebeat:7.10.1`
2.  Deploy stack with filebeat
    -  `docker stack deploy -c  docker-compose-digitalocean.yml only-filebeat`
    -  **or**
    -  by using `portainer` (**but** change version to **3.7**)
 
#####  339. Spring Cloud Configuration

-  we need to pass configuration into our digitalocean profile
```yaml
spring:
  cloud:
    config:
      enabled: true
      fail-fast: true
      username: MyUserName
      password: MySecretPassword
      discovery:
        enabled: true
        service-id: mssc-brewery-config
    discovery:
      enabled: true
```
-  one way is to use SPRING_APPLICATION_JSON
-  transforming config into JSON [onlineyamltools](https://onlineyamltools.com/convert-yaml-to-json)
```json
{
  "spring": {
    "cloud": {
      "config": {
        "enabled": true,
        "fail-fast": true,
        "username": "MyUserName",
        "password": "MySecretPassword",
        "discovery": {
          "enabled": true,
          "service-id": "mssc-brewery-config"
        }
      },
      "discovery": {
        "enabled": true
      }
    }
  }
}
```
-  config for profile digitalocean into repo `art-sfg-mssc-brewery-config-repo`
      
#####  338. Eureka Swarm Configuration (Using SPRING_APPLICATION_JSON)

-  View [2. Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config)  
-  Use [jsoneditoronline](https://jsoneditoronline.org/) to modify JSON
-  Modify config according to digitalocean config
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://EurekaUser:EurekaSuperSecretPass@10.114.16.6:8761/eureka
    region: default
    register-with-eureka: true
  instance:
    prefer-ip-address: false
    hostname: order-service
```  
-  transforming config into JSON [onlineyamltools](https://onlineyamltools.com/convert-yaml-to-json)
```json
{
  "eureka": {
    "client": {
      "service-url": {
        "defaultZone": "http://EurekaUser:EurekaSuperSecretPass@10.114.16.6:8761/eureka"
      },
      "region": "default",
      "register-with-eureka": true
    },
    "instance": {
      "prefer-ip-address": false,
      "hostname": "order-service"
    }
  }
}
```
-  combine it with json in step 339
-  inline JSON into `SPRING_APPLICATION_JSON` using [jsoneditoronline](https://jsoneditoronline.org/)

##### Making Spring Config Service Auto Detect Private IP

1.  SSH to it
2.  Test using [DigitalOcean Metadata API](https://developers.digitalocean.com/documentation/metadata/)
    -  view [How to find your private IP address on DigitalOcean?](https://stackoverflow.com/questions/32554992/how-to-find-your-private-ip-address-on-digitalocean) 
    -  `curl -w "\n" http://169.254.169.254/metadata/v1/interfaces/private/0/ipv4/address`
3.  Modify UserData
```shell script
private_ip=`curl -w "\n" http://169.254.169.254/metadata/v1/interfaces/private/0/ipv4/address`

docker run -d -p 8888:8888 \
 -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@10.114.16.5:8761/eureka \
 -e eureka.instance.prefer-ip-address=true \
 -e eureka.instance.ip-address=$private_ip \
 --restart unless-stopped \
  artarkatesoft/art-sfg-mssc-config-server
```

    
#####  341. Running Microservices with Docker Swarm

1.  Login in Portainer
2.  Stacks -> Add Stack
    -  Name: `brewery`
    -  Insert `docker-compose-digitalocean.yml` content
3.  Debug
    -  update config server
        -  SSH to it
        -  remove container
            -  `docker container rm 636 -f`
        -  run new with it's private IP
            -  `docker run -d -p 8888:8888 -e eureka.client.service-url.defaultZone=http://EurekaUser:EurekaSuperSecretPass@10.114.16.6:8761/eureka -e eureka.instance.prefer-ip-address=true -e eureka.instance.ip-address=10.114.16.9  --restart unless-stopped  artarkatesoft/art-sfg-mssc-config-server`   
4.  **DOES NOT WORK**
    -  Deep debug is needed

#####  Debug on 1 NODE

1.  Create 1 Node or Resize existing to 2vCPU and 4GB
2.  Disable filebeat for now
3.  Increase healthcheck timeouts
4.  Database global without SSL
5.  Added `order-service` to stack
    -  added environment variable
    -  NET_SHYSHKIN_CLIENT_BEER_SERVICE_HOST: 'http://beer-service:8080'
    -  it seems like it's RestTemplate does not use Eureka

#####  342. Tracing Requests for Troubleshooting

1.  Commands
    -  `docker stack ps brewery` - all containers of stack over all nodes
    -  `docker ps` - only current node
2.  Start up all services for tracing
    -  elasticsearch
    -  kibana
    -  filebeat on Swarm Stack
3.  Disable `base_log` profile in Swarm Stack     
4.  Wait all is deployed
5.  View Logs in Kibana
    -  `http://64.225.101.82:5601/`
    -  Search for Log Level: ERROR        

#####  343. Zipkin Tracing

1.  View Zipkin Server Content
    -  http://68.183.222.205:9411/
2.  Search timeouts by traceId, spanId
    -  too many sequential calls -> view in Kibana by traceId
    -  logger_name: net.shyshkin.study.beerorderservice.services.beerservice.BeerServiceRestTemplateImpl
	-  message: Calling Beer service (getBeerByUpc)

#####  Multiple Nodes

1.  Start another 2 Droplet Swarm Nodes
    -  `docker swarm join-token manager`
    -  `docker swarm join --token SWMTKN-1-4ln5cdclpvskm9omec731v810cdk9mjt8pmn22bin2m85pvayf-1h57oqvqx4aje92blmgoq1ppe 10.114.16.9:2377`
2.  Deploy Stack through Portainer
3.  Tested successfully                      