package org.idfc.springboot.dto;

import lombok.Data;

@Data
public class EmployeeRequestDTO {
    private String name;
    private String role;
    private double salary;
    private String email;
}