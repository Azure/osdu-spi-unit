# dps-unit-service integration tests

## Folder structure

testing/  

* unit_test_core/  
  * ...
* unit_test_$PROVIDER_NAME/  
  * jwt_client.py  
  * run_test.py  

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

## Environment

The following parameters are expected as environment variables:

## Google Cloud auth provider (unit_test_gc/jwt_client.py)

| Variable | Contents |
|----------|----------|
| INTEGRATION_TESTER | go to the google IAM & admin console, navigate to Service accounts to create a key and download the account info file. |

# Baremetal auth provider (unit_test_baremetal/jwt_client.py)

## Tests core (unit_test_core/constants.py)

| Variable | Contents |
|----------|----------|
| BASE_URL | e.g. /api/unit |
| VIRTUAL_SERVICE_HOST_NAME | e.g. az-osdu1.evd.csp.slb.com |
| MY_TENANT | e.g. opendes |

## Building/running

Go to the provider folder:

```bash
cd unit_test_$PROVIDER_NAME/ # e.g. unit_test_azure
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

**Note:** To simulate a runtime exactly as that of the vsts build agent, you can simply exec into the docker image we use for the build agent, and run the tests from inside it. To know how to do this, please follow [this](https://slb-swt.visualstudio.com/data-at-rest/_git/dps-vsts-build-agent?path=%2FREADME.md&version=GBmaster) documentation.
