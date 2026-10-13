# swagger_client.HealthcheckApi

All URIs are relative to *https://localhost:8080/api/unit*

Method | HTTP request | Description
------------- | ------------- | -------------
[**liveness_check_using_get**](HealthcheckApi.md#liveness_check_using_get) | **GET** /_ah/liveness_check | livenessCheck
[**readiness_check_using_get**](HealthcheckApi.md#readiness_check_using_get) | **GET** /_ah/readiness_check | readinessCheck


# **liveness_check_using_get**
> ResponseEntity liveness_check_using_get(data_partition_id)

livenessCheck

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer Authorization
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'

# create an instance of the API class
api_instance = swagger_client.HealthcheckApi()
data_partition_id = 'opendes' # str | tenant (default to opendes)

try: 
    # livenessCheck
    api_response = api_instance.liveness_check_using_get(data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling HealthcheckApi->liveness_check_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **readiness_check_using_get**
> ResponseEntity readiness_check_using_get(data_partition_id)

readinessCheck

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer Authorization
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'

# create an instance of the API class
api_instance = swagger_client.HealthcheckApi()
data_partition_id = 'opendes' # str | tenant (default to opendes)

try: 
    # readinessCheck
    api_response = api_instance.readiness_check_using_get(data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling HealthcheckApi->readiness_check_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

