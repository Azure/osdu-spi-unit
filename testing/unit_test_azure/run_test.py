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
