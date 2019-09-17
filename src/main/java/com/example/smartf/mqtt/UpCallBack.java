package com.example.smartf.mqtt;

import com.example.smartf.tool.influxdb.InfluxdbDao;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

//始终开启订阅 上行 传感器数据
public class UpCallBack implements MqttCallback {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    //连接断开时， 需要断线重连
    @Override
    public void connectionLost(Throwable throwable){

    }
    //发送信息成功是回调
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken){

    }

    //接受信息成功时 的回调
    //该方法为单线程操作， 数据处理（处理，存库influxdb） 最好放到线程中
    @Override
    public void messageArrived(String topic , MqttMessage mqttMessage) throws Exception{

    }

    // 此类 用来保存实时数据到数据库
    @Component
    public static class SaveData{
        @Resource
        private InfluxdbDao influxdbDao;

        private static SaveData saveData;

        @PostConstruct
        public void init(){
            saveData = this;
            saveData.influxdbDao = this.influxdbDao;
        }
    }
}
