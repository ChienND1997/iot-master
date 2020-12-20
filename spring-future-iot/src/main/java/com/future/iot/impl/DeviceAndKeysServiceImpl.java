package com.future.iot.impl;

import com.future.iot.dto.DeviceAndKeysDetailDTO;
import com.future.iot.service.DeviceAndKeysService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceAndKeysServiceImpl implements DeviceAndKeysService {

    @PersistenceContext
    private EntityManager em;

    private DeviceAndKeysDetailDTO mapToDeviceAndKeysDetailDTO(Object[] objs){
        DeviceAndKeysDetailDTO deviceAndKeysDetailDTO = new DeviceAndKeysDetailDTO();
        deviceAndKeysDetailDTO.setId((int) objs[0]);
        deviceAndKeysDetailDTO.setMacAddress((String) objs[1]);
        deviceAndKeysDetailDTO.setLocation((String) objs[2]);
        deviceAndKeysDetailDTO.setDescription((String) objs[3]);
        deviceAndKeysDetailDTO.setCreateDate(new SimpleDateFormat("dd-MM-yy").format((Date) objs[4]));
        deviceAndKeysDetailDTO.setKeySecret((String) Optional.ofNullable(objs[5]).orElse("none"));
        deviceAndKeysDetailDTO.setInitVector((String) Optional.ofNullable(objs[6]).orElse("none"));
        deviceAndKeysDetailDTO.setAlgorithm((String) Optional.ofNullable(objs[7]).orElse("none"));
        deviceAndKeysDetailDTO.setTypeCode((String) objs[8]);
        deviceAndKeysDetailDTO.setTypeName((String) objs[9]);
        return deviceAndKeysDetailDTO;
    }

    @Override
    public List<DeviceAndKeysDetailDTO> deviceAndKeysDetailDtoList(long empId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d.id, d.mac_address,  d.location, d.description, d.create_date, k.secret_key, k.iv, ");
        sql.append("k.algo, d.type_code, t.type_name  ");
        sql.append("FROM device d LEFT JOIN type_device t ON d.type_code = t.type_code ");
        sql.append("LEFT JOIN  keys_manage_device k ON d.mac_address = k.mac_address ");
        sql.append("WHERE d.employee_id = ? ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, empId);
        List<Object[]> lstObjs = query.getResultList();
        return lstObjs.stream().map(this::mapToDeviceAndKeysDetailDTO).collect(Collectors.toList());
    }
}
