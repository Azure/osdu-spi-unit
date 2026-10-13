#!/bin/bash -eu
#
#  Copyright 2020-2024 Google LLC
#  Copyright 2020-2024 EPAM Systems, Inc
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

#!/usr/bin/env bash

# Setup Python virtual environment
python3 -m venv venv
source venv/bin/activate

# Install Python dependencies
pip install -q --upgrade pip
pip install -q -r requirements.txt
pip install -q -r v3/requirements.txt

echo ""
echo "***RUNNING UNIT API v3 TESTS WITH ALLURE REPORTING***"
echo ""

# Run tests with pytest and Allure reporting
pytest test_unit_service_v3.py \
    --alluredir=cimpl/allure-results \
    --clean-alluredir \
    -v

TEST_STATUS=$?

echo ""
echo "***FINISHED UNIT API v3 TESTS***"
echo ""
deactivate

exit $TEST_STATUS
