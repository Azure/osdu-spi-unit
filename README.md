# OSDU Unit Service for Azure

[![Release](https://img.shields.io/github/v/release/Azure/osdu-spi-unit)](https://github.com/Azure/osdu-spi-unit/releases)
[![Validate](https://github.com/Azure/osdu-spi-unit/actions/workflows/validate.yml/badge.svg?branch=main)](https://github.com/Azure/osdu-spi-unit/actions/workflows/validate.yml)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

> [!NOTE]
> Shared service code comes from the [OSDU community upstream](https://community.opengroup.org/osdu/platform/system/reference/unit-service).

Unit serves the OSDU units of measure catalog: it looks up units, measurements, and unit systems, and returns the parameters that convert a value from one unit to another.

## At a glance

| | |
|---|---|
| API base path | `/api/unit/`, with `v3/` current and `v2/` deprecated, for example `/api/unit/v3/` |
| Swagger UI | `/api/unit/swagger` |
| OpenAPI | `/api/unit/api-docs`, per version at `/api/unit/api-docs/v2` and `/api/unit/api-docs/v3` |
| Health | `:8080/api/unit/actuator/health` |
| Depends on | Entitlements |
| Azure resources | None for data; the catalog is a JSON file held in memory |
| Deployed by | [OSDU SPI Stack](https://github.com/Azure/osdu-spi-stack) (`software/stacks/osdu/services-reference/unit.yaml`) |

## Repository layout

[CONTRIBUTING.md](CONTRIBUTING.md) explains where each kind of change belongs.

| Path | Owner | Contents |
|---|---|---|
| `unit-core/` | OSDU upstream | Shared service code |
| `provider/unit-azure/unit-aks/` | This repository | Azure provider |
| `unit-acceptance-test/` | OSDU upstream | End-to-end Python suite run against a deployed environment |
| `data/` | OSDU upstream | The unit catalog, `unit_catalog_v2.json` |
| `testing/unit_test_core/` | OSDU upstream | Shared Python integration tests |
| `testing/unit_test_azure/` | This repository | Legacy Azure integration tests |

## Build

Requires Java 17 and Maven 3.6.3+. OSDU dependencies resolve from the public community registry through the settings file in `.mvn`:

```bash
mvn --settings .mvn/community-maven.settings.xml -P core,azure clean install
```

The runnable jar lands at `provider/unit-azure/unit-aks/target/unit-aks-*-spring-boot.jar`.

## Configuration

SPI Stack sets the service's environment from two places: the shared `osdu-config` ConfigMap and the service's own entry in [`services-reference/unit.yaml`](https://github.com/Azure/osdu-spi-stack/blob/main/software/stacks/osdu/services-reference/unit.yaml). Those files are the contract; the tables below list what Unit actually reads from them.

**Shared, from `osdu-config`:**

| Variable | Purpose |
|---|---|
| `AAD_CLIENT_ID` | Application ID that caller tokens are issued for |
| `KEYVAULT_URI` | Central Key Vault |
| `APPINSIGHTS_KEY` | Telemetry |
| `SERVER_PORT` | HTTP port (`8080`) |

**Specific to Unit**, from `services-reference/unit.yaml`:

| Variable and value on SPI Stack | Purpose |
|---|---|
| `SPRING_APPLICATION_NAME`<br>`unit` | Application name; the property has no default, so it must stay set |
| `SERVER_SERVLET_CONTEXTPATH`<br>`/api/unit/` | API base path |
| `ENTITLEMENT_URL`<br>`http://entitlements/api/entitlements/v2` | Caller authorization |
| `AZURE_PAAS_WORKLOADIDENTITY_ISENABLED`<br>`true` | Authenticate to Azure with workload identity |

The stack also sets `ACCEPT_HTTP`, `AZURE_ISTIOAUTH_ENABLED`, and `PARTITION_SERVICE_ENDPOINT`; Unit reads none of them. It reads `UNIT_CATALOG_FILENAME`, which the stack leaves unset, so the default path `/mnt/unit_catalogs/unit_catalog_v2.json` applies; see [Service notes](#service-notes).

The service authenticates to Azure with workload identity, which injects `AZURE_CLIENT_ID` and a federated token; there are no client secrets. Every API request is authorized by asking Entitlements for the caller's groups with the caller's token and `data-partition-id`. Unit keeps no per-partition data.

## Test

| Suite | Where | Runs in CI | Run it yourself |
|---|---|---|---|
| Unit | `unit-core` | Pull requests (Java Build) | `mvn ... install` from [Build](#build) |
| Acceptance | [`unit-acceptance-test`](unit-acceptance-test/docs/README.md) | No | See below |
| Integration | `testing/unit_test_core`, `testing/unit_test_azure` | No | See below |

CI runs the unit tests on pull requests from this repository that change code. Documentation-only changes skip the build.

**Acceptance** is a Python suite that calls the deployed service through the gateway with a bearer token. The Deploy and Test lane and `spi test` run Maven suites, so neither runs it. Against an environment you are connected to:

```bash
cd unit-acceptance-test
python3 -m venv venv && . venv/bin/activate
pip install -r requirements.txt -r v3/requirements.txt
VIRTUAL_SERVICE_HOST_NAME=<gateway> MY_TENANT=<partition> PRIVILEGED_USER_TOKEN="$(spi token)" \
  pytest test_unit_service_v3.py
```

**Integration** is the older Azure suite. `testing/unit_test_azure` runs the shared tests in `testing/unit_test_core` and mints its own token from a service principal secret (`INTEGRATION_TESTER`, `AZURE_TESTER_SERVICEPRINCIPAL_SECRET`). SPI Stack issues no client secrets, so the suite does not run there. Acceptance covers the same v3 API.

To call the API by hand, `spi token` mints a bearer token:

```bash
curl -H "Authorization: Bearer $(spi token)" -H "data-partition-id: <partition>" \
  https://<gateway>/api/unit/v3/catalog/lastmodified
```

## Deploy

For a pull request from this repository that changes code, CI publishes the service image, `osdu-spi-unit`, to GHCR. The Deploy and Test lane borrows an SPI Stack environment, runs the new image there, proves it with the test suite image `osdu-spi-unit-acceptance`, and restores the environment's own image. That lane runs only when `.spi/service.yaml` declares the suites, and the suite image builds Maven suites only, so it does not run for this repository; the Validation Summary on the pull request shows whether it ran. This repository does not own infrastructure; SPI Stack does.

To try a build by hand on an environment you are connected to, pin it by digest and release the pin when done:

```bash
spi service pin unit --image ghcr.io/azure/osdu-spi-unit@sha256:<digest>
spi service reset unit
```

## Service notes

**The catalog comes from upstream at pod start.** The service reads its whole catalog from `/mnt/unit_catalogs/unit_catalog_v2.json` at startup and does not start without it; the service image does not carry the file. On SPI Stack an init container downloads `data/unit_catalog_v2.json` from the upstream project's `master` branch each time the pod starts. The catalog served therefore follows upstream rather than this repository's `data/` copy, a restart can pick up a newer catalog, and the pod needs to reach `community.opengroup.org` to start.

## License

Copyright © Microsoft Corporation

Licensed under the [Apache License 2.0](LICENSE).
