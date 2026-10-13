from ..configuration import Configuration
from ..api_client import ApiClient

class SwaggerApiDocs(object):
    def __init__(self, api_client=None):
        config = Configuration()
        if not config.api_client:
            config.api_client = ApiClient()
        self.api_client = api_client or config.api_client
    def swagger_api_docs_check_using_get(self):
        return self.swagger_api_docs_check_using_get_with_http_info()
    def swagger_api_docs_check_using_get_with_http_info(self):
        header_params = {}
        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['*/*'])
        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])
        return self.api_client.call_api('/api-docs/v3', 'GET', header_params=header_params)