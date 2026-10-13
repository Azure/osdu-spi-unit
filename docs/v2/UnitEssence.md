# UnitEssence

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**abcd** | [**Abcd**](Abcd.md) | The Energistics y &#x3D; (A+B*x)/(C+D*x) parameterization - if not present, scaleOffset is populated. | [optional] 
**scale_offset** | [**ScaleOffset**](ScaleOffset.md) | The y &#x3D; scale*(x-offset) parameterization - if not present, Abcd is populated. | [optional] 
**symbol** | **str** | The unit symbol or short name. | [optional] 
**base_measurement** | [**MeasurementEssence**](MeasurementEssence.md) | The essence to the base measurement (identifying, which units are convertible). | [optional] 
**type** | **str** | The type string for this unit essence, either &#39;USO&#39; for ScaleOffset or &#39;UAD&#39; for Abcd. | [optional] [default to 'USO']

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


