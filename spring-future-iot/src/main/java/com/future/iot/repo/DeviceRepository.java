package com.future.iot.repo;

import com.future.iot.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    @Query(value = "select * from device where mac_address = ?", nativeQuery = true)
    Device findByMac(String macAddress);
}
