package com.cg.estate_tracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotPasswordDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;
}
