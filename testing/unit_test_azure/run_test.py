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


import os
import sys
sys.path.append("..")

API_VER = os.environ.get('API_VER', 'v2').lower()

if API_VER == 'v2':
    from unit_test_core.test_unit_service_v2 import *
else:
    from run_test_v3 import *


if __name__ == '__main__':
    unittest.main()
