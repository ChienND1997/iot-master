package com.future.iot.model;

import javax.persistence.*;

@Entity
@Table(name = "type_device", uniqueConstraints = @UniqueConstraint(columnNames = "type_code"))
public class TypeDevice {
    private String typeCode;
    private String typeName;

    @Id
    @Column(name = "type_code")
    public String getTypeCode() {
        return typeCode;
    }

    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeCode(String typeCode){
        this.typeCode = typeCode;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
