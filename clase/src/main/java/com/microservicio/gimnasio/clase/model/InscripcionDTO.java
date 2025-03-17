package com.microservicio.gimnasio.clase.model;

public class InscripcionDTO {

    private Long claseId;
    private String token;

    public InscripcionDTO(Long claseId, String token) {
        this.claseId = claseId;
        this.token = token;
    }

    public InscripcionDTO() {
    }

    public Long getClaseId() {
        return claseId;
    }

    public void setClaseId(Long claseId) {
        this.claseId = claseId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
