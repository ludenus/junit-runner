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

## Run test OUTSIDE docker container

```shell
./gradlew clean test
```

## Rebuild and run tests INSIDE docker container
```shell
./gradlew clean build jibDockerBuild && docker run -e LOG_LEVEL=INFO ludenus/junit-runner:latest
```

## Build docker image locally WITH docker daemon

```shell
./gradlew clean build jibDockerBuild
```

## Build docker image and push to docker registry WITHOUT docker daemon

```shell
./gradlew clean build jib
```

## deploy junit-runner job along with allure server 
```shell
helm upgrade --install junit-runner ./helm/junit-runner
```