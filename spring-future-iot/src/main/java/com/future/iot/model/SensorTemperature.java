package com.future.iot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.deser.Deserializers;
import org.springframework.core.serializer.Deserializer;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "sensor_temperature", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class SensorTemperature implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private BigInteger id;

    @Size(max = 100)
    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "temperature_value")
    private float temperatureValue;

    @JsonProperty("humidityValue")
    @Column(name = "humidity_value")
    private float humidityValue;

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat( timezone = "Asia/Ho_Chi_Minh",locale = "vi", pattern = "h:mm:ss a dd-MM-yy", shape = JsonFormat.Shape.STRING)
    private Date updateTime;

    @Column(name = "time_lable")
    private int timeLable;


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public float getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public float getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(float humidityValue) {
        this.humidityValue = humidityValue;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getTimeLable() {
        return timeLable;
    }

    public void setTimeLable(int timeLable) {
        this.timeLable = timeLable;
    }
}
