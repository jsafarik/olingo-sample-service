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

Standard way to deploy this service is to generate `.war` file using `mvn clean install` and deploy it to a running server (Tomcat, WildFly, ...). This way it should be easy to deploy this service on OpenShift using s2i WildFly image.

## Local Deployment using embedded Tomcat

In order to run this service locally, there needs to be set maven property `tomcat.server` to `compile`. Running this service then could look like this: `mvn spring-boot:run -Dtomcat.server=compile`. Another property which can be changed (for better testing, usually when there's more services running on one machine) is `server.port`. Standard port is `8080`, but can be changed. This change then should look like this: `-Dserver.port=10010` (note that running embedded tests will fail as they are hardcoded to port 8080 for now).

## Service tests

There are created multiple tests to assure that the service is working as supposed. Every test with class name starting with `IntegrationsOData` is a test mirroring tests in syndesis-qe - for every new scenario there should be added new test to ensure correct behaviour on the service side.
