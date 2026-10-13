# swagger_client.Unitapiv3Api

All URIs are relative to *https://localhost:8080/api/unit*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_catalog_using_get**](Unitapiv3Api.md#get_catalog_using_get) | **GET** /v3/catalog | getCatalog
[**get_conversion_abcd_by_symbols_using_get**](Unitapiv3Api.md#get_conversion_abcd_by_symbols_using_get) | **GET** /v3/conversion/abcd | getConversionABCDBySymbols
[**get_conversion_scale_offset_by_symbols_using_get**](Unitapiv3Api.md#get_conversion_scale_offset_by_symbols_using_get) | **GET** /v3/conversion/scale | getConversionScaleOffsetBySymbols
[**get_last_modified_using_get**](Unitapiv3Api.md#get_last_modified_using_get) | **GET** /v3/catalog/lastmodified | getLastModified
[**get_map_states_using_get**](Unitapiv3Api.md#get_map_states_using_get) | **GET** /v3/catalog/mapstates | getMapStates
[**get_measurement_maps_using_get**](Unitapiv3Api.md#get_measurement_maps_using_get) | **GET** /v3/measurement/maps | getMeasurementMaps
[**get_measurement_using_get**](Unitapiv3Api.md#get_measurement_using_get) | **GET** /v3/measurement | getMeasurement
[**get_measurements_using_get**](Unitapiv3Api.md#get_measurements_using_get) | **GET** /v3/measurement/list | getMeasurements
[**get_preferred_units_by_measurement_using_get**](Unitapiv3Api.md#get_preferred_units_by_measurement_using_get) | **GET** /v3/unit/measurement/preferred | getPreferredUnitsByMeasurement
[**get_unit_by_symbol_using_get**](Unitapiv3Api.md#get_unit_by_symbol_using_get) | **GET** /v3/unit/symbol | getUnitBySymbol
[**get_unit_by_system_and_measurement_using_get**](Unitapiv3Api.md#get_unit_by_system_and_measurement_using_get) | **GET** /v3/unit/unitsystem | getUnitBySystemAndMeasurement
[**get_unit_maps_using_get**](Unitapiv3Api.md#get_unit_maps_using_get) | **GET** /v3/unit/maps | getUnitMaps
[**get_unit_system_info_list_using_get**](Unitapiv3Api.md#get_unit_system_info_list_using_get) | **GET** /v3/unitsystem/list | getUnitSystemInfoList
[**get_unit_system_using_get**](Unitapiv3Api.md#get_unit_system_using_get) | **GET** /v3/unitsystem | getUnitSystem
[**get_units_by_measurement_using_get**](Unitapiv3Api.md#get_units_by_measurement_using_get) | **GET** /v3/unit/measurement | getUnitsByMeasurement
[**get_units_by_symbol_using_get**](Unitapiv3Api.md#get_units_by_symbol_using_get) | **GET** /v3/unit/symbols | getUnitsBySymbol
[**get_units_using_get**](Unitapiv3Api.md#get_units_using_get) | **GET** /v3/unit | getUnits
[**post_conversion_abcd_using_post**](Unitapiv3Api.md#post_conversion_abcd_using_post) | **POST** /v3/conversion/abcd | postConversionABCD
[**post_conversion_scale_offset_using_post**](Unitapiv3Api.md#post_conversion_scale_offset_using_post) | **POST** /v3/conversion/scale | postConversionScaleOffset
[**post_measurement_using_post**](Unitapiv3Api.md#post_measurement_using_post) | **POST** /v3/measurement | postMeasurement
[**post_preferred_units_by_measurement_using_post**](Unitapiv3Api.md#post_preferred_units_by_measurement_using_post) | **POST** /v3/unit/measurement/preferred | postPreferredUnitsByMeasurement
[**post_search_measurements_using_post**](Unitapiv3Api.md#post_search_measurements_using_post) | **POST** /v3/measurement/search | postSearchMeasurements
[**post_search_units_using_post**](Unitapiv3Api.md#post_search_units_using_post) | **POST** /v3/unit/search | postSearchUnits
[**post_search_using_post**](Unitapiv3Api.md#post_search_using_post) | **POST** /v3/catalog/search | postSearch
[**post_unit_by_system_and_measurement_using_post**](Unitapiv3Api.md#post_unit_by_system_and_measurement_using_post) | **POST** /v3/unit/unitsystem | postUnitBySystemAndMeasurement
[**post_unit_system_using_post**](Unitapiv3Api.md#post_unit_system_using_post) | **POST** /v3/unitsystem | postUnitSystem
[**post_unit_using_post**](Unitapiv3Api.md#post_unit_using_post) | **POST** /v3/unit | postUnit
[**post_units_by_measurement_using_post**](Unitapiv3Api.md#post_units_by_measurement_using_post) | **POST** /v3/unit/measurement | postUnitsByMeasurement


# **get_catalog_using_get**
> Catalog get_catalog_using_get(data_partition_id)

getCatalog

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)

try: 
    # getCatalog
    api_response = api_instance.get_catalog_using_get(data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_catalog_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]

### Return type

[**Catalog**](Catalog.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_conversion_abcd_by_symbols_using_get**
> ConversionResult get_conversion_abcd_by_symbols_using_get(data_partition_id, namespaces, from_symbol, to_symbol)

getConversionABCDBySymbols

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
namespaces = 'namespaces_example' # str | namespaces
from_symbol = 'from_symbol_example' # str | fromSymbol
to_symbol = 'to_symbol_example' # str | toSymbol

try: 
    # getConversionABCDBySymbols
    api_response = api_instance.get_conversion_abcd_by_symbols_using_get(data_partition_id, namespaces, from_symbol, to_symbol)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_conversion_abcd_by_symbols_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **namespaces** | **str**| namespaces | 
 **from_symbol** | **str**| fromSymbol | 
 **to_symbol** | **str**| toSymbol | 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_conversion_scale_offset_by_symbols_using_get**
> ConversionResult get_conversion_scale_offset_by_symbols_using_get(data_partition_id, namespaces, from_symbol, to_symbol)

getConversionScaleOffsetBySymbols

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
namespaces = 'namespaces_example' # str | namespaces
from_symbol = 'from_symbol_example' # str | fromSymbol
to_symbol = 'to_symbol_example' # str | toSymbol

try: 
    # getConversionScaleOffsetBySymbols
    api_response = api_instance.get_conversion_scale_offset_by_symbols_using_get(data_partition_id, namespaces, from_symbol, to_symbol)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_conversion_scale_offset_by_symbols_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **namespaces** | **str**| namespaces | 
 **from_symbol** | **str**| fromSymbol | 
 **to_symbol** | **str**| toSymbol | 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_last_modified_using_get**
> CatalogLastModified get_last_modified_using_get(data_partition_id)

getLastModified

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)

try: 
    # getLastModified
    api_response = api_instance.get_last_modified_using_get(data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_last_modified_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]

### Return type

[**CatalogLastModified**](CatalogLastModified.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_map_states_using_get**
> QueryResult get_map_states_using_get(data_partition_id, offset=offset, limit=limit)

getMapStates

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # getMapStates
    api_response = api_instance.get_map_states_using_get(data_partition_id, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_map_states_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_measurement_maps_using_get**
> QueryResult get_measurement_maps_using_get(data_partition_id, offset=offset, limit=limit)

getMeasurementMaps

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # getMeasurementMaps
    api_response = api_instance.get_measurement_maps_using_get(data_partition_id, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_measurement_maps_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_measurement_using_get**
> Measurement get_measurement_using_get(data_partition_id, ancestry)

getMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
ancestry = 'ancestry_example' # str | ancestry

try: 
    # getMeasurement
    api_response = api_instance.get_measurement_using_get(data_partition_id, ancestry)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_measurement_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **ancestry** | **str**| ancestry | 

### Return type

[**Measurement**](Measurement.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_measurements_using_get**
> QueryResult get_measurements_using_get(data_partition_id, offset=offset, limit=limit)

getMeasurements

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # getMeasurements
    api_response = api_instance.get_measurements_using_get(data_partition_id, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_measurements_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_preferred_units_by_measurement_using_get**
> QueryResult get_preferred_units_by_measurement_using_get(data_partition_id, ancestry)

getPreferredUnitsByMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
ancestry = 'ancestry_example' # str | ancestry

try: 
    # getPreferredUnitsByMeasurement
    api_response = api_instance.get_preferred_units_by_measurement_using_get(data_partition_id, ancestry)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_preferred_units_by_measurement_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **ancestry** | **str**| ancestry | 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_by_symbol_using_get**
> Unit get_unit_by_symbol_using_get(data_partition_id, namespaces, symbol)

getUnitBySymbol

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
namespaces = 'namespaces_example' # str | namespaces
symbol = 'symbol_example' # str | symbol

try: 
    # getUnitBySymbol
    api_response = api_instance.get_unit_by_symbol_using_get(data_partition_id, namespaces, symbol)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_unit_by_symbol_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **namespaces** | **str**| namespaces | 
 **symbol** | **str**| symbol | 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_by_system_and_measurement_using_get**
> Unit get_unit_by_system_and_measurement_using_get(data_partition_id, unit_system_name, ancestry)

getUnitBySystemAndMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
unit_system_name = 'unit_system_name_example' # str | unitSystemName
ancestry = 'ancestry_example' # str | ancestry

try: 
    # getUnitBySystemAndMeasurement
    api_response = api_instance.get_unit_by_system_and_measurement_using_get(data_partition_id, unit_system_name, ancestry)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_unit_by_system_and_measurement_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **unit_system_name** | **str**| unitSystemName | 
 **ancestry** | **str**| ancestry | 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_maps_using_get**
> QueryResult get_unit_maps_using_get(data_partition_id, offset=offset, limit=limit)

getUnitMaps

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # getUnitMaps
    api_response = api_instance.get_unit_maps_using_get(data_partition_id, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_unit_maps_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_system_info_list_using_get**
> UnitSystemInfoResponse get_unit_system_info_list_using_get(data_partition_id, offset=offset, limit=limit)

getUnitSystemInfoList

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # getUnitSystemInfoList
    api_response = api_instance.get_unit_system_info_list_using_get(data_partition_id, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_unit_system_info_list_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**UnitSystemInfoResponse**](UnitSystemInfoResponse.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_unit_system_using_get**
> UnitSystem get_unit_system_using_get(data_partition_id, name, offset=offset, limit=limit)

getUnitSystem

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
name = 'name_example' # str | name
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # getUnitSystem
    api_response = api_instance.get_unit_system_using_get(data_partition_id, name, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_unit_system_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **name** | **str**| name | 
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**UnitSystem**](UnitSystem.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_units_by_measurement_using_get**
> QueryResult get_units_by_measurement_using_get(data_partition_id, ancestry)

getUnitsByMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
ancestry = 'ancestry_example' # str | ancestry

try: 
    # getUnitsByMeasurement
    api_response = api_instance.get_units_by_measurement_using_get(data_partition_id, ancestry)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_units_by_measurement_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **ancestry** | **str**| ancestry | 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_units_by_symbol_using_get**
> QueryResult get_units_by_symbol_using_get(data_partition_id, symbol)

getUnitsBySymbol

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
symbol = 'symbol_example' # str | symbol

try: 
    # getUnitsBySymbol
    api_response = api_instance.get_units_by_symbol_using_get(data_partition_id, symbol)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_units_by_symbol_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **symbol** | **str**| symbol | 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_units_using_get**
> QueryResult get_units_using_get(data_partition_id, offset=offset, limit=limit)

getUnits

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # getUnits
    api_response = api_instance.get_units_using_get(data_partition_id, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->get_units_using_get: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_conversion_abcd_using_post**
> ConversionResult post_conversion_abcd_using_post(data_partition_id, request)

postConversionABCD

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.ConversionABCDRequest() # ConversionABCDRequest | request

try: 
    # postConversionABCD
    api_response = api_instance.post_conversion_abcd_using_post(data_partition_id, request)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_conversion_abcd_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**ConversionABCDRequest**](ConversionABCDRequest.md)| request | 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_conversion_scale_offset_using_post**
> ConversionResult post_conversion_scale_offset_using_post(data_partition_id, request)

postConversionScaleOffset

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.ConversionScaleOffsetRequest() # ConversionScaleOffsetRequest | request

try: 
    # postConversionScaleOffset
    api_response = api_instance.post_conversion_scale_offset_using_post(data_partition_id, request)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_conversion_scale_offset_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**ConversionScaleOffsetRequest**](ConversionScaleOffsetRequest.md)| request | 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_measurement_using_post**
> Measurement post_measurement_using_post(data_partition_id, request)

postMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.MeasurementRequest() # MeasurementRequest | request

try: 
    # postMeasurement
    api_response = api_instance.post_measurement_using_post(data_partition_id, request)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_measurement_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**MeasurementRequest**](MeasurementRequest.md)| request | 

### Return type

[**Measurement**](Measurement.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_preferred_units_by_measurement_using_post**
> QueryResult post_preferred_units_by_measurement_using_post(data_partition_id, request)

postPreferredUnitsByMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.MeasurementRequest() # MeasurementRequest | request

try: 
    # postPreferredUnitsByMeasurement
    api_response = api_instance.post_preferred_units_by_measurement_using_post(data_partition_id, request)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_preferred_units_by_measurement_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**MeasurementRequest**](MeasurementRequest.md)| request | 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_search_measurements_using_post**
> QueryResult post_search_measurements_using_post(data_partition_id, request, offset=offset, limit=limit)

postSearchMeasurements

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.SearchRequest() # SearchRequest | request
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # postSearchMeasurements
    api_response = api_instance.post_search_measurements_using_post(data_partition_id, request, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_search_measurements_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**SearchRequest**](SearchRequest.md)| request | 
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_search_units_using_post**
> QueryResult post_search_units_using_post(data_partition_id, request, offset=offset, limit=limit)

postSearchUnits

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.SearchRequest() # SearchRequest | request
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # postSearchUnits
    api_response = api_instance.post_search_units_using_post(data_partition_id, request, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_search_units_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**SearchRequest**](SearchRequest.md)| request | 
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_search_using_post**
> QueryResult post_search_using_post(data_partition_id, request, offset=offset, limit=limit)

postSearch

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.SearchRequest() # SearchRequest | request
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # postSearch
    api_response = api_instance.post_search_using_post(data_partition_id, request, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_search_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**SearchRequest**](SearchRequest.md)| request | 
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_unit_by_system_and_measurement_using_post**
> Unit post_unit_by_system_and_measurement_using_post(data_partition_id, unit_system_name, request)

postUnitBySystemAndMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
unit_system_name = 'unit_system_name_example' # str | unitSystemName
request = swagger_client.MeasurementRequest() # MeasurementRequest | request

try: 
    # postUnitBySystemAndMeasurement
    api_response = api_instance.post_unit_by_system_and_measurement_using_post(data_partition_id, unit_system_name, request)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_unit_by_system_and_measurement_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **unit_system_name** | **str**| unitSystemName | 
 **request** | [**MeasurementRequest**](MeasurementRequest.md)| request | 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_unit_system_using_post**
> UnitSystem post_unit_system_using_post(data_partition_id, request, offset=offset, limit=limit)

postUnitSystem

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.UnitSystemRequest() # UnitSystemRequest | request
offset = 0 # int | offset (optional) (default to 0)
limit = 100 # int | limit (optional) (default to 100)

try: 
    # postUnitSystem
    api_response = api_instance.post_unit_system_using_post(data_partition_id, request, offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_unit_system_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**UnitSystemRequest**](UnitSystemRequest.md)| request | 
 **offset** | **int**| offset | [optional] [default to 0]
 **limit** | **int**| limit | [optional] [default to 100]

### Return type

[**UnitSystem**](UnitSystem.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_unit_using_post**
> Unit post_unit_using_post(data_partition_id, request)

postUnit

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.UnitRequest() # UnitRequest | request

try: 
    # postUnit
    api_response = api_instance.post_unit_using_post(data_partition_id, request)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_unit_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**UnitRequest**](UnitRequest.md)| request | 

### Return type

[**Unit**](Unit.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **post_units_by_measurement_using_post**
> QueryResult post_units_by_measurement_using_post(data_partition_id, request)

postUnitsByMeasurement

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
api_instance = swagger_client.Unitapiv3Api()
data_partition_id = 'opendes' # str | tenant (default to opendes)
request = swagger_client.MeasurementRequest() # MeasurementRequest | request

try: 
    # postUnitsByMeasurement
    api_response = api_instance.post_units_by_measurement_using_post(data_partition_id, request)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling Unitapiv3Api->post_units_by_measurement_using_post: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**| tenant | [default to opendes]
 **request** | [**MeasurementRequest**](MeasurementRequest.md)| request | 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer Authorization](../README.md#Bearer Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

