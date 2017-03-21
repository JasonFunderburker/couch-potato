package com.jasonfunderburker.couchpotato;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by JasonFunderburker on 21.03.17.
 */
@Configuration
public class ProxyConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ProxyConfiguration.class);

    @Value("${rutracker.proxyconfig.proxy-host}")
    private String proxyHost;
    @Value("${rutracker.proxyconfig.proxy-port}")
    private Integer proxyPort;

    @Bean
    public ProxyConfig proxyConfig() {
        logger.debug("proxy-host={}, proxy-port={}", proxyHost, proxyPort);
        return new ProxyConfig(proxyHost, proxyPort, false);
    }
}
