package com.example.smartf.config;

import com.example.smartf.tool.UID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(value = "classpath:config/mqtt.properties")
public class MqttConfig {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${mqtt.ip}")
    private String mqtt_ip;

    @Value("${mqtt.port}")
    private String mqtt_port;

    @Value("${mqtt.username}")
    private String mqtt_username;

    @Value("${mqtt.password}")
    private String mqtt_password;


    public String getHost(){
        //mqtt proxy mqtt代理
        logger.info("mqtt 代理 : tcp://" + mqtt_ip + ":" + mqtt_port);
        return "tcp://" + mqtt_ip + ":" + mqtt_port;
    }

    public String getMqtt_username() {
        return mqtt_username;
    }

    public String getMqtt_password() {
        return mqtt_password;
    }
}
