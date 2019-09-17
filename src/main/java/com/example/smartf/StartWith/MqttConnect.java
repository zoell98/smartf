package com.example.smartf.StartWith;

import com.example.smartf.mqtt.UpMqtt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MqttConnect implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception{
        //服务器启动时 连接mqtt服务器
        UpMqtt.getInstance().firstconnect();
    }
}
