package com.example.smartf.mqtt;


import com.example.smartf.config.MqttConfig;
import com.example.smartf.tool.UID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;



public class UpMqtt {

    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HOST = Config.getHost();
    private static String clientid = UID.getUid();
    private MqttClient client;

    /*单例*/
    private static UpMqtt upMqtt = new UpMqtt();
    /*单例*/
    public static UpMqtt getInstance(){
        return upMqtt;
    }
    public String getHost(){
        return HOST;
    }

    //mqtt 配置选项
    private MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();

        // 设置自动清除session为false， false表示服务器保留客户端连接记录， true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(false);
        //设置超时时间 单位 秒
        options.setConnectionTimeout(50);
        //设置回话心跳时间， 单位 秒  每隔多长时间向客户端发送消息判断客户端是否在线，但是没有重连机制，只能判断客户端是否存活
        options.setKeepAliveInterval(12);
        //设置mqtt连入用户名
        options.setUserName(Config.getUsername());
        //设置mqtt连入用户名密码
        options.setPassword(Config.getPassword().toCharArray());
        return options;
    }

    //连接到代理（mqtt服务器）
    protected void connect() throws MqttException{
        MqttConnectOptions options = getOptions();
        //设置代理host的URL，客户端id，memoryPersistence设置clientid的保存形式，默认以内存保存
        client = new MqttClient(HOST,clientid,new MemoryPersistence());
        //设置回调函数
        client.setCallback(new UpCallBack());

        //若此时处于未连接状态，则去连接
        if(!client.isConnected()){
            client.connect();
            logger.debug("上行--连接 mqtt 代理 成功！");
        }

        //重连后需要 重新订阅 所有设备
        subscribe("6af6188e14aa");
    }

    //发布信息
    public void publish(String topic, String message){
        try {
            client.publish(topic, new MqttMessage(message.getBytes()));
        }catch (MqttException e){
            logger.warn("下行--发布信息异常 ：" + e.getMessage());
        }
    }

    //订阅消息
    public void subscribe(String topic){
        try{
            client.subscribe(topic,1);
        }catch (MqttException e){
            logger.warn("上行--订阅消息异常 ：" + e.getMessage());
        }
    }

    //取消订阅
    public void cancelsub(String topic){
        try {
            client.unsubscribe(topic);
        }catch (MqttException e){
            logger.warn("上行--取消订阅异常 ：" + e.getMessage());
            cancelsub(topic);
        }
    }

    public void firstconnect(){
        System.out.println("第一次连接ing");
        while (true){
            try{
                Thread.sleep(2000);
                connect();
            }catch (MqttException e){
                logger.warn("上行--无法连接MQTT代理服务器 ：" + e.getMessage());
            }catch (Exception e){
                logger.warn("上行--连接时发生异常 ：" + e.getMessage());
            }
            return ;
        }
    }

    @Component
    private static class Config{
        @Autowired
        private MqttConfig mqttConfig;

        private static Config config;

        @PostConstruct
        public void init(){
            config = this;
            config.mqttConfig = this.mqttConfig;
        }

        static String getHost(){
            return config.mqttConfig.getHost();
        }

        static String getPassword(){
            return config.mqttConfig.getMqtt_password();
        }

        static String getUsername(){
            return config.mqttConfig.getMqtt_username();
        }
    }

}
