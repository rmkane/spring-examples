package org.foo.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DefaultUserDto {
    private String username;
    private String password;
    private String email;
    private String role;

    @JsonProperty("fullName")
    private String fullName;
}
