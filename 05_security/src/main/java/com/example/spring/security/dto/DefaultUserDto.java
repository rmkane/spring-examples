package com.example.spring.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DefaultUserDto {
    private String username;
    private String password;
    private String email;

    @JsonProperty("fullName")
    private String fullName;

    private String role;
}
