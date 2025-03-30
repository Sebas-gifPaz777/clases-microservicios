package com.microservicio.gimnasio.clase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data

public class OcupacionClase {

    private String claseId;
    private int ocupacionActual;
    private LocalDateTime hora;

    public OcupacionClase(String claseId, int ocupacionActual, LocalDateTime hora) {
        this.claseId = claseId;
        this.ocupacionActual = ocupacionActual;
        this.hora = hora;
    }

    public OcupacionClase() {
    }

    public String getClaseId() {
        return claseId;
    }

    public void setClaseId(String claseId) {
        this.claseId = claseId;
    }

    public int getOcupacionActual() {
        return ocupacionActual;
    }

    public void setOcupacionActual(int ocupacionActual) {
        this.ocupacionActual = ocupacionActual;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }
}
