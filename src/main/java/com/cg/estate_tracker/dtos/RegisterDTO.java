package com.cg.estate_tracker.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterDTO {
    String name;
    String email;
    String password;
}
