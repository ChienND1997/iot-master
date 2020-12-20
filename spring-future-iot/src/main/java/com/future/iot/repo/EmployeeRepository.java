package com.future.iot.repo;

import com.future.iot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "select * from employee where username = ? ", nativeQuery = true)
    Employee findByName(String username);
}
