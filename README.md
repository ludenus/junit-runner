# junit-runner
Skeleton project for dockerized Kotlin JUnit 5 tests with:
* Springboot config
* log4j logger
* jib docker plugin
* picocli plugin for CLI interface
* axion-release plugin for versioning
* gradle-git-properties for accessing git info
* allure report
* helm deploy

## Problem
Sometimes we need to run integration or system test suite inside kubernetes. 
Naive approach is to pack test source code along with the build system (gradle/maven/sbt) inside docker image so that when we run it - test phase is performed.
It works, but because build system requires a lot of intermittent dependencies to build and run test classes - there are drawbacks:
* huge docker image size - if required dependencies are packed inside docker image beforehand
* extremely slow bootstrap process - if required dependencies are downloaded by build system upon docker container launch  

## Solution
Alternative approach is to build docker image that does NOT require build system to run tests. 
JUnit 5 has [standalone console launcher](https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher) to address such option.
However standalone launcher does not have any convenient way to provide configuration to our tests. 
So we use JUnit 5 launcher as library and build our own application that provide us test configuration and test run entrypoint. 
By doing that we reduce docker image size and make our test docker launch faster.

Current project is a skeleton that implements the described alternative approach. 

## How it works
The compiled test classes are packed inside docker image via [jib gradle plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin) along with skeleton springboot application that provides test entrypoint and enables easy test configuration.
When test container is launched JUnit5 ConsoleTestExecutor runs tests.

### Test Configuration
[Springboot configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config) features can be used to configure our tests. 
Update [application.yaml](src/main/resources/application.yaml), [AppConfig](src/main/kotlin/com/github/ludenus/qa/runner/config/AppConfig.kt) to add new config options. 
Check [MainApplicationTests](src/test/kotlin/com/github/ludenus/qa/runner/springboottest/MainApplicationTests.kt) for test config usage example. 
The most convenient way to use springboot configuration is to set environment variables for docker container.
You may also consider creating [profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.files.profile-specific) for different environments and other springboot config options.

### Test Reports
Fetching test reports from docker container is a tricky task due to container's ephemeral nature.
A possible way is to use external report publishing engine and submit test results after tests are done. 
Current project uses [Allure framework](https://github.com/allure-framework/allure2) and [Allure server](https://github.com/kochetkov-ma/allure-server) to publish test results.
Check sample [helm chart](helm/junit-runner) for report configuration details.


## How to Run

### Run test OUTSIDE docker container

```shell
./gradlew clean test
```

### Rebuild and run tests INSIDE docker container
```shell
./gradlew clean build jibDockerBuild && docker run -e LOG_LEVEL=INFO ludenus/junit-runner:latest
```

### Build docker image locally WITH docker daemon

```shell
./gradlew clean build jibDockerBuild
```

### Build docker image and push to docker registry WITHOUT docker daemon

```shell
./gradlew clean build jib
```

### Deploy junit-runner job along with allure server 
```shell
helm upgrade --install junit-runner ./helm/junit-runner
```