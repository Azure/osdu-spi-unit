# Catalog

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**last_modified** | **datetime** | The unit of measure catalog&#39;s last modification date. | [optional] 
**total_map_state_count** | **int** | The number of MapState items known to this catalog. | [optional] 
**map_states** | [**list[MapState]**](MapState.md) | The MapState items defined in this catalog. | [optional] 
**total_measurement_map_count** | **int** | The number of measurement maps, i.e. one-directional links/correspondences of measurement pairs defined in this catalog. | [optional] 
**measurement_maps** | [**list[MeasurementMap]**](MeasurementMap.md) | The array of measurement maps, i.e. one-directional links/correspondences of measurement pairs defined in this catalog. | [optional] 
**total_measurement_count** | **int** | The number of measurements - base measurements/dimensions and child measurements, defined in this catalog. | [optional] 
**measurements** | [**list[Measurement]**](Measurement.md) | The array of measurements, i.e. base measurements/dimensions and child measurements defined in this catalog. | [optional] 
**total_unit_map_count** | **int** | The number of unit maps, i.e. one-directional links/correspondences of unit pairs defined in this catalog. | [optional] 
**unit_maps** | [**list[UnitMap]**](UnitMap.md) | The unit maps, i.e. one-directional links/correspondences of unit pairs defined in this catalog. | [optional] 
**total_unit_system_count** | **int** | The number of unit systems defined in this catalog. | [optional] 
**unit_system_infos** | [**list[UnitSystemInfoList]**](UnitSystemInfoList.md) | The array of unit systems defined in this catalog. | [optional] 
**total_unit_count** | **int** | The number of units defined in this catalog. | [optional] 
**units** | [**list[Unit]**](Unit.md) | The array of units defined in this catalog. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


