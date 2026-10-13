# swagger_client.UnitSystemsApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_unit_by_unit_system_and_measurement**](UnitSystemsApi.md#get_unit_by_unit_system_and_measurement) | **POST** /unit/unitsystem/{unitSystemName} | Get a unique Unit for the given Measurement in the named UnitSystem
[**get_unit_by_unit_system_and_measurement_ancestry**](UnitSystemsApi.md#get_unit_by_unit_system_and_measurement_ancestry) | **GET** /unit/unitsystem/{unitSystemName}/{ancestry} | Get a unique Unit for the given Measurement ancestry in the named UnitSystem
[**get_unit_system_by_essence**](UnitSystemsApi.md#get_unit_system_by_essence) | **POST** /unitsystem | Get all Units assigned to the UnitSystem
[**get_unit_system_by_name**](UnitSystemsApi.md#get_unit_system_by_name) | **GET** /unitsystem/{name} | Get all Units assigned to the UnitSystem name
[**get_unit_system_list**](UnitSystemsApi.md#get_unit_system_list) | **GET** /unitsystem/list | Get all UnitSystem info


# **get_unit_by_unit_system_and_measurement**
> Unit get_unit_by_unit_system_and_measurement(unit_system_name, body=body, data_partition_id=data_partition_id)

Get a unique Unit for the given Measurement in the named UnitSystem

Get a unique unit given a unit system name and a measurement persistable reference string or measurement essence Json structure. Example: Unit system: English; measurement essence: { \"ancestry\": \"Length\", \"type\": \"UM\" }

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.UnitSystemsApi()
unit_system_name = 'unit_system_name_example' # str | The name of the unit system. Example: 'English'.
body = swagger_client.MeasurementRequest() # MeasurementRequest | The request to get a specific measurement given a persistable reference string or measurement essence structure. (optional)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get a unique Unit for the given Measurement in the named UnitSystem
    api_response = api_instance.get_unit_by_unit_system_and_measurement(unit_system_name, body=body, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitSystemsApi->get_unit_by_unit_system_and_measurement: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **unit_system_name** | **str**| The name of the unit system. Example: &#39;English&#39;. | 
 **body** | [**MeasurementRequest**](MeasurementRequest.md)| The request to get a specific measurement given a persistable reference string or measurement essence structure. | [optional] 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_by_unit_system_and_measurement_ancestry**
> Unit get_unit_by_unit_system_and_measurement_ancestry(unit_system_name, ancestry, data_partition_id=data_partition_id)

Get a unique Unit for the given Measurement ancestry in the named UnitSystem

Get a unique unit given a unit system name and dot separated measurement ancestry, e.g. Time_Per_Length.Acoustic_Slowness.

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.UnitSystemsApi()
unit_system_name = 'unit_system_name_example' # str | The name of the unit system. Example: 'English'.
ancestry = 'ancestry_example' # str | The measurement's ancestry, example: 'Time_Per_Length.Acoustic_Slowness'.
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get a unique Unit for the given Measurement ancestry in the named UnitSystem
    api_response = api_instance.get_unit_by_unit_system_and_measurement_ancestry(unit_system_name, ancestry, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitSystemsApi->get_unit_by_unit_system_and_measurement_ancestry: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **unit_system_name** | **str**| The name of the unit system. Example: &#39;English&#39;. | 
 **ancestry** | **str**| The measurement&#39;s ancestry, example: &#39;Time_Per_Length.Acoustic_Slowness&#39;. | 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_system_by_essence**
> UnitSystem get_unit_system_by_essence(body=body, offset=offset, limit=limit, data_partition_id=data_partition_id)

Get all Units assigned to the UnitSystem

Get all unit assignments for the given unit system essence.

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.UnitSystemsApi()
body = swagger_client.UnitSystemRequest() # UnitSystemRequest | The essence of the unit system. Example: {\"ancestry\":\"English\"} (optional)
offset = 0 # int | The offset into the result array. Default 0. (optional) (default to 0)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all Units assigned to the UnitSystem
    api_response = api_instance.get_unit_system_by_essence(body=body, offset=offset, limit=limit, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitSystemsApi->get_unit_system_by_essence: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UnitSystemRequest**](UnitSystemRequest.md)| The essence of the unit system. Example: {\&quot;ancestry\&quot;:\&quot;English\&quot;} | [optional] 
 **offset** | **int**| The offset into the result array. Default 0. | [optional] [default to 0]
 **limit** | **int**| The size limit for the number of items in the response. Default 100; -1 for all items. | [optional] [default to 100]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**UnitSystem**](UnitSystem.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_system_by_name**
> UnitSystem get_unit_system_by_name(name, offset=offset, limit=limit)

Get all Units assigned to the UnitSystem name

Get all unit assignments for the given unit system.

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.UnitSystemsApi()
name = 'name_example' # str | The name of the unit system. Example: 'English'.
offset = 0 # int | The offset into the result array. Default 0. (optional) (default to 0)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)

try: 
    # Get all Units assigned to the UnitSystem name
    api_response = api_instance.get_unit_system_by_name(name, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitSystemsApi->get_unit_system_by_name: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **str**| The name of the unit system. Example: &#39;English&#39;. | 
 **offset** | **int**| The offset into the result array. Default 0. | [optional] [default to 0]
 **limit** | **int**| The size limit for the number of items in the response. Default 100; -1 for all items. | [optional] [default to 100]

### Return type

[**UnitSystem**](UnitSystem.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_system_list**
> UnitSystemInfoList get_unit_system_list(offset=offset, limit=limit, data_partition_id=data_partition_id)

Get all UnitSystem info

Get all known unit systems as info (name, description, ancestry, persistableReference) as declared in this catalog.

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.UnitSystemsApi()
offset = 56 # int | The offset into the result array. Default 0. (optional)
limit = 100 # int | The size limit of the response. Default 100; -1 for all items. (optional) (default to 100)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all UnitSystem info
    api_response = api_instance.get_unit_system_list(offset=offset, limit=limit, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitSystemsApi->get_unit_system_list: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset into the result array. Default 0. | [optional] 
 **limit** | **int**| The size limit of the response. Default 100; -1 for all items. | [optional] [default to 100]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**UnitSystemInfoList**](UnitSystemInfoList.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

