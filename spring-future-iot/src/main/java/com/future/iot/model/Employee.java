package com.future.iot.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "employee", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "username"})})
public class Employee {

    @Id
    @GeneratedValue
    @Column(name ="id")
    private int id;

    @Column(name = "username")
    @Pattern(regexp = "^[0-9]{10,11}$")
    private String username;


    @Transient
    @Pattern(regexp = "[^\\s].{7,16}$")
    private String password;

    @Column(name = "hash_pass")
    private String hashPass;

    @NotBlank
    @Size(max = 30)
    @Column(name = "fullname")
    private String fullname;


    @Email(regexp = "\\S+")
    @Column(name = "email")
    private String email;


    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @Size(max = 100)
    @Column(name = "address")
    private String address;

    @Column(name = "role")
    private String role = "ROLE_USER";



    public int getId() {
        return id;
    }



    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    @Column(name = "hash_pass")
    public String getHashPass() {
        return hashPass;
    }


    public String getFullname() {
        return fullname;
    }



    public String getEmail() {
        return email;
    }

    public Date getCreateDate() {
        return createDate;
    }


    public String getAddress() {
        return address;
    }


    public String getRole() {
        return role;
    }

    private void setRole(String role){
        this.role = role;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
        this.hashPass = new BCryptPasswordEncoder().encode(password);
    }

    public void setId(int id) {
        this.id = id;
    }
}
