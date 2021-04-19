package com.magic.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author magic_lz
 * @version 1.0
 * @classname FtpProperties
 * @date 2021/4/19 : 20:39
 */
@Data
@Getter
@Setter
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties {

    private String ip;
    private String port;
    private String username;
    private String password;
    private Integer initialSize = 0;
    private String encoding = "UTF-8";
    private Integer bufferSize = 4096;
    private Integer retryCount = 3;

}
