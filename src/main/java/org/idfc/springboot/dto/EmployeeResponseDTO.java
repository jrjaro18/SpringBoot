package org.idfc.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmployeeResponseDTO {
    private String id;
    private String name;
    private String role;
    private double salary;
}
