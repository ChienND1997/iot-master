package com.future.iot.controller.web;

import com.future.iot.crypto.CryptoManage;
import com.future.iot.model.Device;
import com.future.iot.model.Employee;
import com.future.iot.model.KeysManageDevice;
import com.future.iot.model.TypeDevice;
import com.future.iot.repo.DeviceRepository;
import com.future.iot.repo.EmployeeRepository;
import com.future.iot.repo.KeysManageDeviceRepository;
import com.future.iot.repo.TypeDeviceRepository;
import com.future.iot.service.DeviceAndKeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/device")
@PropertySource("classpath:/common.properties")
public class DeviceController {
    @Autowired
    private Environment                 env;
    @Autowired
    private DeviceAndKeysService        deviceAndKeysService;
    @Autowired
    private EmployeeRepository          employeeRepository;
    @Autowired
    private TypeDeviceRepository        typeDeviceRepository;
    @Autowired
    private DeviceRepository            deviceRepository;
    @Autowired
    private CryptoManage                cryptoManage;
    @Autowired
    private KeysManageDeviceRepository  keysManageDeviceRepo;

    @GetMapping("/add")
    public ModelAndView addDevice(Principal principal){
        Employee emp = employeeRepository.findByName(principal.getName());
        ModelAndView mv = new ModelAndView("device/device.add");
        mv.addObject("employee", emp);
        mv.addObject("device", new Device());
        mv.addObject("lstDeviceAndKeys", deviceAndKeysService.deviceAndKeysDetailDtoList(emp.getId()));
        mv.addObject("algorithms",getListAlgorithm());
        mv.addObject("optionTypeDevice", mapToTypeDevice(typeDeviceRepository.findAll()));
        return mv;
    }

    private List<String> getListAlgorithm(){
        String[] arrays = env.getProperty("arrays.name.cypto", String[].class);
        return Arrays.asList(arrays);
    }

    private Map<String, String> mapToTypeDevice(List<TypeDevice> list){
        Map<String, String> map = new HashMap<>();
        list.forEach(t -> map.put(t.getTypeCode(), t.getTypeName()));
        return map;
    }

    @PostMapping("/save")
    public ModelAndView saveDevice(Principal principal, @Valid @ModelAttribute("device") Device device, BindingResult result, @RequestParam(name = "algorithm") String algorithm){
        Employee emp = employeeRepository.findByName(principal.getName());
        ModelAndView mv = addDevice(principal);
        if(result.hasErrors()){
            mv.addObject("device", device);
            return mv;
        }
        if (deviceRepository.findByMac(device.getMacAddress()) != null) {
            mv.addObject("device", device);
            mv.addObject("isExist", "Đã tồn tại !");
            return mv;
        }
        device.setCreatDate(new Date());
        device.setEmployeeId(emp.getId());
        deviceRepository.save(device);

        cryptoManage.getIntanceKey(algorithm);
        KeysManageDevice keysManageDevice = new KeysManageDevice();
        keysManageDevice.setAlgorithm(algorithm);
        keysManageDevice.setMacAddress(device.getMacAddress());
        keysManageDevice.setSecretKey(cryptoManage.getSecretKey());
        keysManageDevice.setIv(cryptoManage.getIv());
        keysManageDevice.setPublicKey(cryptoManage.getPublicKey());
        keysManageDevice.setPrivateKey(cryptoManage.getPrivateKey());
        keysManageDeviceRepo.save(keysManageDevice);

        mv = addDevice(principal);
        return mv;
    }
}
