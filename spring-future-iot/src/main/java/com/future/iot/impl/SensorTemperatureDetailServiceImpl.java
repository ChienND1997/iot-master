package com.future.iot.impl;

import com.future.iot.dto.SensorTemperatureDetailDTO;
import com.future.iot.service.SensorTemperatureDetailService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorTemperatureDetailServiceImpl implements SensorTemperatureDetailService {

    @PersistenceContext
    private EntityManager em;

    private SensorTemperatureDetailDTO mapToSensorTemperatureDetailDTO(Object[] objs) {
        SensorTemperatureDetailDTO sensorTemperatureDetailDTO = new SensorTemperatureDetailDTO();
        sensorTemperatureDetailDTO.setId((int) objs[0]);
        sensorTemperatureDetailDTO.setDescription((String) Optional.ofNullable(objs[1]).orElse("none"));
        sensorTemperatureDetailDTO.setMacAddress((String) objs[2]);
        sensorTemperatureDetailDTO.setLocation((String) Optional.ofNullable(objs[3]).orElse("none"));
        sensorTemperatureDetailDTO.setStatus(Optional.ofNullable(objs[4].toString()).orElse("UnKnow"));
        Date createDate = Optional.ofNullable((Date) objs[5]).orElse(new Date());
        sensorTemperatureDetailDTO.setCreateDate(new SimpleDateFormat("dd-MM-yy").format(createDate));
        sensorTemperatureDetailDTO.setTemperaturevalue((float) Optional.ofNullable(objs[6]).orElse(0f));
        sensorTemperatureDetailDTO.setHumidityValue((float) Optional.ofNullable(objs[7]).orElse(0f));
        Date updateTime = Optional.ofNullable((Date) objs[8]).orElse(new Date());
        sensorTemperatureDetailDTO.setUpdateTime(
                new SimpleDateFormat("h:mm:ss a dd-MM-yy",
                new Locale("vi")).format(updateTime));
        return sensorTemperatureDetailDTO;
    }

    @Override
    public SensorTemperatureDetailDTO findByMac(String macAddress, int empId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d.id, d.description, d.mac_address, d.location, d.de_status, d.create_date, ");
        sql.append("s.temperature_value, s.humidity_value, s.update_time ");
        sql.append("FROM device d LEFT JOIN sensor_temperature s ON d.mac_address = s.mac_address ");
        sql.append("WHERE d.mac_address = ? AND d.employee_id = ? ");
        sql.append("AND s.id = (select max(id) from sensor_temperature where mac_address = d.mac_address)");
        sql.append("OR s.id IS NULL");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, macAddress);
        query.setParameter(2, empId);
        Object[] objs = (Object[]) query.getResultStream().findFirst().get();
        return objs == null ? null : this.mapToSensorTemperatureDetailDTO(objs);
    }

    @Override
    public List<SensorTemperatureDetailDTO> getTemperatureDetailDtoList(int empId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d.id, d.description, d.mac_address, d.location, d.de_status, d.create_date, ");
        sql.append("s.temperature_value, s.humidity_value, s.update_time ");
        sql.append("FROM device d LEFT JOIN sensor_temperature s ON d.mac_address = s.mac_address ");
        sql.append("WHERE  d.employee_id = ? ");
        sql.append("AND s.id = (select max(id) from sensor_temperature where mac_address = d.mac_address) ");
        sql.append("OR s.id IS NULL");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, empId);
        List<Object[]> lstObjs = query.getResultList();
        return lstObjs.stream().map(this::mapToSensorTemperatureDetailDTO).collect(Collectors.toList());
    }
}
