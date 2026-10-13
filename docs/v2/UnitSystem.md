# UnitSystem

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**ancestry** | **str** | The full ancestry of this unit system. | [optional] 
**description** | **str** | Any further description of this unit system. | [optional] 
**last_modified** | **str** | The last modification of this unit system core properties formatted as YYYYMMDD. | [optional] 
**name** | **str** | The name of this unit system. | [optional] 
**reference_unit_system** | **str** | The unit system code, from which this unit system is derived - or a blank string. | [optional] 
**source** | **str** | Source of the unit system definition. | [optional] 
**offset** | **int** | The offset into the unit assignment list as defined in the request. | [optional] 
**unit_assignment_count_total** | **int** | The total number of unit assignments provided by this unit system. | [optional] 
**unit_assignment_count_in_response** | **int** | The actual number of unit assignments delivered in this response. | [optional] 
**unit_assignments** | [**list[UnitAssignment]**](UnitAssignment.md) | The unit assignments provided by this unit system. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


