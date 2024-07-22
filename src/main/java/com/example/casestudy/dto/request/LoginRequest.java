package com.example.casestudy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message="Username cannot be blank")
    private String username;

    @NotBlank(message ="Password cannot be blank")
    private String password;
}