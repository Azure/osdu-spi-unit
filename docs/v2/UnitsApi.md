# swagger_client.UnitsApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_preferred_units_by_measurement**](UnitsApi.md#get_preferred_units_by_measurement) | **POST** /unit/measurement/preferred | Get preferred Units for a Measurement
[**get_preferred_units_by_measurement_ancestry**](UnitsApi.md#get_preferred_units_by_measurement_ancestry) | **GET** /unit/measurement/preferred/{ancestry} | Get preferred Units for a Measurement by ancestry
[**get_unit**](UnitsApi.md#get_unit) | **POST** /unit | Get a specific Unit
[**get_unit_by_namespace_and_symbol**](UnitsApi.md#get_unit_by_namespace_and_symbol) | **GET** /unit/symbol/{namespaces}/{symbol} | Get a unique Unit by Namespace,Symbol
[**get_unit_map_items**](UnitsApi.md#get_unit_map_items) | **GET** /unit/maps | Get Unit maps between namespaces
[**get_units**](UnitsApi.md#get_units) | **GET** /unit | Get all Units
[**get_units_by_measurement_ancestry**](UnitsApi.md#get_units_by_measurement_ancestry) | **GET** /unit/measurement/{ancestry} | Get all Units for a Measurement by ancestry
[**get_units_by_symbol**](UnitsApi.md#get_units_by_symbol) | **GET** /unit/symbol/{symbol} | Get Units by symbol
[**get_units_for_measurement**](UnitsApi.md#get_units_for_measurement) | **POST** /unit/measurement | Get all Units for a Measurement
[**search_units**](UnitsApi.md#search_units) | **POST** /unit/search | Search Units by keyword(s)


# **get_preferred_units_by_measurement**
> QueryResult get_preferred_units_by_measurement(body=body, data_partition_id=data_partition_id)

Get preferred Units for a Measurement

Get the preferred units given a measurement persistable reference string or measurement essence JSON structure.

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
api_instance = swagger_client.UnitsApi()
body = swagger_client.MeasurementRequest() # MeasurementRequest | The request to get a specific measurement given a persistable reference string or measurement essence structure. (optional)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get preferred Units for a Measurement
    api_response = api_instance.get_preferred_units_by_measurement(body=body, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_preferred_units_by_measurement: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**MeasurementRequest**](MeasurementRequest.md)| The request to get a specific measurement given a persistable reference string or measurement essence structure. | [optional] 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_preferred_units_by_measurement_ancestry**
> QueryResult get_preferred_units_by_measurement_ancestry(ancestry, data_partition_id=data_partition_id)

Get preferred Units for a Measurement by ancestry

Get the preferred units given dot separated ancestry, e.g. Time_Per_Length.Acoustic_Slowness.

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
api_instance = swagger_client.UnitsApi()
ancestry = 'ancestry_example' # str | The measurement's ancestry, example: 'Time_Per_Length.Acoustic_Slowness'
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get preferred Units for a Measurement by ancestry
    api_response = api_instance.get_preferred_units_by_measurement_ancestry(ancestry, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_preferred_units_by_measurement_ancestry: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ancestry** | **str**| The measurement&#39;s ancestry, example: &#39;Time_Per_Length.Acoustic_Slowness&#39; | 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit**
> Unit get_unit(body=body, data_partition_id=data_partition_id)

Get a specific Unit

Get a specific unit instance given either a persistable reference string or unit essence JSON structure.

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

# create an instance of the API class
api_instance = swagger_client.UnitsApi()
body = swagger_client.UnitRequest() # UnitRequest | The unit's essence either as persistable reference string or unit essence JSON structure (optional)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get a specific Unit
    api_response = api_instance.get_unit(body=body, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_unit: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UnitRequest**](UnitRequest.md)| The unit&#39;s essence either as persistable reference string or unit essence JSON structure | [optional] 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_by_namespace_and_symbol**
> Unit get_unit_by_namespace_and_symbol(namespaces, symbol, data_partition_id=data_partition_id)

Get a unique Unit by Namespace,Symbol

Get single, unique unit given a namespace or namespace list and a symbol. Example: namespace='LIS,RP66,ECL', symbol='F' returns the LIS Foot.

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
api_instance = swagger_client.UnitsApi()
namespaces = 'namespaces_example' # str | The namespace or namespace list setting the priority of sources to resolve a symbol ambiguity. Example: 'Energistics_UoM' or 'LIS,RP66,ECL'. 
symbol = 'symbol_example' # str | The unit symbol to look up. Example: 'F'. 'F' is ambiguous and requires a namespace to disambiguate.
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get a unique Unit by Namespace,Symbol
    api_response = api_instance.get_unit_by_namespace_and_symbol(namespaces, symbol, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_unit_by_namespace_and_symbol: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **namespaces** | **str**| The namespace or namespace list setting the priority of sources to resolve a symbol ambiguity. Example: &#39;Energistics_UoM&#39; or &#39;LIS,RP66,ECL&#39;.  | 
 **symbol** | **str**| The unit symbol to look up. Example: &#39;F&#39;. &#39;F&#39; is ambiguous and requires a namespace to disambiguate. | 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_map_items**
> QueryResult get_unit_map_items(offset=offset, limit=limit)

Get Unit maps between namespaces

Get the UnitMapItems defined between namespaces in this catalog.

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

# create an instance of the API class
api_instance = swagger_client.UnitsApi()
offset = 0 # int | The offset into the result array. Default 0. (optional) (default to 0)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)

try: 
    # Get Unit maps between namespaces
    api_response = api_instance.get_unit_map_items(offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_unit_map_items: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset into the result array. Default 0. | [optional] [default to 0]
 **limit** | **int**| The size limit for the number of items in the response. Default 100; -1 for all items. | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_units**
> QueryResult get_units(offset=offset, limit=limit, data_partition_id=data_partition_id)

Get all Units

Get all units defined in this catalog.

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
api_instance = swagger_client.UnitsApi()
offset = 0 # int | The offset into the result array. Default 0. (optional) (default to 0)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all Units
    api_response = api_instance.get_units(offset=offset, limit=limit, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_units: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset into the result array. Default 0. | [optional] [default to 0]
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

# **get_units_by_measurement_ancestry**
> QueryResult get_units_by_measurement_ancestry(ancestry, data_partition_id=data_partition_id)

Get all Units for a Measurement by ancestry

Get all units given dot separated ancestry, e.g. Time_Per_Length.Acoustic_Slowness.

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

# create an instance of the API class
api_instance = swagger_client.UnitsApi()
ancestry = 'ancestry_example' # str | The measurement's ancestry, example: 'Time_Per_Length.Acoustic_Slowness'
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all Units for a Measurement by ancestry
    api_response = api_instance.get_units_by_measurement_ancestry(ancestry, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_units_by_measurement_ancestry: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ancestry** | **str**| The measurement&#39;s ancestry, example: &#39;Time_Per_Length.Acoustic_Slowness&#39; | 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_units_by_symbol**
> QueryResult get_units_by_symbol(symbol, data_partition_id=data_partition_id)

Get Units by symbol

Get all units given a specific symbol. Example: 'F': 'F' is defined in multiple namespaces belonging to different measurements.

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
api_instance = swagger_client.UnitsApi()
symbol = 'symbol_example' # str | The unit symbol to look up. Example: 'F'. 'F' is defined in multiple namespaces.
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get Units by symbol
    api_response = api_instance.get_units_by_symbol(symbol, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_units_by_symbol: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **symbol** | **str**| The unit symbol to look up. Example: &#39;F&#39;. &#39;F&#39; is defined in multiple namespaces. | 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_units_for_measurement**
> QueryResult get_units_for_measurement(body=body, data_partition_id=data_partition_id)

Get all Units for a Measurement

Get all units given either a measurement's persistable reference string or a measurement essence JSON structure.

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

# create an instance of the API class
api_instance = swagger_client.UnitsApi()
body = swagger_client.MeasurementRequest() # MeasurementRequest | The request to get a specific measurement given a persistable reference string or measurement essence structure. (optional)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all Units for a Measurement
    api_response = api_instance.get_units_for_measurement(body=body, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->get_units_for_measurement: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**MeasurementRequest**](MeasurementRequest.md)| The request to get a specific measurement given a persistable reference string or measurement essence structure. | [optional] 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **search_units**
> QueryResult search_units(body=body, offset=offset, limit=limit, data_partition_id=data_partition_id)

Search Units by keyword(s)

Search units by keywords. Valid keywords are: 'name', 'namespace', 'source', 'symbol', 'type' (unit parameterization type Abcd or ScaleOffset), 'state'.

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
api_instance = swagger_client.UnitsApi()
body = swagger_client.SearchUnitRequest() # SearchUnitRequest |  (optional)
offset = 56 # int | The offset into the result array. Default 0. (optional)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Search Units by keyword(s)
    api_response = api_instance.search_units(body=body, offset=offset, limit=limit, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UnitsApi->search_units: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SearchUnitRequest**](SearchUnitRequest.md)|  | [optional] 
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

