### Running E2E Tests

You will need to have the following environment variables defined.

| name                                | value                                              |                                                  | sensitive? | source        |
|-------------------------------------|----------------------------------------------------|--------------------------------------------------|------------|---------------|
| `VIRTUAL_SERVICE_HOST_NAME`         | eg. `osdu.core-dev.gcp.gnrg-osdu.projects.epam.com`| Host name of catalog service under test          | no         | -             |
| `MY_TENANT`                         | eg. `osdu`                                         | OSDU tenant used for testing                     | no         | -             |

Authentication can be provided as OIDC config:

| name                                            | value                                   | description                   | sensitive? | source |
|-------------------------------------------------|-----------------------------------------|-------------------------------|------------|--------|
| `PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_ID`     | `********`                              | PRIVILEGED_USER Client Id     | yes        | -      |
| `PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_SECRET` | `********`                              | PRIVILEGED_USER Client secret | yes        | -      |
| `TEST_OPENID_PROVIDER_URL`                      | `https://keycloak.com/auth/realms/osdu` | OpenID provider url           | yes        | -      |

Or tokens can be used directly from env variables:

| name                    | value      | description           | sensitive? | source |
|-------------------------|------------|-----------------------|------------|--------|
| `PRIVILEGED_USER_TOKEN` | `********` | PRIVILEGED_USER Token | yes        | -      |


#### Setup and Run Tests

```bash
cd unit-acceptance-test

# 1. Create virtual environment
python3 -m venv venv
source venv/bin/activate

# 2. Install dependencies
pip install -r requirements.txt
pip install -r v3/requirements.txt

# 3. Set environment variables (or use .env file)
export VIRTUAL_SERVICE_HOST_NAME="your-host.com"
export MY_TENANT="your-tenant"
export PRIVILEGED_USER_TOKEN="your-token"

# Or load from .env file
set -a; source .env; set +a

# 4. Run tests with Allure reporting
pytest test_unit_service_v3.py \
    --alluredir=allure-results \
    --clean-alluredir \
    -v

# 5. Generate and view HTML report (requires Allure CLI)
allure generate allure-results -o allure-report --clean
allure open allure-report
```

For detailed Allure reporting setup and usage, see [ALLURE_REPORTING.md](ALLURE_REPORTING.md)

