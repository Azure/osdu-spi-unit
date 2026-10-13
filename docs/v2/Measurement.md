# Measurement

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**base_measurement** | **bool** | True if the measurement is a root or base measurement, also known as dimension. False for any child measurement. | [optional] 
**child_measurement_essence_jsons** | **list[str]** | The essence Json strings of any child measurement of the current measurement. | [optional] 
**code** | **str** | The measurement code. | [optional] 
**deprecation_info** | [**MeasurementDeprecationInfo**](MeasurementDeprecationInfo.md) | Optional deprecation information, if the current measurement is declared as deprecated. | [optional] 
**description** | **str** | A description string further describing the meaning and purpose of the current measurement. | [optional] 
**dimension_analysis** | **str** | The dimensionality of the measurement in terms of Energistics dimension codes () | [optional] 
**dimension_code** | **str** | The OSDD dimension code in case of &#39;SLB&#39; or the dimension for namespace &#39;Energistics_UoM&#39;. | [optional] 
**essence** | [**MeasurementEssence**](MeasurementEssence.md) | The essential parameterization of the measurement. | [optional] 
**last_modified** | **str** | The date this measurement was last updated in the format YYYYMMDD. | [optional] 
**name** | **str** | The name of the current measurement. | [optional] 
**namespace** | **str** | The namespace in which the current code is unique; typical values are &#39;SLB&#39;, &#39;Energistics_UoM&#39; | [optional] 
**parent_essence_json** | **str** | Only populated for child measurements - the parent measurement as essence Json string. | [optional] 
**base_measurement_essence_json** | **str** | The base measurement as essence Json string; base measurements return themselves as persistable reference string. | [optional] 
**preferred_unit_essence_jsons** | **list[str]** | If populated, the list of preferred units associated with the current measurement. | [optional] 
**unit_quantity_code** | **str** | The OSDD UnitQuantity code associated with this measurement - only meaningful for namespace &#39;SLB&#39;. | [optional] 
**unit_essence_jsons** | **list[str]** | Only populated for base measurements/dimensions: the units associated with this base measurement/dimension. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


