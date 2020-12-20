package com.future.iot.controller.web;

import com.future.iot.model.Employee;
import com.future.iot.repo.EmployeeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
//@RequestMapping("/")
public class HomeController {
    private static final Logger LOG = Logger.getLogger(AcountController.class);
    @Autowired
    private EmployeeRepository empRepo;

    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView("home/home");
    }


    @GetMapping("/login")
    public ModelAndView login(Principal principal, @RequestParam(value = "error", required = false) String error) {
        if(principal != null) return new ModelAndView("redirect:/");
        ModelAndView mv = new ModelAndView("home/login");
        if(error != null) mv.addObject("error", "Số điện thoại hoặc mật khẩu không đúng !");
        return mv;
    }

    @GetMapping("/register")
    public ModelAndView register(Principal principal){
        ModelAndView mv = new ModelAndView("home/register", "info", new Employee());
        return principal == null ? mv : new ModelAndView("redirect:/");
    }

    @PostMapping("/acount-save")
    public ModelAndView employeeSave(@Valid @ModelAttribute("info") Employee emp, BindingResult result, HttpServletRequest request) throws ServletException {
        if(result.hasErrors()) {
            ModelAndView mv = new ModelAndView("home/register", "info", emp);
            mv.addObject("errors", result);
            return mv;
        }
        if(empRepo.findByName(emp.getUsername()) != null) {
            ModelAndView mv = new ModelAndView("home/register", "info", emp);
            mv.addObject("message", "Tài khoản đã tồn tại!");
            return mv;
        }
        emp.setCreateDate(new Date());
        empRepo.save(emp);
        request.login(emp.getUsername(), emp.getPassword());
        LOG.info("Insert User " + emp.getUsername() + " done!");
        return new ModelAndView("redirect:/");
    }
}
