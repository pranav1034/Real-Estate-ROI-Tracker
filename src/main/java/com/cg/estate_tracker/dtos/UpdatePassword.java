package com.cg.estate_tracker.dtos;
import lombok.Data;

@Data
public class UpdatePassword {
    String oldPassword;
    String password;
}
