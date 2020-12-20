package com.future.iot.common;

import com.future.iot.controller.api.SensorTemperatureApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttHandler {
    @Autowired
    private SensorTemperatureApi sensorTemperatureApi;

    public void handlerAfterSubscribe(String topic, String msg){
        switch (topic){
            case "mqtt-cbnd-encrypto" : sensorTemperatureApi.handlerEnCryptoData(msg);
        }
    }

}
