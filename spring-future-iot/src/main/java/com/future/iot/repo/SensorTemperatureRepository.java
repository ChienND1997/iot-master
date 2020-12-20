package com.future.iot.repo;

import com.future.iot.model.SensorTemperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface SensorTemperatureRepository extends JpaRepository<SensorTemperature, BigInteger> {
    @Query(value = "select count(1) from sensor_temperature where mac_address = ? and time_lable = ?",
            nativeQuery = true)
    int resultTimeLable(String macAddress, int timeLable);
}
