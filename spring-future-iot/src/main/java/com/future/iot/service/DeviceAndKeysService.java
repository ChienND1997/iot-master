package com.future.iot.service;

import com.future.iot.dto.DeviceAndKeysDetailDTO;

import java.util.List;

public interface DeviceAndKeysService {
    List<DeviceAndKeysDetailDTO> deviceAndKeysDetailDtoList(long empId);
}
