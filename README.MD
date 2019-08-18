# OData sample service

OData sample service used in syndesis-qe tests.

## Deployment

Standard way to deploy this service is to generate `.war` file using `mvn clean install` and copy it to a running server (Tomcat, WildFly, ...). This is the standard way so it is easy to deploy on OpenShift using s2i WildFly image.

## Local Deployment using embedded Tomcat

In order to "just start" this service, there needs to be a change in `pom.xml`. This would be:
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-tomcat</artifactId>
	<scope>provided</scope>
</dependency>
```

and

```xml
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>servlet-api</artifactId>
	<version>${javax.version}</version>
	<scope>provided</scope>
</dependency>
```

both needs to have the `<scope>provided</scope>` deleted. After that, with `mvn spring-boot:run`, the service should start with embedded Tomcat.

## Features

There are four entity types:

 - Category
 - Product
 - User
 - Advertisement
 
Category and Product have ID of type `int`. Advertisement have ID of type `GUID (UUID)` User has ID of type `String`. There is an Enum type: `Gender` with values: `MALE, FEMALE` and `UNSPECIFIED`.