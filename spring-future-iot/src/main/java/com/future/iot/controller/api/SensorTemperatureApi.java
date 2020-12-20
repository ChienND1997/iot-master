package com.future.iot.controller.api;

import com.future.iot.crypto.CryptoManage;
import com.future.iot.dto.SensorTemperatureDetailDTO;
import com.future.iot.model.Employee;
import com.future.iot.model.KeysManageDevice;
import com.future.iot.model.SensorTemperature;
import com.future.iot.repo.DeviceRepository;
import com.future.iot.repo.EmployeeRepository;
import com.future.iot.repo.KeysManageDeviceRepository;
import com.future.iot.repo.SensorTemperatureRepository;
import com.future.iot.service.SensorTemperatureDetailService;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;


@RestController
@RequestMapping("/api/cbnd")
public class SensorTemperatureApi {

    private static final Logger LOG = Logger.getLogger(SensorTemperatureApi.class);
    @Autowired
    private SensorTemperatureRepository    tempRepo;
    @Autowired
    private DeviceRepository               deviceRepo;
    @Autowired
    private CryptoManage                   cryptoManage;
    @Autowired
    private SensorTemperatureDetailService temperatureDetailService;
    @Autowired
    private EmployeeRepository             employeeRepository;
    @Autowired
    private MqttClient                     mqttClient;


    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SensorTemperatureApi(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/handler-encrypto")
    public void handlerEnCryptoData(@RequestBody String data) { //mac+cipher
        String plainText = cryptoManage.getDataAfterHandlerEnCrypto(data);//tempe-humi
        if(plainText != null) {
            String strs[] = plainText.split(";");
            SensorTemperature sensor = new SensorTemperature();
            try {
                sensor.setMacAddress(strs[0]);
                sensor.setTemperatureValue(Float.parseFloat(strs[1]));
                sensor.setHumidityValue(Float.parseFloat(strs[2]));
                int timeLable = Integer.parseInt(strs[3]);
                sensor.setTimeLable(timeLable);
                if(tempRepo.resultTimeLable(strs[0], timeLable) == 0) {
                    saveSensorTemperature(sensor);
                }else{
                    LOG.info("Du lieu da ton tai -> phat lai !");
                }
            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    @PostMapping("/save")
    public void saveSensorTemperature(@RequestBody SensorTemperature sensor) {
        String macAddress = sensor.getMacAddress();
        if (deviceRepo.findByMac(macAddress) != null) {
            sensor.setUpdateTime(new Date());
            tempRepo.save(sensor);
            LOG.info("update SensorTemperature " + sensor.getMacAddress() + " done!");
            int id = deviceRepo.findByMac(macAddress).getEmployeeId();
            String username = employeeRepository.findById(id).get().getUsername();
            messagingTemplate.convertAndSend("/topic/cbnd/" + id + username, sensor);
        } else {
            LOG.info("Thiết bị không tồn tại !");
        }
    }

    @SendTo("/topic/cbnd/{empId}{username}")
    public SensorTemperature getValueSensor(SensorTemperature sensor) {
        return sensor;
    }

    @GetMapping("/get-{macAddress}")
    public SensorTemperatureDetailDTO getById(@PathVariable("macAddress") String macAddress, Principal principal) {
        LOG.info(macAddress + "====" + principal.getName());
        Employee employee = employeeRepository.findByName(principal.getName());
        SensorTemperatureDetailDTO dto = temperatureDetailService.findByMac(macAddress, employee.getId());
        return dto;
    }



}
