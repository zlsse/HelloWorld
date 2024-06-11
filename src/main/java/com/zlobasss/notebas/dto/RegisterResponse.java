package com.zlobasss.notebas.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RegisterResponse {
    private String message;
    private String token;

    public RegisterResponse(String message) {
        this.message = message;
    }
}
