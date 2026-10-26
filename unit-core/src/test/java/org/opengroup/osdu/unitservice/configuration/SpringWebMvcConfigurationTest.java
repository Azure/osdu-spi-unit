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

package org.opengroup.osdu.unitservice.configuration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class SpringWebMvcConfigurationTest {

    private final SpringWebMvcConfiguration configuration = new SpringWebMvcConfiguration();

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ProbeController())
            .addFilters(configuration.trailingSlashFilter().getFilter())
            .build();

    @ParameterizedTest
    @ValueSource(strings = {"/v3/conversion/scale", "/v3/conversion/scale/"})
    void shouldPreservePostBodyAndQueryWithEitherTrailingSlash(String path) throws Exception {
        mvc.perform(post(path).queryParam("offset", "5")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"fromUnit\":\"m\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("5:{\"fromUnit\":\"m\"}"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/v3/unit/symbol", "/v3/unit/symbol/"})
    void shouldMatchGetWithEitherTrailingSlash(String path) throws Exception {
        mvc.perform(get(path).queryParam("symbol", "m/s"))
                .andExpect(status().isOk())
                .andExpect(content().string("m/s"));
    }

    @RestController
    class ProbeController {
        @PostMapping("/v3/conversion/scale")
        public String post(@RequestParam("offset") String offset, @RequestBody String body) {
            return offset + ":" + body;
        }

        @GetMapping("/v3/unit/symbol")
        public String get(@RequestParam("symbol") String symbol) {
            return symbol;
        }
    }
}
