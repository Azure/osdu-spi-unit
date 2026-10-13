package org.opengroup.osdu.unitservice;

import org.apache.tomcat.util.buf.EncodedSolidusHandling;
import org.opengroup.osdu.unitservice.model.CatalogImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.io.FileReader;
import java.util.logging.Logger;

@Configuration
@PropertySource("classpath:swagger.properties")
public class UnitAksConfiguration {
    private static Logger logger = Logger.getLogger(UnitAksConfiguration.class.getName());

    @Bean(name = "KEY_VAULT_URL")
    public String keyVaultURL(@Value("${azure.keyvault.url}") String keyVaultURL) {
        return keyVaultURL;
    }

    @Bean
    public CatalogImpl catalogImpl(@Value("${osdu.unit.catalog.filename}") String location) throws Exception {
        logger.info("osdu.unit.catalog.filename=" + location);
        try (FileReader reader = new FileReader(location)) {
            return CatalogImpl.createCatalog(reader);
        }
    }
    
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        logger.info("Configuring Tomcat to allow encoded slashes.");
        return factory -> factory.addConnectorCustomizers(connector -> connector.setEncodedSolidusHandling(EncodedSolidusHandling.DECODE.getValue()));
    }
}
