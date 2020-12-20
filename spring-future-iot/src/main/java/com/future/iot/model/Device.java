package com.future.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "device", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "mac_address"}))
public class Device {


    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Size(max = 30)
    @Column(name = "description")
    private String description;

    @Size(min = 1, max = 100)
    @Column(name = "mac_address")
    private String macAddress;

    @Size(max = 30)
    @Column(name = "location")
    private String location;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date")
    private Date creatDate;

    @Column(name = "de_status")
    private String status = "Ngoại tuyến";

    @NotBlank
    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "employee_id")
    private int employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code", referencedColumnName = "type_code",insertable = false, updatable = false)
    @JsonIgnore
    private TypeDevice typeDevice;


    public int getId() {
        return id;
    }


    public String getDescription() {
        return description;
    }


    public String getMacAddress() {
        return macAddress;
    }


    public String getLocation() {
        return location;
    }


    public Date getCreatDate() {
        return creatDate;
    }


    public String getStatus() {
        return status;
    }


    public String getTypeCode() {
        return typeCode;
    }


    public int getEmployeeId(){
        return employeeId;
    }


    public TypeDevice getTypeDevice() {
        return typeDevice;
    }


    public void setTypeDevice(TypeDevice typeDevice) {
        this.typeDevice = typeDevice;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int Id) {
        this.id = id;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
