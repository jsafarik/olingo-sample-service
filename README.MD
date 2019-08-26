# OData sample service

OData sample service used in syndesis-qe tests.

## General info

The standard port of the service is port `8080`. The port can be easily changed with mvn property `-Dserver.port=your_server_port`.

There are four entity types (with types of their IDs):

 - Category `(int)`
 - Product `(int)`
 - User `(String)`
 - Advertisement `(GUID (UUID))`
 
There is an Enum type: `Gender` with values: `MALE, FEMALE` and `UNSPECIFIED` (which are represented by integer values starting from 0).

## Deployment

Standard way to deploy this service is to generate `.war` file using `mvn clean install` and deploy it to a running server (Tomcat, WildFly, ...). This is the standard way so it is easy to deploy on OpenShift using s2i WildFly image.

## Local Deployment using embedded Tomcat

In order to run this service locally, there needs to be set maven property `tomcat.server` to `compile`. Running this service then could look like this: `mvn spring-boot:run -Dtomcat.server=compile`
