#!/usr/bin/env bash
# Copyright © Microsoft Corporation
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


set -e

# Setup Python Environment and install requirements
python3 -m venv env
sed -i 's/$1/${1:-}/' env/bin/activate # Fix deactivation bug '$1 unbound variable'
source env/bin/activate
python3 -m pip install --upgrade pip
python3 -m pip install -r requirements.txt

# Run v3 Tests
echo ""
export API_VER="v3"
echo ***RUNNING UNIT API $API_VER TESTS***
python3 run_test.py
TEST_STATUS_V3=$?
echo ***FINISHED UNIT API $API_VER TESTS***

# Display Results
echo "-------------------------------"
echo "TEST_ERRORS_V3: $TEST_STATUS_V3"
echo "-------------------------------"

# Uninstall Environment if not on ADO Pipelines
if [ -z ${AGENT_POOL+x}  ]; then
  # python3 -m pip freeze > requirements.txt
  python3 -m pip uninstall -r requirements.txt -y
  deactivate
  rm -rf env/
fi

# If Error Exit 1
if [ $TEST_STATUS_V3 -ne 0 ]
then
  exit 1
else
  exit 0
fi
