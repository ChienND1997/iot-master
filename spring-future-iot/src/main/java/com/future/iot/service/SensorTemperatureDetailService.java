package com.future.iot.service;

import com.future.iot.dto.SensorTemperatureDetailDTO;

import java.util.List;


public interface SensorTemperatureDetailService {
    SensorTemperatureDetailDTO findByMac(String macAddress, int empId);
    List<SensorTemperatureDetailDTO> getTemperatureDetailDtoList(int empId);
}
