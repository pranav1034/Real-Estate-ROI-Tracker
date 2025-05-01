package com.cg.estate_tracker.dtos;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordDTO {
    String email;
    String newPassword;
    String otp;
}
