package com.sarinkejohn.jwtsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class TokenPair {
    private String token;
    private String refreshToken;
}
