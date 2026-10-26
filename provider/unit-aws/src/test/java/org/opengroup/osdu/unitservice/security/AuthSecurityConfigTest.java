/* Copyright © 2020 Amazon Web Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.opengroup.osdu.unitservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.unitservice.api.UnitApiV3;
import org.opengroup.osdu.unitservice.middleware.AuthenticationRequestFilter;
import org.opengroup.osdu.unitservice.middleware.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(classes = {
        UnitApiV3.class,
        AuthenticationRequestFilter.class,
        AuthSecurityConfig.class,
        AuthenticationService.class,
        WebMvcAutoConfiguration.class})
@WebAppConfiguration
class AuthSecurityConfigTest {
    private MockMvc mockMvc = null;

    @MockitoBean
    private UnitApiV3 unitApiV3;

    @MockitoBean
    AuthenticationService authenticationService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testConfigureHttpSecurity() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isNotFound())
            .andReturn();
    }
}