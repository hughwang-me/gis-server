package com.uwjx.gisserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wanghuan
 * @email 18501667737@163.com
 * @date 2025/4/8 10:39
 */
@ConfigurationProperties(prefix = "gis")
@Data
public class GisConfigurationProperties {

    private String mbtilesPath;

}
