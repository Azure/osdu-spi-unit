# swagger_client.UoMCatalogApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_catalog_summary**](UoMCatalogApi.md#get_catalog_summary) | **GET** /catalog | Get the entire Catalog
[**get_last_modified_date**](UoMCatalogApi.md#get_last_modified_date) | **GET** /catalog/lastmodified | Gets the Catalog&#39;s last modified date
[**get_map_states**](UoMCatalogApi.md#get_map_states) | **GET** /catalog/mapstates | Get the map and/or deprecation states
[**search_entire_catalog**](UoMCatalogApi.md#search_entire_catalog) | **POST** /catalog/search | Search Catalog by keyword(s)


# **get_catalog_summary**
> Catalog get_catalog_summary(data_partition_id=data_partition_id)

Get the entire Catalog

Get the Units of Measure Catalog summary. The response contains empty lists and corresponding list counts. Use the specific endpoints to obtain the list contents as required.

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
api_instance = swagger_client.UoMCatalogApi()
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get the entire Catalog
    api_response = api_instance.get_catalog_summary(data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UoMCatalogApi->get_catalog_summary: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Catalog**](Catalog.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_last_modified_date**
> CatalogLastModified get_last_modified_date(data_partition_id=data_partition_id)

Gets the Catalog's last modified date

Gets the last modification date of the catalog.

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
api_instance = swagger_client.UoMCatalogApi()
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Gets the Catalog's last modified date
    api_response = api_instance.get_last_modified_date(data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UoMCatalogApi->get_last_modified_date: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CatalogLastModified**](CatalogLastModified.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_map_states**
> QueryResult get_map_states(offset=offset, limit=limit)

Get the map and/or deprecation states

Get the defined states for deprecation and cross-namespace mappings as defined in this catalog.

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
api_instance = swagger_client.UoMCatalogApi()
offset = 56 # int | The offset into the result array. Default 0. (optional)
limit = 100 # int | The size limit for the number of items in the response. Default 100; -1 for all items. (optional) (default to 100)

try: 
    # Get the map and/or deprecation states
    api_response = api_instance.get_map_states(offset=offset, limit=limit)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UoMCatalogApi->get_map_states: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset into the result array. Default 0. | [optional] 
 **limit** | **int**| The size limit for the number of items in the response. Default 100; -1 for all items. | [optional] [default to 100]

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **search_entire_catalog**
> QueryResult search_entire_catalog(body=body, offset=offset, limit=limit, data_partition_id=data_partition_id)

Search Catalog by keyword(s)

Search units, measurements, etc. by keywords. Valid keywords are: 'name', 'namespace', 'source', 'symbol', 'type' (unit parameterization type Abcd or ScaleOffset), 'ancestry', 'code', 'dimensionCode', 'unitQuantityCode', 'dimensionAnalysis', 'state', 'baseMeasurement'.

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
api_instance = swagger_client.UoMCatalogApi()
body = swagger_client.SearchRequest() # SearchRequest |  (optional)
offset = 56 # int | The offset into the result array. Default 0. (optional)
limit = 100 # int | The size limit of the response. Default 100; -1 for all items. (optional) (default to 100)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Search Catalog by keyword(s)
    api_response = api_instance.search_entire_catalog(body=body, offset=offset, limit=limit, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling UoMCatalogApi->search_entire_catalog: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SearchRequest**](SearchRequest.md)|  | [optional] 
 **offset** | **int**| The offset into the result array. Default 0. | [optional] 
 **limit** | **int**| The size limit of the response. Default 100; -1 for all items. | [optional] [default to 100]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**QueryResult**](QueryResult.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

