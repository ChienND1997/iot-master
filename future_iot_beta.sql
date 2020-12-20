create database future_iot_beta character set utf8 collate utf8_unicode_ci;
use future_iot_beta;

create table employee(
	id int auto_increment primary key,
	username varchar(15),
    hash_pass varchar(300),
    fullname nvarchar(50),
    email varchar(50),
    create_date date,
    address nvarchar(100),
    role varchar(20)
	);    

create table device(
	id int auto_increment primary key,
    employee_id int,
	mac_address varchar(50),
    description nvarchar(30),
    location nvarchar(30),
    create_date date,
    de_status nvarchar(20),
    type_code varchar(10)
    );

create table sensor_temperature(
	id bigint auto_increment primary key,
	mac_address varchar(50),
    temperature_value float,
    humidity_value float,
    update_time datetime,
    time_lable int
);
create table type_device(
	type_code varchar(10)  primary key,
    type_name nvarchar(30)
); 
create table keys_manage_device(
	id int auto_increment primary key,
    algo varchar(20),
    mac_address varchar(100),
    secret_key text,
    public_key text,
    public_key_client text,
    private_key text,
    iv text,
    hash_mac text
    );
insert into employee(username,fullname,hash_pass,email,role) values ('0398749499','Trần Việt Anh','$2a$10$bIZ5tAILMh/VpTs3tJVEIeBHxYh/Ut..4qNsI1n4hWw5myhe85Z1q','tranvietanh190196@gmail.com','ROLE_ADMIN');    
insert into type_device(type_code, type_name) values ('cbnd', 'Cảm biến nhiệt độ');
