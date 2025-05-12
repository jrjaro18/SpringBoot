package org.idfc.springboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmployeeRequestDTO {
    @NotNull private String name;
    @NotNull private String role;
    @NotNull private double salary;
    @NotNull @Email private String email;
}