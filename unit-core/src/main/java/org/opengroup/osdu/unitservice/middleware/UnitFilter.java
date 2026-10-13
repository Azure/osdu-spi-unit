// Copyright © 2020 Amazon Web Services
// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.unitservice.middleware;

import org.opengroup.osdu.core.common.http.ResponseHeadersFactory;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class UnitFilter implements Filter {

	@Inject
	private DpsHeaders dpsHeaders;

	private ResponseHeadersFactory responseHeadersFactory = new ResponseHeadersFactory();

	// defaults to * for any front-end, string must be comma-delimited if more than one domain
	@Value("${ACCESS_CONTROL_ALLOW_ORIGIN_DOMAINS:*}")
	String ACCESS_CONTROL_ALLOW_ORIGIN_DOMAINS;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(request, response);

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		this.dpsHeaders.addCorrelationIdIfMissing();

    Map<String, String> responseHeaders = responseHeadersFactory.getResponseHeaders(ACCESS_CONTROL_ALLOW_ORIGIN_DOMAINS);
    for(Map.Entry<String, String> header : responseHeaders.entrySet()){
      httpResponse.setHeader(header.getKey(), header.getValue());
    }
		httpResponse.addHeader(DpsHeaders.CORRELATION_ID, this.dpsHeaders.getCorrelationId());
	}

	@Override
	public void destroy() {
	}
}
