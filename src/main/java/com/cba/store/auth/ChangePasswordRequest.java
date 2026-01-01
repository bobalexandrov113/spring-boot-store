package com.cba.store.auth;

import lombok.Data;

@Data

public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
