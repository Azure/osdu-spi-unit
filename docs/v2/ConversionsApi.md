# swagger_client.ConversionsApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_conversion_as_abcd**](ConversionsApi.md#get_conversion_as_abcd) | **POST** /conversion/abcd | Get Abcd Unit conversion parameters given two Units
[**get_conversion_as_abcd_by_namespace_and_symbols**](ConversionsApi.md#get_conversion_as_abcd_by_namespace_and_symbols) | **GET** /conversion/abcd/{namespaces}/{fromSymbol}/{toSymbol} | Get Abcd Unit conversion parameters given two Unit specifications
[**get_conversion_as_scale_offset**](ConversionsApi.md#get_conversion_as_scale_offset) | **POST** /conversion/scale | Get ScaleOffset Unit conversion parameters given two Units
[**get_conversion_as_scale_offset_by_namespace_and_symbols**](ConversionsApi.md#get_conversion_as_scale_offset_by_namespace_and_symbols) | **GET** /conversion/scale/{namespaces}/{fromSymbol}/{toSymbol} | Get ScaleOffset Unit conversion parameters given two unit specifications


# **get_conversion_as_abcd**
> ConversionResult get_conversion_as_abcd(body=body, data_partition_id=data_partition_id)

Get Abcd Unit conversion parameters given two Units

Get the Energistics style unit parameters given the 'fromUnit' and 'toUnit'. The 'fromUnit' and 'toUnit' definitions can either be passed as persistable reference strings (JSON serialized Unit essence) or as Unit essence structures.

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
api_instance = swagger_client.ConversionsApi()
body = swagger_client.ConversionAbcdRequest() # ConversionAbcdRequest |  (optional)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get Abcd Unit conversion parameters given two Units
    api_response = api_instance.get_conversion_as_abcd(body=body, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling ConversionsApi->get_conversion_as_abcd: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ConversionAbcdRequest**](ConversionAbcdRequest.md)|  | [optional] 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_conversion_as_abcd_by_namespace_and_symbols**
> ConversionResult get_conversion_as_abcd_by_namespace_and_symbols(namespaces, from_symbol, to_symbol, data_partition_id=data_partition_id)

Get Abcd Unit conversion parameters given two Unit specifications

Get the Energistics style unit parameters given the 'from' and 'to' unit symbols and the namespace(-list) to disambiguate the unit symbols. Example for a prioritized namespaces list: 'LIS,RP66,ECL,Energistics_UoM' - this will prioritize the units in the LIS namespace over other namespaces.

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
api_instance = swagger_client.ConversionsApi()
namespaces = 'namespaces_example' # str | List of namespace codes to disambiguate the unit symbols. Example: 'LIS,RP66,ECL,Energistics_UoM' to prioritize old LIS unit symbols.
from_symbol = 'from_symbol_example' # str | The source (=from) unit symbol, example: 'ftUS'.
to_symbol = 'to_symbol_example' # str | The target (=to) unit symbol, example: 'm'.
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get Abcd Unit conversion parameters given two Unit specifications
    api_response = api_instance.get_conversion_as_abcd_by_namespace_and_symbols(namespaces, from_symbol, to_symbol, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling ConversionsApi->get_conversion_as_abcd_by_namespace_and_symbols: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **namespaces** | **str**| List of namespace codes to disambiguate the unit symbols. Example: &#39;LIS,RP66,ECL,Energistics_UoM&#39; to prioritize old LIS unit symbols. | 
 **from_symbol** | **str**| The source (&#x3D;from) unit symbol, example: &#39;ftUS&#39;. | 
 **to_symbol** | **str**| The target (&#x3D;to) unit symbol, example: &#39;m&#39;. | 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_conversion_as_scale_offset**
> ConversionResult get_conversion_as_scale_offset(body=body, data_partition_id=data_partition_id)

Get ScaleOffset Unit conversion parameters given two Units

Get the scale/offset unit parameters given the 'fromUnit' and 'toUnit'. The 'fromUnit' and 'toUnit' definitions can either be passed as persistable reference strings (JSON serialized Unit essence) or as Unit essence structures.

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
api_instance = swagger_client.ConversionsApi()
body = swagger_client.ConversionScaleOffsetRequest() # ConversionScaleOffsetRequest |  (optional)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get ScaleOffset Unit conversion parameters given two Units
    api_response = api_instance.get_conversion_as_scale_offset(body=body, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling ConversionsApi->get_conversion_as_scale_offset: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ConversionScaleOffsetRequest**](ConversionScaleOffsetRequest.md)|  | [optional] 
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_conversion_as_scale_offset_by_namespace_and_symbols**
> ConversionResult get_conversion_as_scale_offset_by_namespace_and_symbols(namespaces, from_symbol, to_symbol)

Get ScaleOffset Unit conversion parameters given two unit specifications

Get the scale/offset unit parameters given the 'from' and 'to' unit symbols and the namespace(-list) to disambiguate the unit symbols. Example for a prioritized namespaces list: 'LIS,RP66,ECL,Energistics_UoM' - this will prioritize the units in the LIS namespace over other namespaces.

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
api_instance = swagger_client.ConversionsApi()
namespaces = 'namespaces_example' # str | List of namespace codes to disambiguate the unit symbols. Example: 'LIS,RP66,ECL,Energistics_UoM' to prioritize old LIS unit symbols.
from_symbol = 'from_symbol_example' # str | The source (=from) unit symbol, example: 'ftUS'.
to_symbol = 'to_symbol_example' # str | The target (=to) unit symbol, example: 'm'.

try: 
    # Get ScaleOffset Unit conversion parameters given two unit specifications
    api_response = api_instance.get_conversion_as_scale_offset_by_namespace_and_symbols(namespaces, from_symbol, to_symbol)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling ConversionsApi->get_conversion_as_scale_offset_by_namespace_and_symbols: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **namespaces** | **str**| List of namespace codes to disambiguate the unit symbols. Example: &#39;LIS,RP66,ECL,Energistics_UoM&#39; to prioritize old LIS unit symbols. | 
 **from_symbol** | **str**| The source (&#x3D;from) unit symbol, example: &#39;ftUS&#39;. | 
 **to_symbol** | **str**| The target (&#x3D;to) unit symbol, example: &#39;m&#39;. | 

### Return type

[**ConversionResult**](ConversionResult.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

