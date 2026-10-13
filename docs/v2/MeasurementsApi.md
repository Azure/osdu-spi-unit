# swagger_client.MeasurementsApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_measurement**](MeasurementsApi.md#get_measurement) | **POST** /measurement | Get a specific Measurement
[**get_measurement_by_ancestry**](MeasurementsApi.md#get_measurement_by_ancestry) | **GET** /measurement/{ancestry} | Get a specific Measurement by ancestry
[**get_measurement_map_items**](MeasurementsApi.md#get_measurement_map_items) | **GET** /measurement/maps | Get the Measurement maps between namespaces
[**get_measurements**](MeasurementsApi.md#get_measurements) | **GET** /measurement | Get all Measurements
[**search_measurements**](MeasurementsApi.md#search_measurements) | **POST** /measurement/search | Search Measurements by keyword(s)


# **get_measurement**
> Measurement get_measurement(body=body, data_partition_id=data_partition_id)

Get a specific Measurement

Get a specific measurement given a persistable reference string or measurement essence structure.

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
api_instance = swagger_client.MeasurementsApi()
body = swagger_client.MeasurementRequest() # MeasurementRequest | The request to get a specific measurement given a persistable reference string or measurement essence structure. (optional)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get a specific Measurement
    api_response = api_instance.get_measurement(body=body, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling MeasurementsApi->get_measurement: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**MeasurementRequest**](MeasurementRequest.md)| The request to get a specific measurement given a persistable reference string or measurement essence structure. | [optional] 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Measurement**](Measurement.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_measurement_by_ancestry**
> Measurement get_measurement_by_ancestry(ancestry, data_partition_id=data_partition_id)

Get a specific Measurement by ancestry

Get a specific measurement given dot separated measurement ancestry

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
api_instance = swagger_client.MeasurementsApi()
ancestry = 'ancestry_example' # str | The measurement's ancestry, example: 'Time_Per_Length.Acoustic_Slowness'
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get a specific Measurement by ancestry
    api_response = api_instance.get_measurement_by_ancestry(ancestry, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling MeasurementsApi->get_measurement_by_ancestry: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ancestry** | **str**| The measurement&#39;s ancestry, example: &#39;Time_Per_Length.Acoustic_Slowness&#39; | 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Measurement**](Measurement.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_measurement_map_items**
> QueryResult get_measurement_map_items(offset=offset, limit=limit, data_partition_id=data_partition_id)

Get the Measurement maps between namespaces

Get the measurement maps between two namespaces as defined in this catalog.

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
api_instance = swagger_client.MeasurementsApi()
offset = 56 # int | The offset into the result array. Default 0. (optional)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get the Measurement maps between namespaces
    api_response = api_instance.get_measurement_map_items(offset=offset, limit=limit, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling MeasurementsApi->get_measurement_map_items: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset into the result array. Default 0. | [optional] 
 **limit** | **int**| The size limit for the number of items in the response. Default 100; -1 for all items. | [optional] [default to 100]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_measurements**
> QueryResult get_measurements(offset=offset, limit=limit, data_partition_id=data_partition_id)

Get all Measurements

Get all measurements defined in this catalog.

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
api_instance = swagger_client.MeasurementsApi()
offset = 56 # int | The offset into the result array. Default 0. (optional)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all Measurements
    api_response = api_instance.get_measurements(offset=offset, limit=limit, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling MeasurementsApi->get_measurements: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset into the result array. Default 0. | [optional] 
 **limit** | **int**| The size limit for the number of items in the response. Default 100; -1 for all items. | [optional] [default to 100]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **search_measurements**
> QueryResult search_measurements(body=body, offset=offset, limit=limit)

Search Measurements by keyword(s)

Search measurements by keywords. Valid keywords are: 'name', 'namespace', 'source', 'ancestry', 'code', 'dimensionCode', 'unitQuantityCode', 'dimensionAnalysis', 'state', 'baseMeasurement'.

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
api_instance = swagger_client.MeasurementsApi()
body = swagger_client.SearchMeasurementRequest() # SearchMeasurementRequest |  (optional)
offset = 56 # int | The offset into the result array. Default 0. (optional)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)

try: 
    # Search Measurements by keyword(s)
    api_response = api_instance.search_measurements(body=body, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling MeasurementsApi->search_measurements: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SearchMeasurementRequest**](SearchMeasurementRequest.md)|  | [optional] 
 **offset** | **int**| The offset into the result array. Default 0. | [optional] 
 **limit** | **int**| The size limit for the number of items in the response. Default 100; -1 for all items. | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

