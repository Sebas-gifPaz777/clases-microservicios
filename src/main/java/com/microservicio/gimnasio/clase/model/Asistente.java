package com.microservicio.gimnasio.clase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity

@AllArgsConstructor

public class Asistente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long miembroId;
    private Long claseId;
    private Date fechaInscripción;


    public Asistente(Long miembroId, Long claseId, Date date) {

        this.miembroId = miembroId;
        this.claseId = claseId;
        fechaInscripción = date;
    }

    public Asistente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMiembroId() {
        return miembroId;
    }

    public void setMiembroId(Long miembroId) {
        this.miembroId = miembroId;
    }

    public Long getClaseId() {
        return claseId;
    }

    public void setClaseId(Long claseId) {
        this.claseId = claseId;
    }

    public Date getFechaInscripción() {
        return fechaInscripción;
    }

    public void setFechaInscripción(Date fechaInscripción) {
        this.fechaInscripción = fechaInscripción;
    }
}
