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