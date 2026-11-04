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

### Azure

#### Build and run Unit Service locally using bash

- Set the required environments described in [Build](##Build) and [Release/deployment](##Release/deployment) sections
- Navigate to the Unit Service's root folder ```unit-service```
- Build core and run unit tests in command line:

```bash
mvn clean install # To run without tests add -Dmaven.test.skip=true
```

- Navigate to the Unit Service's root folder ```unit-service```
- Run application with command

```bash
java -jar provider/unit-azure/unit-aks/target/unit-aks-1.0.0.jar
```

#### Running Azure Unit Service using IntelliJ IDEA

Navigate to the **Create Run/Debug Configuration** tool
Select **'Add New Configuration'** and select **Application**

Type the next commands into the suggested fields:

- Working directory: ```{path_to_the_unit}/unit-service```
- Main class: ```org.opengroup.osdu.unitservice.UomAksApplication```
- Use classpath of module:  ```unit-aks```  
***Note: If you don't see "unit-aks" in the dropdown menu - find appropriate pom.xml and click "Add as a Maven project"***
- Environment variables: Set the required environments described in [Build](##Build) and [Release/deployment](##Release/deployment) section

Execute **Run** or **Debug** for configured Application

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
- default value is false (Currently only in Azure it is enabled)
[Reference]:(https://springdoc.org/faq.html#_how_is_server_url_generated) 

### Build and run the Docker container locally

1. Run the `maven run` command to have the .jar file generated.
1. Have the Azure subscription set up
1. Open a Powershell
1. Install the Azure CLI locally
1. Authenticate yourself to Azure Container Registry (acr) with the following command:
```az acr login --name delfi```
1. Execute the following command to build the container image:
```docker build -t unit .```
1. Execute the following command to build the container image:
```docker run -t --rm -p 8080:8080 unit```
1. Use Postman or curl to try out the endpoints

## Build

### Azure

VSTS build definition is located at build definitions/dps/unit-service, which
requires the following environment variables:

| Variable | Contents |
|----------|----------|
| UNIT_CATALOG_FILENAME | Required, file name for the unit catalog to use. Default to /mnt/unit_catalogs/unit_catalog_v2.json |

## Release/deployment

VSTS release/deployment requires the following environment variables:

| Variable | Contents |
|----------|----------|
| ENTITLEMENT_URL | Required |

### Google Cloud

The Google Cloud implementation of the Unit service is maintained in [gc-osdu-services](https://community.opengroup.org/osdu/platform/deployment-and-operations/base-containers-gcp/gc-osdu-services/-/tree/main/system/reference_and_helper_services/unit).
