package com.future.iot.controller.web;

import com.future.iot.dto.SensorTemperatureDetailDTO;
import com.future.iot.model.Employee;
import com.future.iot.repo.EmployeeRepository;
import com.future.iot.service.SensorTemperatureDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/device/cbnd")
public class SensorTemperatureControllder {

    @Autowired
    private SensorTemperatureDetailService temperatureDetailService;
    @Autowired
    private EmployeeRepository             employeeRepository;

    @GetMapping("")
    public ModelAndView getViewSensor(Principal principal){
        ModelAndView mv = new ModelAndView("device/sensor.temperature");
        Employee employee = employeeRepository.findByName(principal.getName());
        mv.addObject("employee", employee);

        Locale locale = new Locale("vi","VN");
        SimpleDateFormat  dateFormat = new SimpleDateFormat("EEEEE,dd MMMMM, yyyy ", locale);
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());
        String dayOfWeek = date.split(",")[0];
        String dMy = date.replace(dayOfWeek + ",","");
        mv.addObject("dayOfWeek", dayOfWeek).addObject("dMY", dMy);

        List<SensorTemperatureDetailDTO> temperatureDetailDTOS = temperatureDetailService.getTemperatureDetailDtoList(employee.getId());
        mv.addObject("sensor", temperatureDetailDTOS.isEmpty() ? new SensorTemperatureDetailDTO() : temperatureDetailDTOS.get(0));
        mv.addObject("sensors", temperatureDetailDTOS);

        return mv;
    }

    @GetMapping("/{mac}")
    public ModelAndView getViewByMac(@PathVariable("mac") String macAddress, Principal principal){
        Employee employee = employeeRepository.findByName(principal.getName());
        SensorTemperatureDetailDTO temperatureDetailDTO = temperatureDetailService.findByMac(macAddress, employee.getId());
        ModelAndView mv = getViewSensor(principal);
        mv.addObject("sensor", temperatureDetailDTO);
        return mv;
    }


}
