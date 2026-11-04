# Unit Service

The Unit service provides dimension/measurement and unit definitions. Given two unit definitions, the service also offers conversion parameters in two different parameterizations.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- [Maven 3.8.0+](https://maven.apache.org/download.cgi)
- [JDK 25](https://adoptium.net/) (Epic 82 Java 25 baseline)
- [Lombok 1.18 or later](https://projectlombok.org/setup/maven)

### Installation

- Setup Apache Maven
- Setup JDK 25
- Install Eclipse (or other IDE) to run applications
- Set up environment variables for Apache Maven and JDK 25. For example M2_HOME, JAVA_HOME, etc.
- Add a configuration for build project in Eclipse(or other IDE)

### Run Locally

| name                              | value                                         | description                         | sensitive? | source                              |
|-----------------------------------|-----------------------------------------------|-------------------------------------|------------|-------------------------------------|
| `UNIT_CATALOG_FILENAME`           | ex `/mnt/unit_catalogs/unit_catalog_v2.json`  | File location of the unit catalog   | no         | -                                   |
| `LOG_PREFIX`                      | `service`                                     | Logging prefix                      | no         | -                                   |
| `SERVER_SERVLET_CONTEXPATH`       | `/api/unit/`                                  | CRS conversion service context path | no         | -                                   |
| `ENTITLEMENT_URL`                 | ex `https://entitlements.com/entitlements/v1` | Entitlements API endpoint           | no         | output of infrastructure deployment |

Defined in default application property file but possible to override:

| name                              | value                                        | description                         | sensitive? | source        |
|-----------------------------------|----------------------------------------------|-------------------------------------|------------|---------------|
| `MANAGEMENT_ENDPOINTS_WEB_BASE`   | ex `/`                                       | Web base for Actuator               | no         | -             |
| `MANAGEMENT_SERVER_PORT`          | ex `8081`                                    | Port for Actuator                   | no         | -             |

Check that maven is installed:

```bash
$ mvn --version
Apache Maven 3.8.0
Maven home: /usr/share/maven
Java version: 25 vendor: Eclipse Adoptium, runtime: /usr/lib/jvm/jdk-25
...
```

You may need to configure access to the remote maven repository that holds the OSDU dependencies. This file should live within `~/.mvn/community-maven.settings.xml`:

```bash
$ cat ~/.m2/settings.xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>community-maven-via-private-token</id>
            <!-- Treat this auth token like a password. Do not share it with anyone, including Microsoft support. -->
            <!-- The generated token expires on or before 11/14/2019 -->
             <configuration>
              <httpHeaders>
                  <property>
                      <name>Private-Token</name>
                      <value>${env.COMMUNITY_MAVEN_TOKEN}</value>
                  </property>
              </httpHeaders>
             </configuration>
        </server>
    </servers>
</settings>
```

- Navigate to Unit service's root folder and run:

```bash
mvn clean install   
```

- If you wish to build the project without running tests

```bash
mvn clean install -DskipTests
```

After configuring your environment as specified above, you can follow these steps to build and run the application. These steps should be invoked from the *repository root.*

```bash
cd unit-core-plus && mvn spring-boot:run
```

## Testing

### Running E2E Tests

#### Folder structure

testing/  

- unit_test_core/  
  - ...
- unit_test_$PROVIDER_NAME/  
  - jwt_client.py  
  - run_test.py  

This integration test uses a swagger generated Python client to test a
deployed unit-service. The source is located in this repository
```./api_spec/unit_service_openapi.json```.

The python client code is automatically generated. The latest online version (May 2018)
created incorrect impost statements for cyclic class references. Therefore the current
code is generated using [swagger-codegen-cli-2.2.3.jar](https://repo1.maven.org/maven2/io/swagger/swagger-codegen-cli/2.2.3/swagger-codegen-cli-2.2.3.jar).
The command to create the python code is:  
Linux

```bash
cd testing
java -jar ~/swagger-codegen-cli-2.2.3.jar generate -i unit_test_core/api_spec/unit_service_openapi.json -l python -o unit_test_core/v2
```

Windows

```bat
cd testing
java -jar %UserProfile%\swagger-codegen-cli-2.2.3.jar generate -i unit_test_core\api_spec\unit_service_openapi_v2.json -l python -o unit_test_core\v2
```

#### Environment

The following parameters are expected as environment variables:

**Auth provider (catalog_test_gc/jwt_client.py)**

| Variable | Contents |
|----------|----------|
| INTEGRATION_TESTER | go to the Cloud provider environment example google IAM & admin console, navigate to Service accounts to create a key and download the account info file. |

**Tests core (unit_test_core/constants.py)**

| Variable | Contents |
|----------|----------|
| BASE_URL | e.g. /api/unit |
| VIRTUAL_SERVICE_HOST_NAME | e.g. az-osdu1.evd.csp.slb.com |
| MY_TENANT | e.g. opendes |

**Entitlements configuration for integration accounts**

| INTEGRATION_TESTER |
| ---  |
| users<br/>service.entitlements.user<br/>data.test1<br/>data.integration.test<br/>users@{tenant1}@{domain}.com |

#### Building/running

Go to the provider folder:

```bash
cd unit_test_$PROVIDER_NAME/ # e.g. unit_test_baremetal
```

To set up a virtual environment:

```bash
virtualenv venv
```

To activate the venv:

```bash
venv\Scripts\activate (on Windows)
source venv/bin/activate (on Linux)
```

Install runtime dependencies in venv

```bash
python3 -m pip install -r requirements.txt
```

To run:

```bash
python3 run_test.py
```

## Deployment

Please follow cloud providers documentation. 

## Licence

Copyright © Google LLC
Copyright © EPAM Systems
Copyright © Amazon Web Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
