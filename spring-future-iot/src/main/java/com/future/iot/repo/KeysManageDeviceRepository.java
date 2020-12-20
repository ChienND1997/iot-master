package com.future.iot.repo;

import com.future.iot.model.KeysManageDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KeysManageDeviceRepository extends JpaRepository<KeysManageDevice, Integer> {

    @Query(name = "SELECT * FROM key_manage_device WHERE hash_mac = ?", nativeQuery = true)
    KeysManageDevice findByHashMac(String hashMac);

    @Query(name = "SELECT * FROM key_manage_device WHERE mac_address = ?", nativeQuery = true)
    KeysManageDevice findByMacAddress(String mac);
}
