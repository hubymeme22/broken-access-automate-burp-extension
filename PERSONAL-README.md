### Folder Setup

```
mvn archetype:generate -DgroupId=caregoautomation -DartifactId="com.caregoautomation.me" -DarchetypeArti
ficialId=maven-archetype-quickstart -DinteractiveMode=false
```

### Added Dependency

```
    <dependency>
      <groupId>net.portswigger.burp.extensions</groupId>
      <artifactId>montoya-api</artifactId>
      <version>LATEST</version>
    </dependency>
```

### Resolve and download dependencies added

```
mvn dependency:resolve
```

### Build into jar file

```
mvn package
```