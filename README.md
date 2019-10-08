# Docker SDK for Java
[![JDK](https://img.shields.io/badge/java-SE8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) [![Code Style](https://img.shields.io/badge/codestyle-google-blue.svg)](https://google.github.io/styleguide/javaguide.html)

Docker SDK for Java 8.

> <b>IMPORTANT NOTE:</b> Docker Java SDK is yet still in development. Therefore the README file might receive
updates later on. Docker Java SDK can be used for testing etc. but is not ready for any enterprise environment.

## Authors
* **Manuel Kollus** - *Initial work* - [manuelkollus](https://github.com/manuelkollus)

See also the list of [contributors](https://github.com/manuelkollus/docker-java-sdk/contributors) who participated in this project.

## Download
Download the latest JAR 

#### Gradle
```groovy
compile 'io.github.manuelkollus:docker-java-sdk:1.0'
```

##### Maven
```xml
<dependency>
  <groupId>io.github.manuelkollus</groupId>
  <artifactId>docker-java-sdk</artifactId>
  <version>1.0</version>
</dependency>
```

#### Ivy
```ivy
<dependency org="io.github.manuelkollus" name="docker-java-sdk" rev="1.0"/>
```

## Dependencies
Here are all of our dependencies listed which are used for the Docker Java SDK. The dependency's name will take you to the dependency's project page. The _"artifact id"_ will take you to the dependency's index on the [MavenRepository website](https://mvnrepository.com).

- [Protobuf Java](https://developers.google.com/protocol-buffers/docs/javatutorial) ([com.google.protobuf:protobuf-java:3.10.0](https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java/3.10.0))
- [Protobuf Java Format](https://code.google.com/archive/p/protobuf-java-format/) ([com.googlecode.protobuf-java-format:protobuf-java-format:1.4](https://mvnrepository.com/artifact/com.googlecode.protobuf-java-format/protobuf-java-format))
- [Guava](https://github.com/google/guava) ([com.google.guava:guava:28.0-jre](https://mvnrepository.com/artifact/com.google.guava/guava/28.0-jre))
- [Guice](https://github.com/google/guice) ([com.google.inject:guice:4.0](https://mvnrepository.com/artifact/com.google.inject/guice/4.0))
- [HttpClient](http://hc.apache.org/httpcomponents-client-ga/index.html) ([org.apache.httpcomponents:httpclient:4.5.10](https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient/4.5.10))
- [Commons IO](http://commons.apache.org/proper/commons-io/) ([commons-io:commons-io:2.6](https://mvnrepository.com/artifact/commons-io/commons-io/2.6))
- [JUnit4](https://junit.org/junit4/) ([junit:junit:4.12](https://mvnrepository.com/artifact/junit/junit/4.12))

## Contributing
Please read [CONTRIBUTING.md](https://github.com/manuelkollus/docker-java-sdk/blob/master/.github/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning
We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/manuelkollus/docker-java-sdk/tags).
