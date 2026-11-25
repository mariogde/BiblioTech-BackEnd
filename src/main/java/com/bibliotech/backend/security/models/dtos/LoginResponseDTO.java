package com.bibliotech.backend.security.models.dtos;

public class LoginResponseDTO {

    private String token;
    private Boolean isAdmin;

    public LoginResponseDTO(String token, Boolean isAdmin) {
        this.token = token;
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}