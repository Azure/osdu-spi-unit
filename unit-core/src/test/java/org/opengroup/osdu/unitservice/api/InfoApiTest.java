package org.opengroup.osdu.unitservice.api;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.opengroup.osdu.core.common.info.VersionInfoBuilder;
import org.opengroup.osdu.core.common.model.info.VersionInfo;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InfoApiTest {

    @InjectMocks
    private InfoApi sut;

    @Mock
    private VersionInfoBuilder versionInfoBuilder;

    @Test
    public void should_return200_getVersionInfo() throws IOException {
        VersionInfo expectedVersionInfo = VersionInfo.builder()
                .groupId("group")
                .artifactId("artifact")
                .version("0.1.0")
                .buildTime("1000")
                .branch("master")
                .commitId("7777")
                .commitMessage("Test commit")
                .build();
        when(versionInfoBuilder.buildVersionInfo()).thenReturn(expectedVersionInfo);

        VersionInfo actualVersionInfo = this.sut.info();

        assertEquals(expectedVersionInfo, actualVersionInfo);
    }
}
