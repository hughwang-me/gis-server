package com.uwjx.gisserver;

import com.uwjx.gisserver.config.GisConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author wanghuan
 */
@SpringBootApplication
@EnableConfigurationProperties(GisConfigurationProperties.class)
public class GisServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GisServerApplication.class, args);
    }

}
