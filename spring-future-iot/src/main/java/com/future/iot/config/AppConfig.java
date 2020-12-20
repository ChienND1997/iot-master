package com.future.iot.config;

import com.future.iot.common.MqttHandler;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import java.util.Arrays;
import java.util.Properties;

@Configuration
@EnableWebMvc
@PropertySource("classpath:/application.properties")
public class AppConfig implements WebMvcConfigurer {
    private final static Logger LOG = Logger.getLogger(AppConfig.class);

    @Autowired
    private Environment env;
    @Autowired
    private MqttHandler mqttHandler;

    @Override
    public Validator getValidator() {
        return WebMvcConfigurer.super.getValidator();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/public/image/**").addResourceLocations("classpath:/static/uploadmedia/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/ckfinder/**").addResourceLocations("classpath:/static/ckfinder/");

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setOrder(1);
        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setViewClass(TilesView.class);
        return viewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfig = new TilesConfigurer();
        tilesConfig.setDefinitions("/tiles/definitions.xml");
        return tilesConfig;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource mess = new ResourceBundleMessageSource();
        mess.setBasenames("validate");
        mess.setDefaultEncoding("UTF-8");
        mess.setAlwaysUseMessageFormat(true);
        return mess;
    }

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setUsername(env.getProperty("mail.sender.username"));
        mailSender.setPassword(env.getProperty("mail.sender.password"));
        Properties pro = new Properties();
        pro.put("mail.smtp.auth", "true");
        pro.put("mail.smtp.socketFactory.port", "465");
        pro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        pro.put("mail.smtp.port", "465");
        mailSender.setJavaMailProperties(pro);
        return mailSender;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient mqttClient = new MqttClient(env.getProperty("mqtt.url"), "server-app");
        MqttConnectOptions connOptions = new MqttConnectOptions();
        connOptions.setUserName(env.getProperty("mqtt.username"));
        connOptions.setPassword(env.getProperty("mqtt.password").toCharArray());
        mqttClient.connect(connOptions);
        String[] topics = env.getProperty("mqtt.topic", String[].class);
        int qos = env.getProperty("mqtt.qos", Integer.class);
        Arrays.stream(topics).forEach(t -> {
            try {
                mqttClient.subscribe(t, qos, (topic, msg) -> {
                    mqttHandler.handlerAfterSubscribe(topic, msg.toString());
                    LOG.info(msg.toString());
                });
            } catch (MqttException e) {
                LOG.error(e.getMessage());
            }
        });
        return mqttClient;
    }

}
