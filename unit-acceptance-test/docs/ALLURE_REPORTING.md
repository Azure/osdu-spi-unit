# Allure Reporting for Unit Service Tests

This guide explains how to use Allure reporting with the Unit Service acceptance tests.

## Overview

Allure Framework provides clear graphical reports for test execution. All tests (pytest and unittest) are run through a single pytest command with Allure reporting enabled.

## Prerequisites

### 1. Python Environment

```bash
cd services/unit/unit-acceptance-test
python3 -m venv venv
source venv/bin/activate
```

### 2. Install Dependencies

```bash
pip install -r requirements.txt
pip install -r v3/requirements.txt
```

These include:
- `pytest` - Test framework
- `schemathesis` - API contract testing
- `allure-pytest>=2.13.0` - Pytest plugin for Allure
- `allure-python-commons>=2.13.0` - Common utilities for Allure

### 3. Environment Variables

Create a `.env` file or export these variables:

```bash
export VIRTUAL_SERVICE_HOST_NAME="your-host.com"
export MY_TENANT="your-tenant"
export PRIVILEGED_USER_TOKEN="your-token"
```

Or use a `.env` file and load it:
```bash
set -a; source .env; set +a
```

### 4. Allure CLI (Optional - for HTML reports)

**macOS:**
```bash
brew install allure
```

**Linux:**
```bash
curl -o allure-2.31.0.tgz -L https://github.com/allure-framework/allure2/releases/download/2.31.0/allure-2.31.0.tgz
tar -zxvf allure-2.31.0.tgz
sudo mv allure-2.31.0 /opt/
sudo ln -s /opt/allure-2.31.0/bin/allure /usr/bin/allure
```

**Alternative: No CLI needed** - The dependencies include `allure-combine` and `pytest-html` which generate reports without Allure CLI (see below).

## Running Tests

### Basic Test Execution

```bash
# Run all tests with Allure reporting
pytest test_unit_service_v3.py \
    --alluredir=allure-results \
    --clean-alluredir \
    -v
```

### Run Specific Test File

```bash
# Only integration tests
pytest test_unit_service_v3.py --alluredir=allure-results -v
```

### Generate and View HTML Report

**With Allure CLI:**
```bash
# Generate report
allure generate allure-results -o allure-report --clean

# View in browser
allure open allure-report
```

**Without Allure CLI - Using Podman/Docker:**

```bash
# Generate report (creates allure-report directory)
mkdir -p allure-report
podman run --rm \
  -v $(pwd)/allure-results:/allure-results:Z \
  -v $(pwd)/allure-report:/allure-report:Z \
  frankescobar/allure-docker-service \
  allure generate /allure-results -o /allure-report --clean

# Fix permissions (if needed)
sudo chown -R $USER:$USER allure-report

# Serve report on http://localhost:80
podman run --rm -p 8080:80 \
  -v $(pwd)/allure-report:/usr/share/nginx/html:Z,ro \
  nginx:alpine

# Or open directly in browser
xdg-open allure-report/index.html  # Linux
open allure-report/index.html      # macOS
```

### Alternative: Generate Report Without Allure CLI

#### Option 1: Single-file Allure Report (allure-combine)

```bash
# After running tests, combine results into a single HTML file
allure-combine ./allure-results

# Open the generated complete.html
python3 -m http.server 8000 &
open http://localhost:8000/complete.html
```

The report is self-contained in `complete.html` - no server needed, can be opened directly or shared.

#### Option 2: Simple HTML Report (pytest-html)

```bash
# Run tests with HTML report (no Allure, but simpler)
pytest test_unit_service_v3.py \
    --html=report.html \
    --self-contained-html \
    -v

# Open the report
open report.html  # or xdg-open on Linux
```

This generates a standalone HTML file immediately - no additional tools needed.

### One-Liner (Complete Flow)

**With Allure CLI:**
```bash
pytest test_unit_service_v3.py --alluredir=allure-results --clean-alluredir -v && \
allure generate allure-results -o allure-report --clean && \
allure open allure-report
```

**Without Allure CLI (using allure-combine):**
```bash
pytest test_unit_service_v3.py --alluredir=allure-results --clean-alluredir -v && \
allure-combine ./allure-results && \
open complete.html
```

**Simple HTML (no Allure):**
```bash
pytest test_unit_service_v3.py --html=report.html --self-contained-html -v && \
open report.html
```

## Report Structure

- **allure-results/** - JSON test execution data
- **allure-report/** - Generated HTML report with:
  - Overview dashboard with statistics
  - Test suites organized by feature
  - Failed tests grouped by categories
  - Timeline and execution graphs
  - Request/response attachments

## Allure Features in Tests

### Integration Tests (test_unit_service_v3.py)

Unittest-based tests with Allure decorators:

```python
@allure.feature('Unit Conversions')
@allure.epic('Unit Service v3 Integration Tests')
class TestConversions(unittest.TestCase):
    @allure.title("Test ABCD conversion")
    def test_post_conversion_abcd_using_post(self):
        # Integration test logic
```

## Advanced Usage

### Run with Custom Markers

```bash
# Run only critical tests
pytest -m critical --alluredir=allure-results -v

# Run API tests only
pytest -m api --alluredir=allure-results -v
```

### Parallel Execution

```bash
pip install pytest-xdist
pytest -n auto test_unit_service_v3.py --alluredir=allure-results -v
```

### With Coverage

```bash
pip install pytest-cov
pytest --cov=. --cov-report=html --alluredir=allure-results -v
```

## CI/CD Integration

### GitLab CI

**With Allure CLI:**
```yaml
test:
  stage: test
  script:
    - cd services/unit/unit-acceptance-test
    - python3 -m venv venv
    - source venv/bin/activate
    - pip install -r requirements.txt -r v3/requirements.txt
    - pytest test_unit_service_v3.py --alluredir=allure-results -v
    - allure generate allure-results -o allure-report --clean
  artifacts:
    when: always
    paths:
      - services/unit/unit-acceptance-test/allure-results
      - services/unit/unit-acceptance-test/allure-report
    expire_in: 1 week
```

**Without Allure CLI (using allure-combine):**
```yaml
test:
  stage: test
  script:
    - cd services/unit/unit-acceptance-test
    - python3 -m venv venv
    - source venv/bin/activate
    - pip install -r requirements.txt -r v3/requirements.txt
    - pytest test_unit_service_v3.py --alluredir=allure-results -v
    - allure-combine ./allure-results
  artifacts:
    when: always
    paths:
      - services/unit/unit-acceptance-test/complete.html
      - services/unit/unit-acceptance-test/allure-results
    expire_in: 1 week
    reports:
      junit: services/unit/unit-acceptance-test/allure-results/junit.xml
```

**Simple HTML Report:**
```yaml
test:
  stage: test
  script:
    - cd services/unit/unit-acceptance-test
    - python3 -m venv venv
    - source venv/bin/activate
    - pip install -r requirements.txt -r v3/requirements.txt
    - pytest test_unit_service_v3.py --html=report.html --self-contained-html -v
  artifacts:
    when: always
    paths:
      - services/unit/unit-acceptance-test/report.html
    expire_in: 1 week
```

### GitHub Actions

```yaml
- name: Setup Python
  uses: actions/setup-python@v4
  with:
    python-version: '3.11'

- name: Install dependencies
  run: |
    cd services/unit/unit-acceptance-test
    pip install -r requirements.txt -r v3/requirements.txt

- name: Run tests
  run: |
    cd services/unit/unit-acceptance-test
    pytest test_unit_service_v3.py --alluredir=allure-results -v

- name: Generate Allure Report
  if: always()
  run: |
    cd services/unit/unit-acceptance-test
    allure generate allure-results -o allure-report --clean

- name: Upload Report
  if: always()
  uses: actions/upload-artifact@v3
  with:
    name: allure-report
    path: services/unit/unit-acceptance-test/allure-report
```

## Configuration

The `pytest.ini` file configures default behavior:

```ini
[pytest]
addopts = 
    --alluredir=allure-results
    --clean-alluredir
    -v
    --tb=short
```

This means you can simply run `pytest` without extra flags.

## Troubleshooting

**Import errors:**
```bash
# Ensure both requirements are installed
pip install -r requirements.txt -r v3/requirements.txt
```

**No module 'schemathesis':**
```bash
# Install from main requirements.txt
pip install schemathesis
```

**AttributeError with schemathesis:**
- Ensure using `schemathesis.openapi.from_url()` (v4.x API)

**Environment variable not found:**
```bash
# Load .env file
set -a; source .env; set +a
# Or export manually
export VIRTUAL_SERVICE_HOST_NAME="your-host"
```

**Allure command not found:**
- Install Allure CLI (see Prerequisites)
- Or use raw JSON results in `allure-results/`

## Resources

- [Allure Documentation](https://docs.qameta.io/allure/)
- [Pytest Documentation](https://docs.pytest.org/)
- [Schemathesis Documentation](https://schemathesis.readthedocs.io/)
