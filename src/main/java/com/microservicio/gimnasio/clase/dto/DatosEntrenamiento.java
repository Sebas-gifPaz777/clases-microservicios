package com.microservicio.gimnasio.clase.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

public class DatosEntrenamiento implements Serializable {
    private Long claseId;
    private int cantidadAsistentes;
    private LocalDateTime fechaSesion;

    public DatosEntrenamiento() {
    }

    public DatosEntrenamiento(Long claseId, int cantidadAsistentes, LocalDateTime fechaSesion) {
        this.claseId = claseId;
        this.cantidadAsistentes = cantidadAsistentes;
        this.fechaSesion = fechaSesion;
    }

    // Getters y Setters
    public Long getClaseId() { return claseId; }
    public void setClaseId(Long claseId) { this.claseId = claseId; }
    public int getCantidadAsistentes() { return cantidadAsistentes; }
    public void setCantidadAsistentes(int cantidadAsistentes) { this.cantidadAsistentes = cantidadAsistentes; }
    public LocalDateTime getFechaSesion() { return fechaSesion; }
    public void setFechaSesion(LocalDateTime fechaSesion) { this.fechaSesion = fechaSesion; }
}
