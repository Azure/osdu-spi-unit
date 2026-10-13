# coding: utf-8
"""Integration unit tests for unit-service v2"""
import os
import unittest
import json


from unit_test_core.v3.swagger_client.rest import ApiException
from unit_test_core.v3.swagger_client import Unitapiv3Api, Configuration, ApiClient, InfoapiApi, InfoapiApiSlash, SwaggerApiDocs
from unit_test_core.v3.swagger_client.models import CatalogLastModified, Catalog, ConnectedOuterService, \
    QueryResult, SearchRequest, \
    UnitSystem, UnitSystemRequest, Unit, MeasurementRequest, MeasurementEssenceImpl, Measurement, \
    UnitEssenceImpl, UnitRequest, UnitMapItem, ScaleOffsetImpl, UnitSystemInfoResponse, \
    ConversionScaleOffsetRequest, ConversionResult, ABCDImpl, ConversionABCDRequest, \
    UnitSystemEssenceImpl, VersionInfo
import unit_test_core.constants as constants
import jwt_client

import urllib3
import warnings
import logging
logging.basicConfig(level=os.environ.get("LOG_LEVEL", "INFO"))

def is_close(a, b, rel_tol=1e-09, abs_tol=0.0):
    """Compare a double
    https://stackoverflow.com/questions/5595425/what-is-the-best-way-to-compare-floats-for-almost-equality-in-python"""
    return abs(a - b) <= max(rel_tol * max(abs(a), abs(b)), abs_tol)


class TestEnvironment(object):
    """Container for resolved environment variables"""

    def __init__(self):
        self.base_url = constants.BASE_URL
        self.root_url = constants.ROOT_URL
        self.data_partition_id = constants.MY_TENANT

    def is_ok(self):
        return self.data_partition_id is not None and self.root_url is not None

    def client(self):
        # Configure API key authorization: api_key
        configuration = Configuration()
        # Set the bearer token; use a service account to do this
        bearer = jwt_client.get_id_token()
        configuration.api_key['Authorization'] = 'Bearer ' + bearer
        configuration.access_token = bearer
        if 'localhost' in self.root_url:
            url = 'http://' + self.root_url + self.base_url
        else:
            url = 'https://' + self.root_url + self.base_url
        configuration.host = url
        configuration.verify_ssl = False # Configure SSL certificate verification
        client = ApiClient(host=url) # configuration=configuration) # next version of Swagger generator
        client.user_agent = 'IntegrationTest'
        client.set_default_header('data-partition-id', self.data_partition_id)
        return client

    @staticmethod
    def get_unit_essence_so():
        m_essence = MeasurementEssenceImpl(ancestry='Time_Per_Length', type='UM')
        so = ScaleOffsetImpl(offset=0.0, scale=0.000003280839895013123)
        return UnitEssenceImpl(scale_offset=so, symbol='us/ft', base_measurement=m_essence, type='USO')

    @staticmethod
    def get_unit_essence_abcd():
        m_essence = MeasurementEssenceImpl(ancestry='Time_Per_Length', type='UM')
        abcd = ABCDImpl(a=0.0, b=0.000001, c=0.3048, d=0.0)
        return UnitEssenceImpl(abcd=abcd, symbol='us/ft', base_measurement=m_essence, type='UAD')

    @staticmethod
    def get_unit_persistable_reference(essence):
        d = dict()
        e_d = essence.to_dict()
        for key in e_d.keys():
            d[essence.attribute_map[key]] = e_d[key]
        return json.dumps(d)


class TestInfo(unittest.TestCase):
    """Test the info end-points"""
    @classmethod
    def setUpClass(cls):
        warnings.filterwarnings("ignore", category=ResourceWarning, message="unclosed.*<ssl.SSLSocket.*>")
        urllib3.disable_warnings()
        cls.env = TestEnvironment()
        if not cls.env.is_ok():
            raise Exception(
                'Test environment is not properly set up; MY_TENANT, VIRTUAL_SERVICE_HOST_NAME not set.')
        """Common set up for environment"""
        cls.api_instance = InfoapiApi(cls.env.client())

    def test_info_using_get(self):
        """test info_using_get"""
        try:
            api_response = self.api_instance.info_using_get(data_partition_id=self.env.data_partition_id)

            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, VersionInfo)
        except ApiException as e:
            self.fail(str(e))

class TestConversions(unittest.TestCase):
    """Test the conversion end-points"""
    @classmethod
    def setUpClass(cls):
        warnings.filterwarnings("ignore", category=ResourceWarning, message="unclosed.*<ssl.SSLSocket.*>")
        urllib3.disable_warnings()
        cls.env = TestEnvironment()
        if not cls.env.is_ok():
            raise Exception(
                'Test environment is not properly set up; MY_TENANT, VIRTUAL_SERVICE_HOST_NAME not set.')
        """Common set up for environment"""
        cls.api_instance = Unitapiv3Api(cls.env.client())

    def test_post_conversion_abcd_using_post(self):
        """test post_conversion_abcd_using_post"""
        try:
            ess_fr = TestEnvironment.get_unit_essence_abcd()
            ess_to = TestEnvironment.get_unit_essence_so()
            request = ConversionABCDRequest(from_unit=ess_fr, to_unit=ess_to)

            api_response = self.api_instance.post_conversion_abcd_using_post(request=request,
                                                                    data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, ConversionResult)
            self.assertIsNotNone(api_response.abcd)
            self.assertIsNone(api_response.scale_offset)
            self.assertEqual(0.0, api_response.abcd.a)
            self.assertEqual(1.0, api_response.abcd.b)
            self.assertEqual(1.0, api_response.abcd.c)
            self.assertEqual(0.0, api_response.abcd.d)
            pr_fr = TestEnvironment.get_unit_persistable_reference(ess_fr)
            pr_to = TestEnvironment.get_unit_persistable_reference(ess_to)
            request = ConversionABCDRequest(from_unit_persistable_reference=pr_fr,
                                            to_unit_persistable_reference=pr_to)

            api_response = self.api_instance.post_conversion_abcd_using_post(request=request,
                                                                    data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, ConversionResult)
            self.assertIsNotNone(api_response.abcd)
            self.assertIsNone(api_response.scale_offset)
            self.assertEqual(0.0, api_response.abcd.a)
            self.assertEqual(1.0, api_response.abcd.b)
            self.assertEqual(1.0, api_response.abcd.c)
            self.assertEqual(0.0, api_response.abcd.d)
        except ApiException as e:
            self.fail(str(e))

    def test_get_conversion_abcd_by_symbols_using_get(self):
        """Test get_conversion_abcd_by_symbols_using_get"""
        try:

            api_response = self.api_instance.get_conversion_abcd_by_symbols_using_get(
                namespaces='Energistics_UoM,RP66', from_symbol='ft/s', to_symbol='m/s',
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, ConversionResult)
            self.assertIsNotNone(api_response.abcd)
            self.assertIsNone(api_response.scale_offset)
            self.assertEqual(0.0, api_response.abcd.a)
            self.assertEqual(0.3048, api_response.abcd.b)
            self.assertEqual(1.0, api_response.abcd.c)
            self.assertEqual(0.0, api_response.abcd.d)
        except ApiException as e:
            self.fail(str(e))

    def test_post_conversion_scale_offset_using_post(self):
        """test get_conversion_as_scale_offset"""
        try:
            ess_to = TestEnvironment.get_unit_essence_abcd()
            ess_fr = TestEnvironment.get_unit_essence_so()
            request = ConversionScaleOffsetRequest(from_unit=ess_fr, to_unit=ess_to)
            api_response = self.api_instance.post_conversion_scale_offset_using_post(request=request,
                                                                            data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, ConversionResult)
            self.assertIsNone(api_response.abcd)
            self.assertIsNotNone(api_response.scale_offset)
            self.assertEqual(0.0, api_response.scale_offset.offset)
            self.assertEqual(1.0, api_response.scale_offset.scale)
            pr_fr = TestEnvironment.get_unit_persistable_reference(ess_fr)
            pr_to = TestEnvironment.get_unit_persistable_reference(ess_to)
            request = ConversionScaleOffsetRequest(from_unit_persistable_reference=pr_fr,
                                                   to_unit_persistable_reference=pr_to)
            api_response = self.api_instance.post_conversion_scale_offset_using_post(request=request,
                                                                            data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, ConversionResult)
            self.assertIsNone(api_response.abcd)
            self.assertIsNotNone(api_response.scale_offset)
            self.assertEqual(0.0, api_response.scale_offset.offset)
            self.assertEqual(1.0, api_response.scale_offset.scale)
        except ApiException as e:
            self.fail(str(e))

    def test_get_conversion_scale_offset_by_symbols_using_get(self):
        """Test get_conversion_as_scale_offset_by_namespace_and_symbols"""
        try:
            api_response = self.api_instance.get_conversion_scale_offset_by_symbols_using_get(
                data_partition_id=self.env.data_partition_id, namespaces='Energistics_UoM,RP66', from_symbol='ft/s', to_symbol='m/s')
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, ConversionResult)
            self.assertIsNotNone(api_response.scale_offset)
            self.assertIsNone(api_response.abcd)
            self.assertEqual(0.0, api_response.scale_offset.offset)
            self.assertEqual(0.3048, api_response.scale_offset.scale)
        except ApiException as e:
            self.fail(str(e))


class TestUnits(unittest.TestCase):
    """Test the Units end-points"""
    @classmethod
    def setUpClass(cls):
        warnings.filterwarnings("ignore", category=ResourceWarning, message="unclosed.*<ssl.SSLSocket.*>")
        urllib3.disable_warnings()
        cls.env = TestEnvironment()
        if not cls.env.is_ok():
            raise Exception(
                'Test environment is not properly set up; MY_TENANT, VIRTUAL_SERVICE_HOST_NAME not set.')
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = Unitapiv3Api(cls.env.client())

    def test_get_units_using_get(self):
        """Test get_units_using_get"""
        try:
            api_response = self.api_instance.get_units_using_get(offset=0, limit=50,
                                                       data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(50, api_response.count)
            self.assertEqual(50, len(api_response.units))
            self.assertTrue(api_response.total_count > api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_post_unit_using_post(self):
        """Test post_unit_using_post"""
        try:
            essence = TestEnvironment.get_unit_essence_so()
            request = UnitRequest(essence=essence)
            api_response = self.api_instance.post_unit_using_post(request=request,
                                                      data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Unit)
            self.assertIsNone(api_response.essence.abcd)
            self.assertIsNotNone(api_response.essence.scale_offset)
            self.assertEqual(essence.symbol, api_response.essence.symbol)
            pr = TestEnvironment.get_unit_persistable_reference(essence)
            request = UnitRequest(persistable_reference=pr)
            api_response = self.api_instance.post_unit_using_post(request=request,
                                                      data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsNone(api_response.essence.abcd)
            self.assertIsNotNone(api_response.essence.scale_offset)
            self.assertIsInstance(api_response, Unit)
            self.assertEqual(essence.symbol, api_response.essence.symbol)
            essence = TestEnvironment.get_unit_essence_abcd()
            request = UnitRequest(essence=essence)
            api_response = self.api_instance.post_unit_using_post(request=request,
                                                      data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsNotNone(api_response.essence.abcd)
            self.assertIsNone(api_response.essence.scale_offset)
            self.assertIsInstance(api_response, Unit)
            self.assertEqual(essence.symbol, api_response.essence.symbol)
            pr = TestEnvironment.get_unit_persistable_reference(essence)
            request = UnitRequest(persistable_reference=pr)
            api_response = self.api_instance.post_unit_using_post(request=request,
                                                      data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsNotNone(api_response.essence.abcd)
            self.assertIsNone(api_response.essence.scale_offset)
            self.assertIsInstance(api_response, Unit)
            self.assertEqual(essence.symbol, api_response.essence.symbol)
        except ApiException as e:
            self.fail(str(e))

    def test_get_unit_by_symbol_using_get(self):
        """Test get_unit_by_symbol_using_get"""
        try:
            api_response = self.api_instance.get_unit_by_symbol_using_get(namespaces='RP66,LIS', symbol='us/ft',
                                                                              data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Unit)
            self.assertIsNone(api_response.essence.abcd)
            self.assertIsNotNone(api_response.essence.scale_offset)
            self.assertEqual('us/ft', api_response.essence.symbol)
        except ApiException as e:
            self.fail(str(e))

    def test_get_units_for_measurement(self):
        """Test get_units_for_measurement"""
        try:
            essence = MeasurementEssenceImpl(ancestry='Time_Per_Length.Acoustic_Slowness', type='UM')
            request = MeasurementRequest(essence=essence)
            api_response = self.api_instance.post_units_by_measurement_using_post(request=request,
                                                                       data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(api_response.count > 0)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertEqual(api_response.total_count, api_response.count)

            pr = json.dumps(essence.to_dict())
            request = MeasurementRequest(persistable_reference=pr)
            api_response = self.api_instance.post_units_by_measurement_using_post(request=request,
                                                                       data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(api_response.count > 0)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertEqual(api_response.total_count, api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_post_preferred_units_by_measurement_using_post(self):
        """Test post_preferred_units_by_measurement_using_post"""
        try:
            essence = MeasurementEssenceImpl(ancestry='Time_Per_Length.Acoustic_Slowness', type='UM')
            request = MeasurementRequest(essence=essence)
            api_response = self.api_instance.post_preferred_units_by_measurement_using_post(request =request,
                                                                                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(api_response.count > 0)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertEqual(api_response.total_count, api_response.count)
            pr = json.dumps(essence.to_dict())
            request = MeasurementRequest(persistable_reference=pr)
            api_response = self.api_instance.post_preferred_units_by_measurement_using_post(request=request,
                                                                                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(api_response.count > 0)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertEqual(api_response.total_count, api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_get_units_by_measurement_using_get(self):
        """Test get_units_by_measurement_using_get"""
        try:
            api_response = self.api_instance.get_units_by_measurement_using_get \
                (ancestry='Time_Per_Length.Acoustic_Slowness',
                 data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(api_response.count > 0)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertEqual(api_response.total_count, api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_get_preferred_units_by_measurement_using_get(self):
        """Test get_preferred_units_by_measurement_using_get"""
        try:
            api_response = self.api_instance.get_preferred_units_by_measurement_using_get \
                (ancestry='Time_Per_Length.Acoustic_Slowness',
                 data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(api_response.count > 0)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertEqual(api_response.total_count, api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_get_unit_maps_using_get(self):
        """Test get_unit_maps_using_get"""
        try:
            api_response = self.api_instance.get_unit_maps_using_get(offset=0, limit=100, data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.unit_map_items)
            self.assertEqual(0, api_response.offset)
            self.assertEqual(100, api_response.count)
            self.assertEqual(api_response.count, len(api_response.unit_map_items))
            self.assertTrue(api_response.total_count > api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_get_units_by_symbol_using_get(self):
        """Test get_units_by_symbol_using_get"""
        try:
            api_response = self.api_instance.get_units_by_symbol_using_get(symbol='us/ft',
                                                                 data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(0, api_response.offset)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertEqual(api_response.total_count, api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_post_search_units_using_post(self):
        """Test post_search_units_using_post"""
        try:
            request = SearchRequest(query='namespace:LIS OR namespace:Energistics_UoM')
            api_response = self.api_instance.post_search_units_using_post(request=request, offset=10, limit=100,
                                                          data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.units)
            self.assertEqual(10, api_response.offset)
            self.assertEqual(api_response.count, len(api_response.units))
            self.assertTrue(api_response.total_count > api_response.count)
        except ApiException as e:
            self.fail(str(e))


class TestMeasurements(unittest.TestCase):
    """Test the Measurement end-points"""
    @classmethod
    def setUpClass(cls):
        warnings.filterwarnings("ignore", category=ResourceWarning, message="unclosed.*<ssl.SSLSocket.*>")
        urllib3.disable_warnings()
        cls.env = TestEnvironment()
        if not cls.env.is_ok():
            raise Exception(
                'Test environment is not properly set up; MY_TENANT, VIRTUAL_SERVICE_HOST_NAME not set.')
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = Unitapiv3Api(cls.env.client())

    def test_get_measurements_using_get(self):
        """Test get_measurements_using_get"""
        try:
            api_response = self.api_instance.get_measurements_using_get(offset=0, limit=5,
                                                              data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.measurements)
            self.assertEqual(5, api_response.count)
            self.assertEqual(5, len(api_response.measurements))
            self.assertTrue(api_response.total_count > api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_post_measurement_using_post(self):
        """Test post_measurement_using_post"""
        try:
            essence = MeasurementEssenceImpl(ancestry='Time_Per_Length.Acoustic_Slowness')
            request = MeasurementRequest(essence=essence)
            api_response = self.api_instance.post_measurement_using_post(request=request,
                                                             data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Measurement)
            self.assertIsNotNone(api_response.child_measurement_essence_jsons)
            self.assertTrue(len(api_response.child_measurement_essence_jsons) > 0)
            self.assertEqual('Acoustic_Slowness', api_response.code)
            self.assertEqual('Acoustic Slowness', api_response.name)
            self.assertEqual('SLB', api_response.namespace)
            pr = api_response.base_measurement_essence_json
            request = MeasurementRequest(persistable_reference=pr)
            api_response = self.api_instance.post_measurement_using_post(request=request,
                                                             data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Measurement)
            self.assertIsNotNone(api_response.child_measurement_essence_jsons)
            self.assertTrue(len(api_response.unit_essence_jsons) > 0)
            self.assertIsNotNone(api_response.unit_essence_jsons)
            self.assertTrue(len(api_response.child_measurement_essence_jsons) > 0)
            self.assertEqual('Time_Per_Length', api_response.code)
            self.assertEqual('Time Per Length', api_response.name)
            self.assertEqual('SLB', api_response.namespace)
        except ApiException as e:
            self.fail(str(e))

    def test_get_measurement_using_get(self):
        """Test get_measurement_using_get"""
        try:
            api_response = self.api_instance.get_measurement_using_get(
                ancestry='Time_Per_Length.Acoustic_Slowness',
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Measurement)
            self.assertIsNotNone(api_response.child_measurement_essence_jsons)
            self.assertTrue(len(api_response.child_measurement_essence_jsons) > 0)
            self.assertEqual('Acoustic_Slowness', api_response.code)
            self.assertEqual('Acoustic Slowness', api_response.name)
            self.assertEqual('SLB', api_response.namespace)
        except ApiException as e:
            self.fail(str(e))

    def test_get_measurement_maps_using_get(self):
        """Test get_measurement_maps_using_get"""
        try:
            api_response = self.api_instance.get_measurement_maps_using_get(offset=0, limit=10,
                                                                       data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.measurement_map_items)
            self.assertTrue(len(api_response.measurement_map_items) > 0)
            self.assertEqual(10, api_response.count)
            self.assertTrue(api_response.total_count > api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_search_measurements(self):
        """Test search_measurements"""
        try:
            request = SearchRequest(query='dimensionAnalysis:T/L')
            api_response = self.api_instance.post_search_measurements_using_post(request=request, offset=0, limit=10, data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertIsNotNone(api_response.measurements)
            self.assertTrue(len(api_response.measurements) > 0)
            self.assertEqual(10, api_response.count)
            self.assertTrue(api_response.total_count > api_response.count)
        except ApiException as e:
            self.fail(str(e))


class TestUnitSystems(unittest.TestCase):
    """Test Unit Systems end-points"""

    @classmethod
    def setUpClass(cls):
        warnings.filterwarnings("ignore", category=ResourceWarning, message="unclosed.*<ssl.SSLSocket.*>")
        urllib3.disable_warnings()
        cls.env = TestEnvironment()
        if not cls.env.is_ok():
            raise Exception(
                'Test environment is not properly set up; MY_TENANT, VIRTUAL_SERVICE_HOST_NAME not set.')
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = Unitapiv3Api(cls.env.client())

    def test_get_unit_system_info_list_using_get(self):
        """Test get_unit_system_info_list_using_get"""
        try:
            api_response = self.api_instance.get_unit_system_info_list_using_get(offset=0, limit=5,
                                                                  data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, UnitSystemInfoResponse)
            self.assertIsNotNone(api_response.unit_system_info_list)
            self.assertEqual(5, api_response.count)
            self.assertEqual(5, len(api_response.unit_system_info_list))
            self.assertTrue(api_response.total_count > api_response.count)
        except ApiException as e:
            self.fail(str(e))

    def test_get_unit_system_using_get(self):
        """Test get_unit_system_using_get"""
        try:
            api_response = self.api_instance.get_unit_system_using_get(name='English', offset=0,
                                                                     limit=100, data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, UnitSystem)
            self.assertEqual('English', api_response.name)
            self.assertIsNotNone(api_response.unit_assignments)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(
                api_response.unit_assignment_count_total > api_response.unit_assignment_count_in_response)
        except ApiException as e:
            self.fail(str(e))

    def test_post_unit_system_using_post(self):
        """Test post_unit_system_using_post"""
        try:
            essence = UnitSystemEssenceImpl(ancestry='Metric.English')
            request = UnitSystemRequest(essence=essence)
            api_response = self.api_instance.post_unit_system_using_post(request=request, offset=0,
                                                                        limit=100,
                                                                        data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, UnitSystem)
            self.assertEqual('English', api_response.name)
            self.assertIsNotNone(api_response.unit_assignments)
            self.assertEqual(0, api_response.offset)
            self.assertTrue(
                api_response.unit_assignment_count_total > api_response.unit_assignment_count_in_response)
        except ApiException as e:
            self.fail(str(e))

    def test_get_unit_by_system_and_measurement_using_get(self):
        """Test get_unit_by_system_and_measurement_using_get"""
        try:
            api_response = self.api_instance.get_unit_by_system_and_measurement_using_get(
                unit_system_name='English', ancestry='Time_Per_Length.Acoustic_Slowness',
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Unit)
            self.assertEqual('us/ft', api_response.essence.symbol)
        except ApiException as e:
            self.fail(str(e))

    def test_post_unit_by_system_and_measurement_using_post(self):
        """Test get_unit_by_unit_system_and_measurement"""
        try:
            essence = MeasurementEssenceImpl(ancestry='Time_Per_Length.Acoustic_Slowness', type='UM')
            request = MeasurementRequest(essence=essence)
            api_response = self.api_instance.post_unit_by_system_and_measurement_using_post(
                request=request, unit_system_name='English',
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Unit)
            self.assertEqual('us/ft', api_response.essence.symbol)
            pr = json.dumps(essence.to_dict())
            request = MeasurementRequest(persistable_reference=pr)
            api_response = self.api_instance.post_unit_by_system_and_measurement_using_post(
                request=request, unit_system_name='English',
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Unit)
            self.assertEqual('us/ft', api_response.essence.symbol)
        except ApiException as e:
            self.fail(str(e))


class TestUoMCatalog(unittest.TestCase):
    """Test the UoM Catalog end-points"""

    @classmethod
    def setUpClass(cls):
        warnings.filterwarnings("ignore", category=ResourceWarning, message="unclosed.*<ssl.SSLSocket.*>")
        urllib3.disable_warnings()
        cls.env = TestEnvironment()
        if not cls.env.is_ok():
            raise Exception(
                'Test environment is not properly set up; MY_TENANT, VIRTUAL_SERVICE_HOST_NAME not set.')
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = Unitapiv3Api(cls.env.client())

    def test_get_last_modified_using_get(self):
        """Test get_last_modified_using_get"""
        try:
            api_response = self.api_instance.get_last_modified_using_get(
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, CatalogLastModified)
            self.assertIsNotNone(api_response.last_modified)
        except ApiException as e:
            self.fail(str(e))

    def test_get_catalog_using_get(self):
        """Test get_catalog_using_get"""
        try:
            api_response = self.api_instance.get_catalog_using_get(
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Catalog)
            self.assertTrue(api_response.total_measurement_count > 0)
            self.assertTrue(api_response.total_unit_count > 0)
            self.assertTrue(api_response.total_unit_system_count > 0)
            self.assertTrue(api_response.total_map_state_count > 0)
            self.assertTrue(api_response.total_measurement_map_count > 0)
            self.assertTrue(api_response.total_unit_map_count > 0)
        except ApiException as e:
            self.fail(str(e))

    def test_get_map_states_using_get(self):
        """Test get_map_states_using_get"""
        try:
            api_response = self.api_instance.get_map_states_using_get(data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertEqual(8, api_response.count)  # this is fixed
        except ApiException as e:
            self.fail(str(e))

    def test_post_search_using_post(self):
        """Test post_search_using_post"""
        try:
            request = SearchRequest(query='namespace:Energistics_UoM')
            api_response = self.api_instance.post_search_using_post(request=request, offset=0,
                                                                   limit=100,
                                                                   data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, QueryResult)
            self.assertEqual(100, api_response.count)
            self.assertTrue(api_response.total_count > api_response.count)
            self.assertTrue(len(api_response.units) > 0 or len(api_response.measurements) > 0 or
                            len(api_response.measurement_map_items) > 0 or len(
                api_response.unit_map_items) > 0)
        except ApiException as e:
            self.fail(str(e))

class TestUnAuthorizedUoMCatalog(unittest.TestCase):
    """Test the UoM Catalog end-points"""

    @classmethod
    def setUpClass(cls):
        warnings.filterwarnings("ignore", category=ResourceWarning, message="unclosed.*<ssl.SSLSocket.*>")
        urllib3.disable_warnings()
        cls.env = TestEnvironment()
        if not cls.env.is_ok():
            raise Exception(
                'Test environment is not properly set up; MY_TENANT, VIRTUAL_SERVICE_HOST_NAME not set.')
        """Common set up for environment"""
        configuration = Configuration()
        # Set the bearer token; use a service account to do this
        bearer = jwt_client.get_invalid_token()
        configuration.api_key['Authorization'] = 'Bearer ' + bearer
#        configuration.access_token = bearer
        configuration.access_token = ''
        if 'localhost' in cls.env.root_url:
            url = 'http://' + cls.env.root_url + cls.env.base_url
        else:
            url = 'https://' + cls.env.root_url + cls.env.base_url
        configuration.host = url
        client = ApiClient(host=url)  # configuration=configuration) # next version of Swagger generator
        client.user_agent = 'IntegrationTest'
        cls.api_instance = Unitapiv3Api(client)

    def test_get_last_modified_using_get_with_invalid_token(self):
        """Test get_last_modified_using_get"""
        try:
            api_response = self.api_instance.get_last_modified_using_get(
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.fail('should not be coming here')
        except ApiException as e:
            reason = json.loads(e.body)['reason']
            self.assertTrue(401 == e.status or 403 == e.status)
            self.assertTrue("Unauthorized" == reason or "Forbidden" == reason)

    def test_get_catalog_using_get_with_invalid_token(self):
        """Test get_catalog_using_get"""
        try:
            api_response = self.api_instance.get_catalog_using_get(
                data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.fail('should not be coming here')
        except ApiException as e:
            reason = json.loads(e.body)['reason']
            self.assertTrue(401 == e.status or 403 == e.status)
            self.assertTrue("Unauthorized" == reason or "Forbidden" == reason)

    def test_get_map_states_with_invalid_token(self):
        """Test get_map_states_using_get"""
        try:
            api_response = self.api_instance.get_map_states_using_get(data_partition_id=self.env.data_partition_id)
            self.assertIsNotNone(api_response)
            self.fail('should not be coming here')
        except ApiException as e:
            reason = json.loads(e.body)['reason']
            self.assertTrue(401 == e.status or 403 == e.status)
            self.assertTrue("Unauthorized" == reason or "Forbidden" == reason)
