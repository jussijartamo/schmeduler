Schmeduler
==========

Quartzlike, but simpler job scheduling library for Java 8

## Motivation

Simple scheduling case should be simple!

```java
import static org.schmeduler.immutable.SchmedulerBuilder.*;
```

```java
final Runnable job = () -> System.out.println("Hello World!");

newBuilder().addJob(every(5).seconds(), job).build();
```

## Usage

To use Schmeduler in you project, add the following line to your `pom.xml`

```xml
<dependency>
    <groupId>org.schmeduler</groupId>
    <artifactId>schmeduler</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Roadmap to first release

- [ ] Cron trigger implementation
- [ ] Test triggers using Quartz as reference
- [ ] Immutable scheduler tests
- [ ] Optional JodaTime implementation
- [x] Mutable scheduler and triggers implementation
- [x] Immutable scheduler and triggers implementation
- [x] Simple trigger implementation

## Design principles

- Single JAR
- No dependencies
- Extensible
- Support JodaTime (optional dependency)
- Support custom thread pools and executor services
- Simple case should be simple
- Complex case should be possible
- Convention over configuration
- Immutable by default
- Support mutable triggers and scheduler

## License and attribution

Code is licensed MIT.