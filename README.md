# Units of Measure Service

The Unit Service is a Maven multi-module project with each cloud implemention placed in its submodule.

## This repository contains

1. The Java implementation of the Units of Measure catalog and conversion service (aka dps-unit-service). The Java code is located in the ```src``` folder. To open the Java project, open ```pom.xml```.
1. Tests are located in ```src/test/java/org/opengroup/osdu/unitservice/...```
1. The openapi specification file is `unit_service_openapi_v2.json`.
1. Python integration and health tests in the ```testing``` folder.
See also the test's [README.md](testing/README.md)

## Note on API Versions

The Unit Service supports 2 APIs.  These APIs are V2 and V3.
**Note: The V2 API is depcrecated**

The V2 and V3 APIs have the same functionality, however the V3 API uses query params vs inline route path params to set variables.
This allows for better handling of special characters and support for future extensibility of the Unit Service's routes.

Both APIs are available in Swagger at <https://[Unit_Service_Host>]/api/unit/swagger-ui.html

## Prerequisites

1. The project builds with [Maven](https://maven.apache.org/). Make sure Maven is installed locally.
2. [JDK 25](https://adoptium.net/). Set `JAVA_HOME` accordingly.
3. The project requires the [Lombok](https://projectlombok.org/) plug-in installed for your IDE.

### Build service and run unit tests

```sh
mvn clean install
```

## Running Unit Service locally

### Core-plus

Instructions for running the core-plus implementation are in [unit-core-plus/README.md](unit-core-plus/README.md).

### Debug locally - e.g. using Postman

In the Postman Settings / General, turn SSL certificate validation off when running locally.
Similarly, when not using Postman but client code, set the configuration  ```verify_ssl``` false (see [instructions](https://github.com/swagger-api/swagger-codegen/issues/7778))

Run application using debug mode and use [Postman](https://www.getpostman.com/)
to send a GET request to obtaining the Swagger API documentation:
### Open API 3.0 - Swagger

- Swagger UI : https://host/context-path/swagger (will redirect to https://host/context-path/swagger-ui/index.html)
- api-docs [All Versions] (JSON) : https://host/context-path/api-docs
- api-docs [All versions] (YAML) :https://host/context-path/api-docs.yaml
- api-docs [Version V2] (JSON) : https://host/context-path/api-docs/v2 (DEPRECATED)
- api-docs [Version V3] (JSON) : https://host/context-path/api-docs/v3

All the Swagger and OpenAPI related common properties are managed here [swagger.properties](https://community.opengroup.org/osdu/platform/system/reference/unit-service/-/blob/jb/az_swagger_openapi/unit-core/src/main/resources/swagger.properties)


Headers for Postman:

| Key | Value |
|----------|----------|
| Authorization | Bearer `<token>` |
| data-partition-id | $MY_TENANT (see [testing\README.md](testing/README.md)) |

#### Server Url(full path vs relative path) configuration
- `api.server.fullUrl.enabled=true` It will generate full server url in the OpenAPI swagger
- `api.server.fullUrl.enabled=false` It will generate only the contextPath only
- default value is false
[Reference]:(https://springdoc.org/faq.html#_how_is_server_url_generated) 

## Google Cloud

The Google Cloud implementation of the Unit service is maintained in [gc-osdu-services](https://community.opengroup.org/osdu/platform/deployment-and-operations/base-containers-gcp/gc-osdu-services/-/tree/main/system/reference_and_helper_services/unit).
