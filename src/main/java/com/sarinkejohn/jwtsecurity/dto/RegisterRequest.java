package com.sarinkejohn.jwtsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "name is required")
    @Size(min = 2, max = 50, message = "name should be btn 2 and 50 chars")

    private String fullName;
    @NotBlank(message = "username is required")
    @Size(min = 2, max = 50, message = "name should be btn 2 and 50 chars")

    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be 4 chars or more")
    private String password;

    @Pattern(
            regexp = "ROLE_USER|ROLE_ADMIN",
            message = "role must be ROLE_USER or ROLE_ADMIN"
    )
    private String role;
}
